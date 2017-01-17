package de.htwg.se.setGame

import java.awt.Toolkit

import de.htwg.se.setGame.aview.gui.ImageIconGenerator
import de.htwg.se.setGame.model.Card

import scala.swing._
import scala.swing.event.ButtonClicked

/**
  * @author Philipp Daniels
  */
class Gui(private val controller: Controller) extends MainFrame {
  listenTo(controller)

  private val imageIconGenerator = new ImageIconGenerator

  reactions += {
    case e : AddPlayer => {
      frame.updateGame
    }
  }
  val frame = new Frame {

    private var currentPlayer = 0
    private var selectedCards = List.empty[Card]

    title = "SetGame"
    menuBar = new MenuBar {
      contents += new Menu("SetGame") {
        contents += new MenuItem(Action("New Game") {
          controller.createNewGame()
        })
        contents += new MenuItem(Action("Quit Game") {
          System.exit(0)
        })
      }
    }

    this.updateGame

    contents = new FlowPanel() {
      print(controller.getCardsInField().size)
      controller.getCardsInField().map(card => {
        contents += new Button {
          icon = imageIconGenerator.getImageIcon(card)
        }
      })
    }

    def addToSelectedCards(card: Card) = {
      if (currentPlayer != 0 && this.selectedCards.size <= 3) {
        this.selectedCards ::= card
      }
      if (this.selectedCards.size == 3) {

      }
    }

    def updateGame: Unit = {
      contents = new BoxPanel(orientation = Orientation.Horizontal) {
        controller.getCardsInField().map(card => {
          val button = new Button() {
            icon = imageIconGenerator.getImageIcon(card)
            reactions += {
              case _: ButtonClicked => addToSelectedCards(card)
            }
          }
          contents += button
        })
        contents += new Button("Player1") {
          text = "SET Player 1"
          reactions += {
            case _: ButtonClicked => {
              currentPlayer = 1

            }
          }
        }
        contents += new Button("Player2") {
          text = "Set Player 2"
          reactions += {
            case _: ButtonClicked => {
              currentPlayer = 2

            }
          }
        }

        contents += new Label("infos") {
          text = "Player 1: " + "Ina" + "\n" +
            "Player 2: " + "Phllip"
        }
      }
    }


    peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    val dim = Toolkit.getDefaultToolkit.getScreenSize.height - 100
    size = new Dimension(dim, dim)
    visible = true

  }

}

object Gui {
  val Title = "SetGame"

  def apply(controller: Controller): Gui = new Gui(controller)
}