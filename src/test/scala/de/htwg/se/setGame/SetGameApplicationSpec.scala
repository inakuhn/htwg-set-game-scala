package de.htwg.se.setGame

import java.io.ByteArrayInputStream

import de.htwg.se.setGame.tui.MenuMain
import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class SetGameApplicationSpec extends WordSpec {
  val input = new ByteArrayInputStream(MenuMain.ExitCommand.getBytes)

  "SetGameApplication" should {

    "have output" in {
      Console.withIn(input) {
        val target = SetGameApplication

        val testAppender = new TestAppender
        Logger.getRootLogger.removeAllAppenders()
        Logger.getRootLogger.addAppender(testAppender)
        target.main(new Array[String](0))
        testAppender.logAsString().length should be > 0
      }
    }
  }
}
