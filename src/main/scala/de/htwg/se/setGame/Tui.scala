package de.htwg.se.setGame

import com.typesafe.scalalogging.Logger

import scala.io.StdIn
import scala.swing.Reactor

/**
  * @author Philipp Daniels
  */
class Tui(private val controller: Controller) extends Reactor {
  private val logger = Logger(getClass)
  private var continue = true

  logger.info(Tui.InitiateMessage)
  listenTo(controller)

  reactions += {
    case e: ExitApplication => shutdown()
  }

  printMenu()
  readInput()

  private def readInput(): Unit = {
    while(continue) {
      try {
        processInput()
      } catch {
        case e: StringIndexOutOfBoundsException => logger.error(Tui.ErrorReadingInput, e)
      }
    }
  }

  private def processInput(): Unit = {
    val input = StdIn.readChar()
    logger.info(Tui.ReadInput.format(input))
    input match {
      case Tui.CommandExit => controller.exitApplikation()
      case _ =>
        logger.info(Tui.UnknownMenuEntry.format(input))
        printMenu()
    }
  }
  private def printMenu(): Unit = {
    logger.info(Tui.MenuHeading)
    logger.info(Tui.MenuEntryExit)
    logger.info(Tui.MenuInput)
  }

  private def shutdown(): Unit = {
    logger.info(Tui.Shutdown)
    continue = false
  }
}

object Tui {
  val InitiateMessage = "Initiate UI"
  val ErrorReadingInput = "Reading input failed"
  val MenuHeading = "# MENU #"
  val MenuEntryExit = " x - Exit application"
  val MenuInput = "Please choose?"
  val UnknownMenuEntry = "Unkown input entry: %s"
  val ReadInput = "Read input: %s"
  val Shutdown = "Shutdown UI"
  val CommandExit = 'x'
  def apply(controller: Controller): Tui = new Tui(controller)
}