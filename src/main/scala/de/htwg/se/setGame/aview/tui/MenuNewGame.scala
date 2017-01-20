package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{model, _}

/**
  * @author Philipp Daniels
  */
class MenuNewGame(private val controller: Controller, private val playerName: Menu, private val game: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuNewGame.PlayerCommand) = (new Player, MenuNewGame.PlayerDescription)
  getActions(MenuNewGame.StartCommand) = (new Start, MenuNewGame.StartDescription)
  getActions(MenuNewGame.CancelCommand) = (new Cancel, MenuNewGame.CancelDescription)
  getActions(MenuNewGame.ExitCommand) = (new Exit, MenuNewGame.ExitDescription)

  reactions += {
    case e: PlayerAdded =>
      logger.info(MenuNewGame.EventPlayerAdded)
      val formatter = (p: model.Player) => {MenuNewGame.PlayerFormat.format(p.name, p.points)}
      val player = e.game.player.map(formatter)
      logger.info(MenuNewGame.PlayerList.format(player.mkString(", ")))
  }

  protected override def preMenuList(): Unit = {
    super.preMenuList()
    logger.info(MenuNewGame.MenuHeading)
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

  private class Start extends Action {
    override def execute(): Unit = controller.startGame()
  }
}

object MenuNewGame {
  val CancelCommand = "c"
  val CancelDescription = "Cancel"
  val EventCancelAddPlayer = "Received 'CancelAddPlayer' event"
  val EventExitApplication = "Received 'ExitApplication' event"
  val EventPlayerAdded = "Received 'PlayerAdded' event"
  val EventStartGame = "Received 'StartGame' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit"
  val MenuHeading = "# PLAYER-MENU #"
  val PlayerCommand = "a"
  val PlayerDescription = "Add player"
  val PlayerFormat = "%s (%d points)"
  val PlayerList = "Player: %s"
  val StartCommand = "s"
  val StartDescription = "Start game"
  def apply(controller: Controller): MenuNewGame = {
    new MenuNewGame(controller, MenuPlayerName(controller), MenuGame(controller))
  }
}
