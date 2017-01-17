package de.htwg.se.setGame.tui

import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.swing.Reactor

/**
  * @author Philipp Daniels
  */
trait Menu extends NonBlockingInputReader with Reactor {

  private val logger = Logger(getClass)
  private val actions = mutable.HashMap[String, (Action, String)]()

  protected def getActions: mutable.HashMap[String, (Action, String)] = actions

  def process(): Unit = {
    outputMenuList()
    readInput()
  }

  override protected def processInput(input: String): Unit = {
    logger.info(Menu.ReadInput.format(input))
    if (actions.contains(input)) {
      actions(input)._1.execute()
    } else {
      logger.info(Menu.UnknownMenuEntry.format(input))
      outputMenuList()
    }
  }

  protected def preMenuList(): Unit = {
  }

  protected def postMenuList(): Unit = {
    logger.info(Menu.RequestMenuInput)
  }

  private def outputMenuList() = {
    preMenuList()
    for ((key, (_, message)) <- actions.iterator) {
      logger.info(Menu.MenuEntryFormat.format(key, message))
    }
    postMenuList()
  }

  protected trait Action {

    def execute()
  }
}

object Menu {
  val ReadInput = "Read input: %s"
  val ReadInputTiming = 100
  val MenuEntryFormat = "%s - %s"
  val UnknownMenuEntry = "Unknown menu entry: %s"
  val RequestMenuInput = "Please choose?"
}

trait NonBlockingInputReader {
  private var continue = true

  def exit(): Unit = continue = false

  protected def readInput(): Unit = {
    continue = true
    do {
      if (Console.in.ready()) {
        processInput(Console.in.readLine())
      }
      Thread.sleep(Menu.ReadInputTiming)
    } while(continue)
  }

  protected def processInput(input: String)
}