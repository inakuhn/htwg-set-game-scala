package de.htwg.se.setGame

import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.swing.Reactor
import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
class ControllerSpec extends WordSpec {
  private class ReactorSpy(controller: Controller) extends Reactor {
    listenTo(controller)
  }

  private def withController(test: (Controller, TestAppender) => Any) = {
    val testAppender = new TestAppender
    Logger.getRootLogger.removeAllAppenders()
    Logger.getRootLogger.addAppender(testAppender)

    val target = new Controller(null)
    try test(target, testAppender)
    finally {

    }
  }

  "Controller" should {
    "have instance" in {
      Controller(null) shouldBe a [Controller]
    }

    "have exitApplication" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {case e: ExitApplication => called = true}
      }

      target.exitApplication()
      called should be (true)
      logger.logAsString should include(Controller.TriggerExitApp)
    }


    "have send AddPlayer event on createNewGame" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {case e: AddPlayer => called = true}
      }

      target.createNewGame()
      called should be (true)
      logger.logAsString should include(Controller.TriggerAddPlayer)
    }

    "have send PlayerAdded event on addPlayer" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {case e: PlayerAdded => called = true}
      }

      val name = "player"
      target.addPlayer(name)
      called should be (true)
      logger.logAsString should include(Controller.PlayerAdded.format(name))
    }

    "have send CancelAddPlayer event on cancelAddPlayer" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {case e: CancelAddPlayer => called = true}
      }

      target.cancelAddPlayer()
      called should be (true)
      logger.logAsString should include(Controller.TriggerCancelPlayer)
    }
  }

  "Events" should {
    "have ExitApplication" in {
      val event = new ExitApplication
      event shouldBe a [Event]
      ExitApplication.unapply(event)
    }
    "have AddPlayer" in {
      val event = new AddPlayer
      event shouldBe a [Event]
      AddPlayer.unapply(event)
    }
    "have CancelAddPlayer" in {
      val event = new CancelAddPlayer
      event shouldBe a [Event]
      CancelAddPlayer.unapply(event)
    }
    "have PlayerAdded" in {
      val event = new PlayerAdded
      event shouldBe a [Event]
      PlayerAdded.unapply(event)
    }
  }
}
