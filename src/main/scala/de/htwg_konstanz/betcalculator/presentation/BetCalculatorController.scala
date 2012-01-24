package de.htwg_konstanz.betcalculator.presentation
import de.htwg_konstanz.betcalculator._

class BetCalculatorController(val model: BaseModel) extends BaseController {
  
  var chosenGame_ = 0
  
  def chosenGameDay: GameDay = model.chosenGameDay
  def chooseGameDay(id: Int): Unit = model.chooseGameDay(id)
  def getBets: Map[Int, Bet] = model.bets
  def chosenGame: Game = model.chosenGame
  def chooseGame(id: Int): Unit = model.chooseGame(id)
  def getWager: Double = model.bettingAmount
  def chosenSystem: Int = model.chosenSystem
  def chooseSystem(choice: Int): Unit = model.chooseSystem(choice)
  def calculateCombinationNumbers: List[Int] = model.calculateCombinationNumbers
  def setBettingAmount(wager: Double): Unit = model.setBettingAmount(wager)
  def placeBet(choice: Int): Unit = {
    model.placeBet(choice)
  }
  def placeWinningBets(winningBetsIds: List[Int]): Unit = model.placeWinningBets(winningBetsIds)
  def calculateResult: Set[RowWinnings] = { model.calculateResult }
  def getGameDays: List[GameDay] = model.getGameDays
  def clearList = model.clearList
}