package de.htwg.se.setGame

import com.typesafe.scalalogging.Logger

import scala.swing.Reactor

/**
  * @author Philipp Daniels
  */
class Tui(private val controller: Controller) extends Reactor {
  private val logger = Logger(getClass)
  private var continueMain = true
  private var continuePlayer = true
  private var continuePlayerName = true

  logger.info(Tui.InitiateMessage)
  listenTo(controller)

  reactions += {
    case e: ExitApplication => shutdown()
    case e: AddPlayer => printPlayerMenu(); readPlayerMenuInput()
    case e: CancelAddPlayer => continuePlayer = false; printMainMenu()
    case e: PlayerAdded => continuePlayerName = false; printPlayerMenu()
  }

  printMainMenu()
  readMainMenuInput()

  private def printMainMenu(): Unit = {
    logger.info(Tui.MainMenuHeading)
    logger.info(Tui.MainMenuEntryExit.format(Tui.MainCommandExit))
    logger.info(Tui.MainMenuEntryCreate.format(Tui.MainCommandCreate))
    logger.info(Tui.RequestMenuInput)
  }
  private def readMainMenuInput(): Unit = {
    do {
      if (Console.in.ready()) {
        processMainMenuInput(Console.in.readLine())
      }
      Thread.sleep(100)
    } while(continueMain)
  }

  private def processMainMenuInput(input: String): Unit = {
    logger.info(Tui.ReadInput.format(input))
    input match {
      case Tui.MainCommandExit => controller.exitApplication()
      case Tui.MainCommandCreate => controller.createNewGame()
      case _ =>
        logger.info(Tui.UnknownMenuEntry.format(input))
        printMainMenu()
    }
  }
  private def shutdown(): Unit = {
    logger.info(Tui.Shutdown)
    continueMain = false
    continuePlayer = false
    continuePlayerName = false
  }

  private def printPlayerMenu(): Unit = {
    logger.info(Tui.PlayerMenuHeading)
    logger.info(Tui.PlayerMenuEntryPlayer.format(Tui.PlayerCommandPlayer))
    logger.info(Tui.PlayerMenuEntryCancel.format(Tui.PlayerCommandCancel))
    logger.info(Tui.MainMenuEntryExit.format(Tui.MainCommandExit))
    logger.info(Tui.RequestMenuInput)
  }
  private def readPlayerMenuInput(): Unit = {
    continuePlayer = true
    do {
      if (Console.in.ready()) {
        processPlayerMenuInput(Console.in.readLine())
      }
      Thread.sleep(100)
    } while(continuePlayer && continueMain)
  }
  private def processPlayerMenuInput(input: String): Unit = {
    logger.info(Tui.ReadInput.format(input))
    input match {
      case Tui.PlayerCommandPlayer => requestPlayerName()
      case Tui.PlayerCommandCancel => controller.cancelAddPlayer()
      case Tui.MainCommandExit => controller.exitApplication()
      case _ =>
        logger.info(Tui.UnknownMenuEntry.format(input))
        printPlayerMenu()
    }
  }
  private def requestPlayerName(): Unit = {
    logger.info(Tui.RequestPlayerName)
    continuePlayerName = true
    do {
      if (Console.in.ready()) {
        val input = Console.in.readLine()
        logger.info(Tui.ReadInput.format(input))
        controller.addPlayer(input)
      }
      Thread.sleep(100)
    } while(continuePlayerName && continuePlayer && continueMain)
  }
}

object Tui {
  val InitiateMessage = "Initiate UI"
  val RequestMenuInput = "Please choose?"
  val UnknownMenuEntry = "Unknown menu entry: %s"
  val ReadInput = "Read input: %s"
  val MainMenuHeading = "# MAIN-MENU #"
  val MainMenuEntryExit = " %s - Exit application"
  val MainMenuEntryCreate = " %s - Create new game"
  val Shutdown = "Shutdown UI"
  val MainCommandExit = "x"
  val MainCommandCreate = "c"
  val PlayerMenuHeading = "# PLAYER-MENU #"
  val PlayerMenuEntryPlayer = " %s - Add player"
  val PlayerMenuEntryCancel = " %s - Cancel"
  val PlayerCommandPlayer = "p"
  val PlayerCommandCancel = "c"
  val RequestPlayerName = "Choose player name:"
  def apply(controller: Controller): Tui = new Tui(controller)
}