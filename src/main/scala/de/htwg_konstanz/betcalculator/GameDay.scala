package de.htwg_konstanz.betcalculator
import java.util.Date
import java.lang.Integer

final case class GameDay (
    no: Option[Integer] = None,
    date: Option[Date] = None) {
  
	override def toString = List(no, date)
		.map{ _.getOrElse() }
		.mkString("bla")
}