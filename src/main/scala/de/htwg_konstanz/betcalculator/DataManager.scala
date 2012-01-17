package de.htwg_konstanz.betcalculator

import scala.xml._

class DataManager(file: String) {
  def getData = {
    val reader = new FileReader("src/main/resources/test.xml")
    val parser = new XmlParser(reader.readData)
    parser.parseData
  }


}