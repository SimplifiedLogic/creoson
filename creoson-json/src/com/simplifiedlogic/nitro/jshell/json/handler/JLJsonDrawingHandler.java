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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.simplifiedlogic.nitro.jlink.data.DrawingFormatData;
import com.simplifiedlogic.nitro.jlink.data.DrawingTableCellData;
import com.simplifiedlogic.nitro.jlink.data.DrawingTableData;
import com.simplifiedlogic.nitro.jlink.data.JLBox;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.data.SymbolDefData;
import com.simplifiedlogic.nitro.jlink.data.SymbolInstData;
import com.simplifiedlogic.nitro.jlink.data.ViewDetailData;
import com.simplifiedlogic.nitro.jlink.data.ViewDisplayData;
import com.simplifiedlogic.nitro.jlink.data.ViewScaleResults;
import com.simplifiedlogic.nitro.jlink.intf.IJLDrawing;
import com.simplifiedlogic.nitro.jshell.json.request.JLDrawingRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLDrawingResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "drawing" functions
 *
 * @author Adam Andrews
 *
 */
public class JLJsonDrawingHandler extends JLJsonCommandHandler implements JLDrawingRequestParams, JLDrawingResponseParams {

	private IJLDrawing drawHandler = null;

	/**
	 * @param drawHandler
	 */
	public JLJsonDrawingHandler(IJLDrawing drawHandler) {
		this.drawHandler = drawHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;

		if (function.equals(FUNC_CREATE)) return actionCreate(sessionId, input);
		else if (function.equals(FUNC_LIST_MODELS)) return actionListModels(sessionId, input);
		else if (function.equals(FUNC_ADD_MODEL)) return actionAddModel(sessionId, input);
		else if (function.equals(FUNC_DELETE_MODELS)) return actionDeleteModels(sessionId, input);
		else if (function.equals(FUNC_GET_CUR_MODEL)) return actionGetCurModel(sessionId, input);
		else if (function.equals(FUNC_SET_CUR_MODEL)) return actionSetCurModel(sessionId, input);
		else if (function.equals(FUNC_REGENERATE)) return actionRegenerate(sessionId, input);
		else if (function.equals(FUNC_REGENERATE_SHEET)) return actionRegenerateSheet(sessionId, input);
		else if (function.equals(FUNC_SELECT_SHEET)) return actionSelectSheet(sessionId, input);
		else if (function.equals(FUNC_ADD_SHEET)) return actionAddSheet(sessionId, input);
		else if (function.equals(FUNC_DELETE_SHEET)) return actionDeleteSheet(sessionId, input);
		else if (function.equals(FUNC_GET_CUR_SHEET)) return actionGetCurSheet(sessionId, input);
		else if (function.equals(FUNC_GET_NUM_SHEETS)) return actionGetNumSheets(sessionId, input);
		else if (function.equals(FUNC_SCALE_SHEET)) return actionScaleSheet(sessionId, input);
		else if (function.equals(FUNC_GET_SHEET_SCALE)) return actionGetSheetScale(sessionId, input);
		else if (function.equals(FUNC_GET_SHEET_SIZE)) return actionGetSheetSize(sessionId, input);
		else if (function.equals(FUNC_CREATE_GEN_VIEW)) return actionCreateGeneralView(sessionId, input);
		else if (function.equals(FUNC_CREATE_PROJ_VIEW)) return actionCreateProjectionView(sessionId, input);
		else if (function.equals(FUNC_LIST_VIEWS)) return actionListViews(sessionId, input);
		else if (function.equals(FUNC_LIST_VIEW_DETAILS)) return actionListViewDetails(sessionId, input);
		else if (function.equals(FUNC_GET_VIEW_LOC)) return actionGetViewLoc(sessionId, input);
		else if (function.equals(FUNC_SET_VIEW_LOC)) return actionSetViewLoc(sessionId, input);
		else if (function.equals(FUNC_DELETE_VIEW)) return actionDeleteView(sessionId, input);
		else if (function.equals(FUNC_RENAME_VIEW)) return actionRenameView(sessionId, input);
		else if (function.equals(FUNC_SCALE_VIEW)) return actionScaleView(sessionId, input);
		else if (function.equals(FUNC_GET_VIEW_SCALE)) return actionGetViewScale(sessionId, input);
		else if (function.equals(FUNC_GET_VIEW_SHEET)) return actionGetViewSheet(sessionId, input);
		else if (function.equals(FUNC_VIEW_BOUND_BOX)) return actionViewBoundingBox(sessionId, input);
		else if (function.equals(FUNC_LOAD_SYMBOL_DEF)) return actionLoadSymbolDef(sessionId, input);
		else if (function.equals(FUNC_IS_SYMBOL_DEF_LOADED)) return actionIsSymbolDefLoaded(sessionId, input);
		else if (function.equals(FUNC_CREATE_SYMBOL)) return actionCreateSymbol(sessionId, input);
		else if (function.equals(FUNC_LIST_SYMBOLS)) return actionListSymbols(sessionId, input);
		else if (function.equals(FUNC_DELETE_SYMBOL_DEF)) return actionDeleteSymbolDef(sessionId, input);
		else if (function.equals(FUNC_DELETE_SYMBOL_INST)) return actionDeleteSymbolInst(sessionId, input);
		else if (function.equals(FUNC_GET_SHEET_FORMAT)) return actionGetSheetFormat(sessionId, input);
		else if (function.equals(FUNC_SET_SHEET_FORMAT)) return actionSetSheetFormat(sessionId, input);
		else if (function.equals(FUNC_TABLE_LIST)) return actionTableList(sessionId, input);
		else if (function.equals(FUNC_TABLE_GET_INFO)) return actionTableGetInfo(sessionId, input);
		else if (function.equals(FUNC_TABLE_GET_CELL)) return actionTableGetCell(sessionId, input);
		else if (function.equals(FUNC_TABLE_SET_CELL)) return actionTableSetCell(sessionId, input);
		else if (function.equals(FUNC_TABLE_GET_RANGE)) return actionTableGetRange(sessionId, input);
		else if (function.equals(FUNC_TABLE_SET_RANGE)) return actionTableSetRange(sessionId, input);
		else if (function.equals(FUNC_TABLE_INSERT_ROWS)) return actionTableInsertRows(sessionId, input);
		else if (function.equals(FUNC_TABLE_DELETE_ROWS)) return actionTableDeleteRows(sessionId, input);
		else if (function.equals(FUNC_TABLE_INSERT_COLUMNS)) return actionTableInsertColumns(sessionId, input);
		else if (function.equals(FUNC_TABLE_DELETE_COLUMNS)) return actionTableDeleteColumns(sessionId, input);
		else if (function.equals(FUNC_TABLE_CLEAR_CELL)) return actionTableClearCell(sessionId, input);
		else if (function.equals(FUNC_TABLE_CLEAR_RANGE)) return actionTableClearRange(sessionId, input);
		else if (function.equals(FUNC_TABLE_MOVE)) return actionTableMove(sessionId, input);
		else if (function.equals(FUNC_TABLE_SET_COLUMN_WIDTH)) return actionTableSetColumnWidth(sessionId, input);
		else if (function.equals(FUNC_TABLE_SET_ROW_HEIGHT)) return actionTableSetRowHeight(sessionId, input);
		else if (function.equals(FUNC_TABLE_MERGE_CELLS)) return actionTableMergeCells(sessionId, input);
		else if (function.equals(FUNC_TABLE_UNMERGE_CELLS)) return actionTableUnmergeCells(sessionId, input);
		else if (function.equals(FUNC_TABLE_CREATE)) return actionTableCreate(sessionId, input);
		else if (function.equals(FUNC_TABLE_DELETE)) return actionTableDelete(sessionId, input);
		else if (function.equals(FUNC_TABLE_REGENERATE)) return actionTableRegenerate(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}

	private Hashtable<String, Object> actionCreate(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String model = checkStringParameter(input, PARAM_MODEL, false);
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String template = checkStringParameter(input, PARAM_TEMPLATE, true);
		double scale = 0.0;
		Double scaleObj = checkDoubleParameter(input, PARAM_SCALE, false);
		if (scaleObj!=null)
			scale = scaleObj.doubleValue();
		boolean display = checkFlagParameter(input, PARAM_DISPLAY, false, false);
		boolean activate = checkFlagParameter(input, PARAM_ACTIVATE, false, false);
		boolean newwin = checkFlagParameter(input, PARAM_NEWWIN, false, false);

		String result = drawHandler.create(model, drawing, template, scale, display, activate, newwin, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (result!=null) {
    		out.put(OUTPUT_DRAWING, result);
		}
		return out;
	}

	private Hashtable<String, Object> actionListModels(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String model = checkStringParameter(input, PARAM_MODEL, false);
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		List<String> models = drawHandler.listModels(drawing, model, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (models!=null) {
    		out.put(OUTPUT_MODELS, models);
		}
		return out;
	}

	private Hashtable<String, Object> actionAddModel(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String model = checkStringParameter(input, PARAM_MODEL, false);
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		drawHandler.addModel(drawing, model, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionDeleteModels(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String model = checkStringParameter(input, PARAM_MODEL, false);
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		boolean deleteViews = checkFlagParameter(input, PARAM_DELETE_VIEWS, false, false);

		drawHandler.deleteModels(drawing, model, deleteViews, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionGetCurModel(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		String result = drawHandler.getCurrentModel(drawing, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (result!=null) {
    		out.put(OUTPUT_MODEL, result);
		}
		return out;
	}

	private Hashtable<String, Object> actionSetCurModel(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String model = checkStringParameter(input, PARAM_MODEL, false);
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		drawHandler.setCurrentModel(drawing, model, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionRegenerate(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		drawHandler.regenerate(drawing, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionRegenerateSheet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);

		drawHandler.regenerateSheet(drawing, sheet, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionSelectSheet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 0);

		drawHandler.selectSheet(drawing, sheet, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionAddSheet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int position = checkIntParameter(input, PARAM_POSITION, true, 0);

		drawHandler.addSheet(drawing, position, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionDeleteSheet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 0);

		drawHandler.deleteSheet(drawing, sheet, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionGetCurSheet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		int sheet = drawHandler.getCurSheet(drawing, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		out.put(OUTPUT_SHEET, sheet);
		return out;
	}

	private Hashtable<String, Object> actionGetNumSheets(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		int sheets = drawHandler.getNumSheets(drawing, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		out.put(OUTPUT_NUM_SHEETS, sheets);
		return out;
	}

	private Hashtable<String, Object> actionScaleSheet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 0);
		double scale = 0.0;
		Double scaleObj = checkDoubleParameter(input, PARAM_SCALE, true);
		if (scaleObj!=null)
			scale = scaleObj.doubleValue();
		String scaleFilename = checkStringParameter(input, PARAM_MODEL, false);

		drawHandler.scaleSheet(drawing, sheet, scale, scaleFilename, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionGetSheetScale(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 0);
		String scaleFilename = checkStringParameter(input, PARAM_MODEL, false);

		double scale = drawHandler.getSheetScale(drawing, sheet, scaleFilename, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		out.put(OUTPUT_SCALE, scale);
		return out;
	}

	private Hashtable<String, Object> actionGetSheetSize(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 0);

		String size = drawHandler.getSheetSize(drawing, sheet, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (size!=null) {
			out.put(OUTPUT_SIZE, size);
		}
		return out;
	}

	private Hashtable<String, Object> actionCreateGeneralView(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);
		String model = checkStringParameter(input, PARAM_MODEL, false);
		String modelView = checkStringParameter(input, PARAM_MODEL_VIEW, true);
		Map<String, Object> pointObj = checkMapParameter(input, PARAM_POINT, true);
		JLPoint pt = readPoint(pointObj);
		Map<String, Object> displayObj = checkMapParameter(input, PARAM_DISPLAY_DATA, false);
		ViewDisplayData displayData = makeDisplayData(displayObj);
		boolean exploded = checkFlagParameter(input, PARAM_EXPLODED, false, false);

		double scale = 0.0;
		Double scaleObj = checkDoubleParameter(input, PARAM_SCALE, false);
		if (scaleObj!=null)
			scale = scaleObj.doubleValue();

		drawHandler.createGeneralView(drawing, view, sheet, model, modelView, pt, scale, displayData, exploded, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionCreateProjectionView(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);
		String parentView = checkStringParameter(input, PARAM_PARENT_VIEW, true);
		Map<String, Object> pointObj = checkMapParameter(input, PARAM_POINT, true);
		JLPoint pt = readPoint(pointObj);
		Map<String, Object> displayObj = checkMapParameter(input, PARAM_DISPLAY_DATA, false);
		ViewDisplayData displayData = makeDisplayData(displayObj);
		boolean exploded = checkFlagParameter(input, PARAM_EXPLODED, false, false);

		drawHandler.createProjectionView(drawing, view, sheet, parentView, pt, displayData, exploded, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionListViews(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, false);

		List<String> views = drawHandler.listViews(drawing, view, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (views!=null) {
    		out.put(OUTPUT_VIEWS, views);
		}
		return out;
	}

	private Hashtable<String, Object> actionListViewDetails(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, false);

		List<ViewDetailData> views = drawHandler.listViewDetails(drawing, view, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (views!=null) {
			Vector<Map<String, Object>> outViews = new Vector<Map<String, Object>>();
    		out.put(OUTPUT_VIEWS, outViews);
			Map<String, Object> outView = null;
			for (ViewDetailData v : views) {
				outView = new Hashtable<String, Object>();
				if (v.getName()!=null)
					outView.put(OUTPUT_VIEW_NAME, v.getName());
				outView.put(OUTPUT_SHEET, v.getSheetNo());
				Map<String, Object> recPt = writePoint(v.getLocation());
				if (recPt!=null)
					outView.put(OUTPUT_LOCATION, recPt);
				outView.put(OUTPUT_TEXT_HEIGHT, v.getTextHeight());
				if (v.getModel()!=null)
					outView.put(OUTPUT_VIEW_MODEL, v.getModel());
				if (v.getSimpRep()!=null)
					outView.put(OUTPUT_SIMPREP, v.getSimpRep());

				outViews.add(outView);
			}
		}
		return out;
	}

	private Hashtable<String, Object> actionGetViewLoc(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);

		JLPoint pt = drawHandler.getViewLoc(drawing, view, sessionId);

		if (pt!=null) {
			Hashtable<String, Object> out = writePoint(pt);
			return out;
		}
		else {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			return out;
		}
	}

	private Hashtable<String, Object> actionSetViewLoc(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);
		boolean relative = checkFlagParameter(input, PARAM_RELATIVE, false, false);
		Map<String, Object> pointObj = checkMapParameter(input, PARAM_POINT, true);
		JLPoint pt = readPoint(pointObj);

		drawHandler.setViewLoc(drawing, view, pt, relative, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionDeleteView(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);
		boolean deleteChildren = checkFlagParameter(input, PARAM_DEL_CHILDREN, false, false);

		drawHandler.deleteView(drawing, view, sheet, deleteChildren, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionRenameView(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);
		String newView = checkStringParameter(input, PARAM_NEWVIEW, true);

		drawHandler.renameView(drawing, view, newView, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionScaleView(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);
		double scale = 0.0;
		Double scaleObj = checkDoubleParameter(input, PARAM_SCALE, true);
		if (scaleObj!=null)
			scale = scaleObj.doubleValue();

		ViewScaleResults result = drawHandler.scaleView(drawing, view, scale, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (result!=null) {
			if (result.getFailedViews()!=null)
				out.put(OUTPUT_FAILED_VIEWS, result.getFailedViews());
			if (result.getSuccessViews()!=null)
				out.put(OUTPUT_SUCCESS_VIEWS, result.getSuccessViews());
		}
		return out;
	}

	private Hashtable<String, Object> actionGetViewScale(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);

		double scale = drawHandler.getViewScale(drawing, view, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		out.put(OUTPUT_SCALE, scale);
		return out;
	}

	private Hashtable<String, Object> actionGetViewSheet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);

		int sheet = drawHandler.getViewSheet(drawing, view, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		out.put(OUTPUT_SHEET, sheet);
		return out;
	}

	private Hashtable<String, Object> actionViewBoundingBox(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String drawing = checkStringParameter(input, PARAM_DRAWING, false);
        String view = checkStringParameter(input, PARAM_VIEW, false);

        JLBox box = drawHandler.viewBoundingBox(drawing, view, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
        if (box!=null) {
			out.put(OUTPUT_XMIN, box.getXmin());
			out.put(OUTPUT_XMAX, box.getXmax());
			out.put(OUTPUT_YMIN, box.getYmin());
			out.put(OUTPUT_YMAX, box.getYmax());
        }
    	return out;
	}

	private Hashtable<String, Object> actionLoadSymbolDef(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String symbolDir = checkStringParameter(input, PARAM_SYMBOL_DIR, false);
		String symbolFile = checkStringParameter(input, PARAM_SYMBOL_FILE, true);

		SymbolDefData symbolData = drawHandler.loadSymbolDef(drawing, symbolDir, symbolFile, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (symbolData!=null) {
			out.put(OUTPUT_ID, symbolData.getId());
			out.put(OUTPUT_NAME, symbolData.getName());
		}
		return out;
	}

	private Hashtable<String, Object> actionIsSymbolDefLoaded(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String symbolFile = checkStringParameter(input, PARAM_SYMBOL_FILE, true);

		boolean loaded = drawHandler.isSymbolDefLoaded(drawing, symbolFile, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		out.put(OUTPUT_LOADED, loaded);
   		return out;
	}

	private Hashtable<String, Object> actionCreateSymbol(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String symbolFile = checkStringParameter(input, PARAM_SYMBOL_FILE, true);
		Map<String, Object> pointObj = checkMapParameter(input, PARAM_POINT, true);
		JLPoint pt = readPoint(pointObj);
		Map<String, Object> replaceValues = checkMapParameter(input, PARAM_REPLACE_VALUES, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);

		drawHandler.createSymbol(drawing, symbolFile, pt, replaceValues, sheet, sessionId);

   		return null;
	}

	private Hashtable<String, Object> actionListSymbols(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String symbolFile = checkStringParameter(input, PARAM_SYMBOL_FILE, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);

		List<SymbolInstData> symbols = drawHandler.listSymbols(drawing, symbolFile, sheet, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (symbols!=null) {
			Vector<Map<String, Object>> outViews = new Vector<Map<String, Object>>();
    		out.put(OUTPUT_SYMBOLS, outViews);
			Map<String, Object> outView = null;
			for (SymbolInstData v : symbols) {
				outView = new Hashtable<String, Object>();
				outView.put(OUTPUT_ID, v.getId());
				if (v.getName()!=null)
					outView.put(OUTPUT_SYMBOL_NAME, v.getName());
				outView.put(OUTPUT_SHEET, v.getSheet());

				outView.put(OUTPUT_ATTACH_TYPE, v.getAttachType());
				if (v.getLocation()!=null)
					outView.put(OUTPUT_LOCATION, writePoint(v.getLocation()));

				outViews.add(outView);
			}
		}
		return out;
	}

	private Hashtable<String, Object> actionDeleteSymbolDef(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String symbolFile = checkStringParameter(input, PARAM_SYMBOL_FILE, true);

		drawHandler.deleteSymbolDef(drawing, symbolFile, sessionId);

   		return null;
	}

	private Hashtable<String, Object> actionDeleteSymbolInst(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int symbolId = checkIntParameter(input, PARAM_SYMBOL_ID, true, 0);

		drawHandler.deleteSymbolInst(drawing, symbolId, sessionId);

   		return null;
	}

	private Hashtable<String, Object> actionGetSheetFormat(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 1);

		DrawingFormatData result = drawHandler.getSheetFormat(drawing, sheet, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (result!=null) {
			if (result.getFileName()!=null)
				out.put(OUTPUT_MODEL, result.getFileName());
			if (result.getFullName()!=null)
				out.put(OUTPUT_FULLNAME, result.getFullName());
			if (result.getCommonName()!=null)
				out.put(OUTPUT_COMMONNAME, result.getCommonName());
		}
		return out;
	}

	private Hashtable<String, Object> actionSetSheetFormat(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 1);
		String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
		String formatFilename = checkStringParameter(input, PARAM_FILE, true);

		drawHandler.setSheetFormat(drawing, sheet, dirname, formatFilename, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionTableList(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		List<DrawingTableData> tables = drawHandler.tableList(drawing, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
		Vector<Map<String, Object>> outTables = new Vector<Map<String, Object>>();
		out.put(OUTPUT_TABLES, outTables);
		if (tables!=null) {
			for (DrawingTableData table : tables) {
				outTables.add(writeTable(table));
			}
		}
		return out;
	}

	private Hashtable<String, Object> actionTableGetInfo(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);

		return writeTable(drawHandler.tableGetInfo(drawing, tableIndex, sessionId));
	}

	private Hashtable<String, Object> actionTableGetCell(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int row = checkAliasIntParameter(input, PARAM_ROW, null, true, 0).intValue();
		int column = checkAliasIntParameter(input, PARAM_COLUMN, null, true, 0).intValue();

		DrawingTableCellData cell = drawHandler.tableGetCell(drawing, tableIndex, row, column, sessionId);
		Hashtable<String, Object> out = writeCell(cell);
		if (cell!=null)
			out.put(OUTPUT_VALUE, cell.getText());
		return out;
	}

	private Hashtable<String, Object> actionTableSetCell(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int row = checkAliasIntParameter(input, PARAM_ROW, null, true, 0).intValue();
		int column = checkAliasIntParameter(input, PARAM_COLUMN, null, true, 0).intValue();
		String value = checkAliasStringParameter(input, PARAM_VALUE, OUTPUT_TEXT, true);

		drawHandler.tableSetCell(drawing, tableIndex, row, column, value, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableGetRange(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int startRow = checkAliasIntParameter(input, PARAM_START_ROW, "start_row", true, 0).intValue();
		int startColumn = checkAliasIntParameter(input, PARAM_START_COLUMN, "start_column", true, 0).intValue();
		int endRow = checkAliasIntParameter(input, PARAM_END_ROW, "end_row", true, 0).intValue();
		int endColumn = checkAliasIntParameter(input, PARAM_END_COLUMN, "end_column", true, 0).intValue();

		List<List<String>> values = drawHandler.tableGetRange(drawing, tableIndex, startRow, startColumn, endRow, endColumn, sessionId);
		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (values!=null)
			out.put(OUTPUT_VALUES, values);
		return out;
	}

	private Hashtable<String, Object> actionTableSetRange(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int startRow = checkAliasIntParameter(input, PARAM_START_ROW, "start_row", true, 0).intValue();
		int startColumn = checkAliasIntParameter(input, PARAM_START_COLUMN, "start_column", true, 0).intValue();
		List<List<String>> values = readStringMatrix(checkAliasParameter(input, PARAM_VALUES, null, true));

		drawHandler.tableSetRange(drawing, tableIndex, startRow, startColumn, values, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableInsertRows(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int atRow = checkAliasIntParameter(input, PARAM_AT_ROW, "at_row", true, 0).intValue();
		int count = checkAliasIntParameter(input, PARAM_COUNT, null, true, 0).intValue();
		boolean after = checkBeforeAfter(input);

		drawHandler.tableInsertRows(drawing, tableIndex, atRow, count, after, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableDeleteRows(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		List<Integer> rows = getIntArray(PARAM_ROWS, checkAliasParameter(input, PARAM_ROWS, null, true));

		drawHandler.tableDeleteRows(drawing, tableIndex, rows, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableInsertColumns(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int atColumn = checkAliasIntParameter(input, PARAM_AT_COLUMN, "at_column", true, 0).intValue();
		int count = checkAliasIntParameter(input, PARAM_COUNT, null, true, 0).intValue();
		boolean after = checkBeforeAfter(input);

		drawHandler.tableInsertColumns(drawing, tableIndex, atColumn, count, after, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableDeleteColumns(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		List<Integer> columns = getIntArray(PARAM_COLUMNS, checkAliasParameter(input, PARAM_COLUMNS, null, true));

		drawHandler.tableDeleteColumns(drawing, tableIndex, columns, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableClearCell(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int row = checkAliasIntParameter(input, PARAM_ROW, null, true, 0).intValue();
		int column = checkAliasIntParameter(input, PARAM_COLUMN, null, true, 0).intValue();

		drawHandler.tableClearCell(drawing, tableIndex, row, column, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableClearRange(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int startRow = checkAliasIntParameter(input, PARAM_START_ROW, "start_row", true, 0).intValue();
		int startColumn = checkAliasIntParameter(input, PARAM_START_COLUMN, "start_column", true, 0).intValue();
		int endRow = checkAliasIntParameter(input, PARAM_END_ROW, "end_row", true, 0).intValue();
		int endColumn = checkAliasIntParameter(input, PARAM_END_COLUMN, "end_column", true, 0).intValue();

		drawHandler.tableClearRange(drawing, tableIndex, startRow, startColumn, endRow, endColumn, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableMove(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		JLPoint point = readPoint(checkAliasMapParameter(input, PARAM_POINT, PARAM_POSITION, true));

		drawHandler.tableMove(drawing, tableIndex, point, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableSetColumnWidth(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int column = checkAliasIntParameter(input, PARAM_COLUMN, null, true, 0).intValue();
		double width = checkAliasDoubleParameter(input, PARAM_WIDTH, null, true).doubleValue();

		drawHandler.tableSetColumnWidth(drawing, tableIndex, column, width, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableSetRowHeight(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int row = checkAliasIntParameter(input, PARAM_ROW, null, true, 0).intValue();
		double height = checkAliasDoubleParameter(input, PARAM_HEIGHT, null, true).doubleValue();

		drawHandler.tableSetRowHeight(drawing, tableIndex, row, height, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableMergeCells(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int startRow = checkAliasIntParameter(input, PARAM_START_ROW, "start_row", true, 0).intValue();
		int startColumn = checkAliasIntParameter(input, PARAM_START_COLUMN, "start_column", true, 0).intValue();
		int endRow = checkAliasIntParameter(input, PARAM_END_ROW, "end_row", true, 0).intValue();
		int endColumn = checkAliasIntParameter(input, PARAM_END_COLUMN, "end_column", true, 0).intValue();

		drawHandler.tableMergeCells(drawing, tableIndex, startRow, startColumn, endRow, endColumn, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableUnmergeCells(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);
		int startRow = checkAliasIntParameter(input, PARAM_START_ROW, "start_row", true, 0).intValue();
		int startColumn = checkAliasIntParameter(input, PARAM_START_COLUMN, "start_column", true, 0).intValue();
		int endRow = checkAliasIntParameter(input, PARAM_END_ROW, "end_row", true, 0).intValue();
		int endColumn = checkAliasIntParameter(input, PARAM_END_COLUMN, "end_column", true, 0).intValue();

		drawHandler.tableUnmergeCells(drawing, tableIndex, startRow, startColumn, endRow, endColumn, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableCreate(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int rows = checkAliasIntParameter(input, PARAM_ROWS, "row_count", true, 0).intValue();
		int columns = checkAliasIntParameter(input, PARAM_COLUMNS, "column_count", true, 0).intValue();
		Map<String, Object> pointData = checkAliasMapParameter(input, PARAM_POINT, PARAM_POSITION, false);
		JLPoint point = pointData!=null ? readPoint(pointData) : null;
		Object valuesObj = checkAliasParameter(input, PARAM_VALUES, null, false);
		List<List<String>> values = valuesObj!=null ? readStringMatrix(valuesObj) : null;

		drawHandler.tableCreate(drawing, rows, columns, point, values, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableDelete(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int tableIndex = checkTableIndex(input);

		drawHandler.tableDelete(drawing, tableIndex, sessionId);
		return null;
	}

	private Hashtable<String, Object> actionTableRegenerate(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);

		drawHandler.tableRegenerate(drawing, sessionId);
		return null;
	}

	private int checkTableIndex(Hashtable<String, Object> input) throws JLIException {
		return checkAliasIntParameter(input, PARAM_TABLE_INDEX, "table_index", true, 0).intValue();
	}

	private boolean checkBeforeAfter(Hashtable<String, Object> input) throws JLIException {
		String value = checkAliasStringParameter(input, PARAM_POSITION, null, false);
		if (value==null)
			value = "after";
		if (value.equalsIgnoreCase("after"))
			return true;
		if (value.equalsIgnoreCase("before"))
			return false;
		throw new JLIException("Invalid position value: " + value + ". Must be 'before' or 'after'");
	}

	private Object checkAliasParameter(Map<String, Object> input, String name, String alias, boolean required) throws JLIException {
		Object value = null;
		if (input!=null) {
			value = input.get(name);
			if (value==null && alias!=null)
				value = input.get(alias);
		}
		if (value==null && required)
			throw new JLIException("No '" + name + "' parameter given");
		return value;
	}

	private String checkAliasStringParameter(Map<String, Object> input, String name, String alias, boolean required) throws JLIException {
		Object value = checkAliasParameter(input, name, alias, required);
		if (value==null)
			return null;
		Hashtable<String, Object> tmp = new Hashtable<String, Object>();
		tmp.put(name, value);
		return checkStringParameter(tmp, name, required);
	}

	private Integer checkAliasIntParameter(Map<String, Object> input, String name, String alias, boolean required, Integer dflt) throws JLIException {
		Object value = checkAliasParameter(input, name, alias, required);
		if (value==null)
			return dflt;
		Hashtable<String, Object> tmp = new Hashtable<String, Object>();
		tmp.put(name, value);
		return checkIntParameter(tmp, name, required, dflt);
	}

	private Double checkAliasDoubleParameter(Map<String, Object> input, String name, String alias, boolean required) throws JLIException {
		Object value = checkAliasParameter(input, name, alias, required);
		if (value==null)
			return null;
		Hashtable<String, Object> tmp = new Hashtable<String, Object>();
		tmp.put(name, value);
		return checkDoubleParameter(tmp, name, required);
	}

	private Map<String, Object> checkAliasMapParameter(Map<String, Object> input, String name, String alias, boolean required) throws JLIException {
		Object value = checkAliasParameter(input, name, alias, required);
		if (value==null)
			return null;
		Hashtable<String, Object> tmp = new Hashtable<String, Object>();
		tmp.put(name, value);
		return checkMapParameter(tmp, name, required);
	}

	private List<List<String>> readStringMatrix(Object value) throws JLIException {
		if (!(value instanceof List<?>))
			throw new JLIException("Invalid values parameter; expected an array of arrays");
		List<?> inputRows = (List<?>)value;
		if (inputRows.size()==0)
			throw new JLIException("No values parameter given");
		List<List<String>> out = new Vector<List<String>>();
		int columns = -1;
		for (int i=0; i<inputRows.size(); i++) {
			Object rowObj = inputRows.get(i);
			if (!(rowObj instanceof List<?>))
				throw new JLIException("Invalid values row " + i + "; expected an array");
			List<?> inputCols = (List<?>)rowObj;
			if (inputCols.size()==0)
				throw new JLIException("No values given for row " + i);
			if (columns<0)
				columns = inputCols.size();
			else if (inputCols.size()!=columns)
				throw new JLIException("All rows in values must have the same number of columns");
			List<String> outRow = new Vector<String>();
			for (int j=0; j<inputCols.size(); j++) {
				Object cell = inputCols.get(j);
				outRow.add(cell==null ? "" : cell.toString());
			}
			out.add(outRow);
		}
		return out;
	}

	private Hashtable<String, Object> writeTable(DrawingTableData table) {
		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (table==null)
			return out;
		out.put(OUTPUT_TABLE_INDEX, table.getTableIndex());
		out.put(OUTPUT_ROWS, table.getRows());
		out.put(OUTPUT_COLUMNS, table.getColumns());
		if (table.getPosition()!=null)
			out.put(OUTPUT_LOCATION, writePoint(table.getPosition()));
		if (table.getRowHeights()!=null)
			out.put(OUTPUT_ROW_HEIGHTS, table.getRowHeights());
		if (table.getColumnWidths()!=null)
			out.put(OUTPUT_COLUMN_WIDTHS, table.getColumnWidths());
		if (table.getCells()!=null) {
			Vector<Map<String, Object>> cells = new Vector<Map<String, Object>>();
			out.put(OUTPUT_CELLS, cells);
			for (DrawingTableCellData cell : table.getCells()) {
				cells.add(writeCell(cell));
			}
		}
		return out;
	}

	private Hashtable<String, Object> writeCell(DrawingTableCellData cell) {
		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (cell==null)
			return out;
		out.put(OUTPUT_ROW, cell.getRow());
		out.put(OUTPUT_COLUMN, cell.getColumn());
		out.put(OUTPUT_TEXT, cell.getText()==null ? "" : cell.getText());
		return out;
	}


	private ViewDisplayData makeDisplayData(Map<String, Object> displayData) throws JLIException {
		if (displayData==null)
			return null;
		ViewDisplayData vdd = new ViewDisplayData();
		String cableStyle = checkStringParameter(displayData, PARAM_CABLE_STYLE, false);
		if (cableStyle!=null) {
			if (!cableStyle.equalsIgnoreCase(ViewDisplayData.CABLESTYLE_CENTERLINE) &&
				!cableStyle.equalsIgnoreCase(ViewDisplayData.CABLESTYLE_DEFAULT) &&
				!cableStyle.equalsIgnoreCase(ViewDisplayData.CABLESTYLE_THICK))
				throw new JLIException("Invalid cable style: " + cableStyle);
			vdd.setCableStyle(cableStyle);
		}
		String style = checkStringParameter(displayData, PARAM_STYLE, false);
		if (style!=null) {
			if (!style.equalsIgnoreCase(ViewDisplayData.STYLE_DEFAULT) &&
				!style.equalsIgnoreCase(ViewDisplayData.STYLE_FOLLOW_ENV) &&
				!style.equalsIgnoreCase(ViewDisplayData.STYLE_HIDDEN_LINE) &&
				!style.equalsIgnoreCase(ViewDisplayData.STYLE_NO_HIDDEN) &&
				!style.equalsIgnoreCase(ViewDisplayData.STYLE_SHADED) &&
				!style.equalsIgnoreCase(ViewDisplayData.STYLE_WIREFRAME))
				throw new JLIException("Invalid style: " + style);
			vdd.setStyle(style);
		}
		String tangentStyle = checkStringParameter(displayData, PARAM_TANGENT_STYLE, false);
		if (tangentStyle!=null) {
			if (!tangentStyle.equalsIgnoreCase(ViewDisplayData.TANGENT_CENTERLINE) &&
				!tangentStyle.equalsIgnoreCase(ViewDisplayData.TANGENT_DEFAULT) &&
				!tangentStyle.equalsIgnoreCase(ViewDisplayData.TANGENT_DIMMED) &&
				!tangentStyle.equalsIgnoreCase(ViewDisplayData.TANGENT_NONE) &&
				!tangentStyle.equalsIgnoreCase(ViewDisplayData.TANGENT_PHANTOM) &&
				!tangentStyle.equalsIgnoreCase(ViewDisplayData.TANGENT_SOLID))
				throw new JLIException("Invalid tangent style: " + tangentStyle);
			vdd.setTangentStyle(tangentStyle);
		}
		vdd.setRemoveQuiltHiddenLines(checkFlagParameter(displayData, PARAM_REMOVE_QUILT_HIDDEN_LINES, false, false));
		vdd.setShowConceptModel(checkFlagParameter(displayData, PARAM_SHOW_CONCEPT_MODEL, false, false));
		vdd.setShowWeldXSection(checkFlagParameter(displayData, PARAM_SHOW_WELD_XSECTION, false, false));

		return vdd;
	}
}
