package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame.aview.Tui
import de.htwg.se.setGame.model.{Card, Game, Player}
import de.htwg.se.setGame.{CancelAddPlayer, ControllerDummy}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class MenuMainSpec extends WordSpec with TuiSpecExtension {
  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  "MenuMain" should {

    "have called exitApplication on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuMain(new ControllerDummy {
        override def exitApplication(): Unit = {
          called = true
        }
      }, new Tui(new ControllerDummy))
      target.process(MenuMain.ExitCommand)

      called should be(true)
      logger.logAsString() should include(Menu.ReadInput.format(MenuMain.ExitCommand))
    }

    "have called createNewGame on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuMain(new ControllerDummy {
        override def createNewGame(): Unit = {
          called = true
        }
      }, new Tui(new ControllerDummy))
      target.process(MenuMain.CreateCommand)

      called should be(true)
      logger.logAsString() should include(Menu.ReadInput.format(MenuMain.CreateCommand))
    }

    "have listen to CancelAddPlayer event" in withLogger { (logger) =>
      val controller = new ControllerDummy
      val tui = new Tui(new ControllerDummy)
      val target = new MenuMain(controller, tui)
      tui.menu = new MenuDummy
      controller.publish(new CancelAddPlayer)

      tui.menu should be(target)
      val log = logger.logAsString()
      log should include(MenuMain.MenuHeading)
      log should include(MenuMain.EventCancelAddPlayer)
    }

    "have factory method" in {
      MenuMain(new ControllerDummy, new Tui(new ControllerDummy))
    }
  }
}
