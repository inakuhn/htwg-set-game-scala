import de.htwg.se.setGame.model.{Card, CardAttribute}

object RemoveFromList {
  var cardOne = Card(CardAttribute.Form.balk, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)
  var cardTwo = Card(CardAttribute.Form.ellipse, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)
  var cardThree = Card(CardAttribute.Form.wave, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)
  var cardFour = Card(CardAttribute.Form.balk, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)

  val originalList = List(cardOne, cardTwo, cardThree, cardFour)
  val cardsToBeRemoved = List(cardOne, cardTwo, cardThree)

  val newList = originalList diff cardsToBeRemoved
  //val newList = originalList.filterNot(e => e.equals(cardsToBeRemoved))

  newList.size

}