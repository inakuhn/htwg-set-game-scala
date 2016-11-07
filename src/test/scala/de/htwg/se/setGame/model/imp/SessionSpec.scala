package de.htwg.se.setGame.model.imp

import de.htwg.se.setGame.model.imp.CardAttribute.{Color, Count, Fill, Form}
import org.scalatest.WordSpec

class SessionSpec extends WordSpec {

  "Session" should {
    val card = Card(Form.wave, Color.green, Fill.halfFilled, Count.one)
    val cards = List(card)

    "have unused cards" in {
      Session(null, cards, null).unusedCards equals cards
    }
    "have cards in field" in {
      Session(null, null, cards).cardsInField equals cards
    }
    "have players" in {
      val player = Player(1, 0, "Doe")
      val players = List(player)

      Session(players, null, null).players equals players
    }
  }
}
