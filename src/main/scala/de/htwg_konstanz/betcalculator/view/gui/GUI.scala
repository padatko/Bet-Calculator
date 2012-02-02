package de.htwg_konstanz.betcalculator.view.gui

import de.htwg_konstanz.betcalculator.controller.BetCalculatorController
import de.htwg_konstanz.betcalculator.model.BettingSession

object GUI extends App {
  val model = new BettingSession
  val controller = new BetCalculatorController(model)
  val view = new BetCalculatorView(controller)
}