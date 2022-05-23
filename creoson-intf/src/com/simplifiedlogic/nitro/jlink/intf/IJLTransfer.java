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
package com.simplifiedlogic.nitro.jlink.intf;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.ExportResults;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLTransfer {

    public static final String TYPE_PROPROGRAM = "Pro/PROGRAM";
    public static final String TYPE_BMP        = "BMP";
    public static final String TYPE_EPS        = "EPS";
    public static final String TYPE_JPEG       = "JPEG";
    public static final String TYPE_TIFF       = "TIFF";
    public static final String TYPE_POSTSCRIPT = "POSTSCRIPT";
    public static final String TYPE_STEP       = "STEP";
    public static final String TYPE_IGES       = "IGES";
    public static final String TYPE_VRML       = "VRML";
    public static final String TYPE_CATIA      = "CATIA";
    public static final String TYPE_PV         = "PV";
    public static final String TYPE_DXF        = "DXF";
    
    public static final String EXT_ASSEMBLY   = ".asm";
    public static final String EXT_PART       = ".prt";
    public static final String EXT_MFG        = ".mfg";
    public static final String EXT_DRAWING    = ".drw";
    public static final String EXT_ASSEM_PROG = ".als";
    public static final String EXT_PART_PROG  = ".pls";
    public static final String EXT_BMP        = ".bmp";
    public static final String EXT_EPS        = ".eps";
    public static final String EXT_JPEG       = ".jpg";
    public static final String EXT_TIFF       = ".tiff";
    public static final String EXT_POSTSCRIPT = ".ps";
    public static final String EXT_PLOT       = ".plt";
    public static final String EXT_STEP       = ".stp";
    public static final String EXT_IGES       = ".igs";
    public static final String EXT_VRML       = ".wrl";
    public static final String EXT_CATIA      = ".ct";
    public static final String EXT_PV         = ".pvz";
    public static final String EXT_DXF        = ".dxf";
    public static final String EXT_PDF        = ".pdf";
    public static final String EXT_NEUTRAL    = ".neu";
    
    public static final String GEOM_DEFAULT		= "default";
    public static final String GEOM_SOLIDS		= "solids";
    public static final String GEOM_SURFACES	= "surfaces";
    public static final String GEOM_WIREFRAME	= "wireframe";
    public static final String GEOM_WIREFRAME_SURFACES	= "wireframe_surfaces";
    public static final String GEOM_QUILTS		= "quilts";

    public static final String SHEET_RANGE_ALL		= "all";
    public static final String SHEET_RANGE_CURRENT	= "current";

	public static final boolean EXPORT3D_YES = true;
	public static final boolean EXPORT3D_NO = false;
	public static final Boolean USE_DRAWING_SETTINGS_YES = Boolean.TRUE;
	public static final Boolean USE_DRAWING_SETTINGS_NO = Boolean.FALSE;

	public ExportResults exportProgram(String model, String sessionId) throws JLIException;
	public ExportResults exportProgram(String model, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportBMP(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId) throws JLIException;
	public ExportResults exportBMP(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportEPS(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId) throws JLIException;
	public ExportResults exportEPS(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportJPEG(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId) throws JLIException;
	public ExportResults exportJPEG(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportTIFF(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId) throws JLIException;
	public ExportResults exportTIFF(String model, 
			String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportSTEP(String model, 
			String filename, String dirname, String geomType, 
			boolean advanced, String sessionId) throws JLIException;
	public ExportResults exportSTEP(String model, 
			String filename, String dirname, String geomType, 
			boolean advanced, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportIGES(String model, 
			String filename, String dirname, String geomType, 
			boolean advanced, String sessionId) throws JLIException;
	public ExportResults exportIGES(String model, 
			String filename, String dirname, String geomType, 
			boolean advanced, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportVRML(String model, 
			String filename, String dirname, String sessionId) throws JLIException;
	public ExportResults exportVRML(String model, 
			String filename, String dirname, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportCATIA(String model, 
			String filename, String dirname, String sessionId) throws JLIException;
	public ExportResults exportCATIA(String model, 
			String filename, String dirname, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportPV(String model, 
			String filename, String dirname, String sessionId) throws JLIException;
	public ExportResults exportPV(String model, 
			String filename, String dirname, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportDXF(String model, 
			String filename, String dirname,  
			boolean advanced, String sessionId) throws JLIException;
	public ExportResults exportDXF(String model, 
			String filename, String dirname,  
			boolean advanced, AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportNeutral(String model, 
			String filename, String dirname, 
			boolean advanced, String sessionId) throws JLIException;
	public ExportResults exportNeutral(String model, 
			String filename, String dirname, 
			boolean advanced, AbstractJLISession sess) throws JLIException;

	public String importProgram(String dirname, String filename, String model, 
			String sessionId) throws JLIException;
	public String importProgram(String dirname, String filename, String model, 
			AbstractJLISession sess) throws JLIException;
	
	public ExportResults plot(String model, String dirname, String driver, 
			String sessionId) throws JLIException;
	public ExportResults plot(String model, String dirname, String driver, 
			AbstractJLISession sess) throws JLIException;
	
	public void mapkey(String script, int delay, 
			String sessionId) throws JLIException;
	public void mapkey(String script, int delay, 
			AbstractJLISession sess) throws JLIException;
	
	public ExportResults exportPDF(
			String model, 
			String filename, 
			String dirname, 
			boolean export3D, 
			Double height, 
			Double width, 
			Integer dpi, 
			Boolean useDrawingSettings, 
			String sheetRange, 
			String sessionId) throws JLIException;
	public ExportResults exportPDF(
			String model, 
			String filename, 
			String dirname, 
			boolean export3D, 
			Double height, 
			Double width, 
			Integer dpi, 
			Boolean useDrawingSettings, 
			String sheetRange, 
			AbstractJLISession sess) throws JLIException;

	public String importPV(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			String sessionId) throws JLIException;
	public String importPV(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			AbstractJLISession sess) throws JLIException;

	public String importSTEP(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			String sessionId) throws JLIException;
	public String importSTEP(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			AbstractJLISession sess) throws JLIException;

	public String importNeutral(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			String sessionId) throws JLIException;
	public String importNeutral(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			AbstractJLISession sess) throws JLIException;

	public String importIGES(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			String sessionId) throws JLIException;
	public String importIGES(
			String dirname, 
			String filename, 
			String newName,
			String newModelType,
			AbstractJLISession sess) throws JLIException;

}
