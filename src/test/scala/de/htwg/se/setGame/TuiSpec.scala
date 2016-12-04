package de.htwg.se.setGame

import java.io.ByteArrayInputStream

import akka.actor.ActorSystem
import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.collection.mutable.ListBuffer
import scala.swing.Reactions.Reaction

/**
  * @author Philipp Daniels
  */
class TuiSpec extends WordSpec {
  val in = new ByteArrayInputStream(Tui.CommandExit.toString.getBytes)

  "Tui" should {
    Console.withIn(in) {
      val listenerList = new ListBuffer[Reaction]()

      class ControllerSpy(system: ActorSystem) extends Controller(system) {
        def this() {
          this(null)
        }

        override def subscribe(listener: Reaction): Unit = {
          listenerList += listener
          listeners += listener
        }

      }

      val testAppender = new TestAppender
      Logger.getRootLogger.removeAllAppenders()
      Logger.getRootLogger.addAppender(testAppender)

      val controller = new ControllerSpy
      val target = Tui(controller)

      "called listenTo" in {
        listenerList should have length 2
        listenerList contains target
        listenerList contains controller
      }

      "has initiate message" in {
        testAppender.getLog().length should be > 0
        testAppender.getLog should include(Tui.InitiateMessage)
      }
    }
  }
}
