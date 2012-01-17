import de.htwg_konstanz.betcalculator.UnitTestConfiguration
import de.htwg_konstanz.betcalculator.BettingTicket
import de.htwg_konstanz.betcalculator.Bet

class BettingTicketTest extends UnitTestConfiguration {
  
  BettingTicket.addBet(new Bet(1, 2.0))
  BettingTicket.addBet(new Bet(2, 4.0))
  BettingTicket.addBet(new Bet(3, 6.0))
  BettingTicket.addBet(new Bet(4, 5.0))
  
  test("Given 4 bets, calculateCombinationNumbers should return 2 possible combinations") {    
    val expectedNumbers = List(2,3)
    val actualNumbers = BettingTicket.calculateCombinationNumbers()
    
    actualNumbers should be(expectedNumbers)
  }
  
  test("Given 4 bets, clearList should remove all elements and calculateCombinationsNumbers should return empty list") {   
    val expectedNumbers = List()
    BettingTicket.clearList()
    val actualNumbers = BettingTicket.calculateCombinationNumbers()
    
    actualNumbers should be(expectedNumbers)
  }
}