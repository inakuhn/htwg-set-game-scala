package de.htwg.se.setGame

import java.awt.Toolkit

import de.htwg.se.setGame.aview.gui.components.GameField
import de.htwg.se.setGame.model.Game

import scala.swing._

/**
  * @author Philipp Daniels
  */
class Gui(private val controller: Controller) extends MainFrame {
  listenTo(controller)
  title = Gui.Title
  visible = true


  preferredSize = Toolkit.getDefaultToolkit.getScreenSize

  private val field = new GameField()


  contents = new FlowPanel {
    contents+= field
  }

  override def closeOperation(): Unit = {
    controller.exitApplication()
  }

  def startGame(game: Game): Unit = {
      field.setCardsIntoField(game)
  }

  reactions += {
    case e: NewGame => startGame(e.game)
    case e: ExitApplication => exit()
  }

  private def exit(): Unit = {
    close()
    dispose()
  }
}

object Gui {
  val Title = "SetGame"
  def apply(controller: Controller): Gui = new Gui(controller)
}