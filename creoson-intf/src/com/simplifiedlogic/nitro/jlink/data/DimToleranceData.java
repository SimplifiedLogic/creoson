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
 * Data about a dimension tolerance
 * 
 * @author Adam Andrews
 *
 */
public class DimToleranceData implements Serializable {

	private static final long serialVersionUID = 1L;

	// tolerance types
	public static final String TYPE_LIMITS = "limits";
	public static final String TYPE_PLUS_MINUS = "plus_minus";
	public static final String TYPE_SYMMETRIC = "symmetric";
	public static final String TYPE_SYM_SUPERSCRIPT = "sym_superscript";
	public static final String TYPE_ISODIN = "isodin";
	
	// tolerance table types
	public static final String TABLE_GENERAL = "general";
	public static final String TABLE_BROKEN_EDGE = "broken_edge";
	public static final String TABLE_SHAFTS = "shafts";
	public static final String TABLE_HOLES = "holes";

	private String toleranceType;
	private double lowerLimit;
	private double upperLimit;
	private double plus;
	private double minus;
	private double symmetricValue;
	private String tableName;
	private int tableColumn;
	private String tableType;

	/**
	 * @return The tolerance type
	 */
	public String getToleranceType() {
		return toleranceType;
	}
	/**
	 * @param toleranceType The tolerance type
	 */
	public void setToleranceType(String toleranceType) {
		this.toleranceType = toleranceType;
	}
	/**
	 * @return The lower limit; used only when toleranceType=TYPE_LIMITS
	 */
	public double getLowerLimit() {
		return lowerLimit;
	}
	/**
	 * @param lowerLimit The lower limit; used only when toleranceType=TYPE_LIMITS
	 */
	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	/**
	 * @return The upper limit; used only when toleranceType=TYPE_LIMITS
	 */
	public double getUpperLimit() {
		return upperLimit;
	}
	/**
	 * @param upperLimit The upper limit; used only when toleranceType=TYPE_LIMITS
	 */
	public void setUpperLimit(double upperLimit) {
		this.upperLimit = upperLimit;
	}
	/**
	 * @return The plus tolerance; used only when toleranceType=TYPE_PLUSMINUS
	 */
	public double getPlus() {
		return plus;
	}
	/**
	 * @param plus The plus tolerance; used only when toleranceType=TYPE_PLUSMINUS
	 */
	public void setPlus(double plus) {
		this.plus = plus;
	}
	/**
	 * @return The minus tolerance; used only when toleranceType=TYPE_PLUSMINUS
	 */
	public double getMinus() {
		return minus;
	}
	/**
	 * @param minus The minus tolerance; used only when toleranceType=TYPE_PLUSMINUS
	 */
	public void setMinus(double minus) {
		this.minus = minus;
	}
	/**
	 * @return The symmetric value; used only when toleranceType=TYPE_SYMMETRIC or TYPE_SYM_SUPERSCRIPT
	 */
	public double getSymmetricValue() {
		return symmetricValue;
	}
	/**
	 * @param symmetricValue The symmetric value; used only when toleranceType=TYPE_SYMMETRIC or TYPE_SYM_SUPERSCRIPT
	 */
	public void setSymmetricValue(double symmetricValue) {
		this.symmetricValue = symmetricValue;
	}
	/**
	 * @return The tolerance table name; used only when toleranceType=TYPE_ISODIN
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName The tolerance table name; used only when toleranceType=TYPE_ISODIN
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return The tolerance table column; used only when toleranceType=TYPE_ISODIN
	 */
	public int getTableColumn() {
		return tableColumn;
	}
	/**
	 * @param tableColumn The tolerance table column; used only when toleranceType=TYPE_ISODIN
	 */
	public void setTableColumn(int tableColumn) {
		this.tableColumn = tableColumn;
	}
	/**
	 * @return The tolerance table type; used only when toleranceType=TYPE_ISODIN
	 */
	public String getTableType() {
		return tableType;
	}
	/**
	 * @param tableType The tolerance table type; used only when toleranceType=TYPE_ISODIN
	 */
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
}
