package de.htwg.se.setGame

import java.awt.{Color, Toolkit}

import de.htwg.se.setGame.aview.gui.components.GameField
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

  preferredSize = Toolkit.getDefaultToolkit.getScreenSize
  private val label = new Label {
    text = "Hello World"

  }

  contents = new FlowPanel {
    contents += label
    contents += new Button("Start Game") {
      reactions += {
        case _: ButtonClicked => {
          controller.createNewGame()
        }
      }

    }
  }


  def addPlayer(s: String): Unit = {
    controller.addPlayer(s)
  }

  def pressMe() {
    Dialog.showMessage(contents.head, "Thank you!", title = "You pressed me")
  }

  def closeMe() {
      close()
      dispose()
  }

  override def closeOperation(): Unit = {
    controller.exitApplication()
  }

  def showNameDialog(): Unit = {
    val r = Dialog.showInput(contents.head, "New label text", initial="Player name")
    r match {
      case Some(s) => controller.addPlayer(s)
      case None =>
    }
  }

  def startNewGame(): Unit = {
    pressMe()
  }

  reactions += {
    case e: NewGame => showNameDialog()
    case e: StartGame => pressMe()
    case e: PlayerAdded => startNewGame()
    case e: ExitApplication => closeMe()
  }

}

object Gui {
  val Title = "SetGame"

  def apply(controller: Controller): Gui = new Gui(controller)
}