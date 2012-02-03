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

class BCDashboard {
  val selected = scala.collection.mutable.Map[Int, Boolean]()
  var game = 1;
  var wager = "0";
  var system = 2;

  private def placeBet(id: Int): JsCmd = game = id
  private def chooseSystem(choice: Int): JsCmd = system = choice

  private def updateBets(id: Int): JsCmd = {
    game = 1
    BCDashboard.controller.chooseGame(id)
    ReplaceOptions("select_bets", buildBetsOptions, Empty)
  }

  private def buildBetsOptions = {
    val game = BCDashboard.controller.chosenGame
    def createBetRow(id: String, name: String, quote: String) =
      (id, name + " (" + quote + ")")

    List(
      createBetRow("1", game.homeTeamName, game.homeOdds.toString),
      createBetRow("0", "X", game.tieOdds.toString),
      createBetRow("2", game.awayTeamName, game.awayOdds.toString))
  }

  def show(xhtml: Group): NodeSeq = {
    def gameSelect: Elem =
      untrustedSelect(BCDashboard.controller.chosenGameDay.games.map(s => (s.no.toString, s.toString)), Empty, null, "onchange" -> invokeClosure(updateBets))

    def betSelect: Elem =
      untrustedSelect(buildBetsOptions.map(s => s), Empty, null, "onchange" -> invokeClosure(placeBet))

    def systemSelect: Elem =
      untrustedSelect(List(("-" * 3, "-" * 3)).map(s => s), Empty, null, "onchange" -> invokeClosure(chooseSystem), "disabled" -> "disabled")

    def invokeClosure(closure: Int => JsCmd) = {
      val (_, js) = SHtml.ajaxCall(JE.JsRaw("this.value"), s => After(200, closure(s.toInt)))
      js.toJsCmd
    }

    def gamedaysSelect: Elem = {
      SHtml.ajaxSelect(BCDashboard.gamedays.map(i => (i.no.toString, i.no.toString + ". Spieltag")),
        Empty, {
          selectedGameday =>
            BCDashboard.controller.chooseGameDay(selectedGameday.toInt)
            val games = BCDashboard.controller.chosenGameDay.games
            ReplaceOptions("select_games", games.map(s => (s.no.toString, s.toString)), Empty) &
              updateBets(games.head.no)
        })
    }

    bind("bc", xhtml,
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
    BCDashboard.controller.clearList
    selected.clear()
    SetHtml("bets", <div class="bet_entry">{ "You did not add a bet!" }</div>) &
      SetHtml("total_winning", Text("")) &
      SetHtml("result_area", <img src="/images/bc_result.png"/>) &
      ReplaceOptions("select_system", List(("-" * 3, "-" * 3)).map(e => e), Empty)
  }

  private def updateTicket = {
    BCDashboard.controller.placeBet(game)
    val combinations = BCDashboard.controller.calculateCombinationNumbers
    val bets = BCDashboard.controller.getBets
    SetHtml("bets", { bets.toList.flatMap(buildTicketEntry _) }) &
      (if (bets.size >= 3) ReplaceOptions("select_system", combinations.map(e => (e.toString, e + " of " + bets.size)), Empty))
  }

  private def buildTicketEntry(element: (Int, Bet)): NodeSeq = {
    val (id, bet) = element
    selected += id -> false
    <div class="bet_entry">{
      "[" + id + "]"
    }{
      bet.teamName
    }<span class="quote">{
      "(" + bet.quote + ")"
    }</span>{
      ajaxCheckbox(false, { v => selected(id) = v }, "class" -> "bet", "id" -> id.toString)
    }</div>
  }

  private def calculateTicket = {
    BCDashboard.controller.chooseSystem(system)
    BCDashboard.controller.setBettingAmount(wager.toDouble)
    val entries = selected.toList.collect {
      case (id, true) => id
    }
    BCDashboard.controller.placeWinningBets(entries)
    val result = BCDashboard.controller.calculateResult
    SetHtml("result_area", builtResultTable(result)) &
      SetHtml("total_winning", calculateTotalWinning(result))
  }

  private def calculateTotalWinning(result: Set[RowWinnings]) =
    <span>
      { "Total winning: " }
      <span class="result_winning"> {
        Utils.limitDecimals(result.map { _.winning }.sum, 2)
      }</span>{ " Euro" }
    </span>

  private def builtResultTable(result: Set[RowWinnings]) = {
    <table id="result_table">
      <thead>
        <tr>
          {
            for (no <- 1 to result.head.combination.size)
              yield <th> { "Quote " + no }</th>
          }<th>{ "Overall quote" }</th><th>{ "Winning" }</th>
        </tr>
      </thead>
      <tbody>
        {
          for (row <- result)
            yield <tr>
                    {
                      for {
                        combination <- row.combination
                        color = if (combination.winning) <td class="right">  { combination.quote }</td> else <td class="wrong">  { combination.quote }</td>
                      } yield color
                    }
                    <td>{ row.overalQuote }</td>{ if (row.winning > 0) <td class="right"> { row.winning }</td> else <td> { row.winning } </td> }
                  </tr>
        }
      </tbody>
    </table>
  }

}

object BCDashboard {
  private val model = new BettingSession
  private val controller = new BetCalculatorController(model)
  val gamedays = controller.getGameDays
  val initGameday = gamedays.head
  val initGame = initGameday.games.head
  controller.chooseGameDay(initGameday.no)
  controller.chooseGame(initGame.no)
}