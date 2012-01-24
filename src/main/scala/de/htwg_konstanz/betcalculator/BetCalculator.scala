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

  private def generateQuotesList(betRow: Set[Bet]) = {
    betRow.toList.flatMap { e =>
      try {
        List(e.quote)
      } catch {
        case _ => Nil
      }
    }
  }

  def calculateOverallWinnings(combinations: Set[Set[Bet]]): Set[RowWinnings] = {
    calculateOverallQuote(combinations) map {
      combination =>
        estimateRowValue(combination.combination) match {
          case true => RowWinnings(combination.combination, combination.quote, 0.00)
          case false => RowWinnings(combination.combination, combination.quote, limitDecimals(combination.quote * (wager / combinations.size), 2))
        }
    }
  }

  private def estimateRowValue(row: Set[Bet]): Boolean = {
    val values = row.toList.flatMap { e =>
      try {
        List(e.winning)
      } catch {
        case _ => Nil
      }
    }
    values.contains(false)
  }
  private def limitDecimals(number: Double, length: Int) = BigDecimal(number).setScale(length, BigDecimal.RoundingMode.FLOOR).toDouble

}

final case class RowQuotes(val combination: Set[Bet], quote: Double)
final case class RowWinnings(val combination: Set[Bet], overalQuote: Double, winning: Double)