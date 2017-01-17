package de.htwg.se.setGame.aview.gui

import javax.swing.ImageIcon

import de.htwg.se.setGame.model.Card

/**
  * Created by Ina Kuhn on 16.01.2017.
  */

class ImageIconGenerator {

  def getImageIcon(card: Card): ImageIcon = {
    val path = "pack/" +card.name + ".gif"
    val source = ClassLoader.getSystemResource(path)
    new ImageIcon(source.getFile)
  }
}