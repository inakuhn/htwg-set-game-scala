package de.htwg.se.setGame.model.imp

import org.scalatest.WordSpec

class PlayerSpec extends WordSpec {

  "Player" should {
    val target = Player(42, 1337, "Joe")

    "have identity" in {
      target.identify equals 42
    }
    "have points" in {
      target.points equals 1337
    }
    "have name" in {
      target.name equals "Joe"
    }
  }
}
