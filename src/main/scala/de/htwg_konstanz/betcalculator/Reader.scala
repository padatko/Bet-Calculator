package de.htwg_konstanz.betcalculator
import scala.xml.Elem

abstract class Reader {
  def readData
}

abstract class Parser {
  def parseData
}