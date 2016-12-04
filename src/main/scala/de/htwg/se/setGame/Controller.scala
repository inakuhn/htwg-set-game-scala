package de.htwg.se.setGame

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.Logger

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

  def exitApplication(): Unit = {
    logger.info(Controller.EventExitApp)
    publish(new ExitApplication)
  }

  def create(): Unit =  {
    val myActor = system.actorOf(Props[CardActor])
    val future = myActor ? CreateMessage
    val result = Await.result(future, timeout.duration).asInstanceOf[String]
    logger.info("Actor result: " + result)
  }
}

/**
  * @author Philipp Daniels
  */
object Controller {
  val EventExitApp = "Send `ExitApplication` event"
  def apply(system: ActorSystem): Controller = new Controller(system)
}

case class ExitApplication() extends Event