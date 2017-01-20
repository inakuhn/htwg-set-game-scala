package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{model, _}

/**
  * @author Philipp Daniels
  */
class MenuStart(private val controller: Controller, private val playerName: Menu, private val game: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuStart.PlayerCommand) = (new Player, MenuStart.PlayerDescription)
  getActions(MenuStart.StartCommand) = (new Start, MenuStart.StartDescription)
  getActions(MenuStart.CancelCommand) = (new Cancel, MenuStart.CancelDescription)
  getActions(MenuStart.ExitCommand) = (new Exit, MenuStart.ExitDescription)

  reactions += {
    case _: ExitApplication =>
      logger.info(MenuStart.EventExitApplication)
      exit()
    case _: CancelAddPlayer =>
      logger.info(MenuStart.EventCancelAddPlayer)
      exit()
    case e: PlayerAdded =>
      logger.info(MenuStart.EventPlayerAdded)
      val formatter = (p: model.Player) => {MenuStart.PlayerFormat.format(p.name, p.points)}
      val player = e.game.player.map(formatter)
      logger.info(MenuStart.PlayerList.format(player.mkString(", ")))
    case _: StartGame =>
      logger.info(MenuStart.EventStartGame)
      game.process()
  }

  protected override def preMenuList(): Unit = {
    super.preMenuList()
    logger.info(MenuStart.MenuHeading)
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

object MenuStart {
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
  def apply(controller: Controller): MenuStart = {
    new MenuStart(controller, MenuPlayerName(controller), MenuGame(controller))
  }
}
