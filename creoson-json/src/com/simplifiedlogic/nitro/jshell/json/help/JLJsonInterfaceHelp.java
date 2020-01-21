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

import com.simplifiedlogic.nitro.jlink.intf.IJLTransfer;
import com.simplifiedlogic.nitro.jshell.json.request.JLInterfaceRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLInterfaceResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "interface" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonInterfaceHelp extends JLJsonCommandHelp implements JLInterfaceRequestParams, JLInterfaceResponseParams {

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
		list.add(helpExportPDF(true));
		list.add(helpExportFile());
		list.add(helpExportImage());
		list.add(helpExportPDF(false));
		list.add(helpExportProgram());
		list.add(helpImportProgram());
		list.add(helpMapkey());
		list.add(helpPlot());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
//		List<FunctionObject> list = new ArrayList<FunctionObject>();
//		list.add(helpParameterData());
//		return list;
		return null;
	}
	
	private FunctionTemplate helpExportImage() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXPORT_IMAGE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Export a model to an image file");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Image type");
    	arg.setRequired(true);
    	arg.setValidValues(new String[] {
	    	TYPE_BMP,
	    	TYPE_EPS,
	    	TYPE_JPEG,
	    	TYPE_TIFF
    	});
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination file name.  May also contain a path to the file.");
    	arg.setDefaultValue("The model name with the appropriate file extension, in Creo's working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_HEIGHT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Image height");
    	arg.setDefaultValue("7.5");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_WIDTH, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Image width");
    	arg.setDefaultValue("10.0");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DPI, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Image DPI");
    	arg.setDefaultValue("100");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DEPTH, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Image depth");
    	arg.setDefaultValue("24");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Directory of the output file");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FILENAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Name of the output file");
    	spec.addReturn(ret);
        
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_FILENAME, "box-img.bmp");
    	ex.addInput(PARAM_TYPE, TYPE_BMP);
    	ex.addInput(PARAM_HEIGHT, 7.5);
    	ex.addInput(PARAM_WIDTH, 13.0);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "box-img.bmp");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_TYPE, TYPE_TIFF);
    	ex.addInput(PARAM_HEIGHT, 7.5);
    	ex.addInput(PARAM_WIDTH, 13.0);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "box.tiff");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_TYPE, TYPE_JPEG);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.jpg");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_FILENAME, "C:/testing/abc123.jpg");
    	ex.addInput(PARAM_TYPE, TYPE_JPEG);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/testing");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.jpg");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpExportFile() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXPORT_FILE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Export a model to a file");
    	spec.addFootnote("The " + PARAM_GEOM_FLAGS + " option only applies to " + TYPE_IGES + " and " + TYPE_STEP + " exports.");
    	spec.addFootnote("Setting " + PARAM_GEOM_FLAGS + " to '" + IJLTransfer.GEOM_DEFAULT + "' will cause it to check the Creo config option 'intf3d_out_default_option' for the setting");
    	spec.addFootnote("The " + PARAM_ADVANCED + " option will cause the Export to use settings defined in the appropriate \"export_profiles\" Creo Config Option for the file type.");
    	spec.addFootnote("The " + PARAM_ADVANCED + " option only applies to " + TYPE_DXF + ", " + TYPE_IGES + " and " + TYPE_STEP + " exports.");
    	spec.addFootnote("The " + PARAM_ADVANCED + " option will only work with Creo 4 M030 or later.");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File type");
    	arg.setRequired(true);
    	arg.setValidValues(new String[] {
//    			TYPE_CATIA,
    			TYPE_DXF,
    			TYPE_IGES,
    			TYPE_PV,
    			TYPE_STEP,
    			TYPE_VRML
        	});
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination file name.  May also contain a path to the file.");
    	arg.setDefaultValue("The model name with the appropriate file extension, in Creo's working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination directory");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_GEOM_FLAGS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Geometry type for the export.");
    	arg.setDefaultValue(IJLTransfer.GEOM_SOLIDS);
    	arg.setValidValues(new String[] {
    			IJLTransfer.GEOM_SOLIDS,
    			IJLTransfer.GEOM_SURFACES,
    			IJLTransfer.GEOM_WIREFRAME,
    			IJLTransfer.GEOM_WIREFRAME_SURFACES,
    			IJLTransfer.GEOM_QUILTS,
    			IJLTransfer.GEOM_DEFAULT
        	});
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ADVANCED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to use the newer Creo 4 file export function.");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Directory of the output file");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FILENAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Name of the output file");
    	spec.addReturn(ret);
        
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_FILENAME, "box-export.igs");
    	ex.addInput(PARAM_TYPE, TYPE_IGES);
    	ex.addInput(PARAM_GEOM_FLAGS, IJLTransfer.GEOM_WIREFRAME);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "box-export.igs");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DIRNAME, "subdir");
    	ex.addInput(PARAM_TYPE, TYPE_DXF);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts/subdir");
    	ex.addOutput(OUTPUT_FILENAME, "box.dxf");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_TYPE, TYPE_STEP);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.stp");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_FILENAME, "C:/testing/abc123.stp");
    	ex.addInput(PARAM_TYPE, TYPE_STEP);
    	ex.addInput(PARAM_GEOM_FLAGS, IJLTransfer.GEOM_SURFACES);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/testing");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.stp");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpExportPDF(boolean use3d) {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, use3d ? FUNC_EXPORT_3DPDF : FUNC_EXPORT_PDF);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Export a model to a " + (use3d?"3D ":"") + "PDF file");
    	spec.addFootnote("When "+PARAM_USE_DRW_SETTINGS+" is true, the Font Stroke option will be set to Stroke All Fonts, and the Color Depth option will be set to Grayscale.");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination file name.  May also contain a path to the file.");
    	arg.setDefaultValue("The model name with the appropriate file extension, in Creo's working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_HEIGHT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("PDF Image height");
    	arg.setDefaultValue("Creo default export height");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_WIDTH, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("PDF Image width");
    	arg.setDefaultValue("Creo default export width");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DPI, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("PDF Image DPI");
    	arg.setDefaultValue("Creo default export DPI");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination directory");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_USE_DRW_SETTINGS, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to use special settings for exporting drawings");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Directory of the output file");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FILENAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Name of the output file");
    	spec.addReturn(ret);
        
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_FILENAME, "box-export.pdf");
    	ex.addInput(PARAM_DIRNAME, "subdir");
    	ex.addInput(PARAM_HEIGHT, 7.5);
    	ex.addInput(PARAM_WIDTH, 13.0);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts/subdir");
    	ex.addOutput(OUTPUT_FILENAME, "box-export.pdf");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "box.pdf");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.pdf");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_FILENAME, "C:/testing/abc123.pdf");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/testing");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.pdf");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpPlot() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_PLOT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Export a model plot");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination directory");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DRIVER, FunctionSpec.TYPE_STRING);
    	arg.setValidValues(new String[] {
    		TYPE_POSTSCRIPT,
    		TYPE_JPEG,
    		TYPE_TIFF
    	});
    	arg.setDefaultValue(TYPE_POSTSCRIPT);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Directory of the output file");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FILENAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Name of the output file");
    	spec.addReturn(ret);
        
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DRIVER, "JPEG");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "box.jpeg");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DIRNAME, "subdir");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts/subdir");
    	ex.addOutput(OUTPUT_FILENAME, "box.ps");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DRIVER, TYPE_TIFF);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.tiff");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpMapkey() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_MAPKEY);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Run a Mapkey script in Creo");
    	spec.addFootnote("Make sure to remove any \"mapkey(continued)\" clauses from the "+PARAM_SCRIPT+" argument.  The tilde at the start of a line should occur immediately after the semicolon at the end of the previous line.");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_SCRIPT, FunctionSpec.TYPE_STRING);
    	arg.setDescription("The mapkey script to run");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_SCRIPT, "~ Select `main_dlg_cur` `View:casc340798662`;~ Close `main_dlg_cur` `View:casc340798662`;~ Command `ProCmdNamedViewsGalSelect`  `FRONT`;");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpImportProgram() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_IMPORT_PROGRAM);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Import a program file for a model");
    	spec.addFootnote("Cannot specify both " + PARAM_MODEL + " and " + PARAM_FILENAME + " parameters");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Source directory");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Source file name");
    	arg.setDefaultValue("The model name with the appropriate file extension");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination Model name");
    	arg.setDefaultValue("Currently active model, or the model for the " + PARAM_FILENAME + " parameter if given");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MODEL, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Name of the model updated");
    	spec.addReturn(ret);
        
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "c:/myfiles/parts");
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_MODEL, "box.prt");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "c:/myfiles/parts");
    	ex.addInput(PARAM_FILENAME, "abc123.als");
    	ex.addOutput(OUTPUT_MODEL, "abc123.asm");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpExportProgram() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXPORT_PROGRAM);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Export a model's program to a file");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Directory of the output file");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Name of the output file");
    	spec.addReturn(ret);
        
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "box.pls");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILENAME, "abc123.als");
    	template.addExample(ex);

    	return template;
	}

}
