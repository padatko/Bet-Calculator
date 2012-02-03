package de.htwg_konstanz.betcalculator
import de.htwg_konstanz.betcalculator.model._

class BettingSessionTest extends UnitTestConfiguration {

  private var bettingSession: BettingSession = _

  override def beforeEach() {
    bettingSession = new BettingSession
  }

  test("Given 4 bets, clearList should remove all elements and calculateCombinationsNumbers should return empty list") {
    val expectedNumbers = List[Int]()
    bettingSession.clearList()
    val actualNumbers = bettingSession.calculateCombinationNumbers

    actualNumbers should be(expectedNumbers)
  }

  test("No gameday should be chosen") {
    val actualGameDay = bettingSession.chosenGameDay
    actualGameDay should be(null)
  }

  test("No game should be chosen") {
    val actualGame = bettingSession.chosenGame
    actualGame should be(null)
  }

  test("Choose correct game") {
    bettingSession.chooseGameDay(19)
    bettingSession.chooseGame(1)
    val actualGame = bettingSession.chosenGame
    val expectedGame = Game(1, 7, 12, 2.05, 3.35, 3.55)

    actualGame should be(expectedGame)
  }

  test("Place bet on home team in game 1 of gameday 19 and verify bet") {
    bettingSession.chooseGameDay(19)
    bettingSession.chooseGame(1)
    bettingSession.placeBet(1)
    val actualBets = bettingSession.bets
    val expectedBets = Map(1 -> Bet(7, 2.05))

    actualBets should be(expectedBets)
  }

  test("Place bet on away team in game 1 and on tie in game 2 of gameday 19 and verify bet") {
    bettingSession.chooseGameDay(19)
    bettingSession.chooseGame(1)
    bettingSession.placeBet(2)
    bettingSession.chooseGame(2)
    bettingSession.placeBet(0)
    val actualBets = bettingSession.bets
    val expectedBets = Map(
      1 -> Bet(12, 3.55),
      2 -> Bet(0, 4.75))

    actualBets should be(expectedBets)
  }

  test("Choose a Calculation System") {
    val expectedSystem = 2
    bettingSession.chooseSystem(expectedSystem)
    val actualSystem = bettingSession.chosenSystem

    actualSystem should be(expectedSystem)
  }

  test("Choose winning bets and verify result") {
    bettingSession.chooseGameDay(19)
    bettingSession.chooseGame(1)
    bettingSession.placeBet(2)
    bettingSession.chooseGame(2)
    bettingSession.placeBet(0)
    bettingSession.placeWinningBets(List(2))
    val actualWinningBets = bettingSession.bets
    val expectedWinningBets = Map(1 -> Bet(12, 3.55), 2 -> Bet(0, 4.75, true))

    actualWinningBets should be(expectedWinningBets)
  }

  test("Set betting amount and verify result") {
    val expectedBettingAmount = 10.00
    bettingSession.setBettingAmount(expectedBettingAmount)
    val actualBettingAmount = bettingSession.bettingAmount

    actualBettingAmount should be(expectedBettingAmount)
  }

}