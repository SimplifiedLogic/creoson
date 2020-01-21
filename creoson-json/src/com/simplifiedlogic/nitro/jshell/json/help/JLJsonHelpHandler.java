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

import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * This is the main entry point for generating the JSON help doc.
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonHelpHandler {

	/**
	 * The collection of help-documentation generators for the various function families.
	 */
	private List<JLJsonCommandHelp> helps = new ArrayList<JLJsonCommandHelp>();
	
	public JLJsonHelpHandler() {
		
		// assemble the collection of help-doc handlers
		helps.add(new JLJsonBomHelp());
		helps.add(new JLJsonConnectionHelp());
		helps.add(new JLJsonDimensionHelp());
		helps.add(new JLJsonDrawingHelp());
		helps.add(new JLJsonFamilyTableHelp());
		helps.add(new JLJsonFeatureHelp());
		helps.add(new JLJsonFileHelp());
		helps.add(new JLJsonGeometryHelp());
		helps.add(new JLJsonInterfaceHelp());
		helps.add(new JLJsonLayerHelp());
		helps.add(new JLJsonNoteHelp());
		helps.add(new JLJsonParameterHelp());
		helps.add(new JLJsonCreoHelp());
		helps.add(new JLJsonViewHelp());
		helps.add(new JLJsonWindchillHelp());
		
	}
	
	/**
	 * Get the list of help-doc function templates for all function families
	 * @return The list of function template objects
	 */
	public List<FunctionTemplate> getTemplates() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		for (JLJsonCommandHelp help : helps) {
			List<FunctionTemplate> temp = help.getHelp();
			if (temp!=null && temp.size()>0)
				list.addAll(temp);
		}
		return list.size()>0 ? list : null;
	}
	
	/**
	 * Get the list of help-doc shared objects for all function families
	 * @return The list of shared objects
	 */
	public List<FunctionObject> getTemplateObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		for (JLJsonCommandHelp help : helps) {
			List<FunctionObject> temp = help.getHelpObjects();
			if (temp!=null && temp.size()>0)
				list.addAll(temp);
		}
		return list.size()>0 ? list : null;
	}

	/**
	 * Get the list of help-doc handlers, one per function family
	 * @return The list of help handlers
	 */
	public List<JLJsonCommandHelp> getHelps() {
		return helps;
	}
	
}
