package de.htwg.se.setGame.model


import org.scalatest._

class PlayerSpec extends FlatSpec with Matchers {
  "A Player" should "have a name " in {
    Player(0,0,"Joe").name should be("Joe")
  }
  "A Player" should "have a identity" in {
    Player(0,0,"Joe").identify should be(0)
  }
  "A Player" should "have a score" in {
    Player(0,0,"Joe") should be(0)
  }
}
