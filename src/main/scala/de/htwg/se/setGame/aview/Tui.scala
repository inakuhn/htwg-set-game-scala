package de.htwg.se.setGame.aview

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame.aview.tui.{Menu, MenuMain, NonBlockingInputReader}
import de.htwg.se.setGame.{Controller, ExitApplication}

import scala.swing.Reactor

/**
  * @author Philipp Daniels
  */
class Tui(private val controller: Controller) extends NonBlockingInputReader with Reactor {
  private val logger = Logger(getClass)
  var menu: Menu = MenuMain(controller, this)

  logger.info(Tui.InitiateMessage)
  listenTo(controller)

  reactions += {
    case _: ExitApplication =>
      exit()
      logger.info(Tui.Shutdown)
  }

  override def readInput(): Unit = {
    menu.outputMenuList()
    super.readInput()
  }
  override protected def processInput(input: String): Unit = {
    menu.process(input)
  }
}

object Tui {
  val InitiateMessage = "Initiate UI"
  val Shutdown = "Shutdown UI"
  def apply(controller: Controller): Tui = {
    val ui = new Tui(controller)
    ui.readInput()
    ui
  }
}