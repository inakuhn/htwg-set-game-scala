package de.htwg.se.setGame.aview.gui.components

import javax.swing.ImageIcon

import de.htwg.se.setGame.Controller
import de.htwg.se.setGame.actor.CardActor
import de.htwg.se.setGame.model.{Card, Game}

import scala.swing.event.ButtonClicked
import scala.swing.{Button, GridPanel}

/**
  * Created by Ina Kuhn on 19.01.2017.
  */
class GameField() extends GridPanel(3,4){
  var selectCards = List[Card]()
  def setCardsIntoField(game : Game): Unit = {
    selectCards = List[Card]()
    for (card <- game.cardsInField)
      contents += new Button() {
        val myCard = card
        icon = new ImageIcon(ClassLoader.getSystemResource("pack/" + card.name + ".gif").getFile)
        reactions += {
          case _: ButtonClicked => {
            selectCards = selectCards :+ myCard

          }
        }
      }
  }



}
