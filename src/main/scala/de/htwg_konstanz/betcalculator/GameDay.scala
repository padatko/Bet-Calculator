package de.htwg_konstanz.betcalculator

import java.util.Date

final case class GameDay (no: Option[Int] = None, date: Option[Date] = None) {
	override def toString = List(no, date)
		.map{ _.getOrElse("") }
		.mkString("bla")
}