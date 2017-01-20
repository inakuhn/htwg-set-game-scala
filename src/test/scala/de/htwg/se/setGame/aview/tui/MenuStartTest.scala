package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame._
import de.htwg.se.setGame.model.{Card, Game, Player}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
class MenuStartTest extends WordSpec with TuiSpecExtension {

  private val lineBreak = sys.props("line.separator")
  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  private def assertExitByEvent(event: Event): Unit = {
    val controller = new ControllerDummy
    val target = new MenuStart(controller, new MenuDummy, new MenuDummy)
    controller.publish(event)

    target.isContinue should be (false)
  }

  "MenuPlayer" should {
    val assertEvent = (logger: TestAppender, command: String, c: Controller) => {
      val target = new MenuStart(c, new MenuDummy, new MenuDummy)
      overrideConsoleIn(command) {
        target.process()
      }
      logger.logAsString should include (Menu.ReadInput.format(command))
    }

    "have stopped on ExitApplication event" in withLogger { (logger) =>
      assertEvent(logger, MenuStart.ExitCommand, new ControllerDummy {
        override def exitApplication(): Unit = publish(new ExitApplication)
      })
    }

    "have stopped on CancelAddPlayer event" in withLogger { (logger) =>
      assertEvent(logger, MenuStart.CancelCommand, new ControllerDummy {
        override def cancelAddPlayer(): Unit = publish(new CancelAddPlayer)
      })
    }

    "have called MenuPlayerName" in withLogger { (logger) =>
      var playerName = ""
      val controller = new ControllerDummy {
        override def addPlayer(name: String): Unit = {
          playerName = name
          publish(PlayerAdded(createEmptyGame))
          publish(new ExitApplication)
        }
      }
      overrideConsoleIn(MenuStart.PlayerCommand) {
        new MenuStart(controller, new MenuDummy {
          override def process(): Unit = controller.addPlayer("player")
        }, new MenuDummy).process()
      }

      val logs = logger.logAsString()
      logs should include (MenuStart.MenuHeading)
      logs should include (MenuStart.EventPlayerAdded)
      logs should include (MenuStart.PlayerList.format(""))
      playerName should be ("player")
    }

    "have called controller startGame" in withLogger { (logger) =>
      var called = false
      overrideConsoleIn(MenuStart.StartCommand) {
        new MenuStart(new ControllerDummy {
          override def startGame(): Unit = {
            called = true
            publish(new ExitApplication)
          }
        }, new MenuDummy, new MenuDummy).process()
      }

      val logs = logger.logAsString()
      logs should include (MenuStart.StartDescription)
      logs should include (Menu.ReadInput.format(MenuStart.StartCommand))
      called should be (true)
    }

    "have listener on StartGame event" in withLogger { (logger) =>
      var called = false
      val controller = new ControllerDummy
      new MenuStart(controller, new MenuDummy, new MenuDummy {
        override def process(): Unit = called = true
      })
      controller.publish(StartGame(createEmptyGame))

      called should be (true)
      logger.logAsString() should include (MenuStart.EventStartGame)
    }

    "have listener on ExitApplication event" in withLogger { (logger) =>
      assertExitByEvent(new ExitApplication)
      logger.logAsString() should include (MenuStart.EventExitApplication)
    }

    "have listener on CancelAddPlayer event" in withLogger { (logger) =>
      assertExitByEvent(new CancelAddPlayer)
      logger.logAsString() should include (MenuStart.EventCancelAddPlayer)
    }

    "have unknown menu-entry fallback" in withLogger { (logger) =>
      val target = new MenuStart(new ControllerDummy {
        override def exitApplication(): Unit = publish(new ExitApplication)
      }, new MenuDummy, new MenuDummy)

      val input = "test" + lineBreak + MenuStart.ExitCommand
      overrideConsoleIn(input) {
        target.process()
      }
      logger.logAsString should include (Menu.UnknownMenuEntry.format("test"))
    }

    "have factory method" in {
      MenuStart(new ControllerDummy)
    }
  }
}
