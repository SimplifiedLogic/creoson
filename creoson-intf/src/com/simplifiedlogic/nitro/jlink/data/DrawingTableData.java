/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: The above copyright
 * notice and this permission notice shall be included in all copies or
 * substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS",
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;
import java.util.List;

/**
 * Data for a 2D table in a Creo drawing.
 *
 * <p>The table index is the 0-based position in the table list returned by Creo.
 * Row and column values in cells are Creo/J-Link native 1-based indices.
 */
public class DrawingTableData implements Serializable {

	private static final long serialVersionUID = 1L;

	private int tableIndex;
	private int rows;
	private int columns;
	private JLPoint position;
	private List<DrawingTableCellData> cells;
	private List<Double> rowHeights;
	private List<Double> columnWidths;

	/**
	 * @return 0-based table index
	 */
	public int getTableIndex() {
		return tableIndex;
	}
	/**
	 * @param tableIndex 0-based table index
	 */
	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}
	/**
	 * @return Number of rows
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * @param rows Number of rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	/**
	 * @return Number of columns
	 */
	public int getColumns() {
		return columns;
	}
	/**
	 * @param columns Number of columns
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}
	/**
	 * @return Table position/origin, if available
	 */
	public JLPoint getPosition() {
		return position;
	}
	/**
	 * @param position Table position/origin
	 */
	public void setPosition(JLPoint position) {
		this.position = position;
	}
	/**
	 * @return Cell data
	 */
	public List<DrawingTableCellData> getCells() {
		return cells;
	}
	/**
	 * @param cells Cell data
	 */
	public void setCells(List<DrawingTableCellData> cells) {
		this.cells = cells;
	}
	/**
	 * @return Row heights, if available
	 */
	public List<Double> getRowHeights() {
		return rowHeights;
	}
	/**
	 * @param rowHeights Row heights
	 */
	public void setRowHeights(List<Double> rowHeights) {
		this.rowHeights = rowHeights;
	}
	/**
	 * @return Column widths, if available
	 */
	public List<Double> getColumnWidths() {
		return columnWidths;
	}
	/**
	 * @param columnWidths Column widths
	 */
	public void setColumnWidths(List<Double> columnWidths) {
		this.columnWidths = columnWidths;
	}
}
