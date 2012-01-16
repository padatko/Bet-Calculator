package de.htwg_konstanz.betcalculator

class DataManagerTest extends UnitTestConfiguration with ThingsNeededForTests {
  test("src/test/resources/test.xml should be corretly parsed") {
    val manager = new DataManager("src/test/resources/test.xml")

    val games = List(
      Game(1, "FC Bayern Muenchen", "Borussia Dortmund", 1.1, 1.3, 1.7),
      Game(2, "Werder Bremen", "Hamburger SV", 1.7, 1.8, 2.5))

    val gamedays = List(GameDay(1, games))

    manager.getData should be(gamedays)
  }
}