package de.htwg.se.setGame.aview.tui

import java.io.ByteArrayInputStream

import de.htwg.se.setGame.TestAppender

/**
 * @author Philipp Daniels
 */
trait TuiSpecExtension {

  protected def withLogger(test: (TestAppender) => Any): Unit = {
    try test(new TestAppender)
    finally {}
  }

  protected def overrideConsoleIn[T](input: String)(thunk: => T): T = {
    val stream = new ByteArrayInputStream(input.getBytes)
    Console.withIn(stream)(thunk)
  }
}
