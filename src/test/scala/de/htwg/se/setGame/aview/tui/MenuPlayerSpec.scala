package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame._
import de.htwg.se.setGame.model.{Card, Game, Player}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class MenuPlayerSpec extends WordSpec with TuiSpecExtension {

  private val lineBreak = sys.props("line.separator")
  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  "MenuPlayer" should {
    val assertEvent = (logger: TestAppender, command: String, c: Controller) => {
      val target = new MenuPlayer(c, new MenuDummy, new MenuDummy)
      overrideConsoleIn(command) {
        target.process()
      }
      logger.logAsString should include (Menu.ReadInput.format(command))
    }

    "have stopped on ExitApplication event" in withLogger { (logger) =>
      assertEvent(logger, MenuPlayer.ExitCommand, new ControllerDummy {
        override def exitApplication(): Unit = publish(new ExitApplication)
      })
    }

    "have stopped on CancelAddPlayer event" in withLogger { (logger) =>
      assertEvent(logger, MenuPlayer.CancelCommand, new ControllerDummy {
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
      overrideConsoleIn(MenuPlayer.PlayerCommand) {
        new MenuPlayer(controller, new MenuDummy {
          override def process(): Unit = controller.addPlayer("player")
        }, new MenuDummy).process()
      }

      val logs = logger.logAsString()
      logs should include (MenuPlayer.MenuHeading)
      logs should include (MenuPlayer.EventPlayerAdded)
      logs should include (MenuPlayer.PlayerList.format(""))
      playerName should be ("player")
    }

    "have called controller startGame" in withLogger { (logger) =>
      var called = false
      overrideConsoleIn(MenuPlayer.StartCommand) {
        new MenuPlayer(new ControllerDummy {
          override def startGame(): Unit = {
            called = true
            publish(new ExitApplication)
          }
        }, new MenuDummy, new MenuDummy).process()
      }

      val logs = logger.logAsString()
      logs should include (MenuPlayer.StartDescription)
      logs should include (Menu.ReadInput.format(MenuPlayer.StartCommand))
      called should be (true)
    }

    "have listener on StartGame event" in withLogger { (logger) =>
      var called = false
      val controller = new ControllerDummy
      new MenuPlayer(controller, new MenuDummy, new MenuDummy {
        override def process(): Unit = called = true
      })
      controller.publish(StartGame(createEmptyGame))

      called should be (true)
      logger.logAsString() should include (MenuPlayer.EventStartGame)
    }

    "have unknown menu-entry fallback" in withLogger { (logger) =>
      val target = new MenuPlayer(new ControllerDummy {
        override def exitApplication(): Unit = publish(new ExitApplication)
      }, new MenuDummy, new MenuDummy)

      val input = "test" + lineBreak + MenuPlayer.ExitCommand
      overrideConsoleIn(input) {
        target.process()
      }
      logger.logAsString should include (Menu.UnknownMenuEntry.format("test"))
    }

    "have factory method" in {
      MenuPlayer(new ControllerDummy)
    }
  }
}