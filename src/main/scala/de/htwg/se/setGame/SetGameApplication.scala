package de.htwg.se.setGame

import de.htwg.se.setGame.model.ModelFactory
import de.htwg.se.setGame.model.imp.CardAttribute.Color._
import de.htwg.se.setGame.model.imp.CardAttribute.Fill._
import de.htwg.se.setGame.model.imp.CardAttribute.Form._
import de.htwg.se.setGame.model.imp.CardAttribute.Count._



object SetGameApplication {
  def main(args: Array[String]): Unit = {
    val card1 = ModelFactory.card(wave, green, empty, one)
    val card2 = ModelFactory.card(ellipse, red, halfFilled, two)
    val card3 = ModelFactory.card(balk, purple, filled, tree)
    val playerOne = ModelFactory.player(0, 0, "Joe")
    val playerTwo = ModelFactory.player(1, 0, "Doe")
    val players = List(playerOne, playerTwo)
    val unusedCards = List(card1, card2)
    val cardsInField = List(card3)
    val session = ModelFactory.session(players, unusedCards, cardsInField)

    println(session)
  }
}
