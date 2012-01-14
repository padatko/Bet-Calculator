package de.htwg_konstanz.betcalculator

class MatchDayTest extends UnitTestConfiguration  {
  test("All gameday members should be none") {
    val gameday = GameDay()
    import gameday._
    val members = List(no, date)
    members forall { None == } should be(true)
  }
  
}
