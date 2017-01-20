package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.CardAttribute.{Color, Count, Fill, Form}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class CardSpec extends WordSpec {

  "Card" should {
    val target = Card(Form.wave, Color.green, Fill.halfFilled, Count.one)

    "have a form" in {
      target.form equals Form.wave
    }
    "have a color" in {
      target.color equals Color.green
    }
    "have a fill" in {
      target.fill equals Fill.halfFilled
    }
    "have a number of components" in {
      target.count equals Count.one
    }
    "be equal" in {
      val example = Card(Form.wave, Color.green, Fill.halfFilled, Count.one)
      target.equals(example) should be(true)
      Card.unapply(example)
    }
  }
}
