package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame.{ControllerDummy, ExitApplication}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class MenuGameTest extends WordSpec with TuiSpecExtension {

  "MenuGame" should {
    "have exit on ExitApplication event" in withLogger { (logger) =>
      val controller = new ControllerDummy
      val target = new MenuGame(controller)
      controller.publish(new ExitApplication)

      target.isContinue should be (false)
      logger.logAsString() should include (MenuGame.EventExitApplication)
    }

    "have called controller exitApplication on command" in withLogger { (logger) =>
      var called = false
      overrideConsoleIn(MenuGame.ExitCommand) {
        new MenuGame(new ControllerDummy {
          override def exitApplication(): Unit = {
            called = true
            publish(new ExitApplication)
          }
        }).process()
      }

      logger.logAsString() should include (Menu.ReadInput.format(MenuGame.ExitCommand))
      called should be (true)
    }

    "have factory method" in {
      val controller = new ControllerDummy
      val target = MenuGame(controller)
      target.isContinue should be (true)
    }
  }
}
