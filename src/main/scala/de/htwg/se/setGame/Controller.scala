package de.htwg.se.setGame

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.actor._
import de.htwg.se.setGame.model.{Card, Game, Player}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.swing.Publisher
import scala.swing.event.Event

trait Controller extends Publisher {

  def exitApplication()

  def createCards()

  def createNewGame()

  /** Add new player to game session and set name of player
    *
    * @param name Name of new added player
    */
  def addPlayer(name: String)

  def cancelAddPlayer()

  def checkSet(cards: List[Card], player: Player)


  /**
    * Trigger game start for all UIs
    */
  def startGame()

}

/**
  * @author Philipp Daniels
  */
protected class ControllerActorSystem(private val system: ActorSystem) extends Controller {
  private implicit val timeout = Timeout(5 seconds)
  private val logger = Logger(getClass)
  private var game: Game = createEmptyGame

  override def exitApplication(): Unit = {
    logger.info(Controller.TriggerExitApp)
    publish(new ExitApplication)
  }

  override def createCards(): Unit = {

  }


  def generateNewGame(player: Player, set: List[Card]): Unit = {
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? MoveCards(set,player,game)
    game = Await.result(future, timeout.duration).asInstanceOf[Game]
    logger.info("Actor result: " + game)
    publish(UpdateGame(game))
  }

  def checkSet(set: List[Card], player: Player): Unit = {
    logger.info(Controller.TriggerIsSet)
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? Set(set)
    val result = Await.result(future, timeout.duration).asInstanceOf[ResponseTyp]
    logger.info("Actor result: " + result.boolean)
    if (result.boolean && game.pack.size >= Controller.sizeOfSet) generateNewGame(player, set)
    publish(if (result.boolean) new IsSet else new IsInvalidSet)
  }

  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  override def createNewGame(): Unit = {
    logger.info(Controller.CreateNewGame)
    game = createEmptyGame
    publish(NewGame(game))
  }

  override def addPlayer(name: String): Unit = {
    logger.info(Controller.PlayerAdded.format(name))
    game = Game(game.cardsInField, game.pack, game.player :+ Player(0, 0, name))
    publish(PlayerAdded(game))
  }

  override def cancelAddPlayer(): Unit = {
    logger.info(Controller.TriggerCancelPlayer)
    publish(new CancelAddPlayer)
  }

  override def startGame(): Unit = {
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? CreatePack
    val result = Await.result(future, timeout.duration).asInstanceOf[List[Card]]
    logger.info("Actor result: " + result)
    val cardInField = result.slice(0,CardActor.fieldSize)
    val pack = result diff cardInField
    game = Game(cardInField, pack,game.player)
    publish(StartGame(game))
  }
}

/**
  * @author Philipp Daniels
  */
object Controller {
  val CreateNewGame = "Create new Game"
  val TriggerExitApp = "Send `ExitApplication` event"
  val TriggerAddPlayer = "Send `AddPlayer` event"
  val TriggerCancelPlayer = "Send `CancelAddPlayer` event"
  val PlayerAdded = "Player added: %s"
  val TriggerIsSet = "Called is a SET"
  val sizeOfSet = 3

  def apply(system: ActorSystem): Controller = new ControllerActorSystem(system)

}

case class ExitApplication() extends Event

case class AddPlayer(game: Game) extends Event

case class CancelAddPlayer() extends Event

case class PlayerAdded(game: Game) extends Event

case class NewGame(game: Game) extends Event

case class StartGame(game: Game) extends Event

case class IsSet() extends Event
case class IsInvalidSet() extends Event
case class UpdateGame(game: Game) extends Event
