package de.htwg.se.setGame.tui

import de.htwg.se.setGame.{ControllerDummy, ExitApplication}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class TuiSpec extends WordSpec with TuiSpecExtension {
  "Tui" should {

    "have factory method" in withLogger { (logger) =>
      overrideConsoleIn(MenuMain.ExitCommand) {
        Tui(new ControllerDummy{
          override def exitApplication(): Unit = publish(new ExitApplication)
        })
      }
      val logs = logger.logAsString()
      logs should include (Tui.InitiateMessage)
      logs should include (Tui.Shutdown)
    }

    "have listener on ExitApplication event" in withLogger { (logger) =>
      var called = false
      val controller = new ControllerDummy
      new Tui(controller, new MenuDummy {
        override def process(): Unit = called = true
      })
      controller.publish(new ExitApplication)

      val logs = logger.logAsString()
      logs should include (Tui.InitiateMessage)
      logs should include (Tui.Shutdown)
      called should be(true)
    }
  }
}
