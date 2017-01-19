package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.model.{Card, Game}
import de.htwg.se.setGame.{Controller, ExitApplication, StartGame}

/**
  * @author Philipp Daniels
  */
class MenuGame(private val controller: Controller) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  getActions(MenuGame.ExitCommand) = (new Exit, MenuGame.ExitDescription)

  reactions += {
    case _: ExitApplication =>
      logger.info(MenuGame.EventExitApplication)
      exit()
    case e: StartGame =>
      logger.info(MenuGame.EventStartGame)
      showGameField(e.game)
  }

  private class Exit extends Action {
    override def execute(): Unit = controller.exitApplication()
  }

  private def showGameField(game: Game) {
    logger.info(MenuGame.FieldHeading)
    var index = 0
    val cardFormat = (c: Card) => {
      index += 1
      MenuGame.CardFormat.format(index, c.form, c.color, c.fill, c.count)
    }
    val gameField = game.cardsInField.map(cardFormat)
    logger.info(gameField.mkString(sys.props("line.separator")))
  }
}

object MenuGame {
  val FieldHeading = "# GAME-FIELD #"
  val CardFormat = "Card %d:  %s, %s, %s, %s"
  val EventExitApplication = "Received 'ExitApplication' event"
  val EventStartGame = "Received 'StartGame' event"
  val ExitCommand = "x"
  val ExitDescription = "Exit"
  def apply(controller: Controller): MenuGame = new MenuGame(controller)
}
