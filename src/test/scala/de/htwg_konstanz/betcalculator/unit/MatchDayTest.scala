package de.htwg_konstanz.betcalculator.unit
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import org.scalatest.BeforeAndAfterAll
import de.htwg_konstanz.betcalculator._
import org.scalatest.matchers.ShouldMatchers


class MatchDayTest extends FunSuite with ShouldMatchers with Checkers with BeforeAndAfterAll  {
  test("All gameday members should be none") {
    val gameday = GameDay()
    import gameday._
    val members = List(no, date)
    members forall { None == } should be(true)
  }
  

}
