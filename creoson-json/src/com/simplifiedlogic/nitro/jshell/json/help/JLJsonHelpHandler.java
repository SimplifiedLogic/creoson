/**
 * 
 */
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.List;

import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * @author Adama
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
	
}
