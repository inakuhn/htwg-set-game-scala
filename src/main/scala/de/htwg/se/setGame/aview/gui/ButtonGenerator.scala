package de.htwg.se.setGame.aview.gui

import de.htwg.se.setGame.{Controller, Gui}
import de.htwg.se.setGame.model.Player

import scala.swing.{Button, Dialog}
import scala.swing.event.ButtonClicked

/**
  * Created by Ina Kuhn on 20.01.2017.
  */
case class ButtonGenerator(controller: Controller) {

  def createSetButton(player: Player): Button = {
    new Button("Set : " + player.name) {
      val playerB = player
      reactions += {
        case _: ButtonClicked => {
          Gui.setSet = true
          Gui.playerSet = playerB
          chooseCards(Gui.playerSet)
        }
      }
    }
  }
  def createNewGameButton(): Button = {
    new Button("New Game") {
      reactions += {
        case _: ButtonClicked => {
          controller.createNewGame()
        }
      }
    }
  }
  def createRandomButton(): Button = {
    new Button("Random field cards") {
      reactions += {
        case _: ButtonClicked => {
          controller.randomCardsInField()
        }
      }
    }
  }
  def createFinishButon() : Button = {
    new Button("Finish Game") {
      reactions += {
        case _: ButtonClicked => {
          controller.finishGame()
        }
      }
    }
  }
  def createUserNameButton() : Button = {
    new Button("User Name") {
      reactions += {
        case ButtonClicked(button) => changeText()
      }
    }
  }
  def changeText() {
    val r = Dialog.showInput(null, "User Name", initial = "user name")
    r match {
      case Some(s) => controller.addPlayer(s)
      case None =>
    }
  }
  def chooseCards(player: Player) {
    Dialog.showMessage(null, player.name + " you can choose 3 cards", title = "Your Turn!")
  }

}


