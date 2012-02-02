package de.htwg_konstanz.betcalculator.model

import de.htwg_konstanz.betcalculator._

class BettingSession extends BaseModel {

  private var bets_ = Map[Int, Bet]()

  private def addBet(gameId: Int, bet: Bet) = {
    bets_ += gameId -> bet
  }
  def bets: Map[Int, Bet] = bets_
  def calculateCombinationNumbers: List[Int] = (2 to bets.size - 1).toList
  def clearList(): Unit = {
    bets_ = Map[Int, Bet]()
  }

  def getGameDays: List[GameDay] = DataManager.getMatches.toList

  def chooseGameDay(id: Int): Unit = {
    def optionalGameDay = DataManager.getMatches find { e => e.no == id }
    optionalGameDay foreach { chosenGameDay = _ }
  }

  def chooseGame(id: Int): Unit = {
    def optionalGame = chosenGameDay.games find { e => e.no == id }
    optionalGame foreach { chosenGame = _ }
  }

  def chooseSystem(choice: Int): Unit = {
    chosenSystem = choice
  }

  def setBettingAmount(amount: Double): Unit = {
    bettingAmount = amount
  }

  // Explicit properties
  private[this] var chosenGameDay_ : GameDay = null
  def chosenGameDay: GameDay = chosenGameDay_
  private def chosenGameDay_=(gd: GameDay) {
    chosenGameDay_ = gd
  }

  private[this] var chosenGame_ : Game = null
  def chosenGame: Game = chosenGame_
  private def chosenGame_=(game: Game) {
    chosenGame_ = game
  }

  private[this] var chosenSystem_ : Int = 0
  def chosenSystem: Int = chosenSystem_
  private def chosenSystem_=(choice: Int) {
    chosenSystem_ = choice
  }

  private[this] var bettingAmount_ : Double = 0.0
  def bettingAmount: Double = bettingAmount_
  private def bettingAmount_=(amount: Double) {
    bettingAmount_ = amount
  }

  def placeBet(choice: Int): Unit = {
    val c = chosenGame
    import c._
    choice match {
      case 1 => addBet(no, Bet(teamHome, homeOdds))
      case 0 => addBet(no, Bet(0, tieOdds))
      case 2 => addBet(no, Bet(teamAway, awayOdds))
    }
  }

  def placeWinningBets(winningBetsIds: List[Int]): Unit = {
    for ((id, bet) <- bets)
      bet.winning = winningBetsIds.contains(id)
  }

  def calculateResult: Set[RowWinnings] = {
    val calculator = new BetCalculator(bets, chosenSystem, bettingAmount)
    calculator.calculateOverallWinnings(calculator.createCombinations)
  }
}