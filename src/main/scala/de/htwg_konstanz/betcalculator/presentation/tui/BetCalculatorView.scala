package de.htwg_konstanz.betcalculator.presentation.tui

import de.htwg_konstanz.betcalculator.presentation._
import Console._

class BetCalculatorView(var controller: BaseController) {
  def display(): Unit = {
    showGreetings
    initControlls
  }

  private def showGreetings = {
    writeLine("Welcome to the Bet Calculator")(YELLOW)
    writeLine("With this application you have the possibility to determine possible bet winnings")(YELLOW)
  }

  private def writeLine(message: Any = "")(implicit textColor: String = RESET) = {
    println(textColor + message)
  }

  private def writeList(list: scala.collection.GenTraversable[_])(implicit textColor: String = RESET) = {
    writeLine(list mkString "\n")(textColor)
  }

  private def initControlls = {
    while (true) {
      showMenu
    }
  }

  private def showGameDays = {
    writeLine("First of all, please choose a gameday")
    writeLine(controller.getGameDays mkString ("Spieltag \n"))
    waitForInput
  }

  private def showMenu = {
    writeList(menu)
    waitForInput
  }

  private def menu = {
    List("Welcome to the Main Menu. Please choose one of the following options:",
      "[A]: Add bets",
      "[C]: Choose a gameday (Current:" + (if (controller.chosenGameDay == null) "---" else controller.chosenGameDay.no) + ")",
      "[S]: Show betting ticket",
      "[E]: Exit")
  }

  private def waitForInput = readLine.toLowerCase() match {
    case "a" => showBettingMenu
    case "c" => showGamedayMenu
    case "s" => showBettingTicketMenu
    case "e" | "exit" => System.exit(0)
    case unknownInput => writeLine("Your choise is not supported. Please check your input.")(RED)
  }

  private def waitForInputWinning: Boolean = readLine.toLowerCase() match {
    case "e" | "exit" => false
    case x => checkForValidWinning(x)
  }

  private def waitForInputGameDay: Boolean = readLine.toLowerCase() match {
    case "e" | "exit" => false
    case x => checkForValidGameDay(x)
  }

  private def waitForInputGame: Boolean = readLine.toLowerCase() match {
    case "e" | "exit" => false
    case x => checkForValidGame(x)
  }

  private def waitForInputWager: Boolean = readLine.toLowerCase() match {
    case "e" | "exit" => false
    case x => checkForValidWager(x)
  }

  private def waitForInputSystem: Boolean = readLine.toLowerCase() match {
    case "e" | "exit" => false
    case x => checkForValidSystem(x)
  }

  private def waitForInputBettingTicket: Boolean = readLine.toLowerCase() match {
    case "w" => showWagerMenu
    case "s" => showSystemMenu
    case "p" => showPlaceWinningsMenu
    case "c" => showCalculationMenu
    case "e" | "exit" => false
    case x => checkForValidWager(x)
  }

  private def waitForInputPlaceBet: Boolean = checkForValidPlacing(readLine.toLowerCase())

  private def checkForValidWager(input: String) = {
    var status = true
    if (input.forall(_.isDigit)) {
      controller.setBettingAmount(input.toDouble)
      status = false
    } else {
      writeLine("Your input is not a valid wager.")(RED)
    }
    status
  }

  private def checkForValidWinning(input: String) = {
    var status = true
    var list = List[Int]()
    for (i <- input.split(",").toList) {
      if (i.forall(_.isDigit)) {
        list +:= i.toInt
        status = false
      } else {
        writeLine("Your input is not a valid wager.")(RED)
      }
    }
    if (!status) controller.placeWinningBets(list)
    status
  }

  private def checkForValidSystem(system: String) = {
    var status = true
    if (system.forall(_.isDigit)) {
      if (controller.calculateCombinationNumbers.contains(system.toInt)) {
        controller.chooseSystem(system.toInt)
        status = false
      }
    } else {
      writeLine("Your input is not a valid system.")(RED)
    }
    status
  }

  private def checkForValidPlacing(input: String) = {
    var status = true
    if (input.forall(_.isDigit)) {
      if (List(0, 1, 2).contains(input.toInt)) {
        controller.placeBet(input.toInt)
        status = false
      }
    } else {
      writeLine("Your input is not a valid number.")(RED)
    }
    status
  }

  private def checkForValidGame(id: String) = {
    var status = true
    val games = (controller.getGameDays
      .find { e => e.no == controller.chosenGameDay.no }
      .get).games

    val values = games.toList.flatMap { e =>
      try {
        List(e.no)
      } catch {
        case _ => Nil
      }
    }

    if (id.forall(_.isDigit)) {
      if (values.contains(id.toInt)) {
        controller.chooseGame(id.toInt)
        status = false
      } else {
        writeLine("Your chosen game is not valid. Please verify your input")(RED)
      }
    } else {
      writeLine("Your input is not a valid number.")(RED)
    }
    status
  }

  private def checkForValidGameDay(id: String) = {
    var status = true
    val values = controller.getGameDays.toList.flatMap { e =>
      try {
        List(e.no)
      } catch {
        case _ => Nil
      }
    }

    if (id.forall(_.isDigit)) {
      if (values.contains(id.toInt)) {
        controller.chooseGameDay(id.toInt)
        status = false
      } else {
        writeLine("Your chosen gameday is not valid. Please verify your input")(RED)
      }
    } else {
      writeLine("Your input is not a valid number.")(RED)
    }
    status
  }

  private def showGamedayMenu = {
    var active = true;
    while (active) {
      writeLine("===== Gameday Menu =====")(YELLOW)
      writeLine("Please choose one of the gamedays or \"E\" to exit")
      controller.getGameDays.foreach { gameday => writeLine("[" + gameday.no + "]: " + gameday.no + ". Spieltag") }
      writeLine("[E]: Exit")
      active = waitForInputGameDay
    }
  }

  private def showBettingMenu = {
    var active = true;
    while (active) {
      if (controller.chosenGameDay == null) {
        writeLine("You have to choose a gameday before you can bet")(RED)
        active = false
      } else {
        writeLine("===== Betting Menu =====")(YELLOW)
        writeLine("To add a new bet to your ticket, choose one game or \"E\" to exit")
        (controller.getGameDays
          .find { e => e.no == controller.chosenGameDay.no }
          .get).games.foreach {
            game => writeLine(game)
          }

        writeLine("[E]: Exit")
        active = waitForInputGame
        if (controller.chosenGame != null) {
          showPlaceBetMenu
        }
      }
    }
  }

  private def showPlaceBetMenu = {
    var active = true
    while (active) {
      writeLine("Please place your bet")
      writeLine("[1]: " + controller.chosenGame.homeOdds)
      writeLine("[0]: " + controller.chosenGame.tieOdds)
      writeLine("[2]: " + controller.chosenGame.awayOdds)
      active = waitForInputPlaceBet
    }
  }

  private def showWagerMenu = {
    var active = true
    while (active) {
      writeLine("Please set your wager")
      active = waitForInputWager
    }
    true
  }

  private def showSystemMenu = {
    var active = true
    while (active) {
      writeLine("Please choose a System")
      if (controller.getBets.size < 3) {
        writeLine("At least you need 3 bets for to use a system")(RED)
        active = false
      } else {
        controller.calculateCombinationNumbers.foreach {
          combination => writeLine("[" + combination + "]: " + combination + " of " + controller.getBets.size)
        }
        active = waitForInputSystem
      }
    }
    true
  }

  private def showBettingTicketMenu = {
    var active = true;
    while (active) {
      writeLine("===== Your Betting Ticket =====")(YELLOW)
      writeLine("Please choose one of the gamedays or \"E\" to exit")
      controller.getBets.foreach { bet =>
        if (bet._2.winning) {
          writeLine("Game no:" + bet._1 + " " + bet._2)(GREEN)
        } else {
          writeLine("Game no:" + bet._1 + " " + bet._2)(RED)
        }
      }
      writeLine("[W]: Set Wager (Current: " + controller.getWager + ")")(YELLOW)
      writeLine("[S]: Set System (Current: " + (if (controller.chosenSystem == 0) "---" else (controller.chosenSystem + " of " + controller.getBets.size)) + ")")(YELLOW)
      writeLine("[P]: Place winning bets (Current: " + getAllWinningBets + ")")(YELLOW)
      writeLine("[C]: Calculate Result")(YELLOW)
      writeLine("[E]: Exit")
      active = waitForInputBettingTicket
    }
  }

  private def getAllWinningBets = {
    val winningBets = controller.getBets.filter(bet => bet._2.winning == true).keys.toList
    if (winningBets.size != 0) winningBets.toString else "---"
  }

  private def showPlaceWinningsMenu = {
    var active = true;
    while (active) {
      writeLine("Please input your winning bets, with their id comma-seperated e.g. 1,2,5")
      active = waitForInputWinning
    }
    true
  }

  private def showCalculationMenu = {
    var active = true;
    while (active) {
      if (controller.getWager == 0.0 || controller.chosenSystem == 0) {
        writeLine("To calculate a result, you have to place a wager and choose a system")(RED)
        active = false
      } else {
        controller.calculateResult.foreach { row =>
          row.combination.foreach {
            quote =>
              print(" | ")
              (if (quote.winning == true) print((GREEN) + quote.quote) else print((RED) + quote.quote))
              print(" | ")
          }
          writeLine( row.winning )
        }
        active = false
      }
    }
    true
  }

}