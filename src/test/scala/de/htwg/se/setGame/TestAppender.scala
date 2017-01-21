package de.htwg.se.setGame

import scala.collection.JavaConversions._
import uk.org.lidalia.slf4jtest.TestLoggerFactory

/**
  * @author Philipp Daniels
  */
class TestAppender {
  private val lineBreak = sys.props("line.separator")
  TestLoggerFactory.clearAll()

  def logAsString(): String = {
    val sb = new StringBuilder
    for (e <- TestLoggerFactory.getLoggingEvents) {
      sb.append(e.getMessage + lineBreak)
    }
    sb.toString()
  }
}
