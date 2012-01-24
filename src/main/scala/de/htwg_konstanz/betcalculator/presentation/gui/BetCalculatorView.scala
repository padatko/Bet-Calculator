package de.htwg_konstanz.betcalculator.presentation.gui

import scala.swing._
import javax.swing.JFrame
import swing.ListView._
import BorderPanel.Position._
import event._
import de.htwg_konstanz.betcalculator.presentation._
import de.htwg_konstanz.betcalculator._

class BetCalculatorView(var controller: BetCalculatorGuiController) extends Frame with NativeLookAndFeel {

  listenTo(gamedaysComboBox.selection, controller, matchesListView.selection, addBetButton, calculateResultButton, clearTicketButton)

  case class PlaceBet(text: String, choice: Int)
  case class SystemEntry(text: String, system: Int)
  case class BettingEntry(gameId: Int, teamName: String, quote: Double, selectBox: CheckBox)
  case class WinningEntry(combination: Set[Bet], quote: Double)

  var bettingEntries = List[BettingEntry]()
  var winningEntries = List[WinningEntry]()
  var placeBet = List(PlaceBet("---", 0))
  var systemEntry = List(SystemEntry("---", 0))
  var placeBetComboBox = new ComboBox(placeBet) {
    selection.index_=(0)
    renderer = Renderer(_.text)
    enabled_=(false)
  }
  var setSystemComboBox = new ComboBox(systemEntry) {
    selection.index_=(0)
    renderer = Renderer(_.text)
    enabled_=(false)
    minimumSize_=(new Dimension(30, 300))
  }

  contents = new BoxPanel(Orientation.Vertical) {
    border = Swing.EmptyBorder(5, 5, 5, 5)
    contents += inputArea
    contents += outputArea
  }

  def inputArea = new GridPanel(1, 2) {
    border = Swing.EmptyBorder(5, 5, 5, 5)
    contents += matchesArea
    contents += bettingArea
  }

  def matchesArea = new BorderPanel {
    border = Swing.EmptyBorder(5, 5, 5, 5)
    add(gameDayArea, North)
    add(matchesListView, Center)
    add(gameArea, South)
  }

  lazy val test = List[Game]()
  lazy val matchesListView = new ListView(test) {
    selection.intervalMode_=(IntervalMode.SingleInterval)
    border_=(Swing.EtchedBorder)
  }

  def bettingArea = new BorderPanel {
    border = Swing.EmptyBorder(5, 5, 5, 5)
    add(new FlowPanel() {
      contents += new Label("Your betting ticket")
    }, North)
    add(bettingTicketArea, Center)
    add(bettingControls, South)
  }

  lazy val wagerTextField = new TextField {
    text = (0.0).toString
    maximumSize_=(new Dimension(30, 350))
    enabled_=(false)
  }

  def bettingControls = new BorderPanel {
    border = Swing.EtchedBorder
    add(new BoxPanel(Orientation.Horizontal) {
      border = Swing.EmptyBorder(5, 5, 5, 5)
      contents += setSystemComboBox
      contents += calculateResultButton
      contents += clearTicketButton
    }, Center)
    layout(new BoxPanel(Orientation.Horizontal) {
      border = Swing.EmptyBorder(5, 5, 5, 5)
      contents += new Label("Wager:")
      contents += wagerTextField
    }) = North

  }

  lazy val bettingTicketArea = new BoxPanel(Orientation.Vertical) {
    border = Swing.EtchedBorder
  }

  lazy val gamedays = controller.getGameDays
  lazy val gamedaysComboBox = new ComboBox(gamedays) {
    selection.index_=(0)
    renderer = Renderer(_.no + ". Spieltag")
  }

  def gameDayArea = new FlowPanel {
    contents += new Label("Select Gameday:")
    contents += gamedaysComboBox
  }

  def gameArea = new BorderPanel {
    border = Swing.EtchedBorder
    add(gameInfoArea, Center)
    layout(new BoxPanel(Orientation.Horizontal) {
      border = Swing.EmptyBorder(5, 5, 5, 5)
      contents += new Label("Choosen game:")
      contents += gameLabel
    }) = North
  }

  lazy val gameLabel = new Label("---")
  lazy val addBetButton = new Button("Add Bet") { enabled_=(false) }
  lazy val calculateResultButton = new Button("Calculate Result") { enabled_=(false) }
  lazy val clearTicketButton = new Button("Clear Ticket")

  def gameInfoArea = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(5, 5, 5, 5)
    contents += placeBetComboBox
    contents += addBetButton
  }

  def outputArea = new GridPanel(1, 1) {
    contents += resultArea
  }

  def resultArea = new BorderPanel {
    border = Swing.CompoundBorder(Swing.EmptyBorder(5, 5, 5, 5), Swing.EtchedBorder)
    add(resultTable, Center)
  }

  lazy val resultTable = new BoxPanel(Orientation.Vertical) {
  }

  reactions += {
    case WindowClosing(e) => System.exit(0)
    case SelectionChanged(`gamedaysComboBox`) => controller.chooseGameDay(gamedaysComboBox.selection.item.no)
    case SelectionChanged(`matchesListView`) => {
      if (!matchesListView.selection.items.isEmpty) controller.chooseGame(matchesListView.selection.items.head.no)
    }
    case e: GameDayChanged => updateGamesListView
    case e: GameChanged => updateGameInfo(e.game)
    case e: BetAdded => updateBettingTicket(e.bets)
    case ButtonClicked(`addBetButton`) => controller.placeBet(placeBetComboBox.selection.item.choice)
    case ButtonClicked(`calculateResultButton`) => calculateResult
    case ButtonClicked(`clearTicketButton`) => controller.clearList
    case e: WinningsCalculated => publishWinnings(e.result)
    case e: TicketChanged => refreshTicket
  }

  def refreshTicket = {
    bettingTicketArea.contents.clear
    bettingTicketArea.repaint
    bettingTicketArea.revalidate
    resultTable.contents.clear
    resultTable.repaint
    resultTable.revalidate
    addBetButton.enabled_=(false)
    setSystemComboBox.enabled_=(false)
  }

  def calculateResult = {
    val selectedEntries = bettingEntries.filter { e => e.selectBox.selected == true }
    val selectedIds = for (selectedEntry <- selectedEntries)
      yield selectedEntry.gameId
    var inputWager = 0.0
    try {
      inputWager = wagerTextField.text.toDouble
      if (inputWager == 0) {
        Dialog.showMessage(null, "Please use a wager greater than 0.")
      } else {
        controller.placeWinningBets(selectedIds)
        controller.setBettingAmount(inputWager)
        controller.chooseSystem(setSystemComboBox.selection.item.system)
        controller.calculateResult
      }
    } catch {
      case e => Dialog.showMessage(null, "Your input is not a valid number. Please correct your input")
    }
  }

  def generateEntries(bets: Map[Int, Bet]): List[BettingEntry] = {
    (for (bet <- bets)
      yield new BettingEntry(bet._1, bet._2.teamName, bet._2.quote, new CheckBox())).toList
  }

  def updateGameInfo(game: Game) = {
    gameLabel.text_=(controller.chosenGame.toString)
    addBetButton.enabled_=(true)
    placeBetComboBox.enabled_=(true)
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
      calculateResultButton.enabled_=(true)
      setSystemComboBox.enabled_=(true)
      wagerTextField.enabled_=(true)
      var combinations = for (combinationNumber <- controller.calculateCombinationNumbers)
        yield new SystemEntry("System: " + combinationNumber + " of " + bets.size, combinationNumber)
      setSystemComboBox.peer.setModel(ComboBox.newConstantModel(combinations))
    }
  }

  def publishWinnings(winnings: Set[RowWinnings]) = {
    resultTable.contents.clear
    resultTable.repaint
    resultTable.contents += new GridPanel(winnings.size + 1, winnings.head.combination.size + 2) {
      (1 to winnings.head.combination.size).toList.foreach(entry => {
        contents += new Label(entry + ". Quote") { border = Swing.EtchedBorder }
      })
      contents += new Label("Overall Quote") { border = Swing.EtchedBorder }
      contents += new Label("Winning") { border = Swing.EtchedBorder }
      winnings.foreach(winning => {
        winning.combination.foreach(quote => {
          if (quote.winning) {
            contents += new Label(quote.quote.toString) {
              border = Swing.EtchedBorder
              foreground_=(java.awt.Color.WHITE)
              opaque_=(true)
              background_=(java.awt.Color.GREEN)
              font_=(new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.BOLD, 12))
            }
          } else {
            contents += new Label(quote.quote.toString) {
              border = Swing.EtchedBorder
              foreground_=(java.awt.Color.WHITE)
              opaque_=(true)
              background_=(java.awt.Color.RED)
              font_=(new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.BOLD, 12))
            }
          }
        })
        contents += new Label(winning.overalQuote.toString)
        if (winning.winning > 0) {
          contents += new Label(winning.winning.toString + " Euro") {
            border = Swing.EtchedBorder
            foreground_=(java.awt.Color.WHITE)
            opaque_=(true)
            background_=(java.awt.Color.GREEN)
            font_=(new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.BOLD, 12))
          }
        } else {
          border = Swing.EtchedBorder
          contents += new Label(winning.winning.toString + " Euro") {
            font_=(new java.awt.Font("Lucida Sans Typewriter", java.awt.Font.BOLD, 12))
          }
        }

      })
    }
    resultTable.revalidate
  }

  def generateWinnings(winnings: Set[RowWinnings]): List[WinningEntry] = {
    (for (winning <- winnings)
      yield new WinningEntry(winning.combination, winning.overalQuote)).toList
  }

  def updateGamesListView = {
    val games = gamedays.find(gameday => gameday.no == controller.chosenGameDay.no).get.games
    matchesListView.listData_=(games)
  }
  controller.chooseGameDay(19)
  minimumSize = new Dimension(1024, 786)
  peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  centerOnScreen()
  pack()
  title = "BetCalculator"
  visible = true
}

trait NativeLookAndFeel {
  import javax.swing._
  try UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  catch { case _ => UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName) }
}