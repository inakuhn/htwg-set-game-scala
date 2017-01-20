package de.htwg.se.setGame.aview

import de.htwg.se.setGame.actor.CardActor
import de.htwg.se.setGame.controller.{ControllerDummy, ExitApplication}
import de.htwg.se.setGame.model.{Card, CardAttribute, Game, Player}
import org.scalatest.Matchers._
import org.scalatest.{Outcome, WordSpec}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.swing.Reactions.Reaction

/**
  * @author Philipp Daniels
  */
class GuiSpec extends WordSpec {
  val listenerList = new ListBuffer[Reaction]()
  var controller: ControllerSpy = _
  var target: Gui = _
  var exitCalled = false


  class ControllerSpy extends ControllerDummy {

    override def subscribe(listener: Reaction): Unit = {
      listenerList += listener
      listeners += listener
    }

    override def exitApplication(): Unit = {
      exitCalled = true
    }
  }

  override def withFixture(test: NoArgTest): Outcome = {
    controller = new ControllerSpy
    target = Gui(controller)
    exitCalled = false

    try test()
    finally {
      controller.publish(new ExitApplication)
      listenerList.clear()
    }
  }

  "Gui" should {

    "called listenTo" in {
      listenerList should have length 2
      listenerList contains target
      listenerList contains controller
    }

    "has title" in {
      target.title should be(Gui.Title)
    }

    "be visible" in {
      target.visible should be(true)
    }
    "refreshField" in {
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
      target.refreshField(game)
      target.contents != null shouldBe true

    }
    "start Game " in {
      target.startGame()
    }
    "not have called exit on startup" in {
      exitCalled should be(false)
    }
    "close on ExitApplikation event" in {
      controller.publish(new ExitApplication)
      target.visible should be(false)
    }
    "call controller on closeOperation" in {
      target.closeOperation()
      exitCalled should be(true)
    }
  }
}
