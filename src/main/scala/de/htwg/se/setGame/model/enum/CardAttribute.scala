/**
  * Created by raina on 15.10.2016.
  */
package de.htwg.se.setGame.model.enum {

  object Color extends Enumeration {
    val red, green, purple = Value
  }

  object Fill extends Enumeration {
    val filled, halfFilled, empty = Value
  }

  object Form extends Enumeration {
    val ellipse, wave, balk = Value
  }

  object Count extends Enumeration {
    val one, two, tree = Value
  }
}