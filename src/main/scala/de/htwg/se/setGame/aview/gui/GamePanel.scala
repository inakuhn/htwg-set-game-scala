package de.htwg.se.setGame.aview.gui

import javax.swing.ImageIcon

import de.htwg.se.setGame.{AddPlayer, Controller, ExitApplication}
import de.htwg.se.setGame.model.{Card, CardAttribute}

import scala.swing.{Button, FlowPanel}

/**
  * Created by Ina Kuhn on 16.01.2017.
  */
class GamePanel(private val controller: Controller) extends FlowPanel{

  def setCardsInField(): Unit = {
    val button = Field(false, new Card(CardAttribute.Form.balk, CardAttribute.Color.green, CardAttribute.Fill.halfFilled, CardAttribute.Count.one))
    val source = ClassLoader.getSystemResource("pack/" + button.card.name + ".gif")
    button.icon = new ImageIcon(source.getFile)
    contents += button

    revalidate();
    repaint();
  }

}
