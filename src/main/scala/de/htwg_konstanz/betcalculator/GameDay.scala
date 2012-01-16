package de.htwg_konstanz.betcalculator

final case class GameDay(no: Int, games: List[Game]) {
  override def toString = games.mkString(
    start = no + ". Spieltag " + newLine,
    sep = newLine,
    end = "")
}