package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame.model.{Card, Game, Player}
import de.htwg.se.setGame.{AddPlayer, CancelAddPlayer, ControllerDummy, ExitApplication}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class MenuMainSpec extends WordSpec with TuiSpecExtension {
  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  "MenuMain" should {

    "have stopped on ExitApplication event" in withLogger { (logger) =>
      var called = false
      val target = new MenuMain(new ControllerDummy {
        override def exitApplication(): Unit = {
          called = true
          publish(new ExitApplication)
        }
      }, new MenuDummy)
      overrideConsoleIn(MenuMain.ExitCommand) {
        target.process()
      }

      called should be (true)
      target.isContinue should be (false)
      val logs = logger.logAsString()
      logs should include (MenuMain.MenuHeading)
      logs should include (Menu.ReadInput.format(MenuMain.ExitCommand))
    }

    "have called createNewGame on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuMain(new ControllerDummy {
        override def createNewGame(): Unit = {
          called = true
          publish(new ExitApplication)
        }
      }, new MenuDummy)
      overrideConsoleIn(MenuMain.CreateCommand) {
        target.process()
      }

      called should be(true)
      target.isContinue should be (false)
      val logs = logger.logAsString()
      logs should include (MenuMain.MenuHeading)
      logs should include (Menu.ReadInput.format(MenuMain.CreateCommand))
    }

    "have listen to ExitApplication event" in withLogger { (logger) =>
      val controller = new ControllerDummy
      val target = new MenuMain(controller, new MenuDummy)
      controller.publish(new ExitApplication)
      logger.logAsString() should be ("")
      target.isContinue should be (false)
    }

    "have listen to AddPlayer event" in withLogger { (logger) =>
      var called = false
      val controller = new ControllerDummy
      new MenuMain(controller, new MenuDummy {
        override def process(): Unit = called = true
      })
      controller.publish(AddPlayer(createEmptyGame))

      called should be(true)
      logger.logAsString() should be ("")
    }

    "have listen to CancelAddPlayer event" in withLogger { (logger) =>
      val controller = new ControllerDummy
      new MenuMain(controller, new MenuDummy)
      controller.publish(new CancelAddPlayer)
      logger.logAsString() should include (MenuMain.MenuHeading)
    }

    "have factory method" in {
      MenuMain(new ControllerDummy)
    }
  }
}
