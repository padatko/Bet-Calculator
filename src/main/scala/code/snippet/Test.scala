package code.snippet

import de.htwg_konstanz.betcalculator.model._
import de.htwg_konstanz.betcalculator._
import de.htwg_konstanz.betcalculator.controller._
import scala.xml.NodeSeq
import net.liftweb.http.SHtml
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util._
import net.liftweb.http.js.jquery.JqJE._
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.jquery.JqJsCmds._
import net.liftweb.util.Helpers._
import net.liftweb.util.BindPlus._
import scala.xml.Text

class Test {

  private val model = new BettingSession
  private val controller = new BetCalculatorController(model)
  val selectedItem = ValueCell(19)

  def gamedays(xhtml: NodeSeq): NodeSeq = {
    val gamedays = controller.getGameDays

    def doSelect(msg: NodeSeq) = {
      SHtml.ajaxSelect(gamedays.map(i => (i.no.toString, i.no.toString + ". Spieltag")),
        Empty, {
          selectedGameday =>
            selectedItem.set(selectedGameday.toInt)
            controller.chooseGameDay(selectedGameday.toInt)
        })

    }

    bind("ajax", xhtml,
      "select" -> doSelect _)

  }
  
  def cars = WiringUI.apply(selectedItem)(displayResult)
  
  def displayResult(car:Int) ={
    ".test *" #> ("das ist ein test " + car.toString)
  }
  
  def cars2(node: NodeSeq): NodeSeq = {
    controller.chooseGameDay(19)
    val games = controller.chosenGameDay.games
    games match {
      case null => Text("There is no items in db")
      case game => game.flatMap(i =>
        bind("car", node,
          "name" -> "test",
          "type" -> "bla"))
    }
  }

}