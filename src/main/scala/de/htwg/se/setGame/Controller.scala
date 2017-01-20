package de.htwg.se.setGame

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.actor._
import de.htwg.se.setGame.controller._
import de.htwg.se.setGame.model.{Card, Game, Player}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.swing.Publisher
import scala.util.Random

trait Controller extends Publisher {

  def exitApplication()

  def createNewGame()

  /**
    * Add new player to game session and set name of player
    * @param name Name of new added player
    */
  def addPlayer(name: String)

  def cancelAddPlayer()
  def randomCardsInField()
  def finishGame()
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


  def generateNewGame(player: Player, set: List[Card]): Unit = {
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? MoveCards(set, player, game)
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
    if (!result.boolean) {
      publish(UpdateGame(game))
    }
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
    val cardInField = result.slice(0, CardActor.fieldSize)
    val pack = result diff cardInField
    game = Game(cardInField, pack, game.player)
    publish(StartGame(game))
  }

  override def randomCardsInField(): Unit = {
    var listOfCards = game.pack:::game.cardsInField
    listOfCards = Random.shuffle(listOfCards)
    val cardsInField = listOfCards.slice(0,CardActor.fieldSize)
    val pack = listOfCards diff cardsInField
    game = Game(cardsInField, pack, game.player)
    publish(UpdateGame(game))
  }

  override def finishGame(): Unit = {
    publish(FinishGame(game))
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
