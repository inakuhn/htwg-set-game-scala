package de.htwg.se.setGame

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.SetGameApplication.getClass

import scala.swing.Reactor

/**
  * @author Philipp Daniels
  */
class Tui(private val controller: Controller) extends Reactor {
  private val logger = Logger(getClass)
  listenTo(controller)
  logger.info(Tui.InitiateMessage)
}

object Tui {
  val InitiateMessage = "Initiate UI"
  def apply(controller: Controller): Tui = new Tui(controller)
}