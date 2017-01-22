package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.controller.Controller

/**
 * @author Philipp Daniels
 */
class MenuPlayerName(private val controller: Controller) extends Menu {

  private val logger = Logger(getClass)
  listenTo(controller)

  override def process(input: String): Unit = {
    logger.info(Menu.ReadInput.format(input))
    controller.addPlayer(input)
  }

  override def postMenuList(): Unit = logger.info(MenuPlayerName.RequestPlayerName)
}

object MenuPlayerName {
  val RequestPlayerName = "Choose player name:"

  def apply(controller: Controller): MenuPlayerName = new MenuPlayerName(controller)
}