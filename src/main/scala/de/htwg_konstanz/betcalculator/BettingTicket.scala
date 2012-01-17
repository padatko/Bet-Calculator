package de.htwg_konstanz.betcalculator

object BettingTicket {
  
  var bets = List[Bet]()
  
  def addBet(bet: Bet) = {
    bets :+= bet
  }
  
  def calculateCombinationNumbers() = {
    val allGames = bets.size
    (2 to allGames-1).toList
  }
  
  def clearList() = bets = Nil

}