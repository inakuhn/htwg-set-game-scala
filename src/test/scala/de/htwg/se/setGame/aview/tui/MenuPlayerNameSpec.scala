package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame._
import de.htwg.se.setGame.controller.ControllerDummy
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
 * @author Philipp Daniels
 */
class MenuPlayerNameSpec extends WordSpec with TuiSpecExtension {

  "MenuPlayerName" should {
    "have called controller on input" in withLogger { (logger) =>
      var playerName = ""
      val target = MenuPlayerName(new ControllerDummy {
        override def addPlayer(name: String): Unit = {
          playerName = name
        }
      })
      target.process("Player")

      playerName should equal("Player")
      logger.logAsString() should include(Menu.ReadInput.format("Player"))
    }

    "have override default request message" in withLogger { (logger) =>
      val target = MenuPlayerName(new ControllerDummy)
      target.outputMenuList()

      logger.logAsString() should include(MenuPlayerName.RequestPlayerName)
    }
  }
}
