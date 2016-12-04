package de.htwg.se.setGame

import akka.actor.ActorSystem
import com.typesafe.scalalogging.Logger

import scala.swing.Publisher
import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
protected class Controller(private val system: ActorSystem) extends Publisher {
  private val logger = Logger(getClass)

  def exitApplication(): Unit = {
    logger.info(Controller.EventExitApp)
    publish(new ExitApplication)
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