package de.htwg.se.setGame.actor

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorSystem, Props}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec, WordSpecLike}
import akka.testkit.{ImplicitSender, TestActorRef, TestActors, TestKit}
import org.scalatest.Matchers._
import akka.pattern.ask
import de.htwg.se.setGame.model.Card
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.util.Success
import scala.concurrent.duration._

/**
  * Created by Ina Kuhn on 18.01.2017.
  */
class CardActorSpec extends WordSpec {
  private implicit val timeout = Timeout(5 seconds)
  "CardActorSpec" should {

    "generateCards" ignore   {
      implicit val system = ActorSystem("TestSys")
      val actorRef = TestActorRef.create(system, Props[CardActor])
      // hypothetical message stimulating a '42' answer
      val future = actorRef ? CreatePack
      val result = Await.result(actorRef ? CreatePack, 5 seconds).asInstanceOf[List[Card]]
      result.size should be(81)

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
