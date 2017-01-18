package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{AddPlayer, CancelAddPlayer, Controller, ExitApplication}

/**
  * @author Philipp Daniels
  */
class MenuMain(private val controller: Controller, private val player: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuMain.ExitCommand) = (new Exit, MenuMain.ExitDescription)
  getActions(MenuMain.CreateCommand) = (new Create, MenuMain.CreateDescription)

  reactions += {
    case _: ExitApplication => exit()
    case _: AddPlayer => player.process()
    case _: CancelAddPlayer => outputMenuList()
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
  val ExitCommand = "x"
  val ExitDescription = "Exit application"
  val CreateCommand = "c"
  val CreateDescription = "Create new game"
  val MenuHeading = "# MAIN-MENU #"
  def apply(controller: Controller): MenuMain = new MenuMain(controller, MenuPlayer(controller))
}
