package de.htwg.se.setGame.aview.gui

import java.awt.{Color, Dimension}
import javax.swing.ImageIcon
import javax.swing.border.LineBorder

import de.htwg.se.setGame.actor.CardActor
import de.htwg.se.setGame.model.{Card, Player}
import de.htwg.se.setGame.{Controller, Gui}

import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dialog}

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
  //Hier kommt er nicht rein
  val s = new Dimension(Gui.xSizeOfCard, Gui.ySizeOfCard)
  def createSetCardPanel(card: Card): Button = {
    new Button() {
      val myCard = card
      minimumSize = s
      maximumSize = s
      preferredSize = s
      background = Color.white
      icon = new ImageIcon(ClassLoader.getSystemResource("pack/" + card.name + ".gif").getFile)

      reactions += {
        case _: ButtonClicked => {
          if (Gui.setSet) {
            border = new LineBorder(Color.ORANGE, Gui.sizeOfSelectBorder)
            println(myCard)
            Gui.setList = Gui.setList :+ myCard
            if (Gui.setList.size == CardActor.setMax) {
              controller.checkSet(Gui.setList, Gui.playerSet)
              Gui.setList = List[Card]()
              Gui.setSet = false
            }
          } else {
            selectSetFirst()
          }
        }
      }
    }
  }
  def selectSetFirst() {
    Dialog.showMessage(null, "Before Choose card please press Set button", title = "Press Set!")
  }
}

