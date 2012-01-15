package de.htwg_konstanz.betcalculator

final case class GameDay (no: Int, games: List[Game]) {
	override def toString = {
	  var result = no + ". Spieltag "
	  val newline = System.getProperty("line.separator")
	  games.foreach(game => result += newline + game.toString()) 	
	  result
	}
}