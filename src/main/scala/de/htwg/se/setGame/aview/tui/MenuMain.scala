package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{AddPlayer, CancelAddPlayer, Controller, ExitApplication}

/**
  * @author Philipp Daniels
  */
class MenuMain(private val controller: Controller, private val player: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuMain.CreateCommand) = (new Create, MenuMain.CreateDescription)
  getActions(MenuMain.ExitCommand) = (new Exit, MenuMain.ExitDescription)

  reactions += {
    case _: ExitApplication =>
      logger.info(MenuMain.EventExitApplication)
      exit()
    case _: AddPlayer =>
      logger.info(MenuMain.EventAddPlayer)
      player.process()
    case _: CancelAddPlayer =>
      logger.info(MenuMain.EventCancelAddPlayer)
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
  val EventAddPlayer = "Received 'AddPlayer' event"
  val EventCancelAddPlayer = "Received 'CancelAddPlayer' event"
  val EventExitApplication = "Received 'ExitApplication' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit application"
  val CreateCommand = "c"
  val CreateDescription = "Create new game"
  val MenuHeading = "# MAIN-MENU #"
  def apply(controller: Controller): MenuMain = new MenuMain(controller, MenuPlayer(controller))
}
