package code.snippet

import _root_.scala.xml._
import _root_.net.liftweb._
import http._
import S._
import SHtml._
import common._
import util._
import Helpers._
import js._
import JsCmds._
import de.htwg_konstanz.betcalculator.model._
import de.htwg_konstanz.betcalculator._
import de.htwg_konstanz.betcalculator.controller._
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JE.ValById

class AjaxForm {
  var selected = scala.collection.mutable.Map[Int, Boolean]()
  var game = 1;
  var wager = "0";
  var system = 2;

  private def placeBet(id: String): JsCmd = {
    game = id.toInt
  }

  private def chooseSystem(choice: Int): JsCmd = {
    system = choice
  }

  private def replace(id: Int): JsCmd = {
    game = 1
    AjaxForm.controller.chooseGame(id)
    ReplaceOptions("select_bets", buildBetsOptions, Empty)
  }

  private def buildBetsOptions = {
    val game = AjaxForm.controller.chosenGame
    List(
      ("1", game.homeTeamName + " (" + game.homeOdds.toString + ")"),
      ("0", "X (" + game.tieOdds.toString + ")"),
      ("2", game.awayTeamName + " (" + game.awayOdds.toString + ")"))
  }

  def show(xhtml: Group): NodeSeq = {
    def gameSelect = {
      val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
        s => After(200, replace(s.toInt)))
      untrustedSelect(AjaxForm.controller.chosenGameDay.games.map(s => (s.no.toString, s.toString)), Full(AjaxForm.controller.chosenGameDay.games.head.toString), null, "onchange" -> js.toJsCmd)
    }

    def betSelect = {
      val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
        s => After(200, placeBet(s)))
      untrustedSelect(buildBetsOptions.map(s => s), Full("test"), null, "onchange" -> js.toJsCmd)
    }

    def systemSelect = {
      val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
        s => After(200, chooseSystem(s.toInt)))
      untrustedSelect(List(("---", "---")).map(s => s), Full("test"), null, "onchange" -> js.toJsCmd, "disabled" -> "disabled")
    }

    def gamedaysSelect = {
      SHtml.ajaxSelect(AjaxForm.gamedays.map(i => (i.no.toString, i.no.toString + ". Spieltag")),
        Empty, {
          selectedGameday =>
            AjaxForm.controller.chooseGameDay(selectedGameday.toInt)
            val games = AjaxForm.controller.chosenGameDay.games
            ReplaceOptions("select_games", games.map(s => (s.no.toString, s.toString)), Full(AjaxForm.controller.chosenGameDay.games.head.toString)) &
              replace(games.head.no)
        })
    }

    bind("select", xhtml,
      "gamedays" -> gamedaysSelect % ("id" -> "select_gamedays"),
      "games" -> gameSelect % ("id" -> "select_games"),
      "bets" -> betSelect % ("id" -> "select_bets"),
      "addbet" -> ajaxButton(Text("Add bet"), updateTicket _, "id" -> "bt_addbet"),
      "calculateresult" -> ajaxButton(Text("Calculate"), calculateTicket _, "id" -> "bt_calculate", "disabled" -> "disabled"),
      "wager" -> ajaxText("0", value => wager = value, "id" -> "txt_wager", "disabled" -> "disabled"),
      "system" -> systemSelect % ("id" -> "select_system"),
      "clear" -> ajaxButton(Text("Clear Ticket"), clearTicket _, "id" -> "bt_clear"))
  }

  private def clearTicket = {
    AjaxForm.controller.clearList
    selected = scala.collection.mutable.Map[Int, Boolean]()
    SetHtml("bets", <div class="bet_entry">{ Text("No bets") }</div>) &
      ReplaceOptions("select_system", List(("---", "---")).map(e => e), Empty)
  }

  private def updateTicket = {
    AjaxForm.controller.placeBet(game)
    val combinations = AjaxForm.controller.calculateCombinationNumbers
    val bets = AjaxForm.controller.getBets
    SetHtml("bets", { bets.toList.flatMap(buildTicketEntry _) }) &
      (if (bets.size >= 3) ReplaceOptions("select_system", combinations.map(e => (e.toString, e + " of " + bets.size)), Empty))
  }

  private def buildTicketEntry(element: (Int, Bet)): NodeSeq = {
    val (id, bet) = element
    selected += id -> false
    <div class="bet_entry">{ Text("[" + id + "]") }{ Text(bet.teamName) }<span class="quote">{ Text("(" + bet.quote.toString + ")") }</span>{ ajaxCheckbox(false, { v => selected(id) = v }, "class" -> "bet", "id" -> id.toString) }</div>

  }

  private def calculateTicket = {
    AjaxForm.controller.chooseSystem(system)
    AjaxForm.controller.setBettingAmount(wager.toDouble)
    val entries = (selected.filter(e => e._2 == true).map(e => e._1)).toList
    AjaxForm.controller.placeWinningBets(entries)
    val result = AjaxForm.controller.calculateResult
    SetHtml("result_table", builtResultTable(result)) &
    SetHtml("total_winning", calculateTotalWinning(result))
  }
  
  private def calculateTotalWinning(result: Set[RowWinnings]) = <span>{"Total winning: "} <span class="result_winning"> { BigDecimal(result.toList.map( _.winning ).sum).setScale(2, BigDecimal.RoundingMode.FLOOR).toDouble }</span> {" Euro"}</span>

  private def builtResultTable(result: Set[RowWinnings]) = {
    <thead>
      <tr> {
        for (no <- 1 to result.head.combination.size)
          yield <th> { "Quote " + no }</th>
      } <th>{ "Overall quote" }</th><th>{ "Winning" }</th></tr>
    </thead>
    <tbody> {
      for(row <- result)
        yield <tr> { for(combination <- row.combination)
          yield if(combination.winning) <td class="right">  { combination.quote}</td> else <td class="wrong">  { combination.quote}</td>} 
<td>{row.overalQuote}</td> {if(row.winning > 0) <td class="right"> {row.winning}</td> else <td> {row.winning} </td>}</tr>
    }
    </tbody>
  }

}

object AjaxForm {
  private val model = new BettingSession
  private val controller = new BetCalculatorController(model)
  val gamedays = controller.getGameDays
  val initGameday = gamedays.head
  val initGame = initGameday.games.head
  controller.chooseGameDay(initGameday.no)
  controller.chooseGame(initGame.no)
}