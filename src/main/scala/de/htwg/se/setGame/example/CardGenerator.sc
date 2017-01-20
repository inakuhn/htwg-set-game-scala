import de.htwg.se.setGame.model.{Card, CardAttribute}

import scala.collection.mutable


object CardGenerator {


  var cards = mutable.MutableList[Card]()
  for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
    cards += Card(form, color, fill, count)
  cards.size
  val cardsForField = cards.slice(0, 3)
  cardsForField.size
  val cardsInField = cards diff cardsForField
  cardsInField.size
  val cardsFromPack = cards.slice(0, 3)

  val pack = cards diff cardsFromPack
  pack.size

  val newPack = pack.++(cardsFromPack)
  newPack.size

}