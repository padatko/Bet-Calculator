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
  def placeBet(choice: Int): Unit = {
   model.placeBet(choice)
   publish(new BetAdded(model.bets))
  }
  def placeWinningBets(winningBetsIds: List[Int]): Unit = model.placeWinningBets(winningBetsIds)
  def calculateResult: Set[RowWinnings] = {
    publish(new WinningsCalculated(model.calculateResult))
    model.calculateResult
  }
  def getGameDays: List[GameDay] = model.getGameDays
  def clearList = { 
    model.clearList
    publish(new TicketChanged)
  }
}

class GameDayChanged extends Event {}
class GameChanged(var game: Game) extends Event {}
class WinningsCalculated(var result: Set[RowWinnings]) extends Event {}
class BetAdded(var bets: Map[Int,Bet]) extends Event {}
class TicketChanged extends Event {}