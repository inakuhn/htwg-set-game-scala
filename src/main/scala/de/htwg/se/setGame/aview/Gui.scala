package de.htwg.se.setGame

import java.awt.{Color, Toolkit}
import javax.swing.ImageIcon

import de.htwg.se.setGame.model.Game

import scala.swing._
import scala.swing.event.ButtonClicked

/**
  * @author Philipp Daniels
  */
class Gui(private val controller: Controller) extends MainFrame {
  listenTo(controller)
  title = Gui.Title
  visible = true
  visible = true

  contents = new FlowPanel {
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
  def refreshField(game: Game): Unit = {
    nameField.text = "hallo"
  }

  val nameField = new TextField {
    columns = 1
  }


  def showFormular(game: Game): Unit = {
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("My name")
        contents += nameField
      }
      val button = new Button("Enter Player Name") {
      }
      listenTo(button)
      reactions += {
        case ButtonClicked(button) => controller.addPlayer(nameField.text)
      }
      contents += button
    }
  }


  def startGame(): Unit = {
    contents = new BoxPanel(Orientation.Vertical) {
      val button = new Button("Start Game") {
      }
      listenTo(button)
      reactions += {
        case ButtonClicked(button) => controller.startGame()
      }
      contents += button
    }
  }

  def startNewGame(): Unit = {
    controller.createNewGame()
  }

  reactions += {
    case e: NewGame => showFormular(e.game)
    case e: StartGame => refreshField(e.game)
    case e: PlayerAdded => {
      nameField.text = e.game.player.head.name
      startGame()
    }
    case e: ExitApplication => closeMe()

  }

}

object Gui {
  val Title = "SetGame"

  def apply(controller: Controller): Gui = new Gui(controller)
}