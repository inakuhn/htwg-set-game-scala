package de.htwg.se.setGame

import java.awt.{Color, Dimension, Toolkit}
import javax.swing.ImageIcon

import de.htwg.se.setGame.model.Game

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

  preferredSize = new Dimension(1500, 500)
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
  val s = new Dimension(10, 20)
  def refreshField(game: Game): Unit = {
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new GridPanel(4,4) {
        for (card <- game.cardsInField) {

          contents += new Button() {
            val myCard = card
            minimumSize = s
            maximumSize = s
            preferredSize = s
            background = Color.white
            icon = new ImageIcon(ClassLoader.getSystemResource("pack/" + card.name + ".gif").getFile)
            reactions += {
              case _: ButtonClicked => {
                println(myCard)
              }
            }
          }
        }
        contents += new Label() {
          text = game.player.head.name + " : " + game.player.head.points
        }
        contents += new Label() {
          text =game.player.last.name + " : " + game.player.last.points
        }
        contents += new Label() {
          text ="Cards in deck : " + game.pack.size
        }
      }
    }
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
      def changeText() {
        val r = Dialog.showInput(contents.head, "User Name", initial = "user name")
        r match {
          case Some(s) => controller.addPlayer(s)
          case None =>
        }
      }
    }


    def startGame(): Unit = {

      contents = new BoxPanel(Orientation.Vertical) {
        contents += label
        contents += new BoxPanel(Orientation.Horizontal) {
          val button = new Button("Start Game") {
          }
          listenTo(button)
          reactions += {
            case ButtonClicked(button) => controller.startGame()
          }
          contents += button
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
      case e: ExitApplication => closeMe()

    }

  }

  object Gui {
    val Title = "SetGame"

    def apply(controller: Controller): Gui = new Gui(controller)
  }