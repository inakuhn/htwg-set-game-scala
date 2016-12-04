package de.htwg.se.setGame

import akka.actor.{Actor, ActorLogging}

case object CreateMessage

class CardActor extends Actor with ActorLogging {
  def receive: Actor.Receive = {
    case CreateMessage =>
      sender ! "CreateMessage"
      log.info("Stopped")
      context.stop(self)
    case default@_ =>
      log.warning("that was unexpected: " + default.toString)
      sender ! None
      context.stop(self)
  }
}