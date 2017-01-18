package de.htwg.se.setGame.actor

import akka.actor.{Actor, ActorLogging}
import de.htwg.se.setGame.model.{Card, CardAttribute, Game, Player}

import scala.collection.mutable
import scala.util.Random

case object CreatePack

case class Set(cards: List[Card])

case class GeneratingNewGame(set: List[Card], player: Player, game: Game)

/**
  * Created by Ina Kuhn on 17.01.2017.
  */

class CardActor extends Actor with ActorLogging {


  def receive: Actor.Receive = {
    case CreatePack =>
      log.info("Create cards for Game")
      //create card for game
      sender ! generateCards()
      context.stop(self)
      log.info("Stopped")
    case e: Set =>
      log.info("Check is ist a Set")
      val result = isSet(e.cards)
      log.info("is set = " + result)
      sender ! result
      log.info("Stopped")
      context.stop(self)
    case e: GeneratingNewGame =>
      log.info("GeneratingNewGame")
      val cardsFromPack = e.game.pack.slice(0, 3)
      val cardsForField = getCardsForField(cardsFromPack, e.set, e.game.cardsInField)
      val pack = getCardsForPack(e.game.pack, e.set, cardsFromPack)
      val players = getPlayers(e.game.player, e.player)
      val result = Game(cardsForField, pack, players)
      log.info("is set = " + result)
      sender ! result
      log.info("Stopped")
      context.stop(self)
    case default@_ =>
      log.warning("that was unexpected: " + default.toString)
      sender ! None
      context.stop(self)
  }

  def generateCards(): List[Card] = {
    var cards = mutable.MutableList[Card]()
    for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
      cards += Card(form, color, fill, count)
    Random.shuffle(cards)
    List[Card](cards: _*)
  }

  def isSet(list: List[Card]): Boolean = {
    log.info("Entry Set : " + list)
    (isACombination((list map (t => t.color) toSet).size)
      && isACombination((list map (t => t.form) toSet).size)
      && isACombination((list map (t => t.fill) toSet).size)
      && isACombination((list map (t => t.count) toSet).size))
  }

  def isACombination(int: Int): Boolean = {
    (int == CardActor.setMin || int == CardActor.setMax)
  }

  def getCardsForField(cardsFromPack: List[Card], set: List[Card], cardsInField: List[Card]): List[Card] = {
    val cardsInFieldTmp = cardsInField diff set
    cardsInFieldTmp.++(cardsFromPack)
  }

  def getCardsForPack(pack: List[Card], set: List[Card], cardsFromPack: List[Card]): List[Card] = {
    val newPack = pack diff cardsFromPack
    newPack diff set
  }

  def getPlayers(players: List[Player], player: Player): List[Player] = {
    val otherPlayer = players.filterNot(p => p == player).head
    val newPlayer = Player(player.identify, player.points + 1, player.name)
    List[Player](otherPlayer, newPlayer)
  }

}

object CardActor {
  val setMin = 1
  val setMax = 3

  def apply: CardActor = new CardActor()
}
