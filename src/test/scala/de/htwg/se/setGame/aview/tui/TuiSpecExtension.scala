package de.htwg.se.setGame.aview.tui

import java.io.ByteArrayInputStream

import de.htwg.se.setGame.TestAppender
import org.apache.log4j.Logger

/**
  * @author Philipp Daniels
  */
trait TuiSpecExtension {

  protected def withLogger(test: (TestAppender) => Any): Unit = {
    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender)

    try test(testAppender)
    finally {}
  }

  protected def overrideConsoleIn[T](input: String)(thunk: => T): T = {
    val stream = new ByteArrayInputStream(input.getBytes)
    Console.withIn(stream)(thunk)
  }
}
