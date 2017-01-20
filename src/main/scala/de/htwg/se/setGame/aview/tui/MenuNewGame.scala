package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.aview.Tui
import de.htwg.se.setGame.controller.{Controller, NewGame, PlayerAdded}
import de.htwg.se.setGame.{model, _}

/**
  * @author Philipp Daniels
  */
class MenuNewGame(private val controller: Controller, private val tui: Tui, private val playerName: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuNewGame.PlayerCommand) = (new Player, MenuNewGame.PlayerDescription)
  getActions(MenuNewGame.StartCommand) = (new Start, MenuNewGame.StartDescription)
  getActions(MenuNewGame.CancelCommand) = (new Cancel, MenuNewGame.CancelDescription)
  getActions(MenuNewGame.ExitCommand) = (new Exit, MenuNewGame.ExitDescription)

  reactions += {
    case e: PlayerAdded =>
      logger.info(MenuNewGame.EventPlayerAdded)
      val formatter = (p: model.Player) => {
        MenuNewGame.PlayerFormat.format(p.name, p.points)
      }
      val player = e.game.player.map(formatter)
      logger.info(MenuNewGame.PlayerList.format(player.mkString(", ")))
      tui.menu = this
      outputMenuList()
    case _: NewGame =>
      logger.info(MenuNewGame.EventNewGame)
      tui.menu = this
      outputMenuList()
  }

  protected override def preMenuList(): Unit = {
    super.preMenuList()
    logger.info(MenuNewGame.MenuHeading)
  }

  private class Player extends Action {
    override def execute(): Unit = {
      tui.menu = playerName
      playerName.outputMenuList()
    }
  }

  private class Cancel extends Action {
    override def execute(): Unit = controller.cancelAddPlayer()
  }

  private class Exit extends Action {
    override def execute(): Unit = controller.exitApplication()
  }

  private class Start extends Action {
    override def execute(): Unit = controller.startGame()
  }

}

object MenuNewGame {
  val CancelCommand = "c"
  val CancelDescription = "Cancel"
  val EventPlayerAdded = "Received 'PlayerAdded' event"
  val EventNewGame = "Received 'NewGame' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit"
  val MenuHeading = "# PLAYER-MENU #"
  val PlayerCommand = "a"
  val PlayerDescription = "Add player"
  val PlayerFormat = "%s (%d points)"
  val PlayerList = "Player: %s"
  val StartCommand = "s"
  val StartDescription = "Start game"

  def apply(controller: Controller, tui: Tui): MenuNewGame = {
    new MenuNewGame(controller, tui, MenuPlayerName(controller))
  }
}
