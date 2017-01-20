package de.htwg.se.setGame

import java.awt.{Color, Dimension, Toolkit}
import javax.swing.{BoxLayout, ImageIcon, JOptionPane}
import javax.swing.border.{Border, EmptyBorder, LineBorder}

import de.htwg.se.setGame.actor.CardActor
import de.htwg.se.setGame.model.{Card, Game, Player}
import sun.invoke.empty.Empty

import scala.swing.{Dimension, _}
import scala.swing.event.ButtonClicked

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
    val button = new Button("Create Game") {
    }
    listenTo(button)
    reactions += {
      case ButtonClicked(button) => startNewGame()
    }
    contents += button
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


  def refreshField(game: Game): Unit = {
    var setSet = false
    var setList = List[Card]()
    var playerSet = Player(0,0,"")
    contents = new FlowPanel() {
      contents += new GridPanel(Gui.xFieldSize, Gui.yFieldSize) {
        for (card <- game.cardsInField) {
          val button = new Button() {
            val myCard = card
            minimumSize = s
            maximumSize = s
            preferredSize = s
            background = Color.white
            icon = new ImageIcon(ClassLoader.getSystemResource("pack/" + card.name + ".gif").getFile)

            reactions += {
              case _: ButtonClicked => {
                if (setSet) {
                  border = new LineBorder(Color.ORANGE, Gui.sizeOfSelectBorder)
                  println(myCard)
                  setList = setList :+ myCard
                  if (setList.size == CardActor.setMax) {
                    controller.checkSet(setList, playerSet)
                  }
                } else {
                  selectSetFirst()
                }
              }
            }
          }
          contents += new BorderPanel() {
            border = new EmptyBorder(Gui.sizeOfBorderBtwCardsTop, Gui.sizeOfBorderBtwCardsLeftAndRight, 0, Gui.sizeOfBorderBtwCardsLeftAndRight)
            add(button, BorderPanel.Position.Center)
          }
        }
      }
      contents += new GridPanel(2, 1) {
        contents += new BoxPanel(Orientation.Vertical) {
          for (player <- game.player) {
            contents += new Label() {
              text = player.name + " : " + player.points
            }
          }
          contents += new Label() {
            text = "Cards in deck : " + game.pack.size
          }
        }
        contents += new BoxPanel(Orientation.Vertical) {
          for(player <- game.player){
          contents += new Button("Set : "+player.name) {
            val playerB = player
            reactions += {
              case _: ButtonClicked => {
                setSet = true
                playerSet = playerB
              }
            }
          }

          }
          contents += new Button("Finish Game") {
            reactions += {
              case _: ButtonClicked => {
                controller.startGame()
              }
            }
          }

          contents += new Button("New Game") {
            reactions += {
              case _: ButtonClicked => {
                controller.createNewGame()
              }
            }
          }
        }
      }
    }
  }


  def selectSetFirst() {
    Dialog.showMessage(contents.head, "Before Choose card please press Set button", title = "Press Set!")

  }

  def showFormular(game: Game): Unit = {

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      contents += new BoxPanel(Orientation.Horizontal) {
        val button = new Button("User Name") {
        }
        listenTo(button)
        reactions += {
          case ButtonClicked(button) => changeText()
        }
        contents += button
      }
    }
  }

  def changeText() {
    val r = Dialog.showInput(contents.head, "User Name", initial = "user name")
    r match {
      case Some(s) => controller.addPlayer(s)
      case None =>
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
          contents += new Button("User Name") {
            reactions += {
              case ButtonClicked(button) => changeText()
            }
          }


        }

      }
    }
  }

  def startNewGame(): Unit = {
    controller.createNewGame()
  }

  reactions += {
    case e: NewGame => showFormular(e.game)
    case e: StartGame => refreshField(e.game)
    case e: PlayerAdded => {
      startGame()
    }
    case e : IsSet => {
      Dialog.showMessage(contents.head, "Set correct!", title="You are Great!!")
    }
    case e : IsInvalidSet =>{
      Dialog.showMessage(contents.head, "Set wrong!", title="Try Again!!")
    }
    case e : UpdateGame => refreshField(e.game)

    case e: ExitApplication => closeMe()

  }

}

object Gui {
  val Title = "SetGame"
  val xSizeOfCard = 90
  val ySizeOfCard = 140

  val sizeOfSelectBorder = 5

  val sizeOfBorderBtwCardsTop = 10
  val sizeOfBorderBtwCardsLeftAndRight = 20

  val xLayoutSize = 500
  val yLayoutSize = 800
  val xFieldSize = 3
  val yFieldSize = 4


  def apply(controller: Controller): Gui = new Gui(controller)
}