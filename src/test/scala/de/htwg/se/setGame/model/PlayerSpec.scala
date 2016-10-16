package de.htwg.se.setGame.model

import org.specs2.mutable._

class PlayerSpec extends Specification {

  "Player" should {
    val target = Player(42, 1337, "Joe")

    "have identity" in {
      target.identify mustEqual 42
    }
    "have points" in {
      target.points mustEqual 1337
    }
    "have name" in {
      target.name mustEqual "Joe"
    }
  }
}
