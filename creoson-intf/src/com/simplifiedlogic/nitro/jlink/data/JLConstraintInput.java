/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;


/**
 * Data object wrapper for JLConstraint, intended for external applications to use
 * to pass data to JShell.
 * 
 * @author Adam Andrews
 * @see JLConstraint
 */
public class JLConstraintInput implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// constraint types
    public final static String CONSTRAINT_MATE      = "mate";
    public final static String CONSTRAINT_ALIGN     = "align";
    public final static String CONSTRAINT_INSERT    = "insert";
    public final static String CONSTRAINT_ORIENT    = "orient";
    public final static String CONSTRAINT_CSYS      = "csys";
    public final static String CONSTRAINT_TANGENT   = "tangent";
    public final static String CONSTRAINT_PNTONSRF  = "pnt_on_srf";
    public final static String CONSTRAINT_EDGEONSRF = "edge_on_srf";
    public final static String CONSTRAINT_DEFPLMT   = "def_placement";
    public final static String CONSTRAINT_SUBST     = "substitute";
    public final static String CONSTRAINT_PNTONLINE = "pnt_on_line";
    public final static String CONSTRAINT_FIX       = "fix";
    public final static String CONSTRAINT_AUTO      = "auto";
    
    // datum sides
    public final static int DATUM_SIDE_NONE         = 0;
    public final static int DATUM_SIDE_RED          = 1;
    public final static int DATUM_SIDE_YELLOW       = 2;
    
    // parameters
    public final static String PARAM_TYPE           = "type";
    public final static String PARAM_ASMREF         = "asmref";
    public final static String PARAM_COMPREF        = "compref";
    public final static String PARAM_ASMDATUM       = "asmdatum";
    public final static String PARAM_COMPDATUM      = "compdatum";
    public final static String PARAM_OFFSET         = "offset";

    public String type;
    public String asmref;
    public String compref;
    public int asmDatum;
    public int compDatum;
    public Double offset;

    /**
     * @return Returns the asmDatum.
     */
    public int getAsmDatum() {
        return asmDatum;
    }
    /**
     * @param asmDatum The asmDatum to set.
     */
    public void setAsmDatum(int asmDatum) {
        this.asmDatum = asmDatum;
    }
    /**
     * @return Returns the asmref.
     */
    public String getAsmref() {
        return asmref;
    }
    /**
     * @param asmref The asmref to set.
     */
    public void setAsmref(String asmref) {
        this.asmref = asmref;
    }
    /**
     * @return Returns the compDatum.
     */
    public int getCompDatum() {
        return compDatum;
    }
    /**
     * @param compDatum The compDatum to set.
     */
    public void setCompDatum(int compDatum) {
        this.compDatum = compDatum;
    }
    /**
     * @return Returns the compref.
     */
    public String getCompref() {
        return compref;
    }
    /**
     * @param compref The compref to set.
     */
    public void setCompref(String compref) {
        this.compref = compref;
    }
    /**
     * @return Returns the offset.
     */
    public Double getOffset() {
        return offset;
    }
    /**
     * @param offset The offset to set.
     */
    public void setOffset(Double offset) {
        this.offset = offset;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

}
