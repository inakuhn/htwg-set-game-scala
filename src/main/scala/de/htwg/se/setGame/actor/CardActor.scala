package de.htwg.se.setGame.actor

import akka.actor.{Actor, ActorLogging}
import de.htwg.se.setGame.model.{Card, CardAttribute, Game}

import scala.collection.mutable
case object CreatePack
case class Set(cards : List[Card])
case class RemoveCardsFromField(cards : List[Card], game : Game)

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
    case e : RemoveCardsFromField =>{
      log.info("Removing cards from field")
      val cardsInField = e.game.cardsInField diff e.cards
      sender ! cardsInField
      log.info("Stopped")
      context.stop(self)
    }
    case default@_ =>
      log.warning("that was unexpected: " + default.toString)
      sender ! None
      context.stop(self)
  }

  def generateCards() : List[Card] = {
    var cards = mutable.MutableList[Card]()
    for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
    cards += Card(form, color, fill, count)
    List[Card](cards:_*)
  }
  def isSet(list: List[Card]) : Boolean = {
    log.info("Entry Set : " + list)
    (isACombination((list map (t => t.color) toSet).size)
      && isACombination((list map (t => t.form) toSet).size)
      && isACombination((list map (t => t.fill) toSet).size)
      && isACombination((list map (t => t.count) toSet).size))
  }
  def isACombination(int: Int): Boolean = {
    (int == CardActor.setMin || int == CardActor.setMax)
  }


}
object CardActor{
  val setMin = 1
  val setMax = 3
  def apply: CardActor = new CardActor()
}
