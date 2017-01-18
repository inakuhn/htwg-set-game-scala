package de.htwg.se.setGame

import de.htwg.se.setGame.model.{Card, CardAttribute, Game, Player}
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

    val target = new ControllerActorSystem(null)
    try test(target, testAppender)
    finally {

    }
  }

  "Controller" should {
    "have instance" in {
      Controller(null) shouldBe a[Controller]
    }

    "have exitApplication" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {
          case _: ExitApplication => called = true
        }
      }

      target.exitApplication()
      called should be (true)
      logger.logAsString should include(Controller.TriggerExitApp)
    }


    "have send AddPlayer event on createNewGame" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {
          case e: NewGame => called = true
          case _: AddPlayer => called = true
        }
      }

      target.createNewGame()
      called should be (true)
      logger.logAsString should include(Controller.CreateNewGame)
    }
    "have send isSet event on createNewGame" ignore  withController { (target, logger) =>
      var called = false
      var isSetAns = false
      var cardOne = Card(CardAttribute.Form.balk, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)
      var cards = List[Card](cardOne,cardOne,cardOne)
      target.isASet(cards)
      new ReactorSpy(target) {
        reactions += {
          case IsSet(istSet) =>
            called = true
            isSetAns = istSet
        }
      }

      called should be (true)
      isSetAns should be (true)
      logger.logAsString should include(Controller.TriggerIsSet)
    }

    "have send PlayerAdded event on addPlayer" in withController { (target, logger) =>
      var game: Game = Game(List[Card](), List[Card](), List[Player]())
      new ReactorSpy(target) {
        reactions += {
          case e: PlayerAdded => game = e.game
        }
      }

      target.addPlayer("player")
      game.player.isEmpty should be (false)
      game.player should contain (Player(0, 0, "player"))
      logger.logAsString should include(Controller.PlayerAdded.format("player"))
    }

    "have send CancelAddPlayer event on cancelAddPlayer" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {
          case _: CancelAddPlayer => called = true
        }
      }

      target.cancelAddPlayer()
      called should be (true)
      logger.logAsString should include(Controller.TriggerCancelPlayer)
    }

    "have send StartGame event on startGame call" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {
          case _: StartGame => called = true
        }
      }

      target.startGame()
      called should be (true)
      logger.logAsString should be ("")
    }
  }

  "Events" should {
    "have ExitApplication" in {
      val event = new ExitApplication
      event shouldBe a[Event]
      ExitApplication.unapply(event)
    }
    "have AddPlayer" in {
      val event = new AddPlayer
      event shouldBe a[Event]
      AddPlayer.unapply(event)
    }
    "have CancelAddPlayer" in {
      val event = new CancelAddPlayer
      event shouldBe a[Event]
      CancelAddPlayer.unapply(event)
    }
    "have PlayerAdded" in {
      val event = PlayerAdded(Game(List[Card](), List[Card](), List[Player]()))
      event shouldBe a[Event]
      PlayerAdded.unapply(event)
    }
  }
}
