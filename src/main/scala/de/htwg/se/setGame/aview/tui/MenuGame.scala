package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.aview.Tui
import de.htwg.se.setGame.model.{Card, Game}
import de.htwg.se.setGame.{Controller, StartGame}

/**
  * @author Philipp Daniels
  */
class MenuGame(private val controller: Controller, private val tui: Tui) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuGame.ExitCommand) = (new Exit, MenuGame.ExitDescription)

  reactions += {
    case e: StartGame =>
      logger.info(MenuGame.EventStartGame)
      tui.menu = this
      showGameField(e.game)
      outputMenuList()
  }

  private class Exit extends Action {
    override def execute(): Unit = controller.exitApplication()
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
  val FieldHeading = "# GAME-FIELD #"
  val CardFormat = "Card %2s: %s, %s, %s, %s"
  val EventStartGame = "Received 'StartGame' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit"
  val MenuHeading = "# GAME-MENU #"

  def apply(controller: Controller, tui: Tui): MenuGame = new MenuGame(controller, tui)
}
