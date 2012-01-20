package de.htwg_konstanz.betcalculator

import scala.xml._
abstract class XmlParser[A] {
  final def parseData(xml: Elem): A = parseXml(xml)
  protected final def parseXml(xml: Elem): A = parseBetCalculator(xml \\ "BetCalculator")
  protected def parseBetCalculator(calculatorNode: NodeSeq): A
}

object XmlTeamsParser extends XmlParser[Map[Int, String]] {
  protected def parseBetCalculator(calculatorNode: NodeSeq) = parseTeams(calculatorNode \\ "Team")

  private def parseTeams(teamNodes: NodeSeq) = teamNodes
    .map { team => (team \ "id").text.toInt -> (team \ "name").text }
    .toMap
}

object XmlMatchesParser extends XmlParser[Seq[GameDay]] {
  protected def parseBetCalculator(calculatorNode: NodeSeq) = parseGameDays(calculatorNode \\ "GameDay")

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