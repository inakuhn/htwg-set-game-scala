package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.enum.Color._
import de.htwg.se.setGame.model.enum.Fill._
import de.htwg.se.setGame.model.enum.Form._
import de.htwg.se.setGame.model.enum.NumberOfComponents._
import org.scalatest._

class SessionSpec extends FlatSpec with Matchers {
  "A Session" should "have unused cards" in {
    Session(null,List(Card(wave,green,halfFilled,one)),null).unusedCards should be(Card(wave,green,halfFilled,one))
  }
  "A Session" should "have cards in field " in {
    Session(null,null,List(Card(wave,green,halfFilled,one))).cardsInField should be(Card(wave,green,halfFilled,one))
  }
  "A Session" should "have a player or players" in {
    Session(List(Player(1,0,"Doe")),null,null).Player should be(Player(1,0,"Doe"))
  }
}
