package de.htwg_konstanz.betcalculator

class MatchDayTest extends UnitTestConfiguration with ThingsNeededForTests{
  val newline = System.getProperty("line.separator")
 
  test("toString should match committed paramaters") {
    val gameOne = Game(1, 1, 2, 1.8, 1.2, 1.3)
    val gameTwo = Game(2, 3, 4, 1.4, 2.1, 1.9)
    val games = List(gameOne, gameTwo)
    val gameday = GameDay(2, games)
    val expectedGameday = "2. Spieltag " + newline +
      "[1]: FC Bayern Muenchen (1,80) vs (1,20) Borussia Dortmund (1,30)" + newline +
      "[2]: FC Schalke 04 (1,40) vs (2,10) Borussia M'Gladbach (1,90)"

    gameday.toString should be(expectedGameday)
  }

  test("toString of a simple game") {
    val actualGame = Game(1, 1, 2, 1.8, 1.4, 2.1)
    val expectedString = "[1]: FC Bayern Muenchen (1,80) vs (1,40) Borussia Dortmund (2,10)"
    actualGame.toString should be(expectedString)
  }
}
