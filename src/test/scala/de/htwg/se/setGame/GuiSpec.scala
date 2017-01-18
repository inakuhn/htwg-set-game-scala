package de.htwg.se.setGame

import akka.actor.ActorSystem
import org.scalatest.Matchers._
import org.scalatest.{Outcome, WordSpec}

import scala.collection.mutable.ListBuffer
import scala.swing.Reactions.Reaction

/**
  * @author Philipp Daniels
  */
class GuiSpec extends WordSpec {
  val listenerList = new ListBuffer[Reaction]()
  var controller:ControllerSpy = _
  var target:Gui = _
  var exitCalled = false

  class ControllerSpy extends ControllerDummy {

    override def subscribe(listener: Reaction): Unit = {
      listenerList += listener
      listeners += listener
    }

    override def exitApplication(): Unit = {
      exitCalled = true
    }
  }

  override def withFixture(test: NoArgTest): Outcome = {
    controller = new ControllerSpy
    target = Gui(controller)
    exitCalled = false

    try test()
    finally {
      controller.publish(new ExitApplication)
      listenerList.clear()
    }
  }

  "Gui" should {

    "called listenTo" in {
      listenerList should have length 2
      listenerList contains target
      listenerList contains controller
    }

    "has title" in {
      target.title should be (Gui.Title)
    }

    "be visible" in {
      target.visible should be (true)
    }
    "not have called exit on startup" in {
      exitCalled should be (false)
    }
    "close on ExitApplikation event" in {
      controller.publish(new ExitApplication)
      target.visible should be (false)
    }
    "call controller on closeOperation" in {
      target.closeOperation()
      exitCalled should be (true)
    }
  }
}
