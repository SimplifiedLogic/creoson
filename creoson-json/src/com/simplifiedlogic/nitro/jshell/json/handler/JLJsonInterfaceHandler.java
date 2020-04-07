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

import com.simplifiedlogic.nitro.jlink.data.ExportResults;
import com.simplifiedlogic.nitro.jlink.intf.IJLTransfer;
import com.simplifiedlogic.nitro.jshell.json.request.JLInterfaceRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLInterfaceResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "interface" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonInterfaceHandler extends JLJsonCommandHandler implements JLInterfaceRequestParams, JLInterfaceResponseParams {

	private IJLTransfer intfHandler = null;

	/**
	 * @param intfHandler
	 */
	public JLJsonInterfaceHandler(IJLTransfer intfHandler) {
		this.intfHandler = intfHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_EXPORT_IMAGE))
			return actionExportImage(sessionId, input);
		else if (function.equals(FUNC_EXPORT_FILE))
			return actionExportFile(sessionId, input);
		else if (function.equals(FUNC_EXPORT_PDF))
			return actionExportPDF(sessionId, input);
		else if (function.equals(FUNC_EXPORT_3DPDF))
			return actionExport3DPDF(sessionId, input);
		else if (function.equals(FUNC_PLOT))
			return actionPlot(sessionId, input);
		else if (function.equals(FUNC_MAPKEY))
			return actionMapkey(sessionId, input);
		else if (function.equals(FUNC_EXPORT_PROGRAM))
			return actionExportProgram(sessionId, input);
		else if (function.equals(FUNC_IMPORT_PROGRAM))
			return actionImportProgram(sessionId, input);
//		else if (function.equals(FUNC_IMPORT_PV))
//			return actionImportPV(sessionId, input);
		else if (function.equals(FUNC_IMPORT_FILE))
			return actionImportFile(sessionId, input);
//		else if (function.equals(FUNC_IMPORT_STEP))
//			return actionImportSTEP(sessionId, input);
//		else if (function.equals(FUNC_IMPORT_IGES))
//			return actionImportIGES(sessionId, input);
//		else if (function.equals(FUNC_IMPORT_NEUTRAL))
//			return actionImportNeutral(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}

	private Hashtable<String, Object> actionExportImage(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String type = checkStringParameter(input, PARAM_TYPE, true);
        String model = checkStringParameter(input, PARAM_MODEL, false);
        String filename = checkStringParameter(input, PARAM_FILENAME, false);

        Double height = checkDoubleParameter(input, PARAM_HEIGHT, false);
        Double width = checkDoubleParameter(input, PARAM_WIDTH, false);
        Integer dpi = checkIntParameter(input, PARAM_DPI, false, null);
        Integer depth = checkIntParameter(input, PARAM_DEPTH, false, null);

        ExportResults results = null;
        if (TYPE_BMP.equalsIgnoreCase(type))
        	results = intfHandler.exportBMP(model, filename, height, width, dpi, depth, sessionId);
        else if (TYPE_EPS.equalsIgnoreCase(type))
        	results = intfHandler.exportEPS(model, filename, height, width, dpi, depth, sessionId);
        else if (TYPE_JPEG.equalsIgnoreCase(type))
        	results = intfHandler.exportJPEG(model, filename, height, width, dpi, depth, sessionId);
        else if (TYPE_TIFF.equalsIgnoreCase(type))
        	results = intfHandler.exportTIFF(model, filename, height, width, dpi, depth, sessionId);
        
        if (results!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, results.getDirname());
			out.put(OUTPUT_FILENAME, results.getFilename());
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionExportFile(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String type = checkStringParameter(input, PARAM_TYPE, true);
        String model = checkStringParameter(input, PARAM_MODEL, false);
        String filename = checkStringParameter(input, PARAM_FILENAME, false);
        String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
        String geomType = checkStringParameter(input, PARAM_GEOM_FLAGS, false);
        boolean advanced = checkFlagParameter(input, PARAM_ADVANCED, false, false);

        ExportResults results = null;
        if (TYPE_DXF.equalsIgnoreCase(type))
        	results = intfHandler.exportDXF(model, filename, dirname, advanced, sessionId);
//        else if (TYPE_CATIA.equalsIgnoreCase(type))
//        	results = intfHandler.exportCATIA(model, filename, dirname, geomType, sessionId);
        else if (TYPE_IGES.equalsIgnoreCase(type))
        	results = intfHandler.exportIGES(model, filename, dirname, geomType, advanced, sessionId);
        else if (TYPE_PV.equalsIgnoreCase(type))
        	results = intfHandler.exportPV(model, filename, dirname, sessionId);
        else if (TYPE_STEP.equalsIgnoreCase(type))
        	results = intfHandler.exportSTEP(model, filename, dirname, geomType, advanced, sessionId);
        else if (TYPE_VRML.equalsIgnoreCase(type))
        	results = intfHandler.exportVRML(model, filename, dirname, sessionId);
        else if (TYPE_NEUTRAL.equalsIgnoreCase(type))
        	results = intfHandler.exportNeutral(model, filename, dirname, advanced, sessionId);
        
        if (results!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, results.getDirname());
			out.put(OUTPUT_FILENAME, results.getFilename());
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionExportPDF(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String model = checkStringParameter(input, PARAM_MODEL, false);
        String filename = checkStringParameter(input, PARAM_FILENAME, false);
        String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
        boolean useDrawingSettings = checkFlagParameter(input, PARAM_USE_DRW_SETTINGS, false, false);

        Double height = checkDoubleParameter(input, PARAM_HEIGHT, false);
        Double width = checkDoubleParameter(input, PARAM_WIDTH, false);
        Integer dpi = checkIntParameter(input, PARAM_DPI, false, null);

        ExportResults results = intfHandler.exportPDF(model, filename, dirname, false, height, width, dpi, useDrawingSettings, sessionId);
        
        if (results!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, results.getDirname());
			out.put(OUTPUT_FILENAME, results.getFilename());
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionExport3DPDF(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String model = checkStringParameter(input, PARAM_MODEL, false);
        String filename = checkStringParameter(input, PARAM_FILENAME, false);
        String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
        boolean useDrawingSettings = checkFlagParameter(input, PARAM_USE_DRW_SETTINGS, false, false);

        Double height = checkDoubleParameter(input, PARAM_HEIGHT, false);
        Double width = checkDoubleParameter(input, PARAM_WIDTH, false);
        Integer dpi = checkIntParameter(input, PARAM_DPI, false, null);

        ExportResults results = intfHandler.exportPDF(model, filename, dirname, true, height, width, dpi, useDrawingSettings, sessionId);
        
        if (results!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, results.getDirname());
			out.put(OUTPUT_FILENAME, results.getFilename());
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionPlot(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String model = checkStringParameter(input, PARAM_MODEL, false);
        String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
        String driver = checkStringParameter(input, PARAM_DRIVER, false);

        ExportResults results = intfHandler.plot(model, dirname, driver, sessionId);
        
        if (results!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, results.getDirname());
			out.put(OUTPUT_FILENAME, results.getFilename());
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionMapkey(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String script = checkStringParameter(input, PARAM_SCRIPT, true);
        int delay = checkIntParameter(input, PARAM_DELAY, false, 0);

        intfHandler.mapkey(script, delay, sessionId);
        
        return null;
	}

	private Hashtable<String, Object> actionExportProgram(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String model = checkStringParameter(input, PARAM_MODEL, false);

        ExportResults results = intfHandler.exportProgram(model, sessionId);
        
        if (results!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, results.getDirname());
			out.put(OUTPUT_FILENAME, results.getFilename());
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionImportProgram(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
        String filename = checkStringParameter(input, PARAM_FILENAME, false);
        String model = checkStringParameter(input, PARAM_MODEL, false);
        
        if (model!=null && filename!=null) 
            throw new JLIException("Cannot specify both model and input file");
		
        String outModel = intfHandler.importProgram(dirname, filename, model, sessionId);
        
        if (outModel!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_MODEL, outModel);
        	return out;
        }
        return null;
	}
/*
	private Hashtable<String, Object> actionImportPV(String sessionId, Hashtable<String, Object> input) throws JLIException {
//        String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
//        String filename = checkStringParameter(input, PARAM_FILENAME, false);
//        String newName = checkStringParameter(input, PARAM_NEWNAME, false);
//        String newModelType = checkStringParameter(input, PARAM_NEWMODELTYPE, false);
//        
//        String outModel = intfHandler.importPV(dirname, filename, newName, newModelType, sessionId);
//        
//        if (outModel!=null) {
//			Hashtable<String, Object> out = new Hashtable<String, Object>();
//			out.put(OUTPUT_MODEL, outModel);
//        	return out;
//        }
//        return null;
		
		input.put(PARAM_TYPE, TYPE_PV);
		return actionImportFile(sessionId, input);
	}
*/
	private Hashtable<String, Object> actionImportFile(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String type = checkStringParameter(input, PARAM_TYPE, true);
        String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
        String filename = checkStringParameter(input, PARAM_FILENAME, false);
        String newName = checkStringParameter(input, PARAM_NEWNAME, false);
        String newModelType = checkStringParameter(input, PARAM_NEWMODELTYPE, false);
        
        String outModel = null;
        if (TYPE_PV.equalsIgnoreCase(type))
        	outModel = intfHandler.importPV(dirname, filename, newName, newModelType, sessionId);
        else if (TYPE_STEP.equalsIgnoreCase(type))
        	outModel = intfHandler.importSTEP(dirname, filename, newName, newModelType, sessionId);
        else if (TYPE_IGES.equalsIgnoreCase(type))
        	outModel = intfHandler.importIGES(dirname, filename, newName, newModelType, sessionId);
        else if (TYPE_NEUTRAL.equalsIgnoreCase(type))
        	outModel = intfHandler.importNeutral(dirname, filename, newName, newModelType, sessionId);
        
        if (outModel!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_MODEL, outModel);
        	return out;
        }
        return null;
	}

}
