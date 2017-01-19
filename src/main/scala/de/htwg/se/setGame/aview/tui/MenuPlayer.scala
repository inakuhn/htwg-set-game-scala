package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{model, _}

/**
  * @author Philipp Daniels
  */
class MenuPlayer(private val controller: Controller, private val playerName: Menu, private val game: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuPlayer.PlayerCommand) = (new Player, MenuPlayer.PlayerDescription)
  getActions(MenuPlayer.StartCommand) = (new Start, MenuPlayer.StartDescription)
  getActions(MenuPlayer.CancelCommand) = (new Cancel, MenuPlayer.CancelDescription)
  getActions(MenuPlayer.ExitCommand) = (new Exit, MenuPlayer.ExitDescription)

  reactions += {
    case _: ExitApplication =>
      logger.info(MenuPlayer.EventExitApplication)
      exit()
    case _: CancelAddPlayer =>
      logger.info(MenuPlayer.EventCancelAddPlayer)
      exit()
    case e: PlayerAdded =>
      logger.info(MenuPlayer.EventPlayerAdded)
      val formatter = (p: model.Player) => {MenuPlayer.PlayerFormat.format(p.name, p.points)}
      val player = e.game.player.map(formatter)
      logger.info(MenuPlayer.PlayerList.format(player.mkString(", ")))
    case _: StartGame =>
      logger.info(MenuPlayer.EventStartGame)
      game.process()
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

  private class Start extends Action {
    override def execute(): Unit = controller.startGame()
  }
}

object MenuPlayer {
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
  def apply(controller: Controller): MenuPlayer = {
    new MenuPlayer(controller, MenuPlayerName(controller), MenuGame(controller))
  }
}
