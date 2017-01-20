package de.htwg.se.setGame.controller

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import de.htwg.se.setGame.TestAppender
import de.htwg.se.setGame.model.{Card, CardAttribute, Game, Player}
import org.apache.log4j.Logger
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.swing.Reactor

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

    val actor = ActorSystem("Controller", ConfigFactory.parseString("akka {}"))
    val target = new ControllerActorSystem(actor)
    try test(target, testAppender)
    finally {

    }
  }

  private var game: Game = Game(List[Card](), List[Card](), List[Player]())

  private def assertGame(cards: Boolean, pack: Boolean, player: Boolean) = {
    game.cardsInField.isEmpty should be(cards)
    game.pack.isEmpty should be(pack)
    game.player.isEmpty should be(player)
  }

  "Controller" should {
    "have instance" in {
      val actor = ActorSystem("Controller", ConfigFactory.parseString("akka {}"))
      Controller(actor) shouldBe a[Controller]
    }

    "have exitApplication" in withController { (target, logger) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {
          case _: ExitApplication => called = true
        }
      }

      target.exitApplication()
      called should be(true)
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
      called should be(true)
      logger.logAsString should include(Controller.CreateNewGame)
    }
    "have send isSet event on createNewGame" in withController { (target, logger) =>
      var triggerUpdate = false
      var isSetAns = false
      val cardOne = Card(CardAttribute.Form.balk, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)
      val cards = List[Card](cardOne, cardOne, cardOne)

      new ReactorSpy(target) {
        reactions += {
          case _: IsSet =>
            isSetAns = true
          case _: UpdateGame =>
            triggerUpdate = true
        }
      }
      target.checkSet(cards, Player(0, 0, "ina"))

      isSetAns should be(true)
      triggerUpdate should be(false)
      logger.logAsString should include(Controller.TriggerIsSet)
    }

    "have send PlayerAdded event on addPlayer" in withController { (target, logger) =>
      new ReactorSpy(target) {
        reactions += {
          case e: PlayerAdded => game = e.game
        }
      }

      target.addPlayer("player")
      assertGame(cards = true, pack = true, player = false)
      game.player should contain(Player(0, 0, "player"))
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
      called should be(true)
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
      called should be(true)
      logger.logAsString().isEmpty should be (false)
    }

    "have reset Game on createNewGame" in withController { (target, _) =>
      new ReactorSpy(target) {
        reactions += {
          case e: StartGame => game = e.game
        }
      }

      target.addPlayer("player")
      target.createNewGame()
      assertGame(cards = true, pack = true, player = false)
    }

    "have triggered UpdateGame event" in withController { (target, _) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {
          case _: UpdateGame => called = true
        }
      }
      target.randomCardsInField()
      called should be (true)
    }

    "have triggered FinishGame event" in withController { (target, _) =>
      var called = false
      new ReactorSpy(target) {
        reactions += {
          case _: FinishGame => called = true
        }
      }
      target.finishGame()
      called should be (true)
    }
  }
}
