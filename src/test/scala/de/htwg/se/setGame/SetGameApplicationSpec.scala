package de.htwg.se.setGame

import java.io.ByteArrayInputStream

import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class SetGameApplicationSpec extends WordSpec {
  val input = new ByteArrayInputStream(Tui.CommandExit.toString.getBytes)

  "SetGameApplication" should {
    val target = SetGameApplication

    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender)

    "have output" in {
      Console.withIn(input) {
        target.main(new Array[String](0))
        testAppender.getLog().length should be > 0
      }
    }
  }
}
