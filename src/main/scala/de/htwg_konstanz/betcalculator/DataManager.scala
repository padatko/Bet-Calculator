package de.htwg_konstanz.betcalculator

import scala.xml._

class DataManager() {
  
  
  def initializeData = {
    val teamsInXml = FileReader.readData("src/main/resources/teams.xml")
    val matchesInXml = FileReader.readData("src/main/resources/matches.xml")
  }


}