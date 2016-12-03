package de.htwg.se.setGame

import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.collection.mutable.ListBuffer
import scala.swing.Reactions.Reaction

/**
  * @author Philipp Daniels
  */
class GuiSpec extends WordSpec {
  "Gui" should {
    val listenerList = new ListBuffer[Reaction]()

    class ControllerSpy extends Controller {
      override def subscribe(listener: Reaction): Unit = {
        listenerList += listener
      }
    }

    val controller = new ControllerSpy
    val target = Gui(controller)

    "called listenTo" in {
      listenerList should have length 2
      listenerList contains target
      listenerList contains controller
    }

    "has title" in {
      target.title should be (Gui.Title)
    }

    "be visible" in {
      target.visible should be (true)
    }

  }
}
