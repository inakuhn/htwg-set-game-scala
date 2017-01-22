package de.htwg.se.setGame.model

import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
 * @author Philipp Daniels
 */
class GameTest extends WordSpec {

  "Game" should {
    val target = Game(List[Card](), List[Card](), List[Player]())

    "have a cardsInField" in {
      target.cardsInField shouldBe a[List[_]]
      target.cardsInField.isEmpty should be(true)
    }

    "have a pack" in {
      target.pack shouldBe a[List[_]]
      target.pack.isEmpty should be(true)
    }

    "have player" in {
      target.player shouldBe a[List[_]]
      target.player.isEmpty should be(true)
    }

    "be equal" in {
      val example = Game(List[Card](), List[Card](), List[Player]())
      target.equals(example) should be(true)
      Game.unapply(example)
    }
  }
}
