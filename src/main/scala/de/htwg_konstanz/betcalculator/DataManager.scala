package de.htwg_konstanz.betcalculator

import scala.xml._

class DataManager(file: String) {
  def getData = parseXml(XML.load(file))

  private def parseXml(xml: Elem) = parseBetCalculator(xml \\ "BetCalculator")
  private def parseBetCalculator(betCalculatorNode: NodeSeq) = parseGameDays(betCalculatorNode \\ "GameDay")

  private def parseGameDays(gameDayNodes: NodeSeq) =
    for (gameDay <- gameDayNodes)
      yield GameDay(
      no = (gameDay \ "no").text.toInt,
      games = parseGames(gameDay \\ "Game").toList)

  private def parseGames(gameNodes: NodeSeq) =
    for (game <- gameNodes)
      yield Game(
      no = (game \ "id").text.toInt,
      teamHome = (game \ "homeTeam").text,
      teamAway = (game \ "awayTeam").text,
      homeOdds = (game \ "homeOdds").text.toDouble,
      tieOdds = (game \ "tieOdds").text.toDouble,
      awayOdds = (game \ "awayOdds").text.toDouble)
}