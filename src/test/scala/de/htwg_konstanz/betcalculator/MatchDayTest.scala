package de.htwg_konstanz.betcalculator
import java.util.Date

class MatchDayTest extends UnitTestConfiguration  {
  
  val newline = System.getProperty("line.separator")
  
  test("All gameday members should be none") {
    val gameday = GameDay()
    import gameday._
    val members = List(no, games)
    members forall { None == } should be(true)
  }
  
  test("toString function should match committed paramaters") {
    val date = new Date()
    val games = new Array[Game](2)
    games(0) = Game(Some(1), Some("FC Bayern Muenchen"), Some("Borussia Dortmund"))
    games(1) = Game(Some(2), Some("Werder Bremen"), Some("Hamburger SV"))
    val gameday = GameDay(Some(2), Some(games))
    val expectedGameday = "2. Spieltag " + newline +
    		"1 | FC Bayern Muenchen vs Borussia Dortmund" + newline +
    		"2 | Werder Bremen vs Hamburger SV"
      
    gameday.toString() should be(expectedGameday)
  }
  
  test("toString for a simple game") {
    val game = Game(Some(1), Some("FC Bayern Muenchen"), Some("Borussia Dortmund"))
    val expectedGame = "1 | FC Bayern Muenchen vs Borussia Dortmund"
      
      game.toString() should be (expectedGame)
  }
  
}
