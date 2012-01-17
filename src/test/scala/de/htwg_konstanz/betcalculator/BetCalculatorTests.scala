package de.htwg_konstanz.betcalculator

class BetCalculatorTests extends UnitTestConfiguration {

  private val threeChosenQuotes = Set(1.0, 2.0, 3.0)

  test("3 quotes should result in 3 pairs, when invoked with 2 combinations") {
    val actualPairs = BetCalculator.createCombinations(threeChosenQuotes, 2)
    val expectedPairs = Set(Set(1.0, 2.0), Set(2.0, 3.0), Set(3.0, 1.0))

    actualPairs should be(expectedPairs)
  }

  test("4 quotes should result in 3 tuple3s, when invoked with 3 combinations") {
    val chosenQuotes = Set(1.0, 2.0, 3.0, 4.0)
    val actualPairs = BetCalculator.createCombinations(chosenQuotes, 3)
    val expectedPairs = Set(Set(1.0, 2.0, 3.0), Set(1.0, 2.0, 4.0), Set(2.0, 3.0, 4.0), Set(1.0, 3.0, 4.0))

    actualPairs should be(expectedPairs)
  }

  test("4 quotes should result in 6 pairs, when invoked with 2 combinations") {
    val chosenQuotes = Set(1.0, 2.0, 3.0, 4.0)
    val actualPairs = BetCalculator.createCombinations(chosenQuotes, 2)
    val expectedPairs = Set(Set(1.0, 2.0), Set(1.0, 3.0), Set(1.0, 4.0), Set(2.0, 3.0), Set(3.0, 4.0), Set(2.0, 4.0))

    actualPairs should be(expectedPairs)
  }

  test("BetRowCreator should throw an exception when createCombinations is invoked with less than 3 quotes") {
    val chosenQuotes = Set(1.0, 2.0)
    evaluating { BetCalculator.createCombinations(chosenQuotes, 2) } should produce[IllegalArgumentException]
  }

  test("BetRowCreator should throw an exception when less than 2 combinations are invoked on 3 quotes") {
    evaluating { BetCalculator.createCombinations(threeChosenQuotes, 1) } should produce[IllegalArgumentException]
  }

  test("BetRowCreator should throw an exception when more than 2 combinations are invoked on 3 quotes") {
    evaluating { BetCalculator.createCombinations(threeChosenQuotes, 3) } should produce[IllegalArgumentException]
  }

  test("Given 3 pairs calculateOverallQuote should return overall results") {
    val pairs = Set(Set(1.0, 2.0), Set(2.0, 3.0), Set(3.0, 1.0))
    val actualSetOfOverallQuotes = BetCalculator.calculateOverallQuote(pairs)
    val expectedSetOfOverallQuotes = Set(RowQuotes(Set(1.0, 2.0), 2.0), RowQuotes(Set(2.0, 3.0), 6.0), RowQuotes(Set(3.0, 1.0), 3.0))

    actualSetOfOverallQuotes should be(expectedSetOfOverallQuotes)
  }

  test("Given 3 pairs calculateOverallWinning should return overall winning") {
    val pairs = Set(Set(1.0, 2.0), Set(2.0, 3.0), Set(3.0, 1.0))
    val actualSetOfOverallWinnings = BetCalculator.calculateOverallWinnings(pairs, 2.0)
    val expectedSetOfOverallWinnings = Set(RowWinnings(Set(1.0, 2.0), 4.0), RowWinnings(Set(2.0, 3.0), 12.0), RowWinnings(Set(3.0, 1.0), 6.0))

    actualSetOfOverallWinnings should be(expectedSetOfOverallWinnings)
  }

  //   test("Given 3 RowResults calculateOverallWinning should return overall winnig") {
  //    val rowResults = Set(
  //        RowResults(Set(1.0, 2.0), 2.0), 
  //        RowResults(Set(2.0, 3.0), 6.0), 
  //        RowResults(Set(3.0, 1.0), 3.0))
  //    val actualSetOfOverallQuotes = BetCalculator.calculateOverallWinning(rowResults, )
  //    val expectedSetOfOverallQuotes = Set(RowResults(Set(1.0, 2.0), 2.0), RowResults(Set(2.0, 3.0), 6.0), RowResults(Set(3.0, 1.0), 3.0))
  //
  //    actualSetOfOverallQuotes should be(expectedSetOfOverallQuotes)
  //  }

}