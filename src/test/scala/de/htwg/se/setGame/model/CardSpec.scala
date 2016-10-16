package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.enum.Color._
import de.htwg.se.setGame.model.enum.Form._
import de.htwg.se.setGame.model.enum.Count._
import de.htwg.se.setGame.model.enum.Fill._
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
