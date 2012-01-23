package de.htwg_konstanz.betcalculator.presentation.gui

import de.htwg_konstanz.betcalculator.presentation._

object GUI extends App {
  val model = new BettingSession
  val controller = new BetCalculatorGuiController(model)
  val view = new BetCalculatorView(controller)
}