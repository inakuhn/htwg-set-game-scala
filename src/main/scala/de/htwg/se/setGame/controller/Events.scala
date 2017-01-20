package de.htwg.se.setGame.controller

import de.htwg.se.setGame.model.Game

import scala.swing.event.Event

/**
  * @author Philipp Daniels
  */
case class ExitApplication() extends Event

/**
  * @author Philipp Daniels
  */
case class AddPlayer(game: Game) extends Event

/**
  * @author Philipp Daniels
  */
case class CancelAddPlayer() extends Event

/**
  * @author Philipp Daniels
  */
case class PlayerAdded(game: Game) extends Event

/**
  * @author Philipp Daniels
  */
case class NewGame(game: Game) extends Event

/**
  * @author Philipp Daniels
  */
case class StartGame(game: Game) extends Event

/**
  * @author Philipp Daniels
  */
case class IsSet() extends Event

/**
  * @author Philipp Daniels
  */
case class IsInvalidSet() extends Event

/**
  * @author Philipp Daniels
  */
case class UpdateGame(game: Game) extends Event

/**
  * @author Philipp Daniels
  */
case class FinishGame(game: Game) extends Event