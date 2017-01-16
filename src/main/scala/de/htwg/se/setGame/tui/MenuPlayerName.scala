package de.htwg.se.setGame.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.{CancelAddPlayer, Controller, ExitApplication, PlayerAdded}

/**
  * @author Philipp Daniels
  */
class MenuPlayerName(private val controller: Controller) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  reactions += {
    case e: PlayerAdded => exit()
    case e: CancelAddPlayer => exit()
    case e: ExitApplication => exit()
  }

  override def process(): Unit = {
    logger.info(MenuPlayerName.RequestPlayerName)
    readInput()
  }

  override protected def processInput(input: String): Unit = {
    logger.info(Menu.ReadInput.format(input))
    controller.addPlayer(input)
  }
}

object MenuPlayerName {
  val RequestPlayerName = "Choose player name:"
  def apply(controller: Controller): MenuPlayerName = new MenuPlayerName(controller)
}