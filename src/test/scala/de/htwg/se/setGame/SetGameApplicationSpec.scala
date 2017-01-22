package de.htwg.se.setGame

import java.io.ByteArrayInputStream

import de.htwg.se.setGame.aview.tui.MenuMain
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
 * @author Philipp Daniels
 */
class SetGameApplicationSpec extends WordSpec {
  private val input = new ByteArrayInputStream(MenuMain.ExitCommand.getBytes)

  "SetGameApplication" should {

    "have output" in {
      Console.withIn(input) {
        val logger = new TestAppender
        val target = SetGameApplication

        target.main(new Array[String](0))
        logger.logAsString().length should be > 0
      }
    }
  }
}
