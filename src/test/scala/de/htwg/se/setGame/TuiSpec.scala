package de.htwg.se.setGame

import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import scala.collection.mutable.ListBuffer
import scala.swing.Reactions.Reaction

/**
  * @author Philipp Daniels
  */
class TuiSpec extends WordSpec {
  "Tui" should {
    val listenerList = new ListBuffer[Reaction]()

    class ControllerSpy extends Controller {
      override def subscribe(listener: Reaction): Unit = {
        listenerList += listener
      }
    }

    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender)

    val controller = new ControllerSpy
    val target = Tui(controller)

    "called listenTo" in {
      listenerList should have length 2
      listenerList contains target
      listenerList contains controller
    }

    "has initiate message" in {
      testAppender.getLog().length should be >0
      testAppender.getLog should include (Tui.InitiateMessage)
    }
  }
}
