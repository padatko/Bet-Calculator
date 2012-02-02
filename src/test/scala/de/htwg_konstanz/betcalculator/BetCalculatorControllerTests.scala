package de.htwg_konstanz.betcalculator

import de.htwg_konstanz.betcalculator.model._
import de.htwg_konstanz.betcalculator.controller._

class BetCalculatorControllerTests extends UnitTestConfiguration {

  private var model: BettingSession = _
  private var controller: BetCalculatorController = _

  override def beforeEach() {
    model = new BettingSession
    controller = new BetCalculatorController(model)
  }

  test("Set gameday and verify chosenGameDay") {
    controller.chooseGameDay(19)
    val actualChosenGameDay = controller.chosenGameDay

    actualChosenGameDay.no should be(19)
  }

  test("Set game and verify chosenGame") {
    controller.chooseGameDay(19)
    controller.chooseGame(1)
    val actualChosenGame = controller.chosenGame

    actualChosenGame.no should be(1)
  }

  test("Set game, place bet and verify chosenGame") {
    controller.chooseGameDay(19)
    controller.chooseGame(1)
    controller.placeBet(2)
    val actualPlacedBets = controller.getBets
    val expectedPlacedBets = Map(1 -> Bet(12, 3.55))

    actualPlacedBets should be(expectedPlacedBets)
  }

  test("Place wager and verify it") {
    controller.setBettingAmount(5.0)
    val actualWager = controller.getWager
    val expectedWager = 5.0

    actualWager should be(expectedWager)
  }

  test("Place system and verify it") {
    controller.chooseSystem(2)
    val actualSystem = controller.chosenSystem
    val expectedSystem = 2

    actualSystem should be(expectedSystem)
  }


}