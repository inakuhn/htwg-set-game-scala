package de.htwg.se.setGame

import com.typesafe.scalalogging.Logger

object SetGameApplication {
  private val logger = Logger(getClass)

  def main(args: Array[String]): Unit = {
    val controller = Controller()
    Gui(controller)
    Tui(controller)
    logger.info("Shutdown complete")
  }
}