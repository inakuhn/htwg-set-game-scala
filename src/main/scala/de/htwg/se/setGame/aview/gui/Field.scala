package de.htwg.se.setGame.aview.gui

import java.awt.event.ActionListener
import javax.swing.ImageIcon

import de.htwg.se.setGame.Gui
import de.htwg.se.setGame.model.Card

import scala.swing.{Action, Button}

/**
  * Created by Ina Kuhn on 16.01.2017.
  */
case class  Field(isSelected : Boolean, card : Card) extends Button {
  //val source = ClassLoader.getSystemResource("pack/"+card.form+card.color+card.fill+card.color+".gif")
  //this.icon = new ImageIcon(source.getFile)



}
