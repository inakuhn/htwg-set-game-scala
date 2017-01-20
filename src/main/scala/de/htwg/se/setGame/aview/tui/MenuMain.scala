package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame._
import de.htwg.se.setGame.aview.Tui
import de.htwg.se.setGame.controller.{CancelAddPlayer, Controller}

/**
  * @author Philipp Daniels
  */
class MenuMain(private val controller: Controller, private val tui: Tui) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuMain.CreateCommand) = (new Create, MenuMain.CreateDescription)
  getActions(MenuMain.ExitCommand) = (new Exit, MenuMain.ExitDescription)

  reactions += {
    case _: CancelAddPlayer =>
      logger.info(MenuMain.EventCancelAddPlayer)
      tui.menu = this
      outputMenuList()
  }

  protected override def preMenuList(): Unit = {
    super.preMenuList()
    logger.info(MenuMain.MenuHeading)
  }

  private class Exit extends Action {
    override def execute(): Unit = controller.exitApplication()
  }

  private class Create extends Action {
    override def execute(): Unit = controller.createNewGame()
  }

}

object MenuMain {
  val EventCancelAddPlayer = "Received 'CancelAddPlayer' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit application"
  val CreateCommand = "c"
  val CreateDescription = "Create new game"
  val MenuHeading = "# MAIN-MENU #"

  def apply(controller: Controller, tui: Tui): MenuMain = new MenuMain(controller, tui)
}
