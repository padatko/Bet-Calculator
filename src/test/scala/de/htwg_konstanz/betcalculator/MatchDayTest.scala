package de.htwg_konstanz.betcalculator
import java.util.Date

class MatchDayTest extends UnitTestConfiguration  {
  
  val newline = System.getProperty("line.separator")
  
  test("toString function should match committed paramaters") {
    val date = new Date()
    val gameOne = Game(1, "FC Bayern Muenchen", "Borussia Dortmund", 1.8, 1.2, 1.3)
    val gameTwo = Game(2, "Werder Bremen", "Hamburger SV", 1.4, 2.1, 1.9)
    val games = List[Game](gameOne, gameTwo)
    val gameday = GameDay(2, games)
    val expectedGameday = "2. Spieltag " + newline +
    		"1 | FC Bayern Muenchen (1,80) vs (1,20) Borussia Dortmund (1,30)" + newline +
    		"2 | Werder Bremen (1,40) vs (2,10) Hamburger SV (1,90)"
      
    gameday.toString() should be(expectedGameday)
  }
  
  test("toString for a simple game") {
    val game = Game(1, "FC Bayern Muenchen", "Borussia Dortmund", 1.8, 1.4, 2.1)
    val expectedGame = "1 | FC Bayern Muenchen (1,80) vs (1,40) Borussia Dortmund (2,10)"
      
    game.toString() should be (expectedGame)
  }
  
}
