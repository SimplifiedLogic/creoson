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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.data.SymbolInstData;
import com.simplifiedlogic.nitro.jlink.data.ViewDisplayData;
import com.simplifiedlogic.nitro.jshell.json.request.JLDrawingRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLFileRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLDrawingResponseParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFileResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "drawing" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonDrawingHelp extends JLJsonCommandHelp implements JLDrawingRequestParams, JLDrawingResponseParams {

	public static final String OBJ_VIEW_DISPLAY_DATA = "ViewDisplayData";
	public static final String OBJ_VIEW_DETAIL_DATA = "ViewDetailData";
	public static final String OBJ_SYMBOL_INST_DATA = "SymbolInstData";
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getCommand()
	 */
	public String getCommand() {
		return COMMAND;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpAddModel());
		list.add(helpAddSheet());
		list.add(helpCreate());
		list.add(helpCreateGeneralView());
		list.add(helpCreateProjectionView());
		list.add(helpCreateSymbol());
		list.add(helpDeleteModels());
		list.add(helpDeleteSymbolDef());
		list.add(helpDeleteSymbolInst());
		list.add(helpDeleteSheet());
		list.add(helpDeleteView());
		list.add(helpGetCurModel());
		list.add(helpGetCurSheet());
		list.add(helpGetNumSheets());
		list.add(helpGetSheetFormat());
		list.add(helpGetSheetScale());
		list.add(helpGetSheetSize());
		list.add(helpGetViewLoc());
		list.add(helpGetViewScale());
		list.add(helpGetViewSheet());
		list.add(helpIsSymbolLoaded());
		list.add(helpListModels());
		list.add(helpListSymbols());
		list.add(helpListViewDetails());
		list.add(helpListViews());
		list.add(helpLoadSymbolDef());
		list.add(helpRegenerate());
		list.add(helpRegenerateSheet());
		list.add(helpRenameView());
		list.add(helpScaleSheet());
		list.add(helpScaleView());
		list.add(helpSelectSheet());
		list.add(helpSetCurModel());
		list.add(helpSetSheetFormat());
		list.add(helpSetViewLoc());
		list.add(helpTableList());
		list.add(helpTableGetInfo());
		list.add(helpTableGetCell());
		list.add(helpTableSetCell());
		list.add(helpTableGetRange());
		list.add(helpTableSetRange());
		list.add(helpTableInsertRows());
		list.add(helpTableDeleteRows());
		list.add(helpTableInsertColumns());
		list.add(helpTableDeleteColumns());
		list.add(helpTableClearCell());
		list.add(helpTableClearRange());
		list.add(helpTableMove());
		list.add(helpTableSetColumnWidth());
		list.add(helpTableSetRowHeight());
		list.add(helpTableMergeCells());
		list.add(helpTableUnmergeCells());
		list.add(helpTableCreate());
		list.add(helpTableDelete());
		list.add(helpTableRegenerate());
		list.add(helpViewBoundingBox());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpSymbolInstData());
		list.add(helpViewDisplayData());
		list.add(helpViewDetailData());
		return list;
	}
	
	private FunctionTemplate helpCreate() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CREATE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Create a new drawing from a template");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setDefaultValue("Current active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New drawing name");
    	arg.setDefaultValue("A name derived from the model's instance name");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TEMPLATE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Template");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SCALE, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Drawing scale");
    	arg.setDefaultValue("1.0");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DISPLAY, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Display the drawing after opening");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ACTIVATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Activate the drawing window after opening");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NEWWIN, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Open drawing in a new window");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DRAWING, FunctionSpec.TYPE_STRING);
    	ret.setDescription("New drawing name");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DRAWING, "box-test.drw");
    	ex.addInput(PARAM_TEMPLATE, "main_template");
    	ex.addInput(PARAM_SCALE, 0.5);
    	ex.addInput(PARAM_DISPLAY, true);
    	ex.addInput(PARAM_ACTIVATE, true);
    	ex.addInput(PARAM_NEWWIN, true);
    	ex.addOutput(OUTPUT_DRAWING, "box-test.drw");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "thick_bracket<bracket>.prt");
    	ex.addInput(PARAM_TEMPLATE, "main_template");
    	ex.addInput(PARAM_DISPLAY, true);
    	ex.addInput(PARAM_ACTIVATE, true);
    	ex.addOutput(OUTPUT_DRAWING, "thick_bracket.drw");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_TEMPLATE, "main_template");
    	ex.addInput(PARAM_SCALE, 0.5);
    	ex.addOutput(OUTPUT_DRAWING, "box.drw");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpListModels() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_MODELS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List the models contained in a drawing");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of model names in the drawing");
    	spec.addReturn(ret);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addOutput(OUTPUT_MODELS, new String[] {"box.prt","screw.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_MODELS, new String[] {"box.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*w*");
    	ex.addOutput(OUTPUT_MODELS, new String[] {"screw.prt"});
    	template.addExample(ex);
    	
    	return template;
	}

	private FunctionTemplate helpAddModel() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_ADD_MODEL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Add a model to a drawing");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_MODEL, "screw.prt");
    	template.addExample(ex);
    	
    	return template;
	}

	private FunctionTemplate helpDeleteModels() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_MODELS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete one or more models from a drawing");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All models will be deleted from the drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DELETE_VIEWS, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to delete drawing views associated with the model");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_MODEL, "screw.prt");
    	ex.addInput(PARAM_DELETE_VIEWS, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*w*");
    	template.addExample(ex);
    	
    	return template;
	}

	private FunctionTemplate helpGetCurModel() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_CUR_MODEL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the active model on a drawing");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MODEL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Model name");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addOutput(OUTPUT_MODEL, "box.prt");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_MODEL, "screw.prt");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSetCurModel() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_CUR_MODEL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the active model on a drawing");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_MODEL, "screw.prt");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);
    	
    	return template;
	}

	private FunctionTemplate helpRegenerate() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_REGENERATE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Regenerate a drawing");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpRegenerateSheet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_REGENERATE_SHEET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Regenerate a sheet on a drawing");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number (0 for all sheets)");
    	arg.setDefaultValue("All sheets will be regenerated");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SHEET, 2);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSelectSheet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SELECT_SHEET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Make a drawing sheet the current sheet");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SHEET, 2);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpAddSheet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_ADD_SHEET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Add a drawing sheet");
    	spec.addFootnote("Position is a value between 1 and the number of sheets plus one.  Set to 0 to add the sheet last.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_POSITION, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Position to add the sheet");
    	arg.setDefaultValue("Sheet will be added to the end");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_POSITION, 1);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpDeleteSheet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_SHEET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a drawing sheet");
    	spec.addFootnote("An error will occur if you try to delete the only sheet in a drawing.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SHEET, 2);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetCurSheet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_CUR_SHEET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the current drawing sheet");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SHEET, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Sheet number");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addOutput(OUTPUT_SHEET, 1);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_SHEET, 2);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetNumSheets() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_NUM_SHEETS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the number of sheets on a drawing");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_NUM_SHEETS, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Number of sheets");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addOutput(OUTPUT_NUM_SHEETS, 2);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_NUM_SHEETS, 3);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpScaleSheet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SCALE_SHEET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the scale of a drawing sheet");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet Number");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SCALE, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("View scale");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing model to scale");
    	arg.setDefaultValue("The active model on the drawing");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_SCALE, 0.5);
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 2);
    	ex.addInput(PARAM_SCALE, 1.0);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_SCALE, 0.5);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetSheetScale() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_SHEET_SCALE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the scale of a drawing sheet");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing model used to calculate the scale");
    	arg.setDefaultValue("The active model on the drawing");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SCALE, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Sheet scale");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_SCALE, 0.5);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 2);
    	ex.addOutput(OUTPUT_SCALE, 1.0);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addOutput(OUTPUT_SCALE, 0.5);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetSheetSize() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_SHEET_SIZE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the size of a drawing sheet");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SIZE, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Sheet size");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addOutput(OUTPUT_SIZE, "A4");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addOutput(OUTPUT_SIZE, "F");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpCreateGeneralView() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CREATE_GEN_VIEW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Create general view on a drawing");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New view name");
    	arg.setDefaultValue("The " + PARAM_MODEL_VIEW + " parameter");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setDefaultValue("Current active sheet on the drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model for the view");
    	arg.setDefaultValue("Current active model on the drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model view to use for the drawing view orientation");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_POINT, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Coordinates for the view in Drawing Units");
    	arg.setRequired(true);
    	spec.addArgument(arg);
        
    	arg = new FunctionArgument(PARAM_SCALE, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("View scale");
    	arg.setDefaultValue("The sheet's scale");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DISPLAY_DATA, FunctionSpec.TYPE_OBJECT, OBJ_VIEW_DISPLAY_DATA);
    	arg.setDescription("Display parameters used to create the view");
    	arg.setDefaultValue("Creo defaults");
    	spec.addArgument(arg);
        
    	arg = new FunctionArgument(PARAM_EXPLODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to create the view as an exploded view");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "RIGHT_TEST");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MODEL_VIEW, "RIGHT");
    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 10.0);
    	rec.put(JLFileRequestParams.PARAM_Y, 3.25);
    	ex.addInput(PARAM_POINT, rec);
    	ex.addInput(PARAM_SCALE, 0.25);
    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_CABLE_STYLE, ViewDisplayData.CABLESTYLE_DEFAULT);
    	rec.put(PARAM_STYLE, ViewDisplayData.STYLE_HIDDEN_LINE);
    	rec.put(PARAM_TANGENT_STYLE, ViewDisplayData.TANGENT_NONE);
    	rec.put(PARAM_REMOVE_QUILT_HIDDEN_LINES, true);
    	rec.put(PARAM_SHOW_CONCEPT_MODEL, false);
    	rec.put(PARAM_SHOW_WELD_XSECTION, false);
    	ex.addInput(PARAM_DISPLAY_DATA, rec);
    	ex.addInput(PARAM_EXPLODED, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_MODEL_VIEW, "RIGHT");
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 10.0);
    	rec.put(JLFileRequestParams.PARAM_Y, 3.25);
    	ex.addInput(PARAM_POINT, rec);
    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_STYLE, ViewDisplayData.STYLE_HIDDEN_LINE);
    	rec.put(PARAM_TANGENT_STYLE, ViewDisplayData.TANGENT_NONE);
    	ex.addInput(PARAM_DISPLAY_DATA, rec);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL_VIEW, "RIGHT");
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 10.0);
    	rec.put(JLFileRequestParams.PARAM_Y, 3.25);
    	ex.addInput(PARAM_POINT, rec);
    	template.addExample(ex);
    	
    	return template;
	}

	private FunctionTemplate helpCreateProjectionView() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CREATE_PROJ_VIEW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Create projection view on a drawing");
    	spec.addFootnote("When specifying the view coordinates, you should specify only an X or a Y coordinate to avoid confusion.  If you specify both coordinates, it appears Creo may be using whichever has the larger absolute value.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New view name");
    	arg.setDefaultValue("Creo's default name for a new view");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setDefaultValue("Current active sheet on the drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PARENT_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parent view for the projection view");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_POINT, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Coordinates for the view, relative to the location of the parent view, in Drawing Units");
    	arg.setRequired(true);
    	spec.addArgument(arg);
        
    	arg = new FunctionArgument(PARAM_DISPLAY_DATA, FunctionSpec.TYPE_OBJECT, OBJ_VIEW_DISPLAY_DATA);
    	arg.setDescription("Display parameters used to create the view");
    	arg.setDefaultValue("The display parameters of the parent view");
    	spec.addArgument(arg);
        
    	arg = new FunctionArgument(PARAM_EXPLODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to create the view as an exploded view");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "RIGHT_TEST");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_PARENT_VIEW, "FRONT");
    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 10.0);
    	ex.addInput(PARAM_POINT, rec);
    	ex.addInput(PARAM_SCALE, 0.25);
    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_CABLE_STYLE, ViewDisplayData.CABLESTYLE_DEFAULT);
    	rec.put(PARAM_STYLE, ViewDisplayData.STYLE_HIDDEN_LINE);
    	rec.put(PARAM_TANGENT_STYLE, ViewDisplayData.TANGENT_NONE);
    	rec.put(PARAM_REMOVE_QUILT_HIDDEN_LINES, true);
    	rec.put(PARAM_SHOW_CONCEPT_MODEL, false);
    	rec.put(PARAM_SHOW_WELD_XSECTION, false);
    	ex.addInput(PARAM_DISPLAY_DATA, rec);
    	ex.addInput(PARAM_EXPLODED, false);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_PARENT_VIEW, "FRONT");
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 10.0);
    	ex.addInput(PARAM_POINT, rec);
    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_STYLE, ViewDisplayData.STYLE_HIDDEN_LINE);
    	rec.put(PARAM_TANGENT_STYLE, ViewDisplayData.TANGENT_NONE);
    	ex.addInput(PARAM_DISPLAY_DATA, rec);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_VIEW, "TOP");
    	ex.addInput(PARAM_PARENT_VIEW, "FRONT");
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_Y, 3.25);
    	ex.addInput(PARAM_POINT, rec);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_PARENT_VIEW, "LEFT");
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_Y, 3.25);
    	ex.addInput(PARAM_POINT, rec);
    	ex.createError("Parent view does not exist in drawing: LEFT");
    	template.addExample(ex);
    	
    	return template;
	}

	private FunctionTemplate helpListViews() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_VIEWS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List the views contained in a drawing");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_VIEWS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of views in the drawing");
    	spec.addReturn(ret);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addOutput(OUTPUT_VIEWS, new String[] {"FRONT","RIGHT","TOP"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_MODEL, "RIGHT");
    	ex.addOutput(OUTPUT_VIEWS, new String[] {"RIGHT"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*o*");
    	ex.addOutput(OUTPUT_VIEWS, new String[] {"FRONT","TOP"});
    	template.addExample(ex);
    	
    	return template;
	}

	private FunctionTemplate helpListViewDetails() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_VIEW_DETAILS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List the views contained in a drawing, with more details");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_VIEWS, FunctionSpec.TYPE_OBJARRAY, OBJ_VIEW_DETAIL_DATA);
    	ret.setDescription("List of views in the drawing");
    	spec.addReturn(ret);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.drw");
    	Map<String, Object> rec;
    	List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "FRONT");
		rec.put(OUTPUT_SHEET, 1);
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(2.5, 4.0, 0.0));
		rec.put(OUTPUT_TEXT_HEIGHT, 0.2);
		rec.put(OUTPUT_VIEW_MODEL, "box.prt");
		rec.put(OUTPUT_SIMPREP, "Master Rep");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "RIGHT");
		rec.put(OUTPUT_SHEET, 1);
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(6.0, 4.0, 0.0));
		rec.put(OUTPUT_TEXT_HEIGHT, 0.2);
		rec.put(OUTPUT_VIEW_MODEL, "box.prt");
		rec.put(OUTPUT_SIMPREP, "Master Rep");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "TOP");
		rec.put(OUTPUT_SHEET, 1);
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(2.5, 7.2, 0.0));
		rec.put(OUTPUT_TEXT_HEIGHT, 0.2);
		rec.put(OUTPUT_VIEW_MODEL, "box.prt");
		rec.put(OUTPUT_SIMPREP, "Master Rep");
		ex.addOutput(OUTPUT_VIEWS, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.drw");
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "RIGHT");
		rec.put(OUTPUT_SHEET, 1);
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(6.0, 4.0, 0.0));
		rec.put(OUTPUT_TEXT_HEIGHT, 0.2);
		rec.put(OUTPUT_VIEW_MODEL, "box.prt");
		rec.put(OUTPUT_SIMPREP, "Master Rep");
		ex.addOutput(OUTPUT_VIEWS, params);
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpGetViewLoc() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_VIEW_LOC);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the location of a drawing view");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(JLFileResponseParams.OUTPUT_X, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("X-coordinate of the view");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(JLFileResponseParams.OUTPUT_Y, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Y-coordinate of the view");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(JLFileResponseParams.OUTPUT_Z, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Z-coordinate of the view");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addOutput(JLFileResponseParams.OUTPUT_X, 2.5);
    	ex.addOutput(JLFileResponseParams.OUTPUT_Y, 4.0);
    	ex.addOutput(JLFileResponseParams.OUTPUT_Z, 0.0);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSetViewLoc() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_VIEW_LOC);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the location of a drawing view");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_POINT, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Coordinates for the view in Drawing Units");
    	arg.setRequired(true);
    	spec.addArgument(arg);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 2.5);
    	rec.put(JLFileRequestParams.PARAM_Y, 4.0);
    	ex.addInput(PARAM_POINT, rec);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_VIEW, "RIGHT");
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 13.0);
    	rec.put(JLFileRequestParams.PARAM_Y, 4.0);
    	ex.addInput(PARAM_POINT, rec);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpDeleteView() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_VIEW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a drawing view");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number; if filled in, the view will only be deleted if it is on that sheet");
    	arg.setDefaultValue("Delete the view from any sheet");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DEL_CHILDREN, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to also delete any children of the view");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_DEL_CHILDREN, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "RIGHT");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpRenameView() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_RENAME_VIEW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Rename a drawing view");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Old view name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NEWVIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New view name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addInput(PARAM_NEWVIEW, "FRONT_test");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpScaleView() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SCALE_VIEW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the scale of one or more drawing views");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All views will be scaled");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SCALE, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("View scale");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SUCCESS_VIEWS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of view which were successfully scaled");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FAILED_VIEWS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of view which failed to scale");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addInput(PARAM_SCALE, 0.5);
    	ex.addOutput(OUTPUT_SUCCESS_VIEWS, new String[] {"FRONT"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SCALE, 0.5);
    	ex.addOutput(OUTPUT_SUCCESS_VIEWS, new String[] {"FRONT"});
    	ex.addOutput(OUTPUT_FAILED_VIEWS, new String[] {"RIGHT","TOP"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "*O*");
    	ex.addInput(PARAM_SCALE, 0.5);
    	ex.addOutput(OUTPUT_SUCCESS_VIEWS, new String[] {"FRONT"});
    	ex.addOutput(OUTPUT_FAILED_VIEWS, new String[] {"TOP"});
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetViewScale() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_VIEW_SCALE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the scale of a drawing view");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SCALE, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("View scale");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addOutput(OUTPUT_SCALE, 0.5);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "RIGHT");
    	ex.addOutput(OUTPUT_SCALE, 1.0);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addOutput(OUTPUT_SCALE, 0.5);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetViewSheet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_VIEW_SHEET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the sheet number that contains a drawing view");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SHEET, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Sheet number");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addOutput(OUTPUT_SHEET, 1);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "RIGHT");
    	ex.addOutput(OUTPUT_SHEET, 2);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addOutput(OUTPUT_SHEET, 1);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionObject helpViewDisplayData() {
    	FunctionObject obj = new FunctionObject(OBJ_VIEW_DISPLAY_DATA);
    	obj.setDescription("Creo display parameters used to create drawing views");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_CABLE_STYLE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Cable Style");
    	arg.setValidValues(new String[] {
        		ViewDisplayData.CABLESTYLE_DEFAULT,
        		ViewDisplayData.CABLESTYLE_CENTERLINE,
        		ViewDisplayData.CABLESTYLE_THICK
    	});
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_REMOVE_QUILT_HIDDEN_LINES, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to remove quilt hidden lines");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_SHOW_CONCEPT_MODEL, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to show the concept model");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_SHOW_WELD_XSECTION, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to show weld cross-section");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_STYLE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Style");
    	arg.setValidValues(new String[] {
        		ViewDisplayData.STYLE_DEFAULT,
        		ViewDisplayData.STYLE_FOLLOW_ENV,
        		ViewDisplayData.STYLE_HIDDEN_LINE,
        		ViewDisplayData.STYLE_NO_HIDDEN,
        		ViewDisplayData.STYLE_SHADED,
        		ViewDisplayData.STYLE_WIREFRAME
    	});
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_TANGENT_STYLE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Tangent Style");
    	arg.setValidValues(new String[] {
        		ViewDisplayData.TANGENT_DEFAULT,
        		ViewDisplayData.TANGENT_CENTERLINE,
        		ViewDisplayData.TANGENT_DIMMED,
        		ViewDisplayData.TANGENT_NONE,
        		ViewDisplayData.TANGENT_PHANTOM,
        		ViewDisplayData.TANGENT_SOLID
    	});
    	obj.add(arg);

        return obj;
    }
    
	private FunctionObject helpViewDetailData() {
    	FunctionObject obj = new FunctionObject(OBJ_VIEW_DETAIL_DATA);
    	obj.setDescription("Information about a drawing view");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_VIEW_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet Number");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_LOCATION, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("View Location in Drawing Units");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TEXT_HEIGHT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Text Height in Drawing Units");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_VIEW_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View model name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_SIMPREP, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View simplified rep name");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpViewBoundingBox() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_VIEW_BOUND_BOX);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the 2D bounding box for a drawing view");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VIEW, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_XMIN, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Minimum X-coordinate of drawing view");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_XMAX, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Maximum X-coordinate of drawing view");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_YMIN, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Minimum Y-coordinate of drawing view");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_YMAX, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Maximum Y-coordinate of drawing view");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_VIEW, "FRONT");
    	ex.addOutput(OUTPUT_XMIN, 5.0);
    	ex.addOutput(OUTPUT_XMAX, 30.0);
    	ex.addOutput(OUTPUT_YMIN, 12.5);
    	ex.addOutput(OUTPUT_YMAX, 15.0);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpLoadSymbolDef() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LOAD_SYMBOL_DEF);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Load a Creo symbol definition file into Creo from disk");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SYMBOL_DIR, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory containing the symbol file; if relative, assumed to be relative to Creo's current working directory");
    	arg.setRequired(false);
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SYMBOL_FILE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Name of the symbol file");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_ID, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("ID of the loaded symbol");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_NAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Symbol Name of the loaded symbol");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SYMBOL_DIR, "C:/somefiles/parts");
    	ex.addInput(PARAM_SYMBOL_FILE, "my_symbol.sym");
    	ex.addOutput(OUTPUT_ID, 3);
    	ex.addOutput(OUTPUT_NAME, "MY_SYMBOL");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SYMBOL_DIR, "subdir");
    	ex.addInput(PARAM_SYMBOL_FILE, "my_symbol.sym");
    	ex.addOutput(OUTPUT_ID, 3);
    	ex.addOutput(OUTPUT_NAME, "MY_SYMBOL");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpIsSymbolLoaded() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_IS_SYMBOL_DEF_LOADED);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether a symbol definition file is loaded into Creo");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SYMBOL_FILE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Name of the symbol file");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_LOADED, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the symbol definition is loaded into Creo");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SYMBOL_FILE, "my_symbol.sym");
    	ex.addOutput(OUTPUT_LOADED, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpCreateSymbol() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CREATE_SYMBOL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Add a symbol instance to a drawing");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SYMBOL_FILE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Name of the symbol file");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_POINT, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Coordinates for the symbol in Drawing Units");
    	arg.setRequired(true);
    	spec.addArgument(arg);
        
    	arg = new FunctionArgument(PARAM_REPLACE_VALUES, FunctionSpec.TYPE_OBJECT);
    	arg.setDescription("Object containing replacement values for any variable text in the symbol");
    	spec.addArgument(arg);
        
    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number (0 for all sheets)");
    	arg.setDefaultValue("The symbol will be added to all sheets");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SYMBOL_FILE, "my_symbol.sym");
    	Map<String, Object> rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 13.0);
    	rec.put(JLFileRequestParams.PARAM_Y, 4.0);
    	ex.addInput(PARAM_POINT, rec);
    	rec = new OrderedMap<String, Object>();
    	rec.put("TEXT_VALUE", "A3");
    	ex.addInput(PARAM_REPLACE_VALUES, rec);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListSymbols() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_SYMBOLS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List symbols contained on a drawing");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SYMBOL_FILE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Symbol file name filter");
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number (0 for all sheets)");
    	arg.setDefaultValue("The symbol will be added to all sheets");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SYMBOLS, FunctionSpec.TYPE_OBJARRAY, OBJ_SYMBOL_INST_DATA);
    	ret.setDescription("List of symbols in the drawing");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
    	Map<String, Object> rec = new OrderedMap<String, Object>();
    	Map<String, Object> rec2;
    	params.add(rec);
    	rec.put(OUTPUT_ID, 1);
    	rec.put(OUTPUT_SYMBOL_NAME, "MY_SYMBOL");
		rec.put(OUTPUT_SHEET, 1);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(JLFileRequestParams.PARAM_X, 10.0);
    	rec2.put(JLFileRequestParams.PARAM_Y, 3.25);
    	rec2.put(JLFileRequestParams.PARAM_Z, 0.0);
    	rec.put(OUTPUT_LOCATION, rec2);
		rec.put(OUTPUT_ATTACH_TYPE, "free");
		rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_ID, 2);
    	rec.put(OUTPUT_SYMBOL_NAME, "LAST_SYMBOL");
		rec.put(OUTPUT_SHEET, 1);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(JLFileRequestParams.PARAM_X, 30.0);
    	rec2.put(JLFileRequestParams.PARAM_Y, 14.70);
    	rec2.put(JLFileRequestParams.PARAM_Z, 0.0);
    	rec.put(OUTPUT_LOCATION, rec2);
		rec.put(OUTPUT_ATTACH_TYPE, "unknown");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_ID, 3);
    	rec.put(OUTPUT_SYMBOL_NAME, "NOTE_SYMBOL");
		rec.put(OUTPUT_SHEET, 2);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(JLFileRequestParams.PARAM_X, 25.50);
    	rec2.put(JLFileRequestParams.PARAM_Y, 23.0);
    	rec2.put(JLFileRequestParams.PARAM_Z, 0.0);
    	rec.put(OUTPUT_LOCATION, rec2);
		rec.put(OUTPUT_ATTACH_TYPE, "free");
		ex.addOutput(OUTPUT_SYMBOLS, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SYMBOL_FILE, "my_symbol.sym");
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_ID, 1);
    	rec.put(OUTPUT_SYMBOL_NAME, "MY_SYMBOL");
		rec.put(OUTPUT_SHEET, 1);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(JLFileRequestParams.PARAM_X, 10.0);
    	rec2.put(JLFileRequestParams.PARAM_Y, 3.25);
    	rec2.put(JLFileRequestParams.PARAM_Z, 0.0);
    	rec.put(OUTPUT_LOCATION, rec2);
		rec.put(OUTPUT_ATTACH_TYPE, "free");
    	rec = new OrderedMap<String, Object>();
		ex.addOutput(OUTPUT_SYMBOLS, params);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpSymbolInstData() {
    	FunctionObject obj = new FunctionObject(OBJ_SYMBOL_INST_DATA);
    	obj.setDescription("Information about a drawing symbol instance");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Symbol ID");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_SYMBOL_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Symbol definition name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet Number");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_LOCATION, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Location of symbol instance, in drawing coordinates");
    	obj.add(arg);
    	
    	arg = new FunctionArgument(OUTPUT_ATTACH_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Type of attachment location ("+
    			SymbolInstData.ATTACH_TYPE_FREE+","+
    			SymbolInstData.ATTACH_TYPE_OFFSET+","+
    			SymbolInstData.ATTACH_TYPE_PARAMETRIC+","+
    			SymbolInstData.ATTACH_TYPE_UNKNOWN+
    			")");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpDeleteSymbolDef() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_SYMBOL_DEF);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a symbol definition and its instances from a drawing");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SYMBOL_FILE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Name of the symbol file");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SYMBOL_FILE, "my_symbol.sym");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpDeleteSymbolInst() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_SYMBOL_INST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a specific symbol instance from a drawing");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SYMBOL_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("ID of the symbol instance");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SYMBOL_ID, 2);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetSheetFormat() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_SHEET_FORMAT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the drawing format file of a drawing sheet");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MODEL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Format file name, may be null if there is no current format");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FULLNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Format full name");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_COMMONNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Format common name");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addOutput(OUTPUT_MODEL, "engr_fmt.frm");
    	ex.addOutput(OUTPUT_FULLNAME, "ENGR_FMT");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box2.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSetSheetFormat() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_SHEET_FORMAT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the drawing format file of a drawing sheet");
    	spec.addFootnote("The format will use the current drawing model to resolve parameters in the formst.");
    	spec.addFootnote("If '"+PARAM_FILE+"' does not have a file extension, a .frm extension will be added.");
    	spec.addFootnote("This function will regenerate the drawing sheet.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Drawing name");
    	arg.setDefaultValue("Current active drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory name containing the format file");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Format file name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRAWING, "box.drw");
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_DIRNAME, "C:/myfiles/formats");
    	ex.addInput(PARAM_FILE, "engr_fmt");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SHEET, 1);
    	ex.addInput(PARAM_FILE, "engr_fmt2");
    	template.addExample(ex);
    	
        return template;
    }

	private FunctionTemplate helpTableList() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_LIST, "List all 2D tables in the active drawing");
		FunctionSpec spec = template.getSpec();
		FunctionReturn ret = new FunctionReturn(OUTPUT_TABLES, FunctionSpec.TYPE_OBJARRAY, "DrawingTableData");
		ret.setDescription("Tables in the drawing");
		spec.addReturn(ret);
		FunctionExample ex = new FunctionExample();
		ex.addOutput(OUTPUT_TABLES, new Object[] {sampleTable(false)});
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableGetInfo() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_GET_INFO, "Get detailed information for one 2D drawing table");
		addTableIndexArg(template.getSpec());
		addTableReturns(template.getSpec(), true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		addTableOutput(ex, true);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableGetCell() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_GET_CELL, "Read one drawing table cell");
		addTableIndexArg(template.getSpec());
		addCellArgs(template.getSpec());
		addCellReturns(template.getSpec(), true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_ROW, 1);
		ex.addInput(PARAM_COLUMN, 2);
		ex.addOutput(OUTPUT_ROW, 1);
		ex.addOutput(OUTPUT_COLUMN, 2);
		ex.addOutput(OUTPUT_TEXT, "Part number");
		ex.addOutput(OUTPUT_VALUE, "Part number");
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableSetCell() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_SET_CELL, "Set one drawing table cell text value");
		addTableIndexArg(template.getSpec());
		addCellArgs(template.getSpec());
		addArg(template.getSpec(), PARAM_VALUE, FunctionSpec.TYPE_STRING, "Cell text", true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_ROW, 2);
		ex.addInput(PARAM_COLUMN, 3);
		ex.addInput(PARAM_VALUE, "A-123");
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableGetRange() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_GET_RANGE, "Read a rectangular drawing table cell range");
		addTableIndexArg(template.getSpec());
		addRangeArgs(template.getSpec());
		FunctionReturn ret = new FunctionReturn(OUTPUT_VALUES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_ARRAY);
		ret.setDescription("2D array of cell text values");
		template.getSpec().addReturn(ret);
		FunctionExample ex = new FunctionExample();
		addRangeInput(ex);
		ex.addOutput(OUTPUT_VALUES, new String[][] {{"A1", "B1"}, {"A2", "B2"}});
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableSetRange() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_SET_RANGE, "Write multiple cell text values into a rectangular drawing table range");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_START_ROW, FunctionSpec.TYPE_INTEGER, "Start row, 1-based", true);
		addArg(template.getSpec(), PARAM_START_COLUMN, FunctionSpec.TYPE_INTEGER, "Start column, 1-based", true);
		addArg(template.getSpec(), PARAM_VALUES, FunctionSpec.TYPE_ARRAY, "2D array of cell text values", true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_START_ROW, 2);
		ex.addInput(PARAM_START_COLUMN, 1);
		ex.addInput(PARAM_VALUES, new String[][] {{"A2", "B2"}, {"A3", "B3"}});
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableInsertRows() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_INSERT_ROWS, "Insert one or more rows before or after a drawing table row");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_AT_ROW, FunctionSpec.TYPE_INTEGER, "Reference row, 1-based", true);
		addArg(template.getSpec(), PARAM_COUNT, FunctionSpec.TYPE_INTEGER, "Number of rows to insert", true);
		addPositionArg(template.getSpec());
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_AT_ROW, 4);
		ex.addInput(PARAM_COUNT, 3);
		ex.addInput(PARAM_POSITION, "after");
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableDeleteRows() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_DELETE_ROWS, "Delete one or more drawing table rows");
		template.getSpec().addFootnote("Rows are deleted from highest index to lowest index.");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_ROWS, FunctionSpec.TYPE_ARRAY, "Rows to delete, 1-based", true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_ROWS, new int[] {4, 5, 6});
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableInsertColumns() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_INSERT_COLUMNS, "Insert one or more columns before or after a drawing table column");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_AT_COLUMN, FunctionSpec.TYPE_INTEGER, "Reference column, 1-based", true);
		addArg(template.getSpec(), PARAM_COUNT, FunctionSpec.TYPE_INTEGER, "Number of columns to insert", true);
		addPositionArg(template.getSpec());
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_AT_COLUMN, 2);
		ex.addInput(PARAM_COUNT, 1);
		ex.addInput(PARAM_POSITION, "before");
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableDeleteColumns() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_DELETE_COLUMNS, "Delete one or more drawing table columns");
		template.getSpec().addFootnote("Columns are deleted from highest index to lowest index.");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_COLUMNS, FunctionSpec.TYPE_ARRAY, "Columns to delete, 1-based", true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_COLUMNS, new int[] {3, 4});
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableClearCell() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_CLEAR_CELL, "Clear one drawing table cell");
		addTableIndexArg(template.getSpec());
		addCellArgs(template.getSpec());
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_ROW, 2);
		ex.addInput(PARAM_COLUMN, 2);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableClearRange() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_CLEAR_RANGE, "Clear a rectangular drawing table cell range");
		addTableIndexArg(template.getSpec());
		addRangeArgs(template.getSpec());
		FunctionExample ex = new FunctionExample();
		addRangeInput(ex);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableMove() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_MOVE, "Move a drawing table to a new drawing position");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_POINT, FunctionSpec.TYPE_OBJECT, "New drawing position point", true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_POINT, samplePoint(10.0, 20.0, 0.0));
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableSetColumnWidth() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_SET_COLUMN_WIDTH, "Set a drawing table column width");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_COLUMN, FunctionSpec.TYPE_INTEGER, "Column, 1-based", true);
		addArg(template.getSpec(), PARAM_WIDTH, FunctionSpec.TYPE_DOUBLE, "Column width in drawing units", true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_COLUMN, 2);
		ex.addInput(PARAM_WIDTH, 25.0);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableSetRowHeight() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_SET_ROW_HEIGHT, "Set a drawing table row height");
		addTableIndexArg(template.getSpec());
		addArg(template.getSpec(), PARAM_ROW, FunctionSpec.TYPE_INTEGER, "Row, 1-based", true);
		addArg(template.getSpec(), PARAM_HEIGHT, FunctionSpec.TYPE_DOUBLE, "Row height in drawing units", true);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_ROW, 2);
		ex.addInput(PARAM_HEIGHT, 8.0);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableMergeCells() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_MERGE_CELLS, "Merge a rectangular drawing table cell range");
		addTableIndexArg(template.getSpec());
		addRangeArgs(template.getSpec());
		FunctionExample ex = new FunctionExample();
		addRangeInput(ex);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableUnmergeCells() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_UNMERGE_CELLS, "Unmerge drawing table cells in a rectangular range");
		addTableIndexArg(template.getSpec());
		addRangeArgs(template.getSpec());
		FunctionExample ex = new FunctionExample();
		addRangeInput(ex);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableCreate() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_CREATE, "Create a simple drawing table");
		template.getSpec().addFootnote("Created tables use generic defaults: TABLESIZE_BY_NUM_CHARS, row height 1.0, column width 10.0, and left column justification.");
		addArg(template.getSpec(), PARAM_ROWS, FunctionSpec.TYPE_INTEGER, "Number of rows", true);
		addArg(template.getSpec(), PARAM_COLUMNS, FunctionSpec.TYPE_INTEGER, "Number of columns", true);
		addArg(template.getSpec(), PARAM_POINT, FunctionSpec.TYPE_OBJECT, "Table origin point", false);
		addArg(template.getSpec(), PARAM_VALUES, FunctionSpec.TYPE_ARRAY, "Optional 2D array of initial cell text values", false);
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_ROWS, 2);
		ex.addInput(PARAM_COLUMNS, 2);
		ex.addInput(PARAM_POINT, samplePoint(10.0, 20.0, 0.0));
		ex.addInput(PARAM_VALUES, new String[][] {{"Name", "Value"}, {"Part", "A-123"}});
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableDelete() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_DELETE, "Delete an entire drawing table");
		addTableIndexArg(template.getSpec());
		FunctionExample ex = new FunctionExample();
		ex.addInput(PARAM_TABLE_INDEX, 0);
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate helpTableRegenerate() {
		FunctionTemplate template = tableTemplate(FUNC_TABLE_REGENERATE, "Regenerate or update drawing table display");
		FunctionExample ex = new FunctionExample();
		template.addExample(ex);
		return template;
	}

	private FunctionTemplate tableTemplate(String function, String description) {
		FunctionTemplate template = new FunctionTemplate(COMMAND, function);
		FunctionSpec spec = template.getSpec();
		spec.setFunctionDescription(description);
		spec.addFootnote("Compact command form is drawing.table." + function.substring("table_".length()) + ".");
		spec.addFootnote("tableIndex is 0-based. Row and column parameters use Creo/J-Link native 1-based indexing.");
		spec.addFootnote("If the Creo J-Link API does not expose this operation, the response status message begins with not_supported.");
		FunctionArgument arg = new FunctionArgument(PARAM_DRAWING, FunctionSpec.TYPE_STRING);
		arg.setDescription("Drawing name");
		arg.setDefaultValue("Current active drawing");
		spec.addArgument(arg);
		return template;
	}

	private void addTableIndexArg(FunctionSpec spec) {
		addArg(spec, PARAM_TABLE_INDEX, FunctionSpec.TYPE_INTEGER, "0-based table index from table_list", true);
	}

	private void addCellArgs(FunctionSpec spec) {
		addArg(spec, PARAM_ROW, FunctionSpec.TYPE_INTEGER, "Row, 1-based", true);
		addArg(spec, PARAM_COLUMN, FunctionSpec.TYPE_INTEGER, "Column, 1-based", true);
	}

	private void addRangeArgs(FunctionSpec spec) {
		addArg(spec, PARAM_START_ROW, FunctionSpec.TYPE_INTEGER, "Start row, 1-based", true);
		addArg(spec, PARAM_START_COLUMN, FunctionSpec.TYPE_INTEGER, "Start column, 1-based", true);
		addArg(spec, PARAM_END_ROW, FunctionSpec.TYPE_INTEGER, "End row, 1-based", true);
		addArg(spec, PARAM_END_COLUMN, FunctionSpec.TYPE_INTEGER, "End column, 1-based", true);
	}

	private void addPositionArg(FunctionSpec spec) {
		FunctionArgument arg = addArg(spec, PARAM_POSITION, FunctionSpec.TYPE_STRING, "Insert position relative to the reference row or column", false);
		arg.setDefaultValue("after");
		arg.setValidValues(new String[] {"before", "after"});
	}

	private FunctionArgument addArg(FunctionSpec spec, String name, String type, String description, boolean required) {
		FunctionArgument arg = new FunctionArgument(name, type);
		arg.setDescription(description);
		arg.setRequired(required);
		spec.addArgument(arg);
		return arg;
	}

	private void addTableReturns(FunctionSpec spec, boolean includeCells) {
		FunctionReturn ret;
		ret = new FunctionReturn(OUTPUT_TABLE_INDEX, FunctionSpec.TYPE_INTEGER);
		ret.setDescription("0-based table index");
		spec.addReturn(ret);
		ret = new FunctionReturn(OUTPUT_ROWS, FunctionSpec.TYPE_INTEGER);
		ret.setDescription("Row count");
		spec.addReturn(ret);
		ret = new FunctionReturn(OUTPUT_COLUMNS, FunctionSpec.TYPE_INTEGER);
		ret.setDescription("Column count");
		spec.addReturn(ret);
		ret = new FunctionReturn(OUTPUT_LOCATION, FunctionSpec.TYPE_OBJECT);
		ret.setDescription("Table position/origin, if available");
		spec.addReturn(ret);
		ret = new FunctionReturn(OUTPUT_ROW_HEIGHTS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_DOUBLE);
		ret.setDescription("Row heights, if available");
		spec.addReturn(ret);
		ret = new FunctionReturn(OUTPUT_COLUMN_WIDTHS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_DOUBLE);
		ret.setDescription("Column widths, if available");
		spec.addReturn(ret);
		if (includeCells) {
			ret = new FunctionReturn(OUTPUT_CELLS, FunctionSpec.TYPE_OBJARRAY, "DrawingTableCellData");
			ret.setDescription("Cell text values");
			spec.addReturn(ret);
		}
	}

	private void addCellReturns(FunctionSpec spec, boolean includeValueAlias) {
		FunctionReturn ret;
		ret = new FunctionReturn(OUTPUT_ROW, FunctionSpec.TYPE_INTEGER);
		ret.setDescription("Row, 1-based");
		spec.addReturn(ret);
		ret = new FunctionReturn(OUTPUT_COLUMN, FunctionSpec.TYPE_INTEGER);
		ret.setDescription("Column, 1-based");
		spec.addReturn(ret);
		ret = new FunctionReturn(OUTPUT_TEXT, FunctionSpec.TYPE_STRING);
		ret.setDescription("Cell text");
		spec.addReturn(ret);
		if (includeValueAlias) {
			ret = new FunctionReturn(OUTPUT_VALUE, FunctionSpec.TYPE_STRING);
			ret.setDescription("Cell text value alias");
			spec.addReturn(ret);
		}
	}

	private void addRangeInput(FunctionExample ex) {
		ex.addInput(PARAM_TABLE_INDEX, 0);
		ex.addInput(PARAM_START_ROW, 1);
		ex.addInput(PARAM_START_COLUMN, 1);
		ex.addInput(PARAM_END_ROW, 2);
		ex.addInput(PARAM_END_COLUMN, 2);
	}

	private void addTableOutput(FunctionExample ex, boolean includeCells) {
		ex.addOutput(OUTPUT_TABLE_INDEX, 0);
		ex.addOutput(OUTPUT_ROWS, 2);
		ex.addOutput(OUTPUT_COLUMNS, 2);
		ex.addOutput(OUTPUT_LOCATION, samplePoint(10.0, 20.0, 0.0));
		if (includeCells)
			ex.addOutput(OUTPUT_CELLS, new Object[] {sampleCell(1, 1, "Name"), sampleCell(1, 2, "Value")});
	}

	private Map<String, Object> sampleTable(boolean includeCells) {
		Map<String, Object> table = new OrderedMap<String, Object>();
		table.put(OUTPUT_TABLE_INDEX, 0);
		table.put(OUTPUT_ROWS, 2);
		table.put(OUTPUT_COLUMNS, 2);
		table.put(OUTPUT_LOCATION, samplePoint(10.0, 20.0, 0.0));
		if (includeCells)
			table.put(OUTPUT_CELLS, new Object[] {sampleCell(1, 1, "Name"), sampleCell(1, 2, "Value")});
		return table;
	}

	private Map<String, Object> sampleCell(int row, int column, String text) {
		Map<String, Object> cell = new OrderedMap<String, Object>();
		cell.put(OUTPUT_ROW, row);
		cell.put(OUTPUT_COLUMN, column);
		cell.put(OUTPUT_TEXT, text);
		return cell;
	}

	private Map<String, Object> samplePoint(double x, double y, double z) {
		Map<String, Object> point = new OrderedMap<String, Object>();
		point.put("x", x);
		point.put("y", y);
		point.put("z", z);
		return point;
	}
    
	
}
