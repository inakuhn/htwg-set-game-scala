package de.htwg.se.setGame.model

/**
 * Card attribute contains the information for cards.
 *
 */
object CardAttribute {

  /**
   * Color enumeration
   */
  object Color extends Enumeration {
    val red, green, purple = Value
  }

  /**
   * Fill Enumeration
   */
  object Fill extends Enumeration {
    val filled, halfFilled, empty = Value
  }

  /**
   * Form Enumeration
   */
  object Form extends Enumeration {
    val ellipse, wave, balk = Value
  }

  /**
   * Count Enumeration
   */
  object Count extends Enumeration {
    val one, two, three = Value
  }
}
