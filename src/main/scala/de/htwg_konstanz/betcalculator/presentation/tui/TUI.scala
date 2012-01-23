package de.htwg_konstanz.betcalculator.presentation.tui

import de.htwg_konstanz.betcalculator.presentation._

object TUI extends App {
  val model = new BettingSession
  val controller = new BetCalculatorController(model)
  val view = new BetCalculatorView(controller)
  view.display()
}