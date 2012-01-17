package de.htwg_konstanz.betcalculator

import java.util.Date

class MatchDayTest extends UnitTestConfiguration with ThingsNeededForTests{
  val newline = System.getProperty("line.separator")
 
  ignore("toString should match committed paramaters") {
    val date = new Date
    val gameOne = Game(1, "FC Bayern Muenchen", "Borussia Dortmund", 1.8, 1.2, 1.3)
    val gameTwo = Game(2, "Werder Bremen", "Hamburger SV", 1.4, 2.1, 1.9)
    val games = List(gameOne, gameTwo)
    val gameday = GameDay(2, games)
    val expectedGameday = "2. Spieltag " + newline +
      "1 | FC Bayern Muenchen (1" + delimeter + "80) vs (1" + delimeter + "20) Borussia Dortmund (1" + delimeter + "30)" + newline +
      "2 | Werder Bremen (1" + delimeter + "40) vs (2" + delimeter + "10) Hamburger SV (1" + delimeter + "90)"

    gameday.toString should be(expectedGameday)
  }

  ignore("toString of a simple game") {
    val actualGame = Game(1, "FC Bayern Muenchen", "Borussia Dortmund", 1.8, 1.4, 2.1)
    val expectedString = "1 | FC Bayern Muenchen (1" + delimeter + "80) vs (1" + delimeter + "40) Borussia Dortmund (2" + delimeter + "10)"
    actualGame.toString should be(expectedString)
  }
}
