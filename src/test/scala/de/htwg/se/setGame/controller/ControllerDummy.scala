package de.htwg.se.setGame.controller

import de.htwg.se.setGame.model.{ Card, Player }

/**
 * @author Philipp Daniels
 */
class ControllerDummy extends Controller {

  override def exitApplication(): Unit = {}

  override def createNewGame(): Unit = {}

  override def addPlayer(name: String): Unit = {}

  override def cancelAddPlayer(): Unit = {}

  override def checkSet(cards: List[Card], player: Player): Unit = {}

  override def startGame(): Unit = {}

  override def randomCardsInField(): Unit = {}

  override def finishGame(): Unit = {}
}
