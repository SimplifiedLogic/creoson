/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jshell.creo;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcArgument.pfcArgument;
import com.ptc.pfc.pfcDrawing.Drawing;
import com.ptc.pfc.pfcExport.PDFColorDepth;
import com.ptc.pfc.pfcExport.PDFExportInstructions;
import com.ptc.pfc.pfcExport.PDFExportMode;
import com.ptc.pfc.pfcExport.PDFFontStrokeMode;
import com.ptc.pfc.pfcExport.PDFOption;
import com.ptc.pfc.pfcExport.PDFOptionType;
import com.ptc.pfc.pfcExport.PDFOptions;
import com.ptc.pfc.pfcExport.PDFSelectedViewMode;
import com.ptc.pfc.pfcExport.PrintSheets;
import com.ptc.pfc.pfcExport.pfcExport;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.PlotPaperSize;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface;
import com.simplifiedlogic.nitro.rpc.JLIException;


/**
 * A Creo function interface for exporting a model as a PDF file.  This JLink function 
 * did not exist in WF4.
 * 
 * <p>Arguments expected:
 * <ol>
 * <li>The model for which to set the post-regen relations.  Must be of type 
 * {@link com.ptc.pfc.pfcModel.Model} or {@link CallModel}
 * 
 * <li>The name of the export file as a java.lang.String.  The file name may include a path.
 * 
 * <li>The height of the image. (optional)
 * 
 * <li>The width of the image. (optional)
 * 
 * <li>The DPI of the image. (optional)
 * 
 * <li>A boolean flag indicating whether to use drawing settings instead of model settings. (optional)
 * </ol>
 * @author Adam Andrews
 *
 */
public class ExportPDF implements CreoFunctionInterface {

	// must agree with the constants in IJLTransfer
    public static final String SHEET_RANGE_ALL		= "all";
    public static final String SHEET_RANGE_CURRENT	= "current";

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] args) throws JLIException,jxthrowable {
		Model m = null;
		if (args[0] instanceof CallModel)
			m = ((CallModel)args[0]).getModel();
		else 
			m = (Model)args[0];
		String filename = (String)args[1];
		Double height = (Double)args[2];
		Double width = (Double)args[3];
		Integer dpi = (Integer)args[4];
		Boolean useDrawingSettings = (Boolean)args[5];
		String sheetOpt = null;
		if (args.length>6)
			sheetOpt = (String)args[6];

		PDFExportInstructions pxi = pfcExport.PDFExportInstructions_Create();
		
		PDFOptions opts = PDFOptions.create();
		PDFOption opt = null;

		opt = pfcExport.PDFOption_Create();
		opt.SetOptionType(PDFOptionType.PDFOPT_LAUNCH_VIEWER);
		opt.SetOptionValue(pfcArgument.CreateBoolArgValue(false));
		opts.append(opt);

		opt = pfcExport.PDFOption_Create();
		opt.SetOptionType(PDFOptionType.PDFOPT_VIEW_TO_EXPORT);
		opt.SetOptionValue(pfcArgument.CreateIntArgValue(PDFSelectedViewMode._PDF_VIEW_SELECT_CURRENT));
		opts.append(opt);

		if (m instanceof Drawing) {
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_EXPORT_MODE);
			opt.SetOptionValue(pfcArgument.CreateIntArgValue(PDFExportMode._PDF_2D_DRAWING));
			opts.append(opt);
		}
		else {
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_EXPORT_MODE);
			opt.SetOptionValue(pfcArgument.CreateIntArgValue(PDFExportMode._PDF_3D_AS_NAMED_VIEWS));
			opts.append(opt);
		}
		
		if (useDrawingSettings!=null && useDrawingSettings.booleanValue()) {
			System.out.println("using drawing settings");
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_FONT_STROKE);
			opt.SetOptionValue(pfcArgument.CreateIntArgValue(PDFFontStrokeMode._PDF_STROKE_ALL_FONTS));
			opts.append(opt);

			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_COLOR_DEPTH);
			opt.SetOptionValue(pfcArgument.CreateIntArgValue(PDFColorDepth._PDF_CD_GRAY));
			opts.append(opt);
		}

		if (dpi!=null) {
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_RASTER_DPI);
			opt.SetOptionValue(pfcArgument.CreateIntArgValue(dpi.intValue()));
			opts.append(opt);
		}
		
		if (height!=null && width!=null) {
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_SIZE);
			opt.SetOptionValue(pfcArgument.CreateIntArgValue(PlotPaperSize._VARIABLESIZEPLOT));
			opts.append(opt);
			
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_HEIGHT);
			opt.SetOptionValue(pfcArgument.CreateDoubleArgValue(height.doubleValue()));
			opts.append(opt);
			
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_WIDTH);
			opt.SetOptionValue(pfcArgument.CreateDoubleArgValue(width.doubleValue()));
			opts.append(opt);
		}
		
		if (sheetOpt!=null) {
			opt = pfcExport.PDFOption_Create();
			opt.SetOptionType(PDFOptionType.PDFOPT_SHEETS);
			if (SHEET_RANGE_ALL.equalsIgnoreCase(sheetOpt))
				opt.SetOptionValue(pfcArgument.CreateIntArgValue(PrintSheets._PRINT_ALL_SHEETS));
			else if (SHEET_RANGE_CURRENT.equalsIgnoreCase(sheetOpt))
				opt.SetOptionValue(pfcArgument.CreateIntArgValue(PrintSheets._PRINT_CURRENT_SHEET));
			else {
				opt.SetOptionValue(pfcArgument.CreateIntArgValue(PrintSheets._PRINT_SELECTED_SHEETS));
				opts.append(opt);

				opt = pfcExport.PDFOption_Create();
				opt.SetOptionType(PDFOptionType.PDFOPT_SHEET_RANGE);
				opt.SetOptionValue(pfcArgument.CreateStringArgValue(sheetOpt));
			}
			opts.append(opt);
		}
		
		pxi.SetOptions(opts);
		
		m.Export(filename, pxi);
		
		return null;
	}

}
