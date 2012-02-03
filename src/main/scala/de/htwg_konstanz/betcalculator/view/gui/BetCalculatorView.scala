package de.htwg_konstanz.betcalculator.view.gui

import scala.swing._
import javax.swing.JFrame
import swing.ListView._
import BorderPanel.Position._
import event._
import de.htwg_konstanz.betcalculator.controller._
import de.htwg_konstanz.betcalculator._
import scala.swing.Swing._

class BetCalculatorView(var controller: BaseController) extends Frame with NativeLookAndFeel {

  listenTo(gamedaysComboBox.selection, controller, matchesListView.selection, addBetButton, calculateResultButton, clearTicketButton)

  case class PlaceBet(text: String, choice: Int)
  case class SystemEntry(text: String, system: Int)
  case class BettingEntry(gameId: Int, teamName: String, quote: Double, selectBox: CheckBox)

  var bettingEntries = List[BettingEntry]()
  var placeBet = List(PlaceBet("---", 0))
  var systemEntry = List(SystemEntry("---", 0))
  var placeBetComboBox = new ComboBox(placeBet) {
    selection.index = 0
    renderer = Renderer(_.text)
    enabled = false
  }
  var setSystemComboBox = new ComboBox(systemEntry) {
    selection.index = 0
    renderer = Renderer(_.text)
    enabled = false
    minimumSize = new Dimension(30, 300)
  }

  contents = new BoxPanel(Orientation.Vertical) {
    border = EmptyBorder(5, 5, 5, 5)
    contents += inputArea
    contents += outputArea
  }

  def inputArea = new GridPanel(1, 2) {
    border = EmptyBorder(5, 5, 5, 5)
    contents += matchesArea
    contents += bettingArea
  }

  def matchesArea = new BorderPanel {
    border = EmptyBorder(5, 5, 5, 5)
    add(gameDayArea, North)
    add(matchesListView, Center)
    add(gameArea, South)
  }

  lazy val games = List[Game]()
  lazy val matchesListView = new ListView(games) {
    selection.intervalMode = IntervalMode.SingleInterval
    border = EtchedBorder
  }

  def bettingArea = new BorderPanel {
    border = EmptyBorder(5, 5, 5, 5)
    add(new FlowPanel() {
      contents += new Label("Your betting ticket")
    }, North)
    add(bettingTicketArea, Center)
    add(bettingControls, South)
  }

  lazy val wagerTextField = new TextField {
    text = (0.0).toString
    maximumSize = new Dimension(30, 350)
    enabled = false
  }

  def bettingControls = new BorderPanel {
    border = EtchedBorder
    add(new BoxPanel(Orientation.Horizontal) {
      border = EmptyBorder(5, 5, 5, 5)
      contents += setSystemComboBox
      contents += calculateResultButton
      contents += clearTicketButton
    }, Center)
    layout(new BoxPanel(Orientation.Horizontal) {
      border = EmptyBorder(5, 5, 5, 5)
      contents += new Label("Wager:")
      contents += wagerTextField
    }) = North

  }

  lazy val bettingTicketArea = new BoxPanel(Orientation.Vertical) {
    border = EtchedBorder
  }

  lazy val gamedays = controller.getGameDays
  lazy val gamedaysComboBox = new ComboBox(gamedays) {
    selection.index = 0
    renderer = Renderer(_.no + ". Spieltag")
  }

  def gameDayArea = new FlowPanel {
    contents += new Label("Select Gameday:")
    contents += gamedaysComboBox
  }

  def gameArea = new BorderPanel {
    border = EtchedBorder
    add(gameInfoArea, Center)
    layout(new BoxPanel(Orientation.Horizontal) {
      border = EmptyBorder(5, 5, 5, 5)
      contents += new Label("Choosen game:")
      contents += gameLabel
    }) = North
  }

  lazy val gameLabel = new Label("---")
  lazy val addBetButton = new Button("Add Bet") { enabled_=(false) }
  lazy val calculateResultButton = new Button("Calculate Result") { enabled_=(false) }
  lazy val clearTicketButton = new Button("Clear Ticket")

  def gameInfoArea = new BoxPanel(Orientation.Horizontal) {
    border = EmptyBorder(5, 5, 5, 5)
    contents += placeBetComboBox
    contents += addBetButton
  }

  def outputArea = new GridPanel(1, 1) {
    contents += resultArea
  }

  def resultArea = new BorderPanel {
    border = CompoundBorder(EmptyBorder(5, 5, 5, 5), EtchedBorder)
    add(new ScrollPane(resultTable), Center)
  }

  lazy val resultTable = new BoxPanel(Orientation.Vertical) {
  }

  reactions += {
    case WindowClosing(e) => System.exit(0)
    case SelectionChanged(`gamedaysComboBox`) => controller.chooseGameDay(gamedaysComboBox.selection.item.no)
    case SelectionChanged(`matchesListView`) => {
      if (!matchesListView.selection.items.isEmpty)
        controller.chooseGame(matchesListView.selection.items.head.no)
    }
    case e: GameDayChanged => updateGamesListView
    case e: GameChanged => updateGameInfo(e.game)
    case e: BetAdded => updateBettingTicket(e.bets)
    case ButtonClicked(`addBetButton`) => controller.placeBet(placeBetComboBox.selection.item.choice)
    case ButtonClicked(`calculateResultButton`) => calculateResult
    case ButtonClicked(`clearTicketButton`) => controller.clearList
    case e: TicketChanged => refreshTicket
  }

  def refreshTicket = {
    bettingTicketArea.contents.clear
    bettingTicketArea.repaint
    bettingTicketArea.revalidate
    resultTable.contents.clear
    resultTable.repaint
    resultTable.revalidate
    addBetButton.enabled = false
    setSystemComboBox.enabled = false
  }

  def calculateResult = {
    try tryCalculateResult(bettingEntries.collect {
      case BettingEntry(id, _, _, box) if box.selected == true => id
    })
    catch {
      case e => Dialog.showMessage(null, "Your input is not a valid number. Please correct your input")
    }
  }

  def tryCalculateResult(selectedIds: List[Int]) {
    val inputWager = wagerTextField.text.toDouble
    if (inputWager == 0)
      Dialog.showMessage(null, "Please use a wager greater than 0.")
    else {
      controller.placeWinningBets(selectedIds)
      controller.setBettingAmount(inputWager)
      controller.chooseSystem(setSystemComboBox.selection.item.system)
      publishResults(controller.calculateResult)
    }
  }

  def generateEntries(bets: Map[Int, Bet]): List[BettingEntry] = bets.toList.map {
    case (id, bet @ Bet(_, quote, _)) => BettingEntry(id, bet.teamName, quote, new CheckBox)
  }

  def updateGameInfo(game: Game) = {
    gameLabel.text = controller.chosenGame.toString
    addBetButton.enabled = true
    placeBetComboBox.enabled = true
    lazy val placeBets = List(
      PlaceBet(game.homeTeamName + ": (" + game.homeOdds + ")", 1),
      PlaceBet("X: (" + game.tieOdds + ")", 0),
      PlaceBet(game.awayTeamName + ": (" + game.awayOdds + ")", 2))
    placeBetComboBox.peer.setModel(ComboBox.newConstantModel(placeBets))
  }

  def updateBettingTicket(bets: Map[Int, Bet]) = {
    bettingTicketArea.contents.clear
    bettingTicketArea.repaint
    bettingEntries = generateEntries(bets)
    bettingEntries.foreach(bet => bettingTicketArea.contents += new BoxPanel(Orientation.Horizontal) {
      contents += bet.selectBox
      contents += new Label(bet.gameId + ": " + bet.teamName + " (" + bet.quote.toString + ")") { minimumSize_=(new Dimension(80, 10)) }
    })

    bettingTicketArea.revalidate
    if (bets.size >= 3) {
      calculateResultButton.enabled = true
      setSystemComboBox.enabled = true
      wagerTextField.enabled = true
      var combinations = for (combinationNumber <- controller.calculateCombinationNumbers)
        yield SystemEntry("System: " + combinationNumber + " of " + bets.size, combinationNumber)
      setSystemComboBox.peer.setModel(ComboBox.newConstantModel(combinations))
    }
  }

  def publishResults(results: Set[RowWinnings]) = {
    import java.awt.Color._
    import java.awt.Font
    resultTable.contents.clear
    resultTable.repaint
    resultTable.contents += new GridPanel(results.size + 1, results.head.combination.size + 2) {
      (1 to results.head.combination.size).toList.foreach(entry => {
        contents += new Label(entry + ". Quote") { border = EtchedBorder }
      })
      contents += new Label("Overall Quote") { border = EtchedBorder }
      contents += new Label("Winning") { border = EtchedBorder }
      results.foreach(winning => {
        winning.combination.foreach(quote => {
          if (quote.winning) {
            contents += new Label(quote.quote.toString) {
              border = EtchedBorder
              foreground = WHITE
              opaque = true
              background = GREEN
              font = new Font("Lucida Sans Typewriter", Font.BOLD, 12)
            }
          } else {
            contents += new Label(quote.quote.toString) {
              border = EtchedBorder
              foreground = WHITE
              opaque = true
              background = RED
              font = new Font("Lucida Sans Typewriter", Font.BOLD, 12)
            }
          }
        })
        contents += new Label(winning.overalQuote.toString)
        if (winning.winning > 0) {
          contents += new Label(winning.winning + " Euro") {
            border = EtchedBorder
            foreground = WHITE
            opaque = true
            background = GREEN
            font = new Font("Lucida Sans Typewriter", Font.BOLD, 12)
          }
        } else {
          border = EtchedBorder
          contents += new Label(winning.winning + " Euro") {
            font = new Font("Lucida Sans Typewriter", Font.BOLD, 12)
          }
        }

      })
      resultTable.contents += new FlowPanel {
        contents += new Label("Your overall winning: " + Utils.limitDecimals(results.map { _.winning }.sum, 2) + " Euro") {
          font = new Font("Lucida Sans Typewriter", Font.BOLD, 25)  
        }
      }
    }
    resultTable.revalidate
  }

  def updateGamesListView = {
    val games = gamedays.find(gameday => gameday.no == controller.chosenGameDay.no).get.games
    matchesListView.listData = games
  }
  
  controller.chooseGameDay(controller.getGameDays.head.no)
  minimumSize = new Dimension(1024, 786)
  peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  centerOnScreen()
  pack()
  title = "BetCalculator"
  visible = true
}

trait NativeLookAndFeel {
  import javax.swing.UIManager._
  try setLookAndFeel(getSystemLookAndFeelClassName)
  catch { case _ => setLookAndFeel(getCrossPlatformLookAndFeelClassName) }
}