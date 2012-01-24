package de.htwg_konstanz.betcalculator

final case class Bet(teamId: Int, quote: Double, var winning: Boolean = false) {
  override def toString = """%s (%.2f)""".stripMargin.format(DataManager.getTeamName(teamId), quote)
  def teamName = DataManager.getTeamName(teamId)
}