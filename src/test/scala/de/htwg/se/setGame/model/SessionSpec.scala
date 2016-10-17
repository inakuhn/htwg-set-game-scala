package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.imp._
import de.htwg.se.setGame.model.imp.CardAttribute._
import Color._
import Fill._
import Form._
import Count._
import org.specs2.mutable.Specification

class SessionSpec extends Specification {

  "Session" should {
    val card = Card(wave, green, halfFilled, one)
    val cards = List(card)

    "have unused cards" in {
      Session(null, cards, null).unusedCards mustEqual cards
    }
    "have cards in field" in {
      Session(null, null, cards).cardsInField mustEqual cards
    }
    "have cards in field" in {
      val player = Player(1, 0, "Doe")
      val players = List(player)

      Session(players, null, null).players mustEqual players
    }
  }
}
