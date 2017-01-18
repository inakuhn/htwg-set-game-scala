package de.htwg.se.setGame.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{CancelAddPlayer, Controller, ExitApplication, PlayerAdded}

/**
  * @author Philipp Daniels
  */
class MenuPlayer(private val controller: Controller, private val playerName: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuPlayer.PlayerCommand) = (new Player, MenuPlayer.PlayerDescription)
  getActions(MenuPlayer.CancelCommand) = (new Cancel, MenuPlayer.CancelDescription)
  getActions(MenuPlayer.ExitCommand) = (new Exit, MenuPlayer.ExitDescription)

  reactions += {
    case _: ExitApplication => exit()
    case _: CancelAddPlayer => exit()
    case _: PlayerAdded => logger.info(MenuPlayer.PlayerAdded)
  }

  protected override def preMenuList(): Unit = {
    super.preMenuList()
    logger.info(MenuPlayer.MenuHeading)
  }

  private class Player extends Action {
    override def execute(): Unit = {
      playerName.process()
      outputMenuList()
    }
  }

  private class Cancel extends Action {
    override def execute(): Unit = controller.cancelAddPlayer()
  }

  private class Exit extends Action {
    override def execute(): Unit = controller.exitApplication()
  }
}

object MenuPlayer {
  val ExitCommand = "x"
  val ExitDescription = "Exit"
  val CancelCommand = "c"
  val CancelDescription = "Cancel"
  val MenuHeading = "# PLAYER-MENU #"
  val PlayerCommand = "p"
  val PlayerDescription = "Add player"
  val PlayerAdded = "PlayerAdded"
  def apply(controller: Controller): MenuPlayer = new MenuPlayer(controller, MenuPlayerName(controller))
}
