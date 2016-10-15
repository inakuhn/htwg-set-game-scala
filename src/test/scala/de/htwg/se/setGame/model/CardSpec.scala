package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.enum.Color._
import de.htwg.se.setGame.model.enum.Form._
import de.htwg.se.setGame.model.enum.NumberOfComponents._
import de.htwg.se.setGame.model.enum.Fill._
import org.scalatest._

class CardSpec extends FlatSpec with Matchers {
  "A Card" should "have a color " in {
    Card(wave,green,halfFilled ,one).color should be(green)
  }
  "A Card" should "have a fill " in {
    Card(wave,green,halfFilled ,one).fill should be(halfFilled)
  }
  "A Card" should "have a number of components " in {
    Card(wave,green,halfFilled ,one).numberOfComponents should be(one)
  }
  "A Card" should "have a form " in {
    Card(wave,green,halfFilled ,one).color should be(wave)
  }
}
