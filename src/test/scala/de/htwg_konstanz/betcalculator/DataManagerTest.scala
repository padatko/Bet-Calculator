package de.htwg_konstanz.betcalculator

class DataManagerTest extends UnitTestConfiguration {
  test("bla") {
    val manager = new DataManager("src/test/resources/test.xml")
    var games = List[Game]()
    games ::= Game(1, "FC Bayern Muenchen", "Borussia Dortmund", 1.1, 1.3, 1.7)
    games ::= Game(2, "Werder Bremen", "Hamburger SV", 1.7, 1.8, 2.5)
    
    var gamedays = List[GameDay]()
    gamedays ::= GameDay(1, games)
    
    
     manager.getData should equal(gamedays)
  }

}