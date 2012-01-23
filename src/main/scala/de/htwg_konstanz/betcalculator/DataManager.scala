package de.htwg_konstanz.betcalculator

object DataManager {
  val teamsInXml = FileReader.readData("src/main/resources/teams.xml")
  val matchesInXml = FileReader.readData("src/main/resources/matches.xml")
  val teams = XmlTeamsParser.parseData(teamsInXml)
  val matches = XmlMatchesParser.parseData(matchesInXml)
  
  def getTeamName(id: Int): String = {
    id match {
      case 0 => "X"
      case no => teams(no)
    }
  }
  def getMatches: Seq[GameDay] = matches
}