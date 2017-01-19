package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{Controller, ExitApplication}

/**
  * @author Philipp Daniels
  */
class MenuGame(private val controller: Controller) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuGame.ExitCommand) = (new Exit, MenuGame.ExitDescription)

  reactions += {
    case _: ExitApplication =>
      logger.info(MenuGame.EventExitApplication)
      exit()
  }

  private class Exit extends Action {
    override def execute(): Unit = controller.exitApplication()
  }
}

object MenuGame {
  val EventExitApplication = "Received 'ExitApplication' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit"
  def apply(controller: Controller): MenuGame = new MenuGame(controller)
}
