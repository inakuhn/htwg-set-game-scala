package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame._
import de.htwg.se.setGame.model.CardAttribute.{Color, Count, Fill, Form}
import de.htwg.se.setGame.model.{Card, Game, Player}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
class MenuSetTest extends WordSpec with TuiSpecExtension {

  def publishEvent(event: Event): MenuSet = {
    val controller = new ControllerDummy
    val target = new MenuSet(controller)
    controller.publish(event)
    target
  }

  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  "MenuSet" should {

    "have factory method" in {
      MenuSet(new ControllerDummy)
    }

    "have listener on StartGame event" in withLogger { (logger) =>
      publishEvent(StartGame(createEmptyGame))
      logger.logAsString() should include (MenuSet.EventStartGame)
    }

    "have listener on UpdateGame event" in withLogger { (logger) =>
      publishEvent(UpdateGame(createEmptyGame))
      logger.logAsString() should include (MenuSet.EventUpdateGame)
    }

    "have listener on IsSet event" in withLogger { (logger) =>
      publishEvent(new IsSet)
      logger.logAsString() should include (MenuSet.EventIsSet)
    }

    "have listener on IsInvalidSet event" in withLogger { (logger) =>
      publishEvent(new IsInvalidSet)
      logger.logAsString() should include (MenuSet.EventIsInvalidSet)
    }

    "have abort with no game" in withLogger { (logger) =>
      val target = new MenuSet(new ControllerDummy)
      target.process("player")

      val logs = logger.logAsString()
      logs should include (MenuSet.Error)
      logs should include (MenuSet.PlayerRequest)
    }

    "have selected player" in withLogger { (logger) =>
      val player = Player(0, 0, "player")
      val event = StartGame(Game(List[Card](), List[Card](), List[Player]() :+ player))
      val target = publishEvent(event)
      target.process("player")

      val logs = logger.logAsString()
      logs should include (MenuSet.PlayerSelected.format(player))
      logs should include (MenuSet.CardRequest.format(1))
    }

    "have selected cards" in withLogger { (logger) =>
      val player = Player(0, 0, "player")
      val card = Card(Form.balk, Color.red, Fill.halfFilled, Count.one)
      val event = StartGame(Game(List[Card]() :+ card, List[Card](), List[Player]() :+ player))
      var called = false
      val controller = new ControllerDummy {
        override def checkSet(cards: List[Card], player: Player): Unit = called = true
      }
      val target = new MenuSet(controller)
      controller.publish(event)
      target.process("player")
      target.process("1")
      target.process("1")
      target.process("1")

      called should be(true)
      val logs = logger.logAsString()
      logs should include (MenuSet.PlayerSelected.format(player))
      logs should include (MenuSet.CardRequest.format(1))
      logs should include (MenuSet.CardRequest.format(2))
      logs should include (MenuSet.CardRequest.format(3))
    }
  }
}
