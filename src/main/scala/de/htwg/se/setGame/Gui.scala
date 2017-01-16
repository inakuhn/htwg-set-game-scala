package de.htwg.se.setGame

import java.awt.Toolkit
import javax.swing.ImageIcon


import scala.swing._

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
    val button =  Button("Press me, please") {
      println("Thank you")
    }
    val source = ClassLoader.getSystemResource("pack/balkgreenemptyone.gif")
    button.icon = new ImageIcon(source.getFile)

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