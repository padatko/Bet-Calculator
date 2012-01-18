package de.htwg_konstanz.betcalculator
import scala.xml.XML

object FileReader extends Reader {
  override def readData(file: String) = {
    XML.load(file)
  }

}