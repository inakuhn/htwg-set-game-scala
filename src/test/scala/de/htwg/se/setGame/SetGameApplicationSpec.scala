package de.htwg.se.setGame

import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class SetGameApplicationSpec extends WordSpec {

  "SetGameApplication" should {
    val target = SetGameApplication
    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender)

    "have output" in {
      target.main(new Array[String](0))
      testAppender.getLog().length should be >0
    }
  }
}
