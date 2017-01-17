package de.htwg.se.setGame

import java.awt.Toolkit
import javafx.scene.paint.Color
import javax.swing.ImageIcon

import de.htwg.se.setGame.aview.gui.{GamePanel, ImageIconGenerator}
import de.htwg.se.setGame.model.{Card, CardAttribute}

import scala.io.Source
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
  val imageIconGenerator = new ImageIconGenerator()


  override def closeOperation(): Unit = {
    controller.exitApplication()
  }
    contents = new FlowPanel() {
      controller.createCards()
      controller.getCardsInField().map(card => {
        val button = new Button {
          val cardInButton = card
          val isSelected = false;

        }
        button.icon = imageIconGenerator.getImageIcon(card)
        contents += button
      })

  }
  def updateGame: Unit = {

    contents = new FlowPanel() {
      controller.getCardsInField().map(card => {
        contents += new Button {
          icon = imageIconGenerator.getImageIcon(card)
        }
      })
    }
  }
  reactions += {
    case e: ExitApplication => exit()
    case e: AddPlayer => updateGame
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