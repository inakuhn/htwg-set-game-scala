package de.htwg.se.setGame.controller

import de.htwg.se.setGame.model.{ Card, Game, Player }
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.swing.event.Event

/**
 * @author Philipp Daniels
 */
class EventsTest extends WordSpec {
  private def createEmptyGame: Game = Game(List[Card](), List[Card](), List[Player]())

  "Events" should {
    "have ExitApplication" in {
      val event = new ExitApplication
      event shouldBe a[Event]
      ExitApplication.unapply(event)
    }
    "have AddPlayer" in {
      val event = AddPlayer(createEmptyGame)
      event shouldBe a[Event]
      AddPlayer.unapply(event)
    }
    "have CancelAddPlayer" in {
      val event = new CancelAddPlayer
      event shouldBe a[Event]
      CancelAddPlayer.unapply(event)
    }
    "have PlayerAdded" in {
      val event = PlayerAdded(createEmptyGame)
      event shouldBe a[Event]
      PlayerAdded.unapply(event)
    }
    "have IsInvalidSet" in {
      val event = IsInvalidSet()
      event shouldBe a[Event]
      IsInvalidSet.unapply(event)
    }
    "have IsSet" in {
      val event = IsSet()
      event shouldBe a[Event]
      IsSet.unapply(event)
    }
    "have NewGame" in {
      val event = NewGame(createEmptyGame)
      event shouldBe a[Event]
      NewGame.unapply(event)
    }
    "have StartGame" in {
      val event = StartGame(createEmptyGame)
      event shouldBe a[Event]
      StartGame.unapply(event)
    }
    "have UpdateGame" in {
      val event = UpdateGame(createEmptyGame)
      event shouldBe a[Event]
      UpdateGame.unapply(event)
    }
    "have FinishGame" in {
      val event = FinishGame(createEmptyGame)
      event shouldBe a[Event]
      FinishGame.unapply(event)
    }
  }
}
