package de.htwg_konstanz.betcalculator

class UtilsTest extends UnitTestConfiguration {
  
  test("Given double 34.761162288 schould return double with two decimals") {
    val actualResult = Utils.limitDecimals(34.761162288, 2)
    val expectedResult = 34.76
    
    actualResult should be(expectedResult)
  }

}