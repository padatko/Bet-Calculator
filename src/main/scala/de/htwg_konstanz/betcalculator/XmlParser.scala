package de.htwg_konstanz.betcalculator
import scala.xml.Elem
import scala.xml.NodeSeq

object XmlParser {
  def parseData(xml: Elem) = {
    parseXml(xml)
  }
  
  private def parseXml(xml: Elem) = chooseParsingStrategy(xml \\ "BetCalculator")
  private def chooseParsingStrategy(rootNode: NodeSeq) = {
    (rootNode \\ "@type").text match {
      case "matches" => parseMatches(rootNode)
      case "teams" => parseTeams(rootNode)
      case _ => null // error message
    }
  }
  
  private def parseMatches(rootNode: NodeSeq) = {
    parseGameDays(rootNode \\ "GameDay")
  }
  
  private def parseTeams(rootNode: NodeSeq) = {
    var teams = Map[Int,String]();
    for (team <- rootNode \\ "Team")
      yield teams += ((team \ "id").text.toInt -> (team \ "name").text)  
   teams
  }
  

  private def parseGameDays(gameDayNodes: NodeSeq) =
    for (gameDay <- gameDayNodes)
      yield GameDay(
      no = (gameDay \ "no").text.toInt,
      games = parseGames(gameDay \\ "Game").toList)

  private def parseGames(gameNodes: NodeSeq) =
    for (game <- gameNodes)
      yield Game(
      no = (game \ "id").text.toInt,
      teamHome = (game \ "homeTeam").text.toInt,
      teamAway = (game \ "awayTeam").text.toInt,
      homeOdds = (game \ "homeOdds").text.toDouble,
      tieOdds = (game \ "tieOdds").text.toDouble,
      awayOdds = (game \ "awayOdds").text.toDouble)
}