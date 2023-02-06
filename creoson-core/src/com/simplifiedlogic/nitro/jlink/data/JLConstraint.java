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
package com.simplifiedlogic.nitro.jlink.data;

import com.ptc.pfc.pfcBase.DatumSide;
import com.ptc.pfc.pfcComponentFeat.ComponentConstraintType;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Data object representing a Creo constraint for assembling components into assemblies.
 * 
 * @author Adam Andrews
 *
 */
public class JLConstraint {

    public int type;
    public String asmref;
    public String compref;
    public int asmDatum;
    public int compDatum;
    public Double offset;
    
    /**
     * Default constructor, made private to disable it
     */
    private JLConstraint() {
        
    }

    /**
     * Constructor which takes the input wrapper class 
     * @param input The external-input version of JLConstraint
     * @throws JLIException
     */
    public JLConstraint(JLConstraintInput input) throws JLIException {
        String typeString = input.getType();
        asmref = input.getAsmref();
        compref = input.getCompref();
        int asmDatumIn = input.getAsmDatum();
        int compDatumIn = input.getCompDatum();
        offset = input.getOffset();
        
        typeString = typeString.toLowerCase();
        if (typeString.equals(JLConstraintInput.CONSTRAINT_MATE)) {
            if (offset!=null)
                type = ComponentConstraintType._ASM_CONSTRAINT_MATE_OFF;
            else
                type = ComponentConstraintType._ASM_CONSTRAINT_MATE;
        }
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_ALIGN)) {
            if (offset!=null)
                type = ComponentConstraintType._ASM_CONSTRAINT_ALIGN_OFF;
            else
                type = ComponentConstraintType._ASM_CONSTRAINT_ALIGN;
        }
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_INSERT)) type = ComponentConstraintType._ASM_CONSTRAINT_INSERT;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_ORIENT)) type = ComponentConstraintType._ASM_CONSTRAINT_ORIENT;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_CSYS)) type = ComponentConstraintType._ASM_CONSTRAINT_CSYS;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_TANGENT)) type = ComponentConstraintType._ASM_CONSTRAINT_TANGENT;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_PNTONSRF)) type = ComponentConstraintType._ASM_CONSTRAINT_PNT_ON_SRF;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_EDGEONSRF)) type = ComponentConstraintType._ASM_CONSTRAINT_EDGE_ON_SRF;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_DEFPLMT)) type = ComponentConstraintType._ASM_CONSTRAINT_DEF_PLACEMENT;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_SUBST)) type = ComponentConstraintType._ASM_CONSTRAINT_SUBSTITUTE;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_PNTONLINE)) type = ComponentConstraintType._ASM_CONSTRAINT_PNT_ON_LINE;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_FIX)) type = ComponentConstraintType._ASM_CONSTRAINT_FIX;
        else if (typeString.equals(JLConstraintInput.CONSTRAINT_AUTO)) type = ComponentConstraintType._ASM_CONSTRAINT_AUTO;
        else 
            throw new JLIException("Invalid constraint type:" + typeString);
        
        switch (asmDatumIn) {
	        case JLConstraintInput.DATUM_SIDE_RED:
	            asmDatum = DatumSide._DATUM_SIDE_RED;
	            break;
	        case JLConstraintInput.DATUM_SIDE_YELLOW:
	            asmDatum = DatumSide._DATUM_SIDE_YELLOW;
	            break;
	        case JLConstraintInput.DATUM_SIDE_NONE:
	            asmDatum = DatumSide._DATUM_SIDE_NONE;
	            break;
	        default:
	            throw new JLIException("Invalid value for asmDatum in constraint: " + asmDatumIn);
	    }
	    switch (compDatumIn) {
	        case JLConstraintInput.DATUM_SIDE_RED:
	            compDatum = DatumSide._DATUM_SIDE_RED;
	            break;
	        case JLConstraintInput.DATUM_SIDE_YELLOW:
	        	compDatum = DatumSide._DATUM_SIDE_YELLOW;
	            break;
	        case JLConstraintInput.DATUM_SIDE_NONE:
	        	compDatum = DatumSide._DATUM_SIDE_NONE;
	            break;
	        default:
	            throw new JLIException("Invalid value for compDatum in constraint: " + compDatumIn);
	    }
    }

    /**
     * Create a new JLConstraint which is a copy of the current one.
     * 
     * @return A new constraint object
     */
    public JLConstraint copy() {
        JLConstraint con = new JLConstraint();
        con.type = this.type;
        con.asmref = this.asmref;
        con.compref = this.compref;
        con.asmDatum = this.asmDatum;
        con.compDatum = this.compDatum;
        con.offset = this.offset;
        
        return con;
    }
}