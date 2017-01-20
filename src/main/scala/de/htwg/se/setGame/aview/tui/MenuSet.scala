package de.htwg.se.setGame.aview.tui

import com.typesafe.scalalogging.Logger
import de.htwg.se.setGame._
import de.htwg.se.setGame.model.{Card, Game, Player}

import scala.collection.mutable

/**
  * @author Philipp Daniels
  */
class MenuSet(private val controller: Controller) extends Menu {

  private val logger = Logger(getClass)
  private var game = None: Option[Game]
  private var player = None : Option[Player]
  private val cards = mutable.MutableList.empty[Card]
  listenTo(controller)

  reactions += {
    case e: StartGame =>
      logger.info(MenuSet.EventStartGame)
      game = Some(e.game)
    case e: UpdateGame =>
      logger.info(MenuSet.EventUpdateGame)
      game = Some(e.game)
    case _: IsSet =>
      logger.info(MenuSet.EventIsSet)
      player = None
      cards.clear()
    case _: IsInvalidSet =>
      logger.info(MenuSet.EventIsInvalidSet)
      player = None
      cards.clear()
  }

  override def process(input: String): Unit = {
    logger.info(Menu.ReadInput.format(input))
    if (player.isEmpty) {
      processPlayer(input)
    } else {
      processCards(input)
    }
    outputMenuList()
  }

  private def processPlayer(input: String) = {
    game match {
      case None => logger.info(MenuSet.Error)
      case Some(value) =>
        val result = value.player.filter(p => p.name.equals(input))
        if (result.nonEmpty) {
          player = Some(result.head)
          logger.info(MenuSet.PlayerSelected.format(result.head))
        }
    }
  }

  private def processCards(input: String) = {
    game match {
      case None => logger.info(MenuSet.Error)
      case Some(value) =>
        val number = input.toInt
        if (number <= value.cardsInField.size) {
          cards += value.cardsInField(number - 1)
          logger.info(MenuSet.CardSelected.format(cards.mkString(", ")))
        }
        if (cards.size == 3) {
          player match {
            case None => logger.info(MenuSet.Error)
            case Some(playerInstance) => controller.checkSet(cards.toList, playerInstance)
          }
        }
    }
  }

  override def postMenuList(): Unit = {
    requestPlayer()
  }

  private def requestPlayer() = {
    player match {
      case None => logger.info(MenuSet.PlayerRequest)
      case Some(_) => requestSet()
    }
  }

  private def requestSet() = {
    logger.info(MenuSet.CardRequest.format(cards.size + 1))
  }
}

object MenuSet {
  val CardRequest = "Choose %d. Card?"
  val CardSelected = "Cards '%s' have been selected"
  val EventIsInvalidSet = "Received 'IsInvalidSet' event"
  val EventIsSet = "Received 'IsSet' event"
  val EventStartGame = "Received 'StartGame' event"
  val EventUpdateGame = "Received 'UpdateGame' event"
  val PlayerRequest = "Choose Player?"
  val PlayerSelected = "Player '%s' has been selected"
  val Error = "Something went wrong"
  def apply(controller: Controller): MenuSet = new MenuSet(controller)
}