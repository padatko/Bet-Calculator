package de.htwg_konstanz.betcalculator.presentation
import de.htwg_konstanz.betcalculator._
import scala.swing.Publisher
import scala.swing.event.Event

class BetCalculatorGuiController(val model: BaseModel) extends BaseController with Publisher {
  def chosenGameDay: GameDay = model.chosenGameDay
  def chooseGameDay(id: Int): Unit = {
   model.chooseGameDay(id)
   publish(new GameDayChanged)
  }
  def getBets: Map[Int, Bet] = model.bets
  def chosenGame: Game = model.chosenGame
  def chooseGame(id: Int): Unit = {
    model.chooseGame(id)
    publish(new GameChanged(model.chosenGame))
  }
  def getWager: Double = model.bettingAmount
  def chosenSystem: Int = model.chosenSystem
  def chooseSystem(choice: Int): Unit = model.chooseSystem(choice)
  def calculateCombinationNumbers: List[Int] = model.calculateCombinationNumbers
  def setBettingAmount(wager: Double): Unit = model.setBettingAmount(wager)
  def placeBet(choice: Int): Unit = model.placeBet(choice)
  def placeWinningBets(winningBetsIds: List[Int]): Unit = model.placeWinningBets(winningBetsIds)
  def calculateResult: Set[RowWinnings] = { model.calculateResult }
  def getGameDays: List[GameDay] = model.getGameDays
}

class GameDayChanged extends Event {}
class GameChanged(var game: Game) extends Event {}
class SystemChanged extends Event {}
class NewBetAdded extends Event {}