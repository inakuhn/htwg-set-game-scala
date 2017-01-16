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

  class ControllerSpy(system: ActorSystem) extends ControllerActorSystem(system) {
    var exitAppCalled = false
    var createNewGameCalled = false
    var addPlayerCalled = false
    var cancelPlayerCalled = false

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

    override def createNewGame(): Unit = {
      super.createNewGame()
      createNewGameCalled = true
    }

    override def addPlayer(name: String): Unit = {
      super.addPlayer(name)
      addPlayerCalled = true
    }

    override def cancelAddPlayer(): Unit = {
      super.cancelAddPlayer()
      cancelPlayerCalled = true
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
      val stream = new ByteArrayInputStream(Tui.MainCommandExit.getBytes)
      Console.withIn(stream) {
        val target = Tui(controller)

        listenerList contains target

        val logs = testAppender.logAsString()
        logs.length should be > 0
        logs should include(Tui.InitiateMessage)
        logs should include(Tui.MainMenuHeading)
        logs should include(Tui.Shutdown)

        controller.exitAppCalled should be(true)
        controller.createNewGameCalled should be(false)
      }
    }

    "detect unknown meny entry" in withController { (testAppender, controller) =>
      val input = "unknown" + lineBreak + Tui.MainCommandExit
      val stream = new ByteArrayInputStream(input.getBytes)
      Console.withIn(stream) {
        val target = Tui(controller)

        val logs = testAppender.logAsString()
        logs.length should be > 0
        logs should include(Tui.UnknownMenuEntry.format("unknown"))

        controller.exitAppCalled should be(true)
        controller.createNewGameCalled should be(false)
      }
    }

    "have cancel addPlayer" in withController { (testAppender, controller) =>
      val input = Tui.MainCommandCreate + lineBreak +
        Tui.PlayerCommandCancel + lineBreak +
        Tui.MainCommandExit + lineBreak
      val stream = new ByteArrayInputStream(input.getBytes)
      Console.withIn(stream) {
        val target = Tui(controller)

        val logs = testAppender.logAsString()
        logs.length should be > 0
        logs should include(Tui.PlayerMenuHeading)
        logs should not.include(Tui.RequestPlayerName)
        logs should include(Tui.Shutdown)

        controller.exitAppCalled should be(true)
        controller.createNewGameCalled should be(true)
        controller.addPlayerCalled should be(false)
        controller.cancelPlayerCalled should be(true)
        controller.exitAppCalled should be(true)
      }
    }

    "have addPlayer with name" in withController { (testAppender, controller) =>
      val input = Tui.MainCommandCreate + lineBreak +
        Tui.PlayerCommandPlayer + lineBreak +
        "player" + lineBreak +
        Tui.PlayerCommandCancel + lineBreak +
        Tui.MainCommandExit + lineBreak
      val stream = new ByteArrayInputStream(input.getBytes)
      Console.withIn(stream) {
        val target = Tui(controller)

        val logs = testAppender.logAsString()
        logs.length should be > 0
        logs should include(Tui.PlayerMenuHeading)
        logs should include(Tui.RequestPlayerName)
        logs should include(Tui.Shutdown)

        controller.exitAppCalled should be(true)
        controller.createNewGameCalled should be(true)
        controller.addPlayerCalled should be(true)
        controller.cancelPlayerCalled should be(true)
        controller.exitAppCalled should be(true)
      }
    }


    "have exit addPlayer" in withController { (testAppender, controller) =>
      val input = Tui.MainCommandCreate + lineBreak +
        Tui.MainCommandExit + lineBreak
      val stream = new ByteArrayInputStream(input.getBytes)
      Console.withIn(stream) {
        val target = Tui(controller)

        val logs = testAppender.logAsString()
        logs.length should be > 0
        logs should include(Tui.PlayerMenuHeading)
        logs should not.include(Tui.RequestPlayerName)
        logs should include(Tui.Shutdown)

        controller.exitAppCalled should be(true)
        controller.createNewGameCalled should be(true)
        controller.addPlayerCalled should be(false)
        controller.cancelPlayerCalled should be(false)
        controller.exitAppCalled should be(true)
      }
    }
  }
}
