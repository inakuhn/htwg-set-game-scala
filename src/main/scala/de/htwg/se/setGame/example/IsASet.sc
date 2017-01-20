import de.htwg.se.setGame.model.{Card, CardAttribute}

import scala.collection.mutable

object IsASet {
  var cards = mutable.MutableList[Card]()
  for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
    cards += Card(form, color, fill, count)
  println(cards.size)


  var list = mutable.MutableList[Card]()

  for (x <- cards.slice(0, 3)) list += x

  val listFinal = List[Card](list: _*)

  var result = isSet(cards.slice(0, 3))

  def isSet(list: mutable.MutableList[Card]): Boolean = {
    (isACombination((list map (t => t.color) toSet).size)
      && isACombination((list map (t => t.form) toSet).size)
      && isACombination((list map (t => t.fill) toSet).size)
      && isACombination((list map (t => t.count) toSet).size))
  }

  def isACombination(int: Int): Boolean = {
    (int == 1 || int == 3)
  }


}