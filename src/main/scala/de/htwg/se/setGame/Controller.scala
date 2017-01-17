package de.htwg.se.setGame

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.actor.{CardActor, CreatePack, Set}
import de.htwg.se.setGame.model.{Card, Game, Player}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.swing.Publisher
import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
protected class Controller(private val system: ActorSystem) extends Publisher {
  private implicit val timeout = Timeout(5 seconds)
  private val logger = Logger(getClass)
  var game = Game
  def exitApplication(): Unit = {
    logger.info(Controller.TriggerExitApp)
    publish(new ExitApplication)
  }

  def createCards(): Unit =  {
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? CreatePack
    val result = Await.result(future, timeout.duration).asInstanceOf[List[Card]]
    logger.info("Actor result: " + result)
  }


  def isASet(cards : List[Card]) : Unit = {
    logger.info(Controller.TriggerIsSet)
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? Set(cards)
    val result = Await.result(future, timeout.duration).asInstanceOf[Boolean]
    logger.info("Actor result: " + result)
    publish(new IsSet(result))
  }


  def createNewGame(): Unit = {
    logger.info(Controller.CreateNewGame)


    publish(new AddPlayer)
    publish(new NewGame)
  }

  def addPlayer(name: String): Unit = {
    logger.info(Controller.PlayerAdded.format(name))
    publish(new PlayerAdded)
  }

  def cancelAddPlayer(): Unit = {
    logger.info(Controller.TriggerCancelPlayer)
    publish(new CancelAddPlayer)
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
  def apply(system: ActorSystem): Controller = new Controller(system)
}

case class ExitApplication() extends Event
case class AddPlayer() extends Event
case class CancelAddPlayer() extends Event
case class PlayerAdded() extends Event
case class NewGame() extends Event
case class IsSet(boolean: Boolean) extends Event