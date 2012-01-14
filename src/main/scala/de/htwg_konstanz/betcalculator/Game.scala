package de.htwg_konstanz.betcalculator

final case class Game(no: Option[Int], teamHome: Option[String], teamAway: Option[String]) {
  override def toString = {
    var result = no.get + " | " + teamHome.get + " vs " + teamAway.get
    result
  }
}