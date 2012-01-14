package de.htwg_konstanz.betcalculator

import java.util.Date

final case class GameDay (no: Option[Int] = None, games: Option[Array[Game]] = None) {
	override def toString = {
	  var result = no.get + ". Spieltag "
	  val newline = System.getProperty("line.separator")
	  games.get.foreach(game => result += newline + game.toString()) 	
	  result
	}
}