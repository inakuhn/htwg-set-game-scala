package de.htwg.se.setGame.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import de.htwg.se.setGame.model.{Card, CardAttribute, Game, Player}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.mutable
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
      val target = system.actorOf(Props[CardActor])
      within(500 millis) {
        target ! CreatePack

        receiveWhile(500 millis) {
          case result: List[Card] => result.size should be(81)

        }

      }

    }

    "isSet" in {
      val target = system.actorOf(Props[CardActor])
      //preparing
      val cardOne = Card(CardAttribute.Form.balk, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)
      val cards = List[Card](cardOne, cardOne, cardOne)
      within(500 millis) {
        //execute
        target ! Set(cards)

        receiveWhile(500 millis) {
          //verify
          case result: ResponseTyp => result.boolean should be(true)

        }
      }

    }
    "moveCards" in {
      val target = system.actorOf(Props[CardActor])
      //preparing
      //cards
      var pack = mutable.MutableList[Card]()
      for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
        pack += Card(form, color, fill, count)

      var cards = List[Card](pack: _*)
      val cardsInField = cards.slice(0, CardActor.fieldSize)
      cards = cards diff cardsInField
      val set = cardsInField.slice(0, CardActor.setMax)
      //players
      val playerOne = Player(0, 0, "Joe Doe")
      val playerTwo = Player(1, 0, "Smith Doe")
      val players = List[Player](playerOne, playerTwo)
      //Game game
      val game = Game(cardsInField, cards, players)
      within(500 millis) {
        //execute
        target ! MoveCards(set, playerOne, game)

        receiveWhile(500 millis) {
          //verify
          case result: Game => {
            result.player.filter(p => p.name == "Joe Doe").head.points should be(1)
            result.cardsInField.size should be(CardActor.fieldSize)
            result.pack.size should be(pack.size - cardsInField.size - set.size)
          }

        }
      }


    }
    "default " in {
      within(500 millis) {
        val target = system.actorOf(Props[CardActor])
        target ! "some Default value"

        receiveWhile(500 millis) {
          case result => result should be(None)

        }

      }

    }


    /**
      * Actor sub functions test
      */
    "isACombination" in {
      //preparing
      val actorRef = TestActorRef(new CardActor)
      val actor = actorRef.underlyingActor
      val cardOne = Card(CardAttribute.Form.balk, CardAttribute.Color.red, CardAttribute.Fill.halfFilled, CardAttribute.Count.one)
      val cards = List[Card](cardOne, cardOne, cardOne)
      //execute
      val resultTrue = actor.isACombination(1)
      val resultFalse = actor.isACombination(2)

      //verify
      resultTrue should be(true)
      resultFalse should be(false)

    }

    "getCardsForField" in {
      //preparing
      val actorRef = TestActorRef(new CardActor)
      val actor = actorRef.underlyingActor
      //cards
      var pack = mutable.MutableList[Card]()
      for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
        pack += Card(form, color, fill, count)

      var cards = List[Card](pack: _*)
      val cardsInField = cards.slice(0, CardActor.fieldSize)
      cards = cards diff cardsInField
      val set = cardsInField.slice(0, CardActor.setMax)
      val cardsFromPack = cards.slice(0, CardActor.setMax)
      cards = cards diff cardsFromPack


      //execute
      val newCardsInField = actor.getCardsForField(cardsFromPack, set, cardsInField)


      //verify
      newCardsInField diff set should be(newCardsInField)
      cards.size should be(pack.size - newCardsInField.size - cardsFromPack.size)
      newCardsInField.size should be(CardActor.fieldSize)


    }

    "getPlayers" in {
      //preparing
      //actors
      val actorRef = TestActorRef(new CardActor)
      val actor = actorRef.underlyingActor
      //players
      val playerOne = Player(0, 0, "Joe Doe")
      val playerTwo = Player(1, 0, "Smith Doe")
      val players = List[Player](playerOne, playerTwo)
      //execute
      val result = actor.getPlayers(players, playerOne)
      //verify
      result.filter(p => p.identify == 0).head.points should be(1)
      result.filter(p => p.identify == 0).head.name should be("Joe Doe")


    }

    "getCardsForPack" in {
      //preparing
      val actorRef = TestActorRef(new CardActor)
      val actor = actorRef.underlyingActor
      //cards
      var pack = mutable.MutableList[Card]()
      for (form <- CardAttribute.Form.values; color <- CardAttribute.Color.values; fill <- CardAttribute.Fill.values; count <- CardAttribute.Count.values)
        pack += Card(form, color, fill, count)

      var cards = List[Card](pack: _*)
      val cardsInField = cards.slice(0, CardActor.fieldSize)
      cards = cards diff cardsInField
      val set = cardsInField.slice(0, CardActor.setMax)
      val cardsFromPack = cards.slice(0, CardActor.setMax)
      //execute
      val result = actor.getCardsForPack(cards, cardsFromPack)

      result diff cardsFromPack should be(result)
      result diff cardsInField should be(result)


    }
    "Set be equal" in {
      val example = Set(List[Card]())
      Set.unapply(example)
    }
    "MoveCards be equal" in {
      val example = MoveCards(List[Card](), Player(0, 0, "Joe Doe"), Game(List[Card](), List[Card](), List[Player]()))
      MoveCards.unapply(example)
    }
    "ResponseTyp be equal" in {
      val example = ResponseTyp(true)
      ResponseTyp.unapply(example)
    }

  }
}
