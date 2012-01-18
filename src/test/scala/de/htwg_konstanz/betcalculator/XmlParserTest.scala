import de.htwg_konstanz.betcalculator.UnitTestConfiguration
import de.htwg_konstanz.betcalculator.ThingsNeededForTests
import de.htwg_konstanz.betcalculator.XmlParser
import de.htwg_konstanz.betcalculator.FileReader
import de.htwg_konstanz.betcalculator.Game
import de.htwg_konstanz.betcalculator.GameDay


class XmlParserTest extends UnitTestConfiguration with ThingsNeededForTests {
  
  test("parsing of teams xml file should match given map") {
    val expectedTeams = Map(1 -> "FC Bayern Muenchen", 2 -> "Borussia Dortmund", 3 -> "FC Schalke 04")
    val acutalTeams = XmlParser.parseData(FileReader.readData("src/test/resources/teams.xml"))
    
    expectedTeams should be(acutalTeams)
  }
  
  test("parsing of matches xml file should match given list") {
    val games = List(
      Game(1, 1, 2, 1.1, 1.3, 1.7),
      Game(2, 3, 4, 1.5, 1.9, 1.1))
      
    val expectedMatches = List(GameDay(1, games))
    val actualMatches = XmlParser.parseData(FileReader.readData("src/test/resources/matches.xml"))
    
    expectedMatches should be(actualMatches)
  }

}