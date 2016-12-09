package de.htwg.se.setGame

import java.io.ByteArrayInputStream

import akka.actor.ActorSystem
import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.collection.mutable.ListBuffer
import scala.swing.Reactions.Reaction

/**
  * @author Philipp Daniels
  */
class TuiSpec extends WordSpec {
  private val lineBreak = sys.props("line.separator")
  val listenerList = new ListBuffer[Reaction]()

  class ControllerSpy(system: ActorSystem) extends Controller(system) {
    var exitAppCalled = false

    def this() {
      this(null)
      listenerList.clear()
    }

    override def subscribe(listener: Reaction): Unit = {
      listenerList += listener
      listeners += listener
    }

    override def exitApplication(): Unit = {
      super.exitApplication()
      exitAppCalled = true
    }
  }

  private def withController(test: (TestAppender, ControllerSpy) => Any) = {
    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender)

    val controller = new ControllerSpy
    try test(testAppender, controller)
    finally {}
  }

  "Tui" should {

    "initiate and exit gracefully" in withController { (testAppender, controller) =>
      val stream = new ByteArrayInputStream(Tui.CommandExit.getBytes)
      Console.withIn(stream) {
        val target = Tui(controller)

        listenerList should have length 1
        listenerList contains target

        val logs = testAppender.logAsString()
        logs.length should be > 0
        logs should include(Tui.InitiateMessage)
        logs should include(Tui.Shutdown)

        controller.exitAppCalled should be(true)
      }
    }

    "detect unknown meny entry" in withController { (testAppender, controller) =>
      val input = "unknown" + lineBreak + Tui.CommandExit
      val stream = new ByteArrayInputStream(input.getBytes)
      Console.withIn(stream) {
        val target = Tui(controller)

        val logs = testAppender.logAsString()
        logs.length should be > 0
        logs should include(Tui.UnknownMenuEntry.format("unknown"))

        controller.exitAppCalled should be(true)
      }
    }

  }
}
