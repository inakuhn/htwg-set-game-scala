package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{model, _}

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
    case e: PlayerAdded =>
      logger.info(MenuPlayer.PlayerAdded)
      val formatter = (p: model.Player) => {MenuPlayer.PlayerFormat.format(p.name, p.points)}
      val player = e.game.player.map(formatter)
      logger.info(MenuPlayer.PlayerList.format(player.mkString(", ")))
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
  val PlayerCommand = "a"
  val PlayerDescription = "Add player"
  val PlayerAdded = "PlayerAdded"
  val PlayerFormat = "%s (%d points)"
  val PlayerList = "Player: %s"
  def apply(controller: Controller): MenuPlayer = new MenuPlayer(controller, MenuPlayerName(controller))
}
