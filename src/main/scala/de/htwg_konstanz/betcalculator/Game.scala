package de.htwg_konstanz.betcalculator
import java.util.Locale

final case class Game(no: Int, teamHome: Int, teamAway: Int, homeOdds: Double, tieOdds: Double, awayOdds: Double) {
  override def toString = """[%d]: %s (%.2f) vs (%.2f) %s (%.2f)""".stripMargin.format(no, DataManager.getTeamName(teamHome), homeOdds, tieOdds, DataManager.getTeamName(teamAway), awayOdds)
}