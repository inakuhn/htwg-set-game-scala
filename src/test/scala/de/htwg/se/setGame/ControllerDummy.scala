package de.htwg.se.setGame

import de.htwg.se.setGame.model.Card

/**
  * @author Philipp Daniels
  */
class ControllerDummy extends Controller {

  override def exitApplication(): Unit = {}
  override def createCards(): Unit = {}
  override def createNewGame(): Unit = {}
  override def addPlayer(name: String): Unit = {}
  override def cancelAddPlayer(): Unit = {}
  override def getCardsInField: List[Card] = {List[Card]()}
}
