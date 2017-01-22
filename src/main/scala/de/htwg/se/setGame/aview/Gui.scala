package de.htwg.se.setGame.aview

import javax.swing.border.EmptyBorder

import de.htwg.se.setGame.aview.gui.ButtonGenerator
import de.htwg.se.setGame.controller._
import de.htwg.se.setGame.model.{ Card, Game, Player }

import scala.swing.event.ButtonClicked
import scala.swing.{ Dimension, _ }

/**
 * @author Philipp Daniels
 */
class Gui(private val controller: Controller) extends MainFrame {
  listenTo(controller)
  title = Gui.Title
  visible = true
  visible = true
  visible = true
  val buttonGenerator = ButtonGenerator(controller)
  preferredSize = new Dimension(Gui.xLayoutSize, Gui.yLayoutSize)
  private val label = new Label {

  }
  contents = new FlowPanel {
    contents += label
    contents += buttonGenerator.createNewGameButton()
  }

  def addPlayer(s: String): Unit = {
    controller.addPlayer(s)
  }

  def closeMe() {
    close()
    dispose()
  }

  override def closeOperation(): Unit = {
    controller.exitApplication()
  }

  def refreshField(game: Game): Unit = {
    contents = new FlowPanel() {
      contents += createSetFieldPanel(game)
      contents += new GridPanel(2, 1) {
        contents += createInformationPanel(game)
        contents += createOptionsMenu(game)
      }
    }
  }

  def createSetFieldPanel(game: Game): GridPanel = {
    new GridPanel(Gui.xFieldSize, Gui.yFieldSize) {
      for (card <- game.cardsInField) {
        val button = buttonGenerator.createSetCardPanel(card)
        contents += new BorderPanel() {
          border = new EmptyBorder(Gui.sizeOfBorderBtwCardsTop, Gui.sizeOfBorderBtwCardsLeftAndRight, 0, Gui.sizeOfBorderBtwCardsLeftAndRight)
          add(button, BorderPanel.Position.Center)
        }
      }
    }
  }

  def showAddUser(game: Game): Unit = {

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += buttonGenerator.createUserNameButton()
      }
    }
  }

  def startGame(): Unit = {

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Button("Start Game") {
          reactions += {
            case ButtonClicked(_) => controller.startGame()
          }
        }
        contents += new BoxPanel(Orientation.Horizontal) {
          contents += buttonGenerator.createUserNameButton()

        }

      }
    }
  }

  def startNewGame(): Unit = {
    controller.createNewGame()
  }

  def showWinnerDialog(game: Game): Unit = {
    val topPlayer = game.player.reduceLeft(max)
    Dialog.showMessage(contents.head, "Winner : " + topPlayer.name + " with : " + topPlayer.points, title = "Finish Game!")
    controller.createNewGame()
  }

  def max(s1: Player, s2: Player): Player = if (s1.points > s2.points) s1 else s2

  def createInformationPanel(game: Game): BoxPanel = {
    new BoxPanel(Orientation.Vertical) {
      for (player <- game.player) {
        contents += new Label() {
          text = player.name + " : " + player.points
        }
      }
      contents += new Label() {
        text = "Cards in deck : " + game.pack.size
      }
    }
  }

  def createOptionsMenu(game: Game): BoxPanel = {
    new BoxPanel(Orientation.Vertical) {
      for (player <- game.player) {
        contents += buttonGenerator.createSetButton(player)
      }
      contents += buttonGenerator.createFinishButon()
      contents += buttonGenerator.createRandomButton()
      contents += buttonGenerator.createNewGameButton()
    }
  }

  reactions += {
    case e: NewGame => showAddUser(e.game)
    case e: StartGame => refreshField(e.game)
    case _: PlayerAdded => startGame()
    case _: IsSet => Dialog.showMessage(contents.head, "Set correct!", title = "You are Great!!")
    case _: IsInvalidSet => Dialog.showMessage(contents.head, "Set wrong!", title = "Try Again!!")
    case e: UpdateGame => refreshField(e.game)
    case e: FinishGame => showWinnerDialog(e.game)
    case _: ExitApplication => closeMe()

  }

}

object Gui {
  val Title = "SetGame"
  val xSizeOfCard = 90
  val ySizeOfCard = 140

  val sizeOfSelectBorder = 5

  val sizeOfBorderBtwCardsTop = 10
  val sizeOfBorderBtwCardsLeftAndRight = 20

  val xLayoutSize = 600
  val yLayoutSize = 800
  val xFieldSize = 3
  val yFieldSize = 4
  var setSet = false
  var setList = List[Card]()
  var playerSet = Player(0, 0, "")

  def apply(controller: Controller): Gui = new Gui(controller)
}