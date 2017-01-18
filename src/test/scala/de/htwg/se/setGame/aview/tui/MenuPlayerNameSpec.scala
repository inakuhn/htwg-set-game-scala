package de.htwg.se.setGame.aview.tui

import java.io.ByteArrayInputStream

import de.htwg.se.setGame._
import de.htwg.se.setGame.model.{Card, Game, Player}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
class MenuPlayerNameSpec extends WordSpec with TuiSpecExtension {

  "MenuPlayerName" should {
    val assertEvent = (event: Event, logger: TestAppender) => {
      val target = MenuPlayerName(new ControllerDummy {
        override def addPlayer(name: String): Unit = {
          publish(event)
        }
      })
      overrideConsoleIn("Playername") {
        target.process()
        logger.logAsString() should include (Menu.ReadInput.format("Playername"))
      }
    }

    "have called controler with Input" in withLogger { (logger) =>
      var playerName = ""
      val target = MenuPlayerName(new ControllerDummy {
        override def addPlayer(name: String): Unit = {
          playerName = name
          publish(PlayerAdded(Game(List[Card](), List[Card](), List[Player]())))
        }
      })
      overrideConsoleIn("Playername") {
        target.process()
      }
      playerName should equal("Playername")
      logger.logAsString() should include (MenuPlayerName.RequestPlayerName)
    }

    "has stopped on PlayerAdded event" in withLogger { (logger) =>
      assertEvent(PlayerAdded(Game(List[Card](), List[Card](), List[Player]())), logger)
    }

    "has stopped on CancelAddPlayer event" in withLogger { (logger) =>
      assertEvent(CancelAddPlayer(), logger)
    }

    "has stopped on ExitApplication event" in withLogger { (logger) =>
      assertEvent(ExitApplication(), logger)
    }
  }
}
