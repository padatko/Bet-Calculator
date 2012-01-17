package de.htwg_konstanz.betcalculator

import scala.xml._

abstract class Reader {
  def readData: Elem
}

abstract class Parser {
  def parseData
}