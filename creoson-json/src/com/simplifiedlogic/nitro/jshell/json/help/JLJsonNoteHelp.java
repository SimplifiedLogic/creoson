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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.DataUtils;
import com.simplifiedlogic.nitro.jshell.json.request.JLFileRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLNoteRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFileResponseParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLNoteResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "note" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonNoteHelp extends JLJsonCommandHelp implements JLNoteRequestParams, JLNoteResponseParams {

	public static final String OBJ_NOTE_DATA = "NoteData";

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
		list.add(helpCopy());
		list.add(helpDelete());
		list.add(helpExists());
		list.add(helpGet());
		list.add(helpList());
		list.add(helpSet());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpNoteData());
		return list;
	}
	
	private FunctionTemplate helpSet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the text of a model or drawing note");
    	spec.addFootnote("The location parameter can used to position a new note, or to change the position of an existing note.");
    	spec.addFootnote("If the text contains Creo Symbols or other non-ASCII text, you must Base64-encode the "+PARAM_VALUE+" and set "+PARAM_ENCODED+" to true.");
    	spec.addFootnote("You may be able to avoid Base64-encoding symbols by using Unicode for the binary characters, for example including \\u0001#\\u0002 in the "+PARAM_VALUE+" to insert a plus/minus symbol."); 
    	spec.addFootnote("Embed newlines in the "+PARAM_VALUE+" for line breaks");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model or drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note text with parameters not expanded");
    	arg.setDefaultValue("Clears the note if missing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Value is Base64-encoded");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_LOCATION, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Coordinates for the note placement in Drawing Units");
    	arg.setDefaultValue("If missing and this is a new note, note will be placed at 0, 0");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_23");
    	ex.addInput(PARAM_VALUE, "This is a test note\nWith two lines");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_17");
    	byte[] enc = DataUtils.encodeBase64("friendly \001#\0023.5");
//    	ex.addInput(PARAM_VALUE, "ZnJpZW5kbHk=");
    	ex.addInput(PARAM_VALUE, new String(enc));
    	ex.addInput(PARAM_ENCODED, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_17");
    	ex.addInput(PARAM_VALUE, "test \u0001#\u00023.5");
    	ex.addInput(PARAM_ENCODED, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "newnote");
    	ex.addInput(PARAM_VALUE, "This is a test note\nWith two lines");
    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.put(JLFileRequestParams.PARAM_X, 10.0);
    	rec.put(JLFileRequestParams.PARAM_Y, 3.25);
    	ex.addInput(PARAM_LOCATION, rec);
    	template.addExample(ex);

    	return template;
	}
	
	private FunctionTemplate helpGet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the text of a model or drawing note");
    	spec.addFootnote("Values will automatically be returned Base64-encoded if they are strings which contain Creo Symbols or other non-ASCII data");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model or drawign");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_NAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Note name");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_VALUE, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Note text parameters not expanded");
    	spec.addReturn(ret);

//    	ret = new FunctionReturn(OUTPUT_VALUE_EXPANDED, FunctionSpec.TYPE_STRING);
//    	ret.setDescription("Note text with parameters expanded");
//    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_ENCODED, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Value is Base64-encoded");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_URL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Note URL, if there is one");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_LOCATION, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	ret.setDescription("Note location in Drawing Units (drawing notes only)");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_23");
    	ex.addOutput(OUTPUT_NAME, "Note_23");
    	ex.addOutput(OUTPUT_VALUE, "This is a test note\nWith two lines");
    	ex.addOutput(OUTPUT_ENCODED, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_17");
    	ex.addOutput(OUTPUT_NAME, "Note_17");
    	ex.addOutput(OUTPUT_VALUE, "ZnJpZW5kbHk=");
    	ex.addOutput(OUTPUT_ENCODED, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "box.drw");
    	ex.addInput(PARAM_NAME, "Note_2");
    	ex.addOutput(OUTPUT_NAME, "Note_2");
    	ex.addOutput(OUTPUT_VALUE, "Another test note");
    	ex.addOutput(OUTPUT_ENCODED, false);
    	Map<String, Object> rec = new OrderedMap<String, Object>();
    	rec.put(JLFileResponseParams.OUTPUT_X, 2.5);
    	rec.put(JLFileResponseParams.OUTPUT_Y, 4.0);
    	rec.put(JLFileResponseParams.OUTPUT_Z, 0.0);
    	ex.addOutput(OUTPUT_LOCATION, rec);
    	template.addExample(ex);

    	return template;
	}
	
	private FunctionTemplate helpDelete() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a model or drawing note");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model or drawing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note name");
    	arg.setRequired(true);
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_23");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_17");
    	template.addExample(ex);

    	return template;
	}
	
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a list of notes from one or more models");
    	spec.addFootnote("Values will automatically be returned Base64-encoded if they are strings which contain Creo Symbols or other non-ASCII data");
    	spec.addFootnote("If "+PARAM_SELECT+" is true, then the current selection in Creo will be cleared even if no items are found.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note name; only used if " + PARAM_NAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_NAMES + " parameter is used; if both are empty, then all notes are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of note names");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty, then all notes are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter value filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_GET_EXPANDED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return text with parameter values replaced");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SELECT, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("If true, the notes that are found will be selected in Creo");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_ITEMLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_NOTE_DATA);
    	ret.setDescription("List of note information");
    	spec.addReturn(ret);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_*");
    	Map<String, Object> rec;
    	List<Map<String, Object>> notes = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	notes.add(rec);
    	rec.put(OUTPUT_NAME, "Note_23");
    	rec.put(OUTPUT_VALUE, "This is a test note\nWith two lines");
    	rec.put(OUTPUT_ENCODED, false);
    	rec = new OrderedMap<String, Object>();
    	notes.add(rec);
    	rec.put(OUTPUT_NAME, "Note_12");
    	rec.put(OUTPUT_VALUE, "This is a note with a parameter: &ALPHA example");
    	rec.put(OUTPUT_ENCODED, false);
		ex.addOutput(OUTPUT_ITEMLIST, notes);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_12");
    	ex.addInput(PARAM_GET_EXPANDED, true);
    	notes = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	notes.add(rec);
    	rec.put(OUTPUT_NAME, "Note_12");
    	rec.put(OUTPUT_VALUE, "This is a note with a parameter: &ALPHA example");
    	rec.put(OUTPUT_VALUE_EXPANDED, "This is a note with a parameter: OVERAGE example");
    	rec.put(OUTPUT_ENCODED, false);
		ex.addOutput(OUTPUT_ITEMLIST, notes);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAMES, new String[] {"Note_23","NOTE_TEST"});
    	ex.addInput(PARAM_VALUE, "*test*");
    	notes = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	notes.add(rec);
    	rec.put(OUTPUT_NAME, "Note_23");
    	rec.put(OUTPUT_VALUE, "This is a test note\nWith two lines");
    	rec.put(OUTPUT_ENCODED, false);
		ex.addOutput(OUTPUT_ITEMLIST, notes);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.drw");
    	ex.addInput(PARAM_NAME, "Note_2");
    	notes = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	notes.add(rec);
    	rec.put(OUTPUT_NAME, "Note_2");
    	rec.put(OUTPUT_VALUE, "Another test note");
    	rec.put(OUTPUT_ENCODED, false);
    	Map<String, Object> rec2 = new OrderedMap<String, Object>();
    	rec2.put(JLFileResponseParams.OUTPUT_X, 2.5);
    	rec2.put(JLFileResponseParams.OUTPUT_Y, 4.0);
    	rec2.put(JLFileResponseParams.OUTPUT_Z, 0.0);
    	rec.put(OUTPUT_LOCATION, rec2);
		ex.addOutput(OUTPUT_ITEMLIST, notes);
    	template.addExample(ex);

    	return template;
	}

	private FunctionObject helpNoteData() {
    	FunctionObject obj = new FunctionObject(OBJ_NOTE_DATA);
    	obj.setDescription("Information about a model/drawing note");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_VALUE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note text with parameters not expanded");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_VALUE_EXPANDED, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note text with parameters expanded");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Value is Base64-encoded");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_URL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note URL, if there is one");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_LOCATION, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Note location in Drawing Units (drawing notes only)");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpCopy() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_COPY);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Copy note to another in the same model or another model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Source model");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note name to copy");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TONAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination note");
    	arg.setDefaultValue("The source note name");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TOMODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination model");
    	arg.setDefaultValue("The source model");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "ALPHA");
    	ex.addInput(PARAM_TONAME, "BETA");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box_flat.prt");
    	ex.addInput(PARAM_NAME, "Note_2*");
    	ex.addInput(PARAM_TOMODEL, "box.prt");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether note(s) exists on a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Note name; only used if " + PARAM_NAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_NAMES + " parameter is used; if both are empty, then it checks for any note's existence");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of note names");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty, then it checks for any note's existence");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the note exists on the model");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "NOTE_TEST");
    	ex.addOutput(OUTPUT_EXISTS, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "Note_2*");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAMES, new String[] {"NOTE_TEST","Note_33","Note_23"});
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

        return template;
    }
    
}
