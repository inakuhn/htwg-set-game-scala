package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.imp._
import de.htwg.se.setGame.model.imp.CardAttribute._
import Color._
import Form._
import Count._
import Fill._
import org.specs2.mutable._

class CardSpec extends Specification {

  "Card" should {
    val target = Card(wave, green, halfFilled, one)

    "have a form" in {
      target.form mustEqual wave
    }
    "have a color" in {
      target.color mustEqual green
    }
    "have a fill" in {
      target.fill mustEqual halfFilled
    }
    "have a number of components" in {
      target.count mustEqual one
    }
  }
}
