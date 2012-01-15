package de.htwg_konstanz.betcalculator
import scala.xml.XML

class DataManager(file: String) {
  
  def loadFromXmlFile = parseXml(XML.load(file))
  
  private def parseXml(xml: scala.xml.Elem) : List[GameDay] = {
    var data = List[GameDay]()
    val rootNode = xml\\"BetCalculator"
    val gamedayNodes = rootNode\\"GameDay"
    gamedayNodes.foreach(gameday => {
      val no = (gameday\"no").text.toInt
      var games = List[Game]()
      val gameNodes = gameday\\"Game"
      gameNodes.foreach(game => {
        val id = (game\"id").text.toInt
        val homeTeam = (game\"homeTeam").text
        val awayTeam = (game\"awayTeam").text
        val homeOdds = (game\"homeOdds").text.toFloat
        val awayOdds = (game\"awayOdds").text.toFloat
        val tieOdds = (game\"tieOdds").text.toFloat
        games ::= Game(id, homeTeam, awayTeam, homeOdds, tieOdds, awayOdds)
      })
      data ::= GameDay(no, games)
    })
    
    data
  }
  
  def getData = {
    loadFromXmlFile
    
  }

}