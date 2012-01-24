package de.htwg_konstanz.betcalculator.presentation.gui

import scala.swing.Table._
import javax.swing.table._

class GamesTableModel(var rowData: Array[Array[Any]], val columnNames: Seq[String]) extends AbstractTableModel {
  override def getColumnName(column: Int) = columnNames(column)
  def getRowCount() = rowData.length
  def getColumnCount() = columnNames.length
  def getValueAt(row: Int, col: Int): AnyRef = rowData(row)(col).asInstanceOf[AnyRef]
  override def isCellEditable(row: Int, column: Int) = false
  override def setValueAt(value: Any, row: Int, col: Int) {
    rowData(row)(col) = value
  }
  def addRow(data: Array[AnyRef]) {
    rowData ++= Array(data.asInstanceOf[Array[Any]])
  }
}