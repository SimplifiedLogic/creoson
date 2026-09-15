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
package com.simplifiedlogic.nitro.jlink.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawing;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.DrawingTableCellData;
import com.simplifiedlogic.nitro.jlink.data.DrawingTableData;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;

/**
 * Generic support for Creo drawing tables.
 *
 * <p>This class uses reflection over documented Creo J-Link/Object Toolkit
 * table APIs so Creoson can still compile in environments where the table
 * package is not present. Missing native operations are reported as
 * not_supported instead of being emulated with drawing-specific business rules.
 */
class DrawingTableHelper {

	private static final Object NOT_FOUND = new Object();
	private static final Object[] NO_ARGS = new Object[0];
	private static final double DEFAULT_CREATE_ROW_HEIGHT = 1.0;
	private static final double DEFAULT_CREATE_COLUMN_WIDTH = 10.0;

	private DrawingTableHelper() {
	}

	public static List<DrawingTableData> tableList(String filename, AbstractJLISession sess) throws JLIException {
		try {
			CallDrawing drawing = getDrawing(filename, sess);
			Object[] tables = getTables(drawing);
			List<DrawingTableData> out = new Vector<DrawingTableData>();
			for (int i=0; i<tables.length; i++) {
				out.add(readTableData(tables[i], i, false));
			}
			return out;
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static DrawingTableData tableGetInfo(String filename, int tableIndex, AbstractJLISession sess) throws JLIException {
		try {
			Object table = getTable(filename, sess, tableIndex);
			return readTableData(table, tableIndex, true);
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static DrawingTableCellData tableGetCell(String filename, int tableIndex, int row, int column, AbstractJLISession sess) throws JLIException {
		try {
			Object table = getTable(filename, sess, tableIndex);
			validateCell(table, row, column);
			return makeCellData(row, column, getCellText(table, row, column));
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableSetCell(String filename, int tableIndex, int row, int column, String value, AbstractJLISession sess) throws JLIException {
		try {
			Object table = getTable(filename, sess, tableIndex);
			validateCell(table, row, column);
			setCellText(table, row, column, value);
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static List<List<String>> tableGetRange(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException {
		try {
			Object table = getTable(filename, sess, tableIndex);
			validateRange(table, startRow, startColumn, endRow, endColumn);
			List<List<String>> values = new Vector<List<String>>();
			for (int row=startRow; row<=endRow; row++) {
				List<String> rowValues = new Vector<String>();
				for (int col=startColumn; col<=endColumn; col++) {
					rowValues.add(getCellText(table, row, col));
				}
				values.add(rowValues);
			}
			return values;
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableSetRange(String filename, int tableIndex, int startRow, int startColumn, List<List<String>> values, AbstractJLISession sess) throws JLIException {
		try {
			if (values==null || values.size()==0)
				throw new JLIException("No values parameter given");
			int rowCount = values.size();
			int colCount = 0;
			for (int i=0; i<values.size(); i++) {
				List<String> rowValues = values.get(i);
				if (rowValues==null || rowValues.size()==0)
					throw new JLIException("No values given for row " + i);
				if (colCount==0)
					colCount = rowValues.size();
				else if (rowValues.size()!=colCount)
					throw new JLIException("All rows in values must have the same number of columns");
			}
			Object table = getTable(filename, sess, tableIndex);
			validateRange(table, startRow, startColumn, startRow+rowCount-1, startColumn+colCount-1);
			for (int r=0; r<rowCount; r++) {
				List<String> rowValues = values.get(r);
				for (int c=0; c<colCount; c++) {
					setCellText(table, startRow+r, startColumn+c, rowValues.get(c));
				}
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableInsertRows(String filename, int tableIndex, int atRow, int count, boolean after, AbstractJLISession sess) throws JLIException {
		try {
			if (count<1)
				throw new JLIException("Row count must be greater than zero");
			Object table = getTable(filename, sess, tableIndex);
			validateRow(table, atRow);
			double height = getRowHeightForInsert(table, atRow);
			int insertAfter = after ? atRow : atRow-1;
			for (int i=0; i<count; i++) {
				if (!invokeIfFound(table, new String[] {"InsertRow"}, new Object[] {new Double(height), new Integer(insertAfter+i), Boolean.TRUE}))
					throw notSupported("insert_rows");
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableDeleteRows(String filename, int tableIndex, List<Integer> rows, AbstractJLISession sess) throws JLIException {
		try {
			if (rows==null || rows.size()==0)
				throw new JLIException("No rows parameter given");
			Object table = getTable(filename, sess, tableIndex);
			List<Integer> sorted = sortDescending(rows);
			for (Integer row : sorted) {
				validateRow(table, row.intValue());
				if (!invokeIfFound(table, new String[] {"DeleteRow"}, new Object[] {row, Boolean.TRUE})) {
					throw notSupported("delete_rows");
				}
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableInsertColumns(String filename, int tableIndex, int atColumn, int count, boolean after, AbstractJLISession sess) throws JLIException {
		try {
			if (count<1)
				throw new JLIException("Column count must be greater than zero");
			Object table = getTable(filename, sess, tableIndex);
			validateColumn(table, atColumn);
			double width = getColumnWidth(table, atColumn);
			int insertAfter = after ? atColumn : atColumn-1;
			for (int i=0; i<count; i++) {
				if (!invokeIfFound(table, new String[] {"InsertColumn"}, new Object[] {new Double(width), new Integer(insertAfter+i), Boolean.TRUE}))
					throw notSupported("insert_columns");
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableDeleteColumns(String filename, int tableIndex, List<Integer> columns, AbstractJLISession sess) throws JLIException {
		try {
			if (columns==null || columns.size()==0)
				throw new JLIException("No columns parameter given");
			Object table = getTable(filename, sess, tableIndex);
			List<Integer> sorted = sortDescending(columns);
			for (Integer col : sorted) {
				validateColumn(table, col.intValue());
				if (!invokeIfFound(table, new String[] {"DeleteColumn"}, new Object[] {col, Boolean.TRUE})) {
					throw notSupported("delete_columns");
				}
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableClearCell(String filename, int tableIndex, int row, int column, AbstractJLISession sess) throws JLIException {
		tableSetCell(filename, tableIndex, row, column, "", sess);
	}

	public static void tableClearRange(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException {
		try {
			Object table = getTable(filename, sess, tableIndex);
			validateRange(table, startRow, startColumn, endRow, endColumn);
			for (int row=startRow; row<=endRow; row++) {
				for (int col=startColumn; col<=endColumn; col++) {
					setCellText(table, row, col, "");
				}
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableMove(String filename, int tableIndex, JLPoint position, AbstractJLISession sess) throws JLIException {
		try {
			if (position==null)
				throw new JLIException("No position parameter given");
			Object table = getTable(filename, sess, tableIndex);
			Object point = createPoint(position);
			if (invokeIfFound(table, new String[] {"MoveSegment"}, new Object[] {new Integer(0), point, Boolean.TRUE}))
				return;
			throw notSupported("move");
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableSetColumnWidth(String filename, int tableIndex, int column, double width, AbstractJLISession sess) throws JLIException {
		try {
			if (width<=0.0)
				throw new JLIException("Column width must be greater than zero");
			Object table = getTable(filename, sess, tableIndex);
			validateColumn(table, column);
			if (!invokeIfFound(table, new String[] {"SetColumnWidth"}, new Object[] {new Double(width), new Integer(column), getTableSizeLength()}))
				throw notSupported("set_column_width");
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableSetRowHeight(String filename, int tableIndex, int row, double height, AbstractJLISession sess) throws JLIException {
		try {
			if (height<=0.0)
				throw new JLIException("Row height must be greater than zero");
			Object table = getTable(filename, sess, tableIndex);
			validateRow(table, row);
			if (!invokeIfFound(table, new String[] {"SetRowHeight"}, new Object[] {new Double(height), new Integer(row), getTableSizeLength()}))
				throw notSupported("set_row_height");
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableMergeCells(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException {
		try {
			Object table = getTable(filename, sess, tableIndex);
			validateRange(table, startRow, startColumn, endRow, endColumn);
			Object startCell = getCellObject(table, startRow, startColumn);
			Object endCell = getCellObject(table, endRow, endColumn);
			if (!invokeIfFound(table, new String[] {"MergeRegion"}, new Object[] {startCell, endCell, Boolean.TRUE})) {
				throw notSupported("merge_cells");
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableUnmergeCells(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException {
		try {
			Object table = getTable(filename, sess, tableIndex);
			validateRange(table, startRow, startColumn, endRow, endColumn);
			Object startCell = getCellObject(table, startRow, startColumn);
			Object endCell = getCellObject(table, endRow, endColumn);
			if (!invokeIfFound(table, new String[] {"SubdivideRegion"}, new Object[] {startCell, endCell, Boolean.TRUE})) {
				throw notSupported("unmerge_cells");
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableCreate(String filename, int rows, int columns, JLPoint position, List<List<String>> values, AbstractJLISession sess) throws JLIException {
		try {
			if (rows<1)
				throw new JLIException("Row count must be greater than zero");
			if (columns<1)
				throw new JLIException("Column count must be greater than zero");
			validateCreateValues(rows, columns, values);
			CallDrawing drawing = getDrawing(filename, sess);
			Object instructions = createTableInstructions(rows, columns, position);
			Object table = invokeOptional(drawing.getModel2D(), new String[] {"CreateTable"}, new Object[] {instructions});
			if (table==NOT_FOUND)
				table = invokeOptional(drawing.getDrawing(), new String[] {"CreateTable"}, new Object[] {instructions});
			if (table==NOT_FOUND)
				throw notSupported("create");
			if (table==null)
				throw new JLIException("Unable to create drawing table");
			if (values!=null) {
				for (int r=0; r<values.size(); r++) {
					List<String> rowValues = values.get(r);
					for (int c=0; c<rowValues.size(); c++) {
						setCellText(table, r+1, c+1, rowValues.get(c));
					}
				}
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableDelete(String filename, int tableIndex, AbstractJLISession sess) throws JLIException {
		try {
			CallDrawing drawing = getDrawing(filename, sess);
			Object[] tables = getTables(drawing);
			validateTableIndex(tableIndex, tables.length);
			Object table = tables[tableIndex];
			if (!invokeIfFound(drawing.getModel2D(), new String[] {"DeleteTable"}, new Object[] {table, Boolean.TRUE})) {
				throw notSupported("delete");
			}
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	public static void tableRegenerate(String filename, AbstractJLISession sess) throws JLIException {
		try {
			CallDrawing drawing = getDrawing(filename, sess);
			drawing.updateTables();
		}
		catch (JLIException e) {
			throw e;
		}
		catch (Exception e) {
			throw JlinkUtils.createException(e);
		}
	}

	private static CallDrawing getDrawing(String filename, AbstractJLISession sess) throws Exception {
		if (sess==null)
			throw new JLIException("No session found");
		JLGlobal.loadLibrary();
		CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
		if (session==null)
			throw new JLIException("No Creo session found");
		CallModel model = JlinkUtils.getFile(session, filename, false);
		if (!(model instanceof CallDrawing)) {
			if (filename==null)
				throw new JLIException("Active model is not a drawing");
			throw new JLIException("Model is not a drawing: " + filename);
		}
		return (CallDrawing)model;
	}

	private static Object[] getTables(CallDrawing drawing) throws Exception {
		Object tables;
		try {
			tables = invokeOptional(drawing.getDrawing(), new String[] {"ListTables"}, NO_ARGS);
			if (tables==NOT_FOUND)
				tables = invokeOptional(drawing.getModel2D(), new String[] {"ListTables"}, NO_ARGS);
		}
		catch (Exception e) {
			if (isCreoNotFound(e))
				return new Object[0];
			throw e;
		}
		if (tables==NOT_FOUND || tables==null)
			throw notSupported("list");
		int len = getSequenceSize(tables);
		Object[] out = new Object[len];
		for (int i=0; i<len; i++) {
			out[i] = getSequenceValue(tables, i);
		}
		return out;
	}

	private static Object getTable(String filename, AbstractJLISession sess, int tableIndex) throws Exception {
		CallDrawing drawing = getDrawing(filename, sess);
		Object[] tables = getTables(drawing);
		validateTableIndex(tableIndex, tables.length);
		return tables[tableIndex];
	}

	private static DrawingTableData readTableData(Object table, int tableIndex, boolean includeCells) throws Exception {
		DrawingTableData data = new DrawingTableData();
		data.setTableIndex(tableIndex);
		int rows = getRowCount(table);
		int columns = getColumnCount(table);
		data.setRows(rows);
		data.setColumns(columns);
		JLPoint position = getPosition(table);
		if (position!=null)
			data.setPosition(position);
		List<Double> rowHeights = getRowHeights(table, rows);
		if (rowHeights!=null)
			data.setRowHeights(rowHeights);
		List<Double> columnWidths = getColumnWidths(table, columns);
		if (columnWidths!=null)
			data.setColumnWidths(columnWidths);
		if (includeCells) {
			List<DrawingTableCellData> cells = new Vector<DrawingTableCellData>();
			for (int row=1; row<=rows; row++) {
				for (int col=1; col<=columns; col++) {
					cells.add(makeCellData(row, col, getCellText(table, row, col)));
				}
			}
			data.setCells(cells);
		}
		return data;
	}

	private static DrawingTableCellData makeCellData(int row, int column, String text) {
		DrawingTableCellData cell = new DrawingTableCellData();
		cell.setRow(row);
		cell.setColumn(column);
		cell.setText(text==null ? "" : text);
		return cell;
	}

	private static int getRowCount(Object table) throws Exception {
		Object val = invokeRequired(table, new String[] {"GetRowCount"}, NO_ARGS, "row_count");
		return toInt(val);
	}

	private static int getColumnCount(Object table) throws Exception {
		Object val = invokeRequired(table, new String[] {"GetColumnCount"}, NO_ARGS, "column_count");
		return toInt(val);
	}

	private static JLPoint getPosition(Object table) throws Exception {
		Object info = invokeOptional(table, new String[] {"GetInfo"}, new Object[] {new Integer(0)});
		if (info==NOT_FOUND || info==null)
			return null;
		Object val = invokeOptional(info, new String[] {"GetOrigin"}, NO_ARGS);
		if (val==NOT_FOUND || val==null)
			return null;
		return toPoint(val);
	}

	private static List<Double> getRowHeights(Object table, int count) throws Exception {
		List<Double> out = new Vector<Double>();
		for (int i=1; i<=count; i++) {
			try {
				out.add(new Double(getRowHeight(table, i)));
			}
			catch (Exception e) {
				return null;
			}
		}
		return out;
	}

	private static List<Double> getColumnWidths(Object table, int count) throws Exception {
		List<Double> out = new Vector<Double>();
		for (int i=1; i<=count; i++) {
			try {
				out.add(new Double(getColumnWidth(table, i)));
			}
			catch (Exception e) {
				return null;
			}
		}
		return out;
	}

	private static double getRowHeight(Object table, int row) throws Exception {
		Object sizeType = getTableSizeLengthOptional();
		if (sizeType!=NOT_FOUND) {
			try {
				Object val = invokeOptional(table, new String[] {"GetRowHeight"}, new Object[] {new Integer(row), sizeType});
				if (val!=NOT_FOUND)
					return toDouble(val);
			}
			catch (Exception e) {
			}
		}
		Object val = invokeOptional(table, new String[] {"GetRowSize"}, new Object[] {new Integer(0), new Integer(row)});
		if (val!=NOT_FOUND)
			return toDouble(val);
		throw notSupported("row_height");
	}

	private static double getRowHeightForInsert(Object table, int row) throws Exception {
		Object sizeType = getStaticField("com.ptc.pfc.pfcTable.TableSizeType", "TABLESIZE_BY_NUM_CHARS");
		if (sizeType!=NOT_FOUND) {
			try {
				Object val = invokeOptional(table, new String[] {"GetRowHeight"}, new Object[] {new Integer(row), sizeType});
				if (val!=NOT_FOUND)
					return toDouble(val);
			}
			catch (Exception e) {
			}
		}
		try {
			return getRowHeight(table, row);
		}
		catch (Exception e) {
			return DEFAULT_CREATE_ROW_HEIGHT;
		}
	}

	private static double getColumnWidth(Object table, int column) throws Exception {
		Object sizeType = getTableSizeLengthOptional();
		if (sizeType!=NOT_FOUND) {
			try {
				Object val = invokeOptional(table, new String[] {"GetColumnWidth"}, new Object[] {new Integer(column), sizeType});
				if (val!=NOT_FOUND)
					return toDouble(val);
			}
			catch (Exception e) {
			}
		}
		Object val = invokeOptional(table, new String[] {"GetColumnSize"}, new Object[] {new Integer(0), new Integer(column)});
		if (val!=NOT_FOUND)
			return toDouble(val);
		throw notSupported("column_width");
	}

	private static String getCellText(Object table, int row, int column) throws Exception {
		Object cell = getCellObject(table, row, column);
		Object mode = getParamModeNormal();
		Object val;
		try {
			val = invokeOptional(table, new String[] {"GetText"}, new Object[] {cell, mode});
		}
		catch (Exception e) {
			return "";
		}
		if (val!=NOT_FOUND)
			return toText(val);
		throw notSupported("get_cell");
	}

	private static void setCellText(Object table, int row, int column, String text) throws Exception {
		String value = text==null ? "" : text;
		Object cell = getCellObject(table, row, column);
		Object lines = createStringSeq(value);
		if (invokeIfFound(table, new String[] {"SetText"}, new Object[] {cell, lines}))
			return;
		throw notSupported("set_cell");
	}

	private static Object getCellObject(Object table, int row, int column) throws Exception {
		Object cell = invokeStaticOptional("com.ptc.pfc.pfcTable.pfcTable", new String[] {"TableCell_Create"}, new Object[] {new Integer(row), new Integer(column)});
		if (cell!=NOT_FOUND)
			return cell;
		throw notSupported("cell_reference");
	}

	private static Object getParamModeNormal() throws Exception {
		Object val = getStaticField("com.ptc.pfc.pfcTable.ParamMode", "DWGTABLE_NORMAL");
		if (val==NOT_FOUND)
			throw notSupported("param_mode");
		return val;
	}

	private static Object getTableSizeLength() throws Exception {
		Object val = getTableSizeLengthOptional();
		if (val==NOT_FOUND)
			throw notSupported("table_size_type");
		return val;
	}

	private static Object getTableSizeLengthOptional() throws Exception {
		return getStaticField("com.ptc.pfc.pfcTable.TableSizeType", "TABLESIZE_BY_LENGTH");
	}

	private static Object getTableSizeNumChars() throws Exception {
		Object val = getStaticField("com.ptc.pfc.pfcTable.TableSizeType", "TABLESIZE_BY_NUM_CHARS");
		if (val==NOT_FOUND)
			throw notSupported("table_size_type");
		return val;
	}

	private static Object getColumnJustificationLeft() throws Exception {
		Object val = getStaticField("com.ptc.pfc.pfcTable.ColumnJustification", "COL_JUSTIFY_LEFT");
		if (val==NOT_FOUND)
			throw notSupported("column_justification");
		return val;
	}

	private static Object createTableInstructions(int rows, int columns, JLPoint position) throws Exception {
		JLPoint origin = position!=null ? position : new JLPoint(0.0, 0.0, 0.0);
		Object instructions = invokeStaticOptional("com.ptc.pfc.pfcTable.pfcTable", new String[] {"TableCreateInstructions_Create"}, new Object[] {createPoint(origin)});
		if (instructions==NOT_FOUND || instructions==null)
			throw notSupported("create");
		Object sizeType = getTableSizeNumChars();
		if (!invokeIfFound(instructions, new String[] {"SetSizeType"}, new Object[] {sizeType}))
			throw notSupported("create");
		if (!invokeIfFound(instructions, new String[] {"SetColumnData"}, new Object[] {createColumnCreateOptions(columns)}))
			throw notSupported("create");
		if (!invokeIfFound(instructions, new String[] {"SetRowHeights"}, new Object[] {createRealSeq(rows, DEFAULT_CREATE_ROW_HEIGHT)}))
			throw notSupported("create");
		return instructions;
	}

	private static Object createColumnCreateOptions(int columns) throws Exception {
		Object options = invokeStaticOptional("com.ptc.pfc.pfcTable.ColumnCreateOptions", new String[] {"create"}, NO_ARGS);
		if (options==NOT_FOUND || options==null)
			throw notSupported("create");
		Object justification = getColumnJustificationLeft();
		for (int i=0; i<columns; i++) {
			Object option = invokeStaticOptional("com.ptc.pfc.pfcTable.pfcTable", new String[] {"ColumnCreateOption_Create"}, new Object[] {justification, new Double(DEFAULT_CREATE_COLUMN_WIDTH)});
			if (option==NOT_FOUND || option==null)
				throw notSupported("create");
			if (!invokeIfFound(options, new String[] {"insert"}, new Object[] {new Integer(i), option}))
				throw notSupported("create");
		}
		return options;
	}

	private static Object createRealSeq(int count, double value) throws Exception {
		Object seq = invokeStaticOptional("com.ptc.cipjava.realseq", new String[] {"create"}, NO_ARGS);
		if (seq==NOT_FOUND || seq==null)
			throw notSupported("create");
		for (int i=0; i<count; i++) {
			if (!invokeIfFound(seq, new String[] {"insert"}, new Object[] {new Integer(i), new Double(value)}))
				throw notSupported("create");
		}
		return seq;
	}

	private static Object createStringSeq(String text) throws Exception {
		Object lines = invokeStaticOptional("com.ptc.cipjava.stringseq", new String[] {"create"}, NO_ARGS);
		if (lines==NOT_FOUND || lines==null)
			throw notSupported("set_cell");
		String[] split = text.split("\\n", -1);
		for (int i=0; i<split.length; i++) {
			if (!invokeIfFound(lines, new String[] {"insert"}, new Object[] {new Integer(i), split[i]}))
				throw notSupported("set_cell");
		}
		return lines;
	}

	private static Object createPoint(JLPoint point) throws JLIException {
		try {
			CallPoint3D pt = CallPoint3D.create();
			if (pt==null)
				throw notSupported("point");
			pt.set(0, point.getX());
			pt.set(1, point.getY());
			pt.set(2, point.getZ());
			return pt.getPoint();
		}
		catch (jxthrowable e) {
			throw JlinkUtils.createException(e);
		}
	}

	private static JLPoint toPoint(Object obj) throws Exception {
		if (obj==null)
			return null;
		return new JLPoint(getPointCoord(obj, 0), getPointCoord(obj, 1), getPointCoord(obj, 2));
	}

	private static double getPointCoord(Object obj, int idx) throws Exception {
		Object val = invokeOptional(obj, new String[] {"get"}, new Object[] {new Integer(idx)});
		if (val!=NOT_FOUND)
			return toDouble(val);
		return 0.0;
	}

	private static String toText(Object value) throws Exception {
		if (value==null)
			return "";
		if (value instanceof String)
			return (String)value;
		if (value instanceof Number || value instanceof Boolean)
			return value.toString();

		int size = getSequenceSizeOptional(value);
		if (size>=0) {
			String delimiter = value.getClass().getName().indexOf("DetailTexts")>=0 ? "" : "\n";
			StringBuffer buf = new StringBuffer();
			for (int i=0; i<size; i++) {
				if (i>0)
					buf.append(delimiter);
				buf.append(toText(getSequenceValue(value, i)));
			}
			return buf.toString();
		}
		return value.toString();
	}

	private static void validateTableIndex(int tableIndex, int tableCount) throws JLIException {
		if (tableCount==0)
			throw new JLIException("Table index " + tableIndex + " is out of range. No tables were found");
		if (tableIndex<0 || tableIndex>=tableCount)
			throw new JLIException("Table index " + tableIndex + " is out of range. Valid range is 0 to " + (tableCount-1));
	}

	private static void validateCell(Object table, int row, int column) throws Exception {
		validateRow(table, row);
		validateColumn(table, column);
	}

	private static void validateRange(Object table, int startRow, int startColumn, int endRow, int endColumn) throws Exception {
		if (startRow>endRow)
			throw new JLIException("startRow must be less than or equal to endRow");
		if (startColumn>endColumn)
			throw new JLIException("startColumn must be less than or equal to endColumn");
		validateCell(table, startRow, startColumn);
		validateCell(table, endRow, endColumn);
	}

	private static void validateCreateValues(int rows, int columns, List<List<String>> values) throws JLIException {
		if (values==null)
			return;
		if (values.size()>rows)
			throw new JLIException("values contains more rows than the requested table");
		for (int i=0; i<values.size(); i++) {
			List<String> rowValues = values.get(i);
			if (rowValues==null)
				throw new JLIException("No values given for row " + i);
			if (rowValues.size()>columns)
				throw new JLIException("values row " + i + " contains more columns than the requested table");
		}
	}

	private static void validateRow(Object table, int row) throws Exception {
		int rows = getRowCount(table);
		if (row<1 || row>rows)
			throw new JLIException("Row index " + row + " is out of range. Valid range is 1 to " + rows);
	}

	private static void validateColumn(Object table, int column) throws Exception {
		int columns = getColumnCount(table);
		if (column<1 || column>columns)
			throw new JLIException("Column index " + column + " is out of range. Valid range is 1 to " + columns);
	}

	private static List<Integer> sortDescending(List<Integer> values) {
		List<Integer> sorted = new Vector<Integer>(values);
		Collections.sort(sorted, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o2.intValue() - o1.intValue();
			}
		});
		List<Integer> unique = new Vector<Integer>();
		Integer previous = null;
		for (Integer value : sorted) {
			if (previous==null || previous.intValue()!=value.intValue()) {
				unique.add(value);
				previous = value;
			}
		}
		return unique;
	}

	private static int getSequenceSize(Object seq) throws Exception {
		int size = getSequenceSizeOptional(seq);
		if (size<0)
			throw new JLIException("Unable to read Creo sequence size");
		return size;
	}

	private static int getSequenceSizeOptional(Object seq) throws Exception {
		if (seq==null)
			return -1;
		if (seq instanceof List)
			return ((List<?>)seq).size();
		if (seq.getClass().isArray())
			return Array.getLength(seq);
		Object val = invokeOptional(seq, new String[] {"getarraysize"}, NO_ARGS);
		if (val==NOT_FOUND)
			return -1;
		return toInt(val);
	}

	private static Object getSequenceValue(Object seq, int index) throws Exception {
		if (seq instanceof List)
			return ((List<?>)seq).get(index);
		if (seq.getClass().isArray())
			return Array.get(seq, index);
		Object val = invokeOptional(seq, new String[] {"get"}, new Object[] {new Integer(index)});
		if (val==NOT_FOUND)
			throw new JLIException("Unable to read Creo sequence value");
		return val;
	}

	private static int toInt(Object val) throws JLIException {
		if (val instanceof Number)
			return ((Number)val).intValue();
		try {
			return Integer.parseInt(val.toString());
		}
		catch (Exception e) {
			throw new JLIException("Invalid integer value: " + val);
		}
	}

	private static double toDouble(Object val) throws JLIException {
		if (val instanceof Number)
			return ((Number)val).doubleValue();
		try {
			return Double.parseDouble(val.toString());
		}
		catch (Exception e) {
			throw new JLIException("Invalid double value: " + val);
		}
	}

	private static Object invokeRequired(Object target, String[] names, Object[] args, String operation) throws Exception {
		Object val = invokeOptional(target, names, args);
		if (val==NOT_FOUND)
			throw notSupported(operation);
		return val;
	}

	private static boolean invokeIfFound(Object target, String[] names, Object[] args) throws Exception {
		Object val = invokeOptional(target, names, args);
		return val!=NOT_FOUND;
	}

	private static Object invokeOptional(Object target, String[] names, Object[] args) throws Exception {
		if (target==null)
			return NOT_FOUND;
		Class<?> cls = target.getClass();
		for (int i=0; i<names.length; i++) {
			Method method = findMethod(cls, names[i], args, false);
			if (method!=null)
				return invoke(method, target, args);
		}
		return NOT_FOUND;
	}

	private static Object invokeStaticOptional(String className, String[] names, Object[] args) throws Exception {
		Class<?> cls;
		try {
			cls = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			return NOT_FOUND;
		}
		for (int i=0; i<names.length; i++) {
			Method method = findMethod(cls, names[i], args, true);
			if (method!=null)
				return invoke(method, null, args);
		}
		return NOT_FOUND;
	}

	private static Object getStaticField(String className, String fieldName) throws Exception {
		Class<?> cls;
		try {
			cls = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			return NOT_FOUND;
		}
		try {
			Field field = cls.getField(fieldName);
			if (!Modifier.isStatic(field.getModifiers()))
				return NOT_FOUND;
			return field.get(null);
		}
		catch (NoSuchFieldException e) {
			return NOT_FOUND;
		}
		catch (IllegalAccessException e) {
			return NOT_FOUND;
		}
	}

	private static Method findMethod(Class<?> cls, String name, Object[] args, boolean requireStatic) {
		Method[] methods = cls.getMethods();
		for (int i=0; i<methods.length; i++) {
			Method method = methods[i];
			if (!method.getName().equals(name))
				continue;
			if (requireStatic && !Modifier.isStatic(method.getModifiers()))
				continue;
			Class<?>[] types = method.getParameterTypes();
			if (types.length!=args.length)
				continue;
			boolean ok = true;
			for (int j=0; j<types.length; j++) {
				if (!isCompatible(types[j], args[j])) {
					ok = false;
					break;
				}
			}
			if (ok)
				return method;
		}
		return null;
	}

	private static boolean isCompatible(Class<?> type, Object arg) {
		if (arg==null)
			return !type.isPrimitive();
		Class<?> argType = arg.getClass();
		if (type.isPrimitive()) {
			if (type==Integer.TYPE)
				return arg instanceof Integer;
			if (type==Double.TYPE)
				return arg instanceof Double;
			if (type==Boolean.TYPE)
				return arg instanceof Boolean;
			if (type==Long.TYPE)
				return arg instanceof Long;
			if (type==Float.TYPE)
				return arg instanceof Float;
			if (type==Short.TYPE)
				return arg instanceof Short;
			if (type==Byte.TYPE)
				return arg instanceof Byte;
			if (type==Character.TYPE)
				return arg instanceof Character;
		}
		return type.isAssignableFrom(argType);
	}

	private static boolean isCreoNotFound(Exception e) {
		Throwable t = e;
		while (t!=null) {
			if (t.getClass().getName().indexOf("XToolkitNotFound")>=0)
				return true;
			t = t.getCause();
		}
		return false;
	}

	private static Object invoke(Method method, Object target, Object[] args) throws Exception {
		try {
			return method.invoke(target, args);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getTargetException();
			if (cause instanceof Exception)
				throw (Exception)cause;
			throw new JLIException(cause.toString());
		}
		catch (IllegalAccessException e) {
			return NOT_FOUND;
		}
	}

	private static JLIException notSupported(String operation) {
		return new JLIException("not_supported: drawing table operation '" + operation + "' is not available in this Creo J-Link API");
	}
}
