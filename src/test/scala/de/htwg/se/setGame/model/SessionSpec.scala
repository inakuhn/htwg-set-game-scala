package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.CardAttribute.{Color, Count, Fill, Form}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class SessionSpec extends WordSpec {

  "Session" should {
    val card = Card(Form.wave, Color.green, Fill.halfFilled, Count.one)
    val cards = List(card)
    val player = Player(1, 0, "Doe")
    val players = List(player)

    val target = Session(players, cards, cards)

    "have unused cards" in {
      target.unusedCards equals cards
    }
    "have cards in field" in {
      target.cardsInField equals cards
    }
    "have players" in {
      target.players equals players
    }
    "be equal" in {
      val example = Session(players, cards, cards)
      target.equals(example) should be (true)
      Session.unapply(example)
    }
  }
}
