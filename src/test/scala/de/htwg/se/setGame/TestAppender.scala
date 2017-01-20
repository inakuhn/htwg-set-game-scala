package de.htwg.se.setGame

import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.spi.LoggingEvent

import scala.collection.mutable.ListBuffer

/**
  * @author Philipp Daniels
  */
class TestAppender extends AppenderSkeleton {
  private val log = new ListBuffer[LoggingEvent]()
  private val lineBreak = sys.props("line.separator")

  override def append(event: LoggingEvent): Unit = {
    log += event
  }

  override def requiresLayout(): Boolean = false

  override def close(): Unit = {}

  def logAsString(): String = {
    val sb = new StringBuilder
    for (entry <- log.toList) sb.append(entry.getMessage + lineBreak)
    sb.toString()
  }
}
