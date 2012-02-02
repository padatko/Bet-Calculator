package de.htwg_konstanz.betcalculator.model

import de.htwg_konstanz.betcalculator._

abstract class BaseModel {
  def chosenGameDay: GameDay
  def getGameDays: List[GameDay]
  def bets: Map[Int, Bet]
  def chosenGame: Game
  def bettingAmount: Double
  def chosenSystem: Int
  def calculateCombinationNumbers: List[Int]
  def chooseGameDay(id: Int): Unit
  def chooseGame(id: Int): Unit
  def chooseSystem(choice: Int): Unit
  def setBettingAmount(wager: Double): Unit
  def placeBet(choice: Int): Unit
  def placeWinningBets(winningBetsIds: List[Int]): Unit
  def calculateResult: Set[RowWinnings]
  def clearList(): Unit
}