package de.htwg.se.setGame

import de.htwg.se.setGame.model.enum.Form._
import de.htwg.se.setGame.model.enum.Color._
import de.htwg.se.setGame.model.enum.Fill._
import de.htwg.se.setGame.model.enum.NumberOfComponents._

import de.htwg.se.setGame.model._


object SetGameApplication{
  def main(args: Array[String]): Unit = {
    val card1 = Card(wave,green,empty,one)
    val card2 = Card(ellipse,red,halfFilled,two)
    val card3 = Card(balk,purple,filled,tree)
    val playerOne = Player(0,0,"Joe")
    val playerTwo = Player(1,0,"Doe")
    val players = List(playerOne,playerTwo)
    val unusedCards =  List(card1,card2)
    val cardsInField = List(card3)
    val session =  Session(players,unusedCards,cardsInField)

    println(session)

  }
}
