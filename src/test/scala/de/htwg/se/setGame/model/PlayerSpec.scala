package de.htwg.se.setGame.model

import org.scalatest.Matchers._
import org.scalatest.WordSpec

class PlayerSpec extends WordSpec {
  val identity = 42
  val points = 1337
  val name = "Joe"

  "Player" should {
    val target = Player(identity, points, name)

    "have identity" in {
      target.identify equals identity
    }
    "have points" in {
      target.points equals points
    }
    "have name" in {
      target.name equals name
    }
    "have toString" in {
      target.toString.isEmpty should be (false)
    }
    "have HashCode" in {
      target.hashCode.toString.isEmpty should be (false)
    }
    "be equal" in {
      val example = Player(identity, points, name)
      target.equals(example) should be (true)
      Player.unapply(example)
    }
  }
}
