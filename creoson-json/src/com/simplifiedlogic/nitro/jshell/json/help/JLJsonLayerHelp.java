/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.intf.IJLLayer;
import com.simplifiedlogic.nitro.jshell.json.request.JLLayerRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLLayerResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "layer" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonLayerHelp extends JLJsonCommandHelp implements JLLayerRequestParams, JLLayerResponseParams {

	private String[] validStatuses;
	private Map<String, Map<String, Object>> sampleLayers;
	
	/**
	 * 
	 */
	public JLJsonLayerHelp() {
		validStatuses = new String[] {
			IJLLayer.STATUS_BLANK,
			IJLLayer.STATUS_DISPLAY,
			IJLLayer.STATUS_HIDDEN,
			IJLLayer.STATUS_NORMAL
		};

		sampleLayers = new HashMap<String, Map<String, Object>>();

    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "01___PRT_ALL_DTM_PLN");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 17);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "01___PRT_DEF_DTM_PLN");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 19);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "02___PRT_ALL_AXES");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 22);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "03___PRT_ALL_CURVES");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 25);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "04___PRT_ALL_DTM_PNT");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 28);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "05___PRT_ALL_DTM_CSYS");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 32);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "05___PRT_DEF_DTM_CSYS");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 33);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "06___PRT_ALL_SURFS");
    	rec.put(OUTPUT_STATUS, "NORMAL");
		rec.put(OUTPUT_ID, 38);
    	sampleLayers.put((String)rec.get(PARAM_NAME), rec);

	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpDelete());
		list.add(helpExists());
		list.add(helpList());
		list.add(helpShow());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpLayerData());
		return list;
	}
	
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List layers that match criteria");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Layer name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All layers are listed");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_LAYERS, FunctionSpec.TYPE_OBJARRAY, "LayerData");
    	ret.setDescription("List of layer data");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	List<Map<String, Object>> layers = new ArrayList<Map<String, Object>>();
    	layers.add(sampleLayers.get("01___PRT_ALL_DTM_PLN"));
    	layers.add(sampleLayers.get("01___PRT_DEF_DTM_PLN"));
    	layers.add(sampleLayers.get("02___PRT_ALL_AXES"));
    	layers.add(sampleLayers.get("03___PRT_ALL_CURVES"));
    	layers.add(sampleLayers.get("04___PRT_ALL_DTM_PNT"));
    	layers.add(sampleLayers.get("05___PRT_ALL_DTM_CSYS"));
    	layers.add(sampleLayers.get("05___PRT_DEF_DTM_CSYS"));
    	layers.add(sampleLayers.get("06___PRT_ALL_SURFS"));
		ex.addOutput(OUTPUT_LAYERS, layers);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "*CSYS");
    	layers = new ArrayList<Map<String, Object>>();
    	layers.add(sampleLayers.get("05___PRT_ALL_DTM_CSYS"));
    	layers.add(sampleLayers.get("05___PRT_DEF_DTM_CSYS"));
		ex.addOutput(OUTPUT_LAYERS, layers);
    	template.addExample(ex);

    	return template;
	}

	private FunctionObject helpLayerData() {
    	FunctionObject obj = new FunctionObject("LayerData");
    	obj.setDescription("Information about a layer");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Layer name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_STATUS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Layer status");
    	arg.setValidValues(validStatuses);
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Layer ID");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpDelete() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete one or more layers");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Layer name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All layers will be deleted");
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "01___PRT_ALL_DTM_PLN");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "*CSYS");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.asm");
    	ex.addInput(PARAM_NAME, "*CSYS");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpShow() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SHOW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Show/Hide one or more layers");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Layer name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All layers will be deleted");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SHOW, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to show or hide the layers");
    	arg.setDefaultValue("true (show)");
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "01___PRT_ALL_DTM_PLN");
    	ex.addInput(PARAM_SHOW, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "*DTM*");
    	ex.addInput(PARAM_SHOW, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.asm");
    	ex.addInput(PARAM_NAME, "*CSYS");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.asm");
    	ex.addInput(PARAM_NAME, "*CSYS");
    	ex.addInput(PARAM_SHOW, false);
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether layer(s) exists on a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Layer name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All layers are listed");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the layer exists on the model");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
		ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "*CSYS");
		ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "BAKER");
		ex.addOutput(OUTPUT_EXISTS, false);
    	template.addExample(ex);

    	return template;
	}

}
