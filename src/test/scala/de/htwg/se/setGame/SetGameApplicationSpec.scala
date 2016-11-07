package de.htwg.se.setGame

import org.apache.log4j.Logger
import org.scalatest.WordSpec

import scala.collection.mutable.ListBuffer

/**
  * @author Philipp Daniels
  */
class SetGameApplicationSpec extends WordSpec {

  "SetGameApplication" should {
    val target = SetGameApplication
    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender);

    "have output" in {
      target.main(new Array[String](0))
      val result = testAppender.getLog
      assert(result contains "Joe")
      assert(result contains "Doe")
    }
  }
}
