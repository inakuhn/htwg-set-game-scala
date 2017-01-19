package de.htwg.se.setGame.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import de.htwg.se.setGame.model.Card
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Ina Kuhn on 18.01.2017.
  */
class CardActorSpec extends TestKit(ActorSystem("CardActorSpec", ConfigFactory.parseString("akka {}")))
  with DefaultTimeout
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  "CardActorSpec" should {
    val target = system.actorOf(Props[CardActor])
    "generateCards" in {
      within(500 millis) {
        target ! CreatePack

        receiveWhile(500 millis) {
          case result: List[Card] => result.size should be(81)
        }
      }

    }


    "isSet" in {

    }

    "isACombination" in {

    }

    "getCardsForField" in {

    }

    "getPlayers" in {

    }

    "getCardsForPack" in {

    }

    "receive" in {

    }

  }
}
