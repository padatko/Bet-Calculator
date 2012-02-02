package de.htwg_konstanz.betcalculator.view.tui

import de.htwg_konstanz.betcalculator.controller._
import de.htwg_konstanz.betcalculator.model._

object TUI extends App {
  val model = new BettingSession
  val controller = new BetCalculatorController(model)
  val view = new BetCalculatorView(controller)
  view.display()
}