package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.aview.Tui
import de.htwg.se.setGame.model.{Card, Game}
import de.htwg.se.setGame.{Controller, IsInvalidSet, IsSet, StartGame}

/**
  * @author Philipp Daniels
  */
class MenuGame(private val controller: Controller, private val tui: Tui, private val set: Menu) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuGame.SetCommand) = (new Set, MenuGame.SetDescription)
  getActions(MenuGame.ExitCommand) = (new Exit, MenuGame.ExitDescription)

  reactions += {
    case e: StartGame =>
      logger.info(MenuGame.EventStartGame)
      tui.menu = this
      showGameField(e.game)
      outputMenuList()
    case _: IsSet =>
      tui.menu = this
      outputMenuList()
    case _: IsInvalidSet =>
      tui.menu = this
      outputMenuList()
  }

  private class Exit extends Action {
    override def execute(): Unit = controller.exitApplication()
  }

  private class Set extends Action {
    override def execute(): Unit = {
      logger.info(MenuGame.MenuSetSwitch)
      tui.menu = set
      set.outputMenuList()
    }
  }

  protected override def preMenuList(): Unit = {
    super.preMenuList()
    logger.info(MenuGame.MenuHeading)
  }

  private def showGameField(game: Game) {
    var index = 0
    val cardFormat = (c: Card) => {
      index += 1
      MenuGame.CardFormat.format(index.toString, c.form, c.color, c.fill, c.count)
    }
    val gameField = game.cardsInField.map(cardFormat)
    val lb = sys.props("line.separator")
    logger.info(MenuGame.FieldHeading + lb + gameField.mkString(lb))
  }
}

object MenuGame {
  val CardFormat = "Card %2s: %s, %s, %s, %s"
  val EventStartGame = "Received 'StartGame' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit"
  val FieldHeading = "# GAME-FIELD #"
  val MenuHeading = "# GAME-MENU #"
  val SetCommand = "s"
  val SetDescription = "Mark set"
  val MenuSetSwitch = "Switch to MenuSet"
  def apply(controller: Controller, tui: Tui): MenuGame = new MenuGame(controller, tui, MenuSet(controller))
}
