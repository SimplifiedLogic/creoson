/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
		
		if (result!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
    		out.put(OUTPUT_DRAWING, result);
    		return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionListModels(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String model = checkStringParameter(input, PARAM_MODEL, false);
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		
		List<String> models = drawHandler.listModels(drawing, model, sessionId);
		
		if (models!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
    		out.put(OUTPUT_MODELS, models);
    		return out;
		}
		return null;
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
		
		if (result!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
    		out.put(OUTPUT_MODEL, result);
    		return out;
		}
		return null;
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
		
		if (size!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_SIZE, size);
			return out;
		}
		return null;
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
		Double scaleObj = checkDoubleParameter(input, PARAM_SCALE, true);
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
		
		if (views!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
    		out.put(OUTPUT_VIEWS, views);
    		return out;
		}
		return null;
	}
	
	private Hashtable<String, Object> actionListViewDetails(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, false);
		
		List<ViewDetailData> views = drawHandler.listViewDetails(drawing, view, sessionId);
		
		if (views!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
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
				
				outViews.add(outView);
			}
    		return out;
		}
		return null;
	}
	
	private Hashtable<String, Object> actionGetViewLoc(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String view = checkStringParameter(input, PARAM_VIEW, true);
		
		JLPoint pt = drawHandler.getViewLoc(drawing, view, sessionId);
		
		if (pt!=null) {
			Hashtable<String, Object> out = writePoint(pt);
			return out;
		}
		return null;
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
		
		if (result!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			if (result.getFailedViews()!=null && result.getFailedViews().size()>0)
				out.put(OUTPUT_FAILED_VIEWS, result.getFailedViews());
			if (result.getSuccessViews()!=null && result.getSuccessViews().size()>0)
				out.put(OUTPUT_SUCCESS_VIEWS, result.getSuccessViews());
			return out;
		}
		return null;
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
        
        if (box!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_XMIN, box.getXmin());
			out.put(OUTPUT_XMAX, box.getXmax());
			out.put(OUTPUT_YMIN, box.getYmin());
			out.put(OUTPUT_YMAX, box.getYmax());
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionLoadSymbolDef(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String symbolDir = checkStringParameter(input, PARAM_SYMBOL_DIR, false);
		String symbolFile = checkStringParameter(input, PARAM_SYMBOL_FILE, true);
		
		SymbolDefData symbolData = drawHandler.loadSymbolDef(drawing, symbolDir, symbolFile, sessionId);
		
		if (symbolData!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_ID, symbolData.getId());
			out.put(OUTPUT_NAME, symbolData.getName());
    		return out;
		}
		return null;
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
		Map<String, Object> replaceValues = checkMapParameter(input, PARAM_REPLACE_VALUES, true);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);
		
		drawHandler.createSymbol(drawing, symbolFile, pt, replaceValues, sheet, sessionId);
		
   		return null;
	}
	
	private Hashtable<String, Object> actionListSymbols(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		String symbolFile = checkStringParameter(input, PARAM_SYMBOL_FILE, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, false, 0);
		
		List<SymbolInstData> symbols = drawHandler.listSymbols(drawing, symbolFile, sheet, sessionId);
		
		if (symbols!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
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
    		return out;
		}
   		return null;
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
		
		if (result!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			if (result.getFileName()!=null)
				out.put(OUTPUT_MODEL, result.getFileName());
			if (result.getFullName()!=null)
				out.put(OUTPUT_FULLNAME, result.getFullName());
			if (result.getCommonName()!=null)
				out.put(OUTPUT_COMMONNAME, result.getCommonName());
    		return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionSetSheetFormat(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String drawing = checkStringParameter(input, PARAM_DRAWING, false);
		int sheet = checkIntParameter(input, PARAM_SHEET, true, 1);
		String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
		String formatFilename = checkStringParameter(input, PARAM_FILE, true);
		
		drawHandler.setSheetFormat(drawing, sheet, dirname, formatFilename, sessionId);
		
		return null;
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
