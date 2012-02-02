package de.htwg_konstanz.betcalculator

class BetCalculator(
  val allBets: Map[Int, Bet],
  val systemCombination: Int,
  val wager: Double) {

  def createCombinations: Set[Set[Bet]] = {
    require(allBets.size > 2)
    require(systemCombination > 1 && systemCombination < allBets.size)
    allBets.values.toList.combinations(systemCombination).toSet map { (list: List[Bet]) => list.toSet }
  }

  def calculateOverallQuote(combinations: Set[Set[Bet]]): Set[RowQuotes] = combinations map {
    combination =>
      RowQuotes(combination, limitDecimals(generateQuotesList(combination).product, 2))
  }

  private def generateQuotesList(betRow: Set[Bet]) = betRow.toList map { _.quote }

  def calculateOverallWinnings(combinations: Set[Set[Bet]]): Set[RowWinnings] = {
    calculateOverallQuote(combinations) map { c =>
      if (estimateRowValue(c.combination))
        RowWinnings(c.combination, c.quote, 0.00)
      else
        RowWinnings(c.combination, c.quote, limitDecimals(c.quote * (wager / combinations.size), 2))
    }
  }

  private def estimateRowValue(row: Set[Bet]) = row.map { _.winning }.contains(false)
  private def limitDecimals(number: Double, length: Int) = BigDecimal(number).setScale(length, BigDecimal.RoundingMode.FLOOR).toDouble

}

final case class RowQuotes(val combination: Set[Bet], quote: Double)
final case class RowWinnings(val combination: Set[Bet], overalQuote: Double, winning: Double)