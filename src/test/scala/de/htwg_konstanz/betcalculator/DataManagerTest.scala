package de.htwg_konstanz.betcalculator

class DataManagerTest extends UnitTestConfiguration with ThingsNeededForTests {
  test("src/test/resources/test.xml should be corretly parsed") {
    DataManager.getTeamName(1) should be("FC Bayern Muenchen")
  }
}