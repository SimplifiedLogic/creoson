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

/**
 * Data for one drawing table cell.
 *
 * <p>Row and column values use Creo/J-Link native 1-based indexing.
 */
public class DrawingTableCellData implements Serializable {

	private static final long serialVersionUID = 1L;

	private int row;
	private int column;
	private String text;

	/**
	 * @return 1-based row number
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param row 1-based row number
	 */
	public void setRow(int row) {
		this.row = row;
	}
	/**
	 * @return 1-based column number
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * @param column 1-based column number
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	/**
	 * @return Cell text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text Cell text
	 */
	public void setText(String text) {
		this.text = text;
	}
}
