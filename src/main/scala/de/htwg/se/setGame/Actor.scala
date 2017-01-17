package de.htwg.se.setGame

import akka.actor.{Actor, ActorLogging}
import de.htwg.se.setGame.model.{Card, CardAttribute}

import scala.collection.mutable

case object CreateMessage

class CardActor extends Actor with ActorLogging {
  def receive: Actor.Receive = {
    case CreateMessage =>
      log.info("Stopped")
      //create card for game
      var cards = mutable.MutableList[Card]()
      for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
        cards += Card(form, color, fill, count)
      val cardsFinal = List[Card](cards:_*)
      sender ! cardsFinal
      context.stop(self)
    case default@_ =>
      log.warning("that was unexpected: " + default.toString)
      sender ! None
      context.stop(self)
  }
}