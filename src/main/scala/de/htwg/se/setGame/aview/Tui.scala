package de.htwg.se.setGame.aview

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.aview.tui.{Menu, MenuMain}
import de.htwg.se.setGame.{Controller, ExitApplication}

import scala.swing.Reactor

/**
  * @author Philipp Daniels
  */
class Tui(private val controller: Controller, menu: Menu) extends Reactor {
  private val logger = Logger(getClass)

  logger.info(Tui.InitiateMessage)
  listenTo(controller)

  reactions += {
    case _: ExitApplication => logger.info(Tui.Shutdown)
  }

  menu.process()
}

object Tui {
  val InitiateMessage = "Initiate UI"
  val Shutdown = "Shutdown UI"
  def apply(controller: Controller): Tui = new Tui(controller, MenuMain(controller))
}