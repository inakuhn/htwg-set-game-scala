package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.CardAttribute.{Color, Count, Fill, Form}

case class Card(form: Form.Value, color: Color.Value, fill: Fill.Value, count: Count.Value) {
  val name = form.toString + color.toString + fill.toString + count.toString


}
