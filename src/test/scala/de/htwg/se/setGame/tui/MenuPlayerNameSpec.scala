package de.htwg.se.setGame.tui

import java.io.ByteArrayInputStream

import de.htwg.se.setGame._
import org.apache.log4j.Logger
import org.scalatest.WordSpec
import org.scalatest.Matchers._

import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
class MenuPlayerNameSpec extends WordSpec {
  private def withLogger(test: (TestAppender) => Any) = {
    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender)

    try test(testAppender)
    finally {}
  }

  "MenuPlayerName" should {
    val assertEvent = (event: Event, logger: TestAppender) => {
      val target = MenuPlayerName(new ControllerDummy {
        override def addPlayer(name: String): Unit = {
          publish(event)
        }
      })
      val stream = new ByteArrayInputStream("Playername".getBytes)
      Console.withIn(stream) {
        target.process()
        logger.logAsString() should include (Menu.ReadInput.format("Playername"))
      }
    }

    "have called controler with Input" in withLogger { (logger) =>
      var playername = ""
      val target = MenuPlayerName(new ControllerDummy {
        override def addPlayer(name: String): Unit = {
          playername = name
          publish(PlayerAdded())
        }
      })
      val stream = new ByteArrayInputStream("Playername".getBytes)
      Console.withIn(stream) {
        target.process()
      }
      playername should equal("Playername")
      logger.logAsString() should include (MenuPlayerName.RequestPlayerName)
    }

    "has stopped on PlayerAdded event" in withLogger { (logger) =>
      assertEvent(PlayerAdded(), logger)
    }

    "has stopped on CancelAddPlayer event" in withLogger { (logger) =>
      assertEvent(CancelAddPlayer(), logger)
    }

    "has stopped on ExitApplication event" in withLogger { (logger) =>
      assertEvent(ExitApplication(), logger)
    }
  }
}
