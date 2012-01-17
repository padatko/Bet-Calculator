package de.htwg_konstanz.betcalculator
import scala.xml.XML

class FileReader(file: String) extends Reader {
  override def readData = {
    XML.load(file)
  }

}