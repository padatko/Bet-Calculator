package de.htwg_konstanz.betcalculator

class BetCalculator(
  val allBets: Map[Int, Bet],
  val systemCombination: Int) {

  def createCombinations(quotes: Set[Double], systemCombination: Int): Set[Set[Double]] = {
    require(quotes.size > 2)
    require(systemCombination > 1 && systemCombination < quotes.size)
    quotes.toList.combinations(systemCombination).toSet map { (list: List[Double]) => list.toSet }
  }

  def createCombinations2: Set[Set[Bet]] = {
    require(allBets.size > 2)
    require(systemCombination > 1 && systemCombination < allBets.size)
    allBets.values.toList.combinations(systemCombination).toSet map { (t: List[Bet] => t.toSet)}
//    allBets.combinations(systemCombination).toSet map { (list: List[Bet]) => list.toSet }
//    allBets.values.toList.combinations(systemCombination).toSet map { (list: List[Double]) => list.toSet }
  }

  def calculateOverallQuote(combinations: Set[Set[Double]]): Set[RowQuotes] = combinations map {
    combination => RowQuotes(combination, combination.product)
  }

  def calculateOverallWinnings(combinations: Set[Set[Double]], betPart: Double): Set[RowWinnings] = {
    calculateOverallQuote(combinations) map {
      case RowQuotes(combination, quote) => RowWinnings(combination, quote * betPart)
    }
  }
}

final case class RowQuotes(val combination: Set[Double], quote: Double)
final case class RowWinnings(val combination: Set[Double], winning: Double)