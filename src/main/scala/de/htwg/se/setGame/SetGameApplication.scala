package de.htwg.se.setGame

import akka.actor.ActorSystem
import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.aview.{Gui, Tui}
import de.htwg.se.setGame.controller.Controller

object SetGameApplication {
  private val logger = Logger(getClass)

  def main(args: Array[String]): Unit = {
    val system = ActorSystem()
    val controller = Controller(system)
    Gui(controller)
    Tui(controller)
    system.terminate()
    logger.info("Shutdown complete")
  }
}