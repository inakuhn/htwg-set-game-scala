package de.htwg.se.setGame

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.model.{Card, Player}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.swing.Publisher
import scala.swing.event.Event

trait Controller extends Publisher {

  def exitApplication()
  def createCards()
  def createNewGame()
  def addPlayer(name: String)
  def cancelAddPlayer()
}

/**
  * @author Philipp Daniels
  */
protected class ControllerActorSystem(private val system: ActorSystem) extends Controller {
  private implicit val timeout = Timeout(5 seconds)
  private val logger = Logger(getClass)
  var pack = List[Card]() ;
  def exitApplication(): Unit = {
    logger.info(Controller.TriggerExitApp)
    publish(new ExitApplication)
  }

  def createCards(): Unit =  {
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? CreateMessage
    val result = Await.result(future, timeout.duration).asInstanceOf[List[Card]]
    pack = result
    logger.info("Actor result: " + result)
  }

  /**
    * Dummys
    */
  /**
    *
    * @return
    */
  def getCardsInField() : List[Card] = {
    return pack;
  }
  def isASet(card: Card, player : Player) : Boolean = {

    return true;

  }




  def createNewGame(): Unit = {
    logger.info(Controller.TriggerAddPlayer)
    createCards()

    publish(new AddPlayer)
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
  val TriggerExitApp = "Send `ExitApplication` event"
  val TriggerAddPlayer = "Send `AddPlayer` event"
  val TriggerCancelPlayer = "Send `CancelAddPlayer` event"
  val PlayerAdded = "Player added: %s"
  def apply(system: ActorSystem): Controller = new ControllerActorSystem(system)
}

case class ExitApplication() extends Event
case class AddPlayer() extends Event
case class CancelAddPlayer() extends Event
case class PlayerAdded() extends Event
case class NewGame() extends Event