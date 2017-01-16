package de.htwg.se.setGame

import java.awt.Toolkit
import javafx.scene.paint.Color
import javax.swing.ImageIcon

import de.htwg.se.setGame.aview.gui.Field
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


  contents = new FlowPanel {
    contents += label
    val button = Field(false, new Card(CardAttribute.Form.balk, CardAttribute.Color.green, CardAttribute.Fill.halfFilled, CardAttribute.Count.one))

    val source = ClassLoader.getSystemResource("pack/" + button.card.name + ".gif")
    button.icon = new ImageIcon(source.getFile)
    listenTo(button)
    contents += button

  }

  override def closeOperation(): Unit = {
    controller.exitApplication()
  }

  reactions += {
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