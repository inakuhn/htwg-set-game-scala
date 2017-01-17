package de.htwg.se.setGame.example

import de.htwg.se.setGame.model.{Card, CardAttribute}

import scala.collection.mutable


object CardGenerator {


  var cards = mutable.MutableList[Card]()
  for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
    cards += Card(form, color, fill, count)
  cards.size


}