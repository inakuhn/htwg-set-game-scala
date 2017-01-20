package de.htwg.se.setGame.aview

import de.htwg.se.setGame.aview.tui.{MenuDummy, MenuMain, TuiSpecExtension}
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
      logs should include (MenuMain.MenuHeading)
    }

    "have listener on ExitApplication event" in withLogger { (logger) =>
      var inputString = ""
      val controller = new ControllerDummy {
        override def exitApplication(): Unit = publish(new ExitApplication)
      }
      val target = new Tui(controller)
      target.menu = new MenuDummy {
        override def process(input: String): Unit = {
          inputString = input
          controller.exitApplication()
        }
      }

      overrideConsoleIn(MenuMain.ExitCommand) {
        target.readInput()
      }

      target.isContinue should be (false)
      val logs = logger.logAsString()
      logs should include (Tui.InitiateMessage)
      logs should include (Tui.Shutdown)
      inputString should be(MenuMain.ExitCommand)
    }
  }
}
