package de.htwg.se.setGame

import java.awt.Toolkit
import javafx.scene.paint.Color
import javax.swing.ImageIcon

import de.htwg.se.setGame.aview.gui.{Field, GamePanel}
import de.htwg.se.setGame.model.{Card, CardAttribute}

import scala.swing._
import scala.swing.event.{ButtonClicked, MouseClicked}

/**
  * @author Philipp Daniels
  */
class Gui(private val controller: Controller) extends MainFrame {
  listenTo(controller)
  title = Gui.Title
  visible = true


  preferredSize = Toolkit.getDefaultToolkit.getScreenSize
  private val label = new Label {

  }


  val frame  = new GamePanel(controller)
  contents = frame

  override def closeOperation(): Unit = {
    controller.exitApplication()
  }

  reactions += {
    case e: ExitApplication => exit()
    case e: AddPlayer => frame.setCardsInField()
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