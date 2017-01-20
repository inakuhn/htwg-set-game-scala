package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame._
import de.htwg.se.setGame.aview.Tui
import de.htwg.se.setGame.model.{Card, Game, Player}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class MenuNewGameTest extends WordSpec with TuiSpecExtension {

  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  "MenuPlayer" should {
    "have called MenuPlayerName" ignore withLogger { (logger) =>
      var playerName = ""
      val controller = new ControllerDummy {
        override def addPlayer(name: String): Unit = {
          playerName = name
          publish(PlayerAdded(createEmptyGame))
          publish(new ExitApplication)
        }
      }
      overrideConsoleIn(MenuNewGame.PlayerCommand) {
        new MenuNewGame(controller, new Tui(new ControllerDummy), new MenuDummy).process()
      }

      val logs = logger.logAsString()
      logs should include (MenuNewGame.MenuHeading)
      logs should include (MenuNewGame.EventPlayerAdded)
      logs should include (MenuNewGame.PlayerList.format(""))
      playerName should be ("player")
    }

    "have called controller cancelAddPlayer on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuNewGame(new ControllerDummy {
        override def cancelAddPlayer(): Unit = called = true
      }, new Tui(new ControllerDummy), new MenuDummy)
      target.process(MenuNewGame.CancelCommand)

      called should be (true)
      logger.logAsString() should include (Menu.ReadInput.format(MenuNewGame.CancelCommand))
    }

    "have called controller exitApplication on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuNewGame(new ControllerDummy {
        override def exitApplication(): Unit = called = true
      }, new Tui(new ControllerDummy), new MenuDummy)
      target.process(MenuNewGame.ExitCommand)

      called should be (true)
      logger.logAsString() should include (Menu.ReadInput.format(MenuNewGame.ExitCommand))
    }

    "have called controller startGame on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuNewGame(new ControllerDummy {
        override def startGame(): Unit = called = true
      }, new Tui(new ControllerDummy), new MenuDummy)
      target.process(MenuNewGame.StartCommand)

      called should be (true)
      logger.logAsString() should include (Menu.ReadInput.format(MenuNewGame.StartCommand))
    }

    "have listener on PlayerAdded event" in withLogger { (logger) =>
      val controller = new ControllerDummy
      new MenuNewGame(controller, new Tui(new ControllerDummy), new MenuDummy)
      controller.publish(PlayerAdded(createEmptyGame))

      logger.logAsString() should include (MenuNewGame.EventPlayerAdded)
    }

    "have listener on NewGame event" in withLogger { (logger) =>
      val controller = new ControllerDummy
      val tui = new Tui(new ControllerDummy)
      val target = new MenuNewGame(controller, tui, new MenuDummy)
      tui.menu = new MenuDummy
      controller.publish(NewGame(createEmptyGame))

      tui.menu should be (target)
      val logs = logger.logAsString()
      logs should include (MenuNewGame.EventNewGame)
      logs should include (MenuNewGame.MenuHeading)
    }

    "have unknown menu-entry fallback" in withLogger { (logger) =>
      val target = new MenuNewGame(new ControllerDummy, new Tui(new ControllerDummy), new MenuDummy)
      target.process("test")

      logger.logAsString should include (Menu.UnknownMenuEntry.format("test"))
    }

    "have factory method" in {
      MenuNewGame(new ControllerDummy, new Tui(new ControllerDummy))
    }
  }
}
