package de.htwg.se.setGame.aview

import java.awt.Color
import javax.swing.ImageIcon
import javax.swing.border.{EmptyBorder, LineBorder}

import de.htwg.se.setGame.actor.CardActor
import de.htwg.se.setGame.controller.{Controller, _}
import de.htwg.se.setGame.model.{Card, Game, Player}

import scala.swing.event.ButtonClicked
import scala.swing.{Dimension, _}

/**
  * @author Philipp Daniels
  */
class Gui(private val controller: Controller) extends MainFrame {
  listenTo(controller)
  title = Gui.Title
  visible = true
  visible = true
  visible = true

  preferredSize = new Dimension(Gui.xLayoutSize, Gui.yLayoutSize)
  private val label = new Label {

  }
  contents = new FlowPanel {
    contents += label
    contents += createNewGameButton()
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


  //Hier kommt er nicht rein
  val s = new Dimension(Gui.xSizeOfCard, Gui.ySizeOfCard)

  var setSet = false
  private var setList = List[Card]()
  var playerSet = Player(0, 0, "")
  def refreshField(game: Game): Unit = {
    contents = new FlowPanel() {
      contents += createSetFieldPanel(game)
      contents += new GridPanel(2, 1) {
        contents += createInformationPanel(game)
        contents += createOptionsMenu(game)
      }
    }
  }
  def createSetFieldPanel(game: Game) : GridPanel = {
    new GridPanel(Gui.xFieldSize, Gui.yFieldSize) {
      for (card <- game.cardsInField) {
        val button = createSetCardPanel(card)
        contents += new BorderPanel() {
          border = new EmptyBorder(Gui.sizeOfBorderBtwCardsTop, Gui.sizeOfBorderBtwCardsLeftAndRight, 0, Gui.sizeOfBorderBtwCardsLeftAndRight)
          add(button, BorderPanel.Position.Center)
        }
      }
    }
  }
  def createSetCardPanel(card: Card) : Button = {
    new Button() {
      private val myCard = card
      minimumSize = s
      maximumSize = s
      preferredSize = s
      background = Color.white
      icon = new ImageIcon(ClassLoader.getSystemResource("pack/" + card.name + ".gif").getFile)

      reactions += {
        case _: ButtonClicked =>
          if (setSet) {
            border = new LineBorder(Color.ORANGE, Gui.sizeOfSelectBorder)
            println(myCard)
            setList = setList :+ myCard
            if (setList.size == CardActor.setMax) {
              controller.checkSet(setList, playerSet)
              setList = List[Card]()
              setSet = false
            }
          } else {
            selectSetFirst()
          }
      }
    }
  }

  def createInformationPanel(game: Game) : BoxPanel = {
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
  def createOptionsMenu(game: Game) : BoxPanel = {
    new BoxPanel(Orientation.Vertical) {
      for (player <- game.player) {
        contents += createSetButton(player)
      }
      contents += createFinishButon()
      contents += createRandomButton()

      contents += createNewGameButton()
    }
  }
  def createSetButton(player: Player): Button = {
    new Button("Set : " + player.name) {
      private val playerB = player
      reactions += {
        case _: ButtonClicked =>
          setSet = true
          playerSet = playerB
          chooseCards(playerSet)
      }
    }
  }
  def createNewGameButton(): Button = {
    new Button("New Game") {
      reactions += {
        case _: ButtonClicked =>
          controller.createNewGame()
      }
    }
  }
  def createRandomButton(): Button = {
    new Button("Random field cards") {
      reactions += {
        case _: ButtonClicked =>
          controller.randomCardsInField()
      }
    }
  }
  def createFinishButon() : Button = {
    new Button("Finish Game") {
      reactions += {
        case _: ButtonClicked =>
          controller.finishGame()
      }
    }
  }

  def selectSetFirst() {
    Dialog.showMessage(contents.head, "Before Choose card please press Set button", title = "Press Set!")
  }

  def chooseCards(player: Player) {
    Dialog.showMessage(contents.head, player.name + " you can choose 3 cards", title = "Your Turn!")
  }

  def showAddUser(game: Game): Unit = {

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += createUserNameButton()
      }
    }
  }


  def startGame(): Unit = {

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Button("Start Game") {
          reactions += {
            case ButtonClicked(button) => controller.startGame()
          }
        }
        contents += new BoxPanel(Orientation.Horizontal) {
          contents += createUserNameButton()


        }

      }
    }
  }

  def createUserNameButton() : Button = {
    new Button("User Name") {
      reactions += {
        case ButtonClicked(button) => changeText()
      }
    }
  }

  def changeText() {
    val r = Dialog.showInput(contents.head, "User Name", initial = "user name")
    r match {
      case Some(value) => controller.addPlayer(value)
      case None =>
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

  reactions += {
    case e: NewGame => showAddUser(e.game)
    case e: StartGame => refreshField(e.game)
    case _: PlayerAdded =>
      startGame()
    case _: IsSet =>
      Dialog.showMessage(contents.head, "Set correct!", title = "You are Great!!")
    case _: IsInvalidSet =>
      Dialog.showMessage(contents.head, "Set wrong!", title = "Try Again!!")
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

  def apply(controller: Controller): Gui = new Gui(controller)
}