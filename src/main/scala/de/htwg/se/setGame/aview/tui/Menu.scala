package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.swing.Reactor

/**
 * @author Philipp Daniels
 */
trait Menu extends Reactor {

  private val logger = Logger(getClass)
  private val actions = mutable.LinkedHashMap[String, (Action, String)]()

  protected def getActions: mutable.Map[String, (Action, String)] = actions

  def process(input: String): Unit = {
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

  final def outputMenuList(): Unit = {
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

  def isContinue: Boolean = continue

  def readInput(): Unit = {
    continue = true
    do {
      if (Console.in.ready()) {
        processInput(Console.in.readLine())
      }
      Thread.sleep(Menu.ReadInputTiming)
    } while (continue)
  }

  protected def processInput(input: String)
}