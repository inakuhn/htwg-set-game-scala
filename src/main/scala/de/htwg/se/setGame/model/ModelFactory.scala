package de.htwg.se.setGame.model

import de.htwg.se.setGame.model.imp._
import de.htwg.se.setGame.model.imp.CardAttribute._

/**
 * In order to abstract the model pck. a model factory has been build.
 * Created by raina on 17.10.2016.
 */
object ModelFactory {
  /**
   *
   * @param form  of card {@see Form}
   * @param color of card {@see Color}
   * @param fill  of card {@see Fill}
   * @param count of card {@see Count}
   * @return Card
   */
def card (form: Form.Value, color: Color.Value, fill: Fill.Value, count: Count.Value) : Card = Card(form, color, fill, count)

  /**
   * @param identify user ID
   * @param points user score of game
   * @param name some name {@example Joe Doe}
   * @return Player
   */
def player(identify: Int, points: Int, name: String): Player = Player(identify, points, name)

  /**
   * @param players of the game {@see Player}
   * @param unusedCards of the game {@see Card}
   * @param cardsInField of the game{@see Card}
   * @return Session for a game
   */
def session(players: List[Player], unusedCards: List[Card], cardsInField: List[Card]) : Session = Session(players,unusedCards, cardsInField)

}
