package de.htwg_konstanz.betcalculator

class BettingSessionTest extends UnitTestConfiguration {

  private var bettingSession: BettingSession = _

  override def beforeEach() {
    bettingSession = new BettingSession
  }

  test("Given 4 bets, calculateCombinationNumbers should return 2 possible combinations") {
    //    bettingSession.addBet(new Bet(1, 2.0))
    //    bettingSession.addBet(new Bet(2, 4.0))
    //    bettingSession.addBet(new Bet(3, 6.0))
    //    bettingSession.addBet(new Bet(4, 5.0))
    //    val expectedNumbers = List(2, 3)
    //    val actualNumbers = bettingSession.calculateCombinationNumbers
    //
    //    actualNumbers should be(expectedNumbers)
  }

  test("Given 4 bets, clearList should remove all elements and calculateCombinationsNumbers should return empty list") {
    val expectedNumbers = List[Int]()
    bettingSession.clearList()
    val actualNumbers = bettingSession.calculateCombinationNumbers

    actualNumbers should be(expectedNumbers)
  }

  test("No gameday should be chosen") {
    val actualGameDay = bettingSession.chosenGameDay
    val expectedGameDay = null

    actualGameDay should be(expectedGameDay)
  }

  test("Choose correct gameday") {
    bettingSession.chooseGameDay(1)
    val actualGameDay = bettingSession.chosenGameDay
    val expectedGameDay = GameDay(1,
      List(Game(1, 1, 2, 1.1, 1.3, 1.7),
        Game(2, 3, 4, 1.5, 1.9, 1.1)))

    actualGameDay should be(expectedGameDay)
  }

  test("No game should be chosen") {
    val actualGame = bettingSession.chosenGame
    val expectedGame = null

    actualGame should be(expectedGame)
  }

  test("Choose correct game") {
    bettingSession.chooseGameDay(1)
    bettingSession.chooseGame(1)
    val actualGame = bettingSession.chosenGame
    val expectedGame = Game(1, 1, 2, 1.1, 1.3, 1.7)

    actualGame should be(expectedGame)
  }

  test("Place bet on home team in game 1 of gameday 1 and verify bet") {
    bettingSession.chooseGameDay(1)
    bettingSession.chooseGame(1)
    bettingSession.placeBet(1)
    val actualBets = bettingSession.bets
    val expectedBets = Map(1 -> Bet(1, 1.1))

    actualBets should be(expectedBets)
  }

  test("Place bet on away team in game 1 and on tie in game 2 of gameday 1 and verify bet") {
    bettingSession.chooseGameDay(1)
    bettingSession.chooseGame(1)
    bettingSession.placeBet(2)
    bettingSession.chooseGame(2)
    bettingSession.placeBet(0)
    val actualBets = bettingSession.bets
    val expectedBets = Map(1 -> Bet(2, 1.7), 2 -> Bet(0, 1.9))

    actualBets should be(expectedBets)
  }

  test("Choose a Calculation System") {
    val expectedSystem = 2
    bettingSession.chooseSystem(expectedSystem)
    val actualSystem = bettingSession.chosenSystem

    actualSystem should be(expectedSystem)
  }

  test("Choose winning bets and verify result") {
    bettingSession.chooseGameDay(1)
    bettingSession.chooseGame(1)
    bettingSession.placeBet(2)
    bettingSession.chooseGame(2)
    bettingSession.placeBet(0)
    bettingSession.placeWinningBets(List(2))
    val actualWinningBets = bettingSession.winningBets
    val expectedWinningBets = Map(2->Bet(0,1.9))

    actualWinningBets should be(expectedWinningBets)
  }


  ignore("Set betting amount and verify result") {
    val expectedBettingAmount = 10.00
    bettingSession.setBettingAmount(expectedBettingAmount)
    val actualBettingAmount = bettingSession.bettingAmount

    actualBettingAmount should be(expectedBettingAmount)
  }

  ignore("Calculate result") {}

}