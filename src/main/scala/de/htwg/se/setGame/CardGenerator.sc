import de.htwg.se.setGame.model.{Card, CardAttribute}
import de.htwg.se.setGame.model.CardAttribute.Color._

import scala.collection.mutable

object CardGenerator{

  val colors = CardAttribute.Color.values
  val forms =CardAttribute.Form.values
  val counts = CardAttribute.Count.values
  val fills = CardAttribute.Fill.values

  var cards = mutable.MutableList[Card]()
  for (color <- colors; count <- counts; form <- forms; fill <- fills ) {
   val card = Card(form, color, fill, count)
    cards += card
  }
  cards.size

}