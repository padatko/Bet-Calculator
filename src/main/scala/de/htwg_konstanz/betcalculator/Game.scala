package de.htwg_konstanz.betcalculator
import java.util.Locale

final case class Game(no: Int, teamHome: Int, teamAway: Int, homeOdds: Double, tieOdds: Double, awayOdds: Double) {
  override def toString = """%d | %s (%.2f) vs (%.2f) %s (%.2f)""".stripMargin.format(no, teamHome, homeOdds, tieOdds, teamAway, awayOdds)
}