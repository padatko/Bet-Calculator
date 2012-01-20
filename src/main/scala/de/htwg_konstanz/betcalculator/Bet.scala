package de.htwg_konstanz.betcalculator

final case class Bet(teamId: Int, quote: Double,var winning: Boolean = false) 