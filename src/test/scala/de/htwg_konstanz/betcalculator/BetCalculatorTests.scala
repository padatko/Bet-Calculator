package de.htwg_konstanz.betcalculator

class BetCalculatorTests extends UnitTestConfiguration {

  private val betA = Bet(1, 1.1, true)
  private val betB = Bet(4, 1.1)
  private val betC = Bet(0, 1.3, true)
  private val betD = Bet(2, 2.1)
  private val threeBets: Map[Int, Bet] = Map(
    1 -> betA,
    2 -> betB,
    3 -> betC)
  private val fourBets: Map[Int, Bet] = Map(
    1 -> betA,
    2 -> betB,
    3 -> betC,
    4 -> betD)
  private val twoOfThreeCombinations: Set[Set[Bet]] = Set(
    Set(betA, betB),
    Set(betA, betC),
    Set(betB, betC))
  private val twoOfFourCombinations: Set[Set[Bet]] = Set(
    Set(betA, betB),
    Set(betC, betD),
    Set(betA, betC),
    Set(betB, betD),
    Set(betA, betD),
    Set(betB, betC))
  private val threeOfFourCombinations: Set[Set[Bet]] = Set(
    Set(betA, betB, betC),
    Set(betA, betB, betD),
    Set(betA, betC, betD),
    Set(betB, betC, betD))
  private val basicWager = 10.00

  test("3 quotes should result in 3 pairs, when invoked with 2 combinations") {
    val betCalculator = new BetCalculator(threeBets, 2, basicWager)
    val actualPairs = betCalculator.createCombinations
    val expectedPairs = twoOfThreeCombinations

    actualPairs should be(expectedPairs)
  }

  test("4 quotes should result in 3 tuple3s, when invoked with 3 combinations") {
    val betCalculator = new BetCalculator(fourBets, 3, basicWager)
    val actualPairs = betCalculator.createCombinations
    val expectedPairs = threeOfFourCombinations

    actualPairs should be(expectedPairs)
  }

  test("4 quotes should result in 6 pairs, when invoked with 2 combinations") {
    val betCalculator = new BetCalculator(fourBets, 2, basicWager)
    val actualPairs = betCalculator.createCombinations
    val expectedPairs = twoOfFourCombinations

    actualPairs should be(expectedPairs)
  }

  test("BetRowCreator should throw an exception when createCombinations is invoked with less than 3 quotes") {
    val betCalculator = new BetCalculator(Map(1 -> betA, 2 -> betB), 2, basicWager)
    evaluating { betCalculator.createCombinations } should produce[IllegalArgumentException]
  }

  test("BetRowCreator should throw an exception when less than 2 combinations are invoked on 3 quotes") {
    val betCalculator = new BetCalculator(Map(1 -> betA, 2 -> betB), 1, basicWager)
    evaluating { betCalculator.createCombinations } should produce[IllegalArgumentException]
  }

  test("BetRowCreator should throw an exception when more than 2 combinations are invoked on 3 quotes") {
    val betCalculator = new BetCalculator(Map(1 -> betA, 2 -> betB), 2, basicWager)
    evaluating { betCalculator.createCombinations } should produce[IllegalArgumentException]
  }

  test("Given 3 pairs calculateOverallQuote should return overall results") {
    val betCalculator = new BetCalculator(threeBets, 2, basicWager)
    val combinations = betCalculator.createCombinations
    val actualSetOfOverallQuotes = betCalculator.calculateOverallQuote(combinations)
    val expectedSetOfOverallQuotes = Set(RowQuotes(Set(betA, betB), 1.21), RowQuotes(Set(betA, betC), 1.43), RowQuotes(Set(betB, betC), 1.43))

    actualSetOfOverallQuotes should be(expectedSetOfOverallQuotes)
  }

  test("Given 3 pairs calculateOverallWinning should return overall winning") {
    val betCalculator = new BetCalculator(threeBets, 2, 10.00)
    val combinations = betCalculator.createCombinations
    val actualSetOfOverallWinnings = betCalculator.calculateOverallWinnings(combinations)
    val expectedSetOfOverallWinnings = Set(RowWinnings(Set(betA, betB), 1.21, 0.00), RowWinnings(Set(betB, betC), 1.43, 0.00), RowWinnings(Set(betA, betC), 1.43, 4.76))

    actualSetOfOverallWinnings should be(expectedSetOfOverallWinnings)
  }

}