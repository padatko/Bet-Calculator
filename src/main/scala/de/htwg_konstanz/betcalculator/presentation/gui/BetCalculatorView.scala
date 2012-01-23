package de.htwg_konstanz.betcalculator.presentation.gui

import scala.swing._
import javax.swing.JFrame
import swing.ListView._
import BorderPanel.Position._
import event._
import de.htwg_konstanz.betcalculator.presentation._
import de.htwg_konstanz.betcalculator.Game
import javax.swing.JComboBox

class BetCalculatorView(var controller: BetCalculatorGuiController) extends Frame with NativeLookAndFeel {

  listenTo(gamedaysComboBox.selection, controller, table.selection, matchesListView.selection)

  contents = new BoxPanel(Orientation.Vertical) {
    border = Swing.EmptyBorder(5, 5, 5, 5)
    contents += inputArea
    contents += outputArea
  }

  lazy val tableModel = new GamesTableModel(Array[Array[Any]](), List("A", "B"))
  lazy val table = new Table(1, 5) { model = tableModel }
  for (i <- 0 to 10) { tableModel.addRow(Array[AnyRef]("i", "test")) }

  def inputArea = new GridPanel(1, 2) {
    border = Swing.EmptyBorder(5, 5, 5, 5)
    contents += matchesArea
    contents += new ScrollPane(table)
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

  def gameInfoArea2 = new GridBagPanel {
    val gbc = new Constraints()
    gbc.gridx = 0
    gbc.gridy = 0
    add(new Button("Button 1"), gbc)
    gbc.gridx = 1
    add(new Button("Button 2"), gbc)
    gbc.gridy = 1
    add(new Button("Button 3"), gbc)
  }
  case class PlaceBet(text: String, choice: Int)
  lazy val placeBet = List(PlaceBet("---", 0))
  lazy val placeBetComboBox = new ComboBox(placeBet) {
    selection.index_=(0)
    renderer = Renderer(_.text)
    enabled_=(false)
  }

  def gameInfoArea = new BoxPanel(Orientation.Vertical) {

    contents += placeBetComboBox
  }

  def outputArea = new GridPanel(1, 1) {
    contents += new ScrollPane(table)
  }

  reactions += {
    case WindowClosing(e) => System.exit(0)
    case SelectionChanged(`gamedaysComboBox`) => controller.chooseGameDay(gamedaysComboBox.selection.item.no)
    case SelectionChanged(`matchesListView`) => {
      if (!matchesListView.selection.items.isEmpty) controller.chooseGame(matchesListView.selection.items.head.no)
    }
    case TableRowsSelected(`table`, r, test) => println(if (table.selection.rows.size == 1) { table.selection.cells })
    case e: GameDayChanged => updateGamesListView
    case e: GameChanged => updateGameInfo(e.game)

  }

  def updateGameInfo(game: Game) = {
    gameLabel.text_=(controller.chosenGame.toString)
    placeBetComboBox.enabled_=(true)
    lazy val placeBets = List(
      PlaceBet(game.homeTeamName + ": (" + game.homeOdds + ")", 1),
      PlaceBet("X: (" + game.homeOdds + ")", 0),
      PlaceBet(game.awayTeamName + "(" + game.awayOdds + ")", 2))
//      placeBetComboBox.peer.setModel

  }

  def updateGamesListView = {
    val games = gamedays.find(gameday => gameday.no == controller.chosenGameDay.no).get.games
    matchesListView.listData_=(games)
  }

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