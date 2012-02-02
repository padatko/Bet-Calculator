package de.htwg_konstanz.betcalculator.controller

import de.htwg_konstanz.betcalculator._
import scala.swing.Publisher

abstract class BaseController extends Publisher {
  def chosenGameDay: GameDay
  def getGameDays: List[GameDay]
  def getBets: Map[Int, Bet]
  def getWager: Double
  def chosenGame: Game
  def chosenSystem: Int
  def chooseGameDay(id: Int): Unit
  def calculateCombinationNumbers: List[Int]
  def chooseGame(id: Int): Unit
  def chooseSystem(choice: Int): Unit
  def setBettingAmount(wager: Double): Unit
  def placeBet(choice: Int): Unit
  def placeWinningBets(winningBetsIds: List[Int]): Unit
  def calculateResult: Set[RowWinnings]
  def clearList: Unit
}