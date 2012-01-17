package de.htwg_konstanz.betcalculator

object BetCalculator {
  def createCombinations(quotes: Set[Double], combinationsAmount: Int): Set[Set[Double]] = {
    require(quotes.size > 2)
    require(combinationsAmount > 1 && combinationsAmount < quotes.size)
    quotes.toList.combinations(combinationsAmount).toSet map { (list: List[Double]) => list.toSet }
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