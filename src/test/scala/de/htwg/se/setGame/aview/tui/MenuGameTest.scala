package de.htwg.se.setGame.aview.tui

import de.htwg.se.setGame.aview.Tui
import de.htwg.se.setGame.controller.{ ControllerDummy, IsInvalidSet, IsSet, StartGame }
import de.htwg.se.setGame.model.CardAttribute.{ Color, Count, Fill, Form }
import de.htwg.se.setGame.model.{ Card, Game, Player }
import org.scalatest.Matchers._
import org.scalatest.WordSpec

import scala.swing.event.Event

/**
 * @author Philipp Daniels
 */
class MenuGameTest extends WordSpec with TuiSpecExtension {
  private def assertEvent(event: Event) = {
    val controller = new ControllerDummy
    val tui = new Tui(new ControllerDummy)
    val target = new MenuGame(controller, tui, new MenuDummy)
    tui.menu = new MenuDummy
    controller.publish(event)

    tui.menu should be(target)
  }

  "MenuGame" should {
    "have called controller exitApplication on command" in withLogger { (logger) =>
      var called = false
      val target = new MenuGame(new ControllerDummy {
        override def exitApplication(): Unit = called = true
      }, new Tui(new ControllerDummy), new MenuDummy)
      target.process(MenuGame.ExitCommand)

      called should be(true)
      logger.logAsString() should include(Menu.ReadInput.format(MenuGame.ExitCommand))
    }

    "have change to menu on command" in withLogger { (logger) =>
      val tui = new Tui(new ControllerDummy)
      val menu = new MenuDummy
      val target = new MenuGame(new ControllerDummy, tui, menu)
      tui.menu = new MenuDummy
      target.process(MenuGame.SetCommand)

      val logs = logger.logAsString()
      logs should include(Menu.ReadInput.format(MenuGame.SetCommand))
      logs should include(MenuGame.MenuSetSwitch)
    }

    "have listener on StartGame event" in withLogger { (logger) =>
      val controller = new ControllerDummy
      val tui = new Tui(new ControllerDummy)
      val target = new MenuGame(controller, tui, new MenuDummy)
      tui.menu = new MenuDummy

      val card = Card(Form.wave, Color.green, Fill.halfFilled, Count.one)
      val game = Game(List[Card]() :+ card, List[Card](), List[Player]())
      controller.publish(StartGame(game))

      tui.menu should be(target)
      val logs = logger.logAsString()
      logs should include(MenuGame.EventStartGame)
      logs should include(MenuGame.FieldHeading)
      logs should include(MenuGame.CardFormat.format(1, Form.wave, Color.green, Fill.halfFilled, Count.one))
    }

    "have listener on IsSet event" in withLogger { (logger) =>
      assertEvent(new IsSet)
      logger.logAsString() should include(MenuGame.MenuHeading)
    }

    "have listener on IsInvalidSet event" in withLogger { (logger) =>
      assertEvent(new IsInvalidSet)
      logger.logAsString() should include(MenuGame.MenuHeading)
    }

    "have factory method" in {
      MenuGame(new ControllerDummy, new Tui(new ControllerDummy))
    }
  }
}
