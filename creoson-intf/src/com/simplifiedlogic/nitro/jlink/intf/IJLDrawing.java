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
package com.simplifiedlogic.nitro.jlink.intf;

import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.DrawingTableCellData;
import com.simplifiedlogic.nitro.jlink.data.DrawingTableData;
import com.simplifiedlogic.nitro.jlink.data.DrawingFormatData;
import com.simplifiedlogic.nitro.jlink.data.JLBox;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.data.SymbolDefData;
import com.simplifiedlogic.nitro.jlink.data.SymbolInstData;
import com.simplifiedlogic.nitro.jlink.data.ViewDetailData;
import com.simplifiedlogic.nitro.jlink.data.ViewDisplayData;
import com.simplifiedlogic.nitro.jlink.data.ViewScaleResults;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLDrawing {

	public static final boolean DISPLAY_YES = true;
	public static final boolean DISPLAY_NO = false;
	public static final boolean ACTIVATE_YES = true;
	public static final boolean ACTIVATE_NO = false;
	public static final boolean NEWWIN_YES = true;
	public static final boolean NEWWIN_NO = false;
	public static final boolean DELETECHILDREN_YES = true;
	public static final boolean DELETECHILDREN_NO = false;
	public static final boolean DELETEVIEWS_YES = true;
	public static final boolean DELETEVIEWS_NO = false;

	public String create(String model, String drawing, String template, double scale, boolean display, boolean activate, boolean newwin, String sessionId) throws JLIException;
	public String create(String model, String drawing, String template, double scale, boolean display, boolean activate, boolean newwin, AbstractJLISession sess) throws JLIException;

	public List<String> listModels(String drawingName, String modelName, String sessionId)
		throws JLIException;
	public List<String> listModels(String drawingName, String modelName, AbstractJLISession sess)
		throws JLIException;

	public void addModel(String drawingName, String modelName, String sessionId) throws JLIException;
	public void addModel(String drawingName, String modelName, AbstractJLISession sess) throws JLIException;

	public void deleteModels(String drawingName, String modelName, boolean deleteViews, String sessionId) throws JLIException;
	public void deleteModels(String drawingName, String modelName, boolean deleteViews, AbstractJLISession sess) throws JLIException;
	
	public String getCurrentModel(String filename, String sessionId) throws JLIException;
	public String getCurrentModel(String filename, AbstractJLISession sess) throws JLIException;
	
	public void setCurrentModel(String drawingName, String modelName, String sessionId) throws JLIException;
	public void setCurrentModel(String drawingName, String modelName, AbstractJLISession sess) throws JLIException;

	public void regenerate(String filename, String sessionId)
		throws JLIException;
	public void regenerate(String filename, AbstractJLISession sess)
		throws JLIException;

	public void regenerateSheet(String filename, int sheet, String sessionId)
		throws JLIException;
	public void regenerateSheet(String filename, int sheet, AbstractJLISession sess)
		throws JLIException;

	public void selectSheet(String filename, int sheet, String sessionId) 
		throws JLIException;
	public void selectSheet(String filename, int sheet, AbstractJLISession sess) 
		throws JLIException;

	public void addSheet(String drawingName, int position, String sessionId) throws JLIException;
	public void addSheet(String drawingName, int position, AbstractJLISession sess) throws JLIException;

	public void deleteSheet(String filename, int sheet, String sessionId) 
		throws JLIException;
	public void deleteSheet(String filename, int sheet, AbstractJLISession sess) 
		throws JLIException;

	public int getCurSheet(String filename, String sessionId)
		throws JLIException;
	public int getCurSheet(String filename, AbstractJLISession sess)
		throws JLIException;

	public int getNumSheets(String filename, String sessionId)
		throws JLIException;
	public int getNumSheets(String filename, AbstractJLISession sess)
		throws JLIException;

	public void scaleSheet(String filename, int sheet, 
			double scale, String scaleFilename, 
			String sessionId) 
		throws JLIException;
	public void scaleSheet(String filename, int sheet, 
			double scale, String scaleFilename, 
			AbstractJLISession sess) 
		throws JLIException;

	public double getSheetScale(String filename, int sheet, String scaleFilename, String sessionId) throws JLIException;
	public double getSheetScale(String filename, int sheet, String scaleFilename, AbstractJLISession sess) throws JLIException;

	public String getSheetSize(String filename, int sheet, String sessionId) throws JLIException;
	public String getSheetSize(String filename, int sheet, AbstractJLISession sess) throws JLIException;

	public void createGeneralView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String modelName,
			String modelViewName,
			JLPoint location,
			double scale,
			ViewDisplayData displayData,
			boolean exploded, 
			String sessionId) throws JLIException;
	public void createGeneralView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String modelName,
			String modelViewName,
			JLPoint location,
			double scale,
			ViewDisplayData displayData,
			boolean exploded, 
			AbstractJLISession sess) throws JLIException;

	public void createProjectionView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String parentViewName,
			JLPoint location,
			ViewDisplayData displayData,
			boolean exploded, 
			String sessionId) throws JLIException;
	public void createProjectionView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String parentViewName,
			JLPoint location,
			ViewDisplayData displayData,
			boolean exploded, 
			AbstractJLISession sess) throws JLIException;
	
	public List<String> listViews(String drawingName, String viewName, String sessionId) throws JLIException;
	public List<String> listViews(String drawingName, String viewName, AbstractJLISession sess) throws JLIException;
	
	public List<ViewDetailData> listViewDetails(String drawingName, String viewName, String sessionId) throws JLIException;
	public List<ViewDetailData> listViewDetails(String drawingName, String viewName, AbstractJLISession sess) throws JLIException;

	public JLPoint getViewLoc(String drawingName, String viewName, String sessionId) throws JLIException;
	public JLPoint getViewLoc(String drawingName, String viewName, AbstractJLISession sess) throws JLIException;
	
	public void setViewLoc(String drawingName, String viewName, JLPoint point, boolean relative, String sessionId) throws JLIException;
	public void setViewLoc(String drawingName, String viewName, JLPoint point, boolean relative, AbstractJLISession sess) throws JLIException;
	
	public void deleteView(String drawingName, String viewName, int sheetno, boolean deleteChildren, String sessionId) throws JLIException;
	public void deleteView(String drawingName, String viewName, int sheetno, boolean deleteChildren, AbstractJLISession sess) throws JLIException;

	public void renameView(String drawingName, String viewName, String newName, String sessionId) throws JLIException;
	public void renameView(String drawingName, String viewName, String newName, AbstractJLISession sess) throws JLIException;

	public ViewScaleResults scaleView(String filename, String viewName, double scale, String sessionId) throws JLIException;
	public ViewScaleResults scaleView(String filename, String viewName, double scale, AbstractJLISession sess) throws JLIException;

	public double getViewScale(String filename, String viewName, String sessionId) throws JLIException;
	public double getViewScale(String filename, String viewName, AbstractJLISession sess) throws JLIException;

	public int getViewSheet(String filename, String viewName, String sessionId) throws JLIException;
	public int getViewSheet(String filename, String viewName, AbstractJLISession sess) throws JLIException;

    public JLBox viewBoundingBox(String filename, String viewName, String sessionId) throws JLIException;
    public JLBox viewBoundingBox(String filename, String viewName, AbstractJLISession sess) throws JLIException;

    public SymbolDefData loadSymbolDef(String filename, String symbolDir, String symbolFile, String sessionId) throws JLIException;
    public SymbolDefData loadSymbolDef(String filename, String symbolDir, String symbolFile, AbstractJLISession sess) throws JLIException;

    public boolean isSymbolDefLoaded(String filename, String symbolFile, String sessionId) throws JLIException;
    public boolean isSymbolDefLoaded(String filename, String symbolFile, AbstractJLISession sess) throws JLIException;
    
    public void deleteSymbolDef(String filename, String symbolFile, String sessionId) throws JLIException;
    public void deleteSymbolDef(String filename, String symbolFile, AbstractJLISession sess) throws JLIException;

    public void createSymbol(String filename, String symbolFile, JLPoint location, Map<String, Object> replaceValues, int sheet, String sessionId) throws JLIException;
    public void createSymbol(String filename, String symbolFile, JLPoint location, Map<String, Object> replaceValues, int sheet, AbstractJLISession sess) throws JLIException;
    
    public List<SymbolInstData> listSymbols(String filename, String symbolFile, int sheet, String sessionId) throws JLIException;
    public List<SymbolInstData> listSymbols(String filename, String symbolFile, int sheet, AbstractJLISession sess) throws JLIException;

    public void deleteSymbolInst(String filename, int symbolId, String sessionId) throws JLIException;
    public void deleteSymbolInst(String filename, int symbolId, AbstractJLISession sess) throws JLIException;

    public DrawingFormatData getSheetFormat(String filename, int sheet, String sessionId) throws JLIException;
    public DrawingFormatData getSheetFormat(String filename, int sheet, AbstractJLISession sess) throws JLIException;
    
    public void setSheetFormat(String filename, int sheet, String dirname, String formatFilename, String sessionId) throws JLIException;
    public void setSheetFormat(String filename, int sheet, String dirname, String formatFilename, AbstractJLISession sess) throws JLIException;

    public List<DrawingTableData> tableList(String filename, String sessionId) throws JLIException;
    public List<DrawingTableData> tableList(String filename, AbstractJLISession sess) throws JLIException;

    public DrawingTableData tableGetInfo(String filename, int tableIndex, String sessionId) throws JLIException;
    public DrawingTableData tableGetInfo(String filename, int tableIndex, AbstractJLISession sess) throws JLIException;

    public DrawingTableCellData tableGetCell(String filename, int tableIndex, int row, int column, String sessionId) throws JLIException;
    public DrawingTableCellData tableGetCell(String filename, int tableIndex, int row, int column, AbstractJLISession sess) throws JLIException;

    public void tableSetCell(String filename, int tableIndex, int row, int column, String value, String sessionId) throws JLIException;
    public void tableSetCell(String filename, int tableIndex, int row, int column, String value, AbstractJLISession sess) throws JLIException;

    public List<List<String>> tableGetRange(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, String sessionId) throws JLIException;
    public List<List<String>> tableGetRange(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException;

    public void tableSetRange(String filename, int tableIndex, int startRow, int startColumn, List<List<String>> values, String sessionId) throws JLIException;
    public void tableSetRange(String filename, int tableIndex, int startRow, int startColumn, List<List<String>> values, AbstractJLISession sess) throws JLIException;

    public void tableInsertRows(String filename, int tableIndex, int atRow, int count, boolean after, String sessionId) throws JLIException;
    public void tableInsertRows(String filename, int tableIndex, int atRow, int count, boolean after, AbstractJLISession sess) throws JLIException;

    public void tableDeleteRows(String filename, int tableIndex, List<Integer> rows, String sessionId) throws JLIException;
    public void tableDeleteRows(String filename, int tableIndex, List<Integer> rows, AbstractJLISession sess) throws JLIException;

    public void tableInsertColumns(String filename, int tableIndex, int atColumn, int count, boolean after, String sessionId) throws JLIException;
    public void tableInsertColumns(String filename, int tableIndex, int atColumn, int count, boolean after, AbstractJLISession sess) throws JLIException;

    public void tableDeleteColumns(String filename, int tableIndex, List<Integer> columns, String sessionId) throws JLIException;
    public void tableDeleteColumns(String filename, int tableIndex, List<Integer> columns, AbstractJLISession sess) throws JLIException;

    public void tableClearCell(String filename, int tableIndex, int row, int column, String sessionId) throws JLIException;
    public void tableClearCell(String filename, int tableIndex, int row, int column, AbstractJLISession sess) throws JLIException;

    public void tableClearRange(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, String sessionId) throws JLIException;
    public void tableClearRange(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException;

    public void tableMove(String filename, int tableIndex, JLPoint position, String sessionId) throws JLIException;
    public void tableMove(String filename, int tableIndex, JLPoint position, AbstractJLISession sess) throws JLIException;

    public void tableSetColumnWidth(String filename, int tableIndex, int column, double width, String sessionId) throws JLIException;
    public void tableSetColumnWidth(String filename, int tableIndex, int column, double width, AbstractJLISession sess) throws JLIException;

    public void tableSetRowHeight(String filename, int tableIndex, int row, double height, String sessionId) throws JLIException;
    public void tableSetRowHeight(String filename, int tableIndex, int row, double height, AbstractJLISession sess) throws JLIException;

    public void tableMergeCells(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, String sessionId) throws JLIException;
    public void tableMergeCells(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException;

    public void tableUnmergeCells(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, String sessionId) throws JLIException;
    public void tableUnmergeCells(String filename, int tableIndex, int startRow, int startColumn, int endRow, int endColumn, AbstractJLISession sess) throws JLIException;

    public void tableCreate(String filename, int rows, int columns, JLPoint position, List<List<String>> values, String sessionId) throws JLIException;
    public void tableCreate(String filename, int rows, int columns, JLPoint position, List<List<String>> values, AbstractJLISession sess) throws JLIException;

    public void tableDelete(String filename, int tableIndex, String sessionId) throws JLIException;
    public void tableDelete(String filename, int tableIndex, AbstractJLISession sess) throws JLIException;

    public void tableRegenerate(String filename, String sessionId) throws JLIException;
    public void tableRegenerate(String filename, AbstractJLISession sess) throws JLIException;
}
