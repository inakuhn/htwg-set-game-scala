package de.htwg.se.setGame

import scala.swing._

/**
  * @author Philipp Daniels
  */
class Gui(private val controller: Controller) extends MainFrame {
  listenTo(controller)
  title = Gui.Title
  visible = true

  contents = new FlowPanel {
    contents += new Label {
      text = "Hello World"
    }
  }
}

object Gui {
  val Title = "SetGame"
  def apply(controller: Controller): Gui = new Gui(controller)
}