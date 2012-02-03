package de.htwg_konstanz.betcalculator

object Utils {
  def limitDecimals(number: Double, length: Int): Double = BigDecimal(number).setScale(length, BigDecimal.RoundingMode.FLOOR).toDouble
}