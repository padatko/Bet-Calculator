package de.htwg_konstanz.betcalculator

import scala.xml._

abstract class Reader {
  def readData(file: String): Elem
}