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
    val res = Dialog.showConfirmation(contents.head,
      "Start new Game?",
      optionType=Dialog.Options.YesNo,
      title=title)
    if (res == Dialog.Result.Ok)
      controller.startGame()
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
//Hier kommt er nicht rein
  def startNewGame(game: Game): Unit = {
    contents = new GridPanel(3,4){
      for (card <- game.cardsInField)
        contents += new Button() {
          val myCard = card
          icon = new ImageIcon(ClassLoader.getSystemResource("pack/" + card.name + ".gif").getFile)
          reactions += {
            case _: ButtonClicked => {

            }
          }
        }

    }
  }

  reactions += {
    case e: NewGame => showNameDialog()
    case e: StartGame => startNewGame(e.game)
    case e: PlayerAdded => pressMe()
    case e: ExitApplication => closeMe()
  }

}

object Gui {
  val Title = "SetGame"

  def apply(controller: Controller): Gui = new Gui(controller)
}