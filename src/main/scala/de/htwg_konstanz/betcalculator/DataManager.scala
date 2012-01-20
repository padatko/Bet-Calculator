package de.htwg_konstanz.betcalculator

object DataManager {
  val teamsInXml = FileReader.readData("src/main/resources/teams.xml")
  val matchesInXml = FileReader.readData("src/main/resources/matches.xml")
  val teams = XmlTeamsParser.parseData(teamsInXml)
  val matches = XmlMatchesParser.parseData(matchesInXml)
  
  def getTeamName(id: Int): String = teams(id)
  def getMatches: Seq[GameDay] = matches
}