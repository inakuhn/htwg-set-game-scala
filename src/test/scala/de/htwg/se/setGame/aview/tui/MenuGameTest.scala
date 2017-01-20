package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame.model.CardAttribute.{Color, Count, Fill, Form}
import de.htwg.se.setGame.model.{Card, Game, Player}
import de.htwg.se.setGame.{ControllerDummy, StartGame}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

/**
  * @author Philipp Daniels
  */
class MenuGameTest extends WordSpec with TuiSpecExtension {

  "MenuGame" should {
    "have called controller exitApplication on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuGame(new ControllerDummy {
        override def exitApplication(): Unit = {
          called = true
        }
      })
      target.process(MenuGame.ExitCommand)

      called should be (true)
      logger.logAsString() should include (Menu.ReadInput.format(MenuGame.ExitCommand))
    }

    "have shown game field on StartGame" in withLogger { (logger) =>
      val controller = new ControllerDummy
      val target = new MenuGame(controller)
      val card = Card(Form.wave, Color.green, Fill.halfFilled, Count.one)
      val game = Game(List[Card]() :+ card, List[Card](), List[Player]())
      controller.publish(StartGame(game))

      target.isContinue should be (true)
      val logs = logger.logAsString()
      logs should include (MenuGame.EventStartGame)
      logs should include (MenuGame.FieldHeading)
      logs should include (MenuGame.CardFormat.format(1, Form.wave, Color.green, Fill.halfFilled, Count.one))
    }

    "have factory method" in {
      MenuGame(new ControllerDummy)
    }
  }
}
