/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.simplifiedlogic.nitro.jlink.data.FamTableInstance;
import com.simplifiedlogic.nitro.jlink.data.FamilyTableGetRowReturn;
import com.simplifiedlogic.nitro.jlink.data.FamilyTableRowColumns;
import com.simplifiedlogic.nitro.jlink.intf.IJLFamilyTable;
import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.jshell.json.request.JLFamilyTableRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFamilyTableResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "familytable" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonFamilyTableHandler extends JLJsonCommandHandler implements JLFamilyTableRequestParams, JLFamilyTableResponseParams {

	private IJLFamilyTable ftHandler = null;

	/**
	 * @param ftHandler
	 */
	public JLJsonFamilyTableHandler(IJLFamilyTable ftHandler) {
		this.ftHandler = ftHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_EXISTS))
			return actionExists(sessionId, input);
		else if (function.equals(FUNC_DELETE))
			return actionDelete(sessionId, input);
		else if (function.equals(FUNC_DELETE_INST))
			return actionDeleteInst(sessionId, input);
		else if (function.equals(FUNC_GET_HEADER))
			return actionGetHeader(sessionId, input);
		else if (function.equals(FUNC_GET_ROW))
			return actionGetRow(sessionId, input);
		else if (function.equals(FUNC_GET_CELL))
			return actionGetCell(sessionId, input);
		else if (function.equals(FUNC_SET_CELL))
			return actionSetCell(sessionId, input);
		else if (function.equals(FUNC_ADD_INST))
			return actionAddInst(sessionId, input);
		else if (function.equals(FUNC_REPLACE))
			return actionReplace(sessionId, input);
		else if (function.equals(FUNC_LIST))
			return actionList(sessionId, input);
		else if (function.equals(FUNC_LIST_TREE))
			return actionListTree(sessionId, input);
		else if (function.equals(FUNC_CREATE_INST))
			return actionCreateInst(sessionId, input);
		else if (function.equals(FUNC_GET_PARENTS))
			return actionGetParents(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionExists(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String instname = checkStringParameter(input, PARAM_INSTANCE, true);
        
        boolean exists = ftHandler.exists(modelname, instname, sessionId);
        
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_EXISTS, exists);
       	return out;
	}

	private Hashtable<String, Object> actionDelete(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        
        ftHandler.delete(filename, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionDeleteInst(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String instname = checkStringParameter(input, PARAM_INSTANCE, true);
        
        ftHandler.deleteInst(filename, instname, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionGetRow(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String instname = checkStringParameter(input, PARAM_INSTANCE, true);
        
        FamilyTableGetRowReturn row = ftHandler.getRow(filename, instname, sessionId);
        
        if (row!=null) {
    		Hashtable<String, Object> out = new Hashtable<String, Object>();
        	out.put(OUTPUT_INSTANCE, row.getInstanceName());
        	if (row.getColumns()!=null && row.getColumns().size()>0) {
    			Vector<Map<String, Object>> outColumns = new Vector<Map<String, Object>>();
    			out.put(OUTPUT_COLUMNS, outColumns);
    			Map<String, Object> outColumn = null;
    			for (FamilyTableRowColumns col : row.getColumns()) {
    				outColumn = new Hashtable<String, Object>();
    				if (col.getColid()!=null)
    					outColumn.put(PARAM_COLID, col.getColid());
    				if (col.getValue()!=null)
    					outColumn.put(OUTPUT_VALUE, col.getValue());
    				if (col.getColumnType()!=null)
    					outColumn.put(OUTPUT_COLTYPE, col.getColumnType());
    				switch (col.getDataType()) {
    					case FamilyTableRowColumns.DATA_TYPE_BOOL : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_BOOL); break;
    					case FamilyTableRowColumns.DATA_TYPE_DOUBLE : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE); break;
    					case FamilyTableRowColumns.DATA_TYPE_INTEGER : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_INTEGER); break;
    					case FamilyTableRowColumns.DATA_TYPE_NOTE : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_NOTE); break;
    					case FamilyTableRowColumns.DATA_TYPE_STRING : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_STRING); break;
    				}
    				outColumns.add(outColumn);
    			}
        	}
    		return out;
        }

        return null;
	}

	private Hashtable<String, Object> actionGetCell(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String instname = checkStringParameter(input, PARAM_INSTANCE, true);
        String colid = checkStringParameter(input, PARAM_COLID, true);
        
        FamilyTableRowColumns col = ftHandler.getCell(filename, instname, colid, sessionId);
        
        if (col!=null) {
    		Hashtable<String, Object> out = new Hashtable<String, Object>();
			if (col.getColid()!=null)
				out.put(OUTPUT_COLID, col.getColid());
			if (col.getValue()!=null)
				out.put(OUTPUT_VALUE, col.getValue());
			if (col.getColumnType()!=null)
				out.put(OUTPUT_COLTYPE, col.getColumnType());
			switch (col.getDataType()) {
				case FamilyTableRowColumns.DATA_TYPE_BOOL : out.put(OUTPUT_DATATYPE, IJLParameter.TYPE_BOOL); break;
				case FamilyTableRowColumns.DATA_TYPE_DOUBLE : out.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE); break;
				case FamilyTableRowColumns.DATA_TYPE_INTEGER : out.put(OUTPUT_DATATYPE, IJLParameter.TYPE_INTEGER); break;
				case FamilyTableRowColumns.DATA_TYPE_NOTE : out.put(OUTPUT_DATATYPE, IJLParameter.TYPE_NOTE); break;
				case FamilyTableRowColumns.DATA_TYPE_STRING : out.put(OUTPUT_DATATYPE, IJLParameter.TYPE_STRING); break;
			}
    		return out;
        }

        return null;
	}

	private Hashtable<String, Object> actionSetCell(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String instname = checkStringParameter(input, PARAM_INSTANCE, true);
        String colid = checkStringParameter(input, PARAM_COLID, true);
        Object value = checkParameter(input, PARAM_VALUE, true);

        ftHandler.setCell(modelname, instname, colid, value, sessionId);
        
        return null;
	}

	private Hashtable<String, Object> actionGetHeader(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        
        List<FamilyTableRowColumns> cols = ftHandler.getHeader(filename, sessionId);
        
        if (cols!=null && cols.size()>0) {
    		Hashtable<String, Object> out = new Hashtable<String, Object>();
			Map<String, Object> outColumn = null;
			for (FamilyTableRowColumns col : cols) {
    			Vector<Map<String, Object>> outColumns = new Vector<Map<String, Object>>();
    			out.put(OUTPUT_COLUMNS, outColumns);
				outColumn = new Hashtable<String, Object>();
				if (col.getColid()!=null)
					outColumn.put(OUTPUT_COLID, col.getColid());
				if (col.getColumnType()!=null)
					outColumn.put(OUTPUT_COLTYPE, col.getColumnType());
				switch (col.getDataType()) {
					case FamilyTableRowColumns.DATA_TYPE_BOOL : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_BOOL); break;
					case FamilyTableRowColumns.DATA_TYPE_DOUBLE : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE); break;
					case FamilyTableRowColumns.DATA_TYPE_INTEGER : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_INTEGER); break;
					case FamilyTableRowColumns.DATA_TYPE_NOTE : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_NOTE); break;
					case FamilyTableRowColumns.DATA_TYPE_STRING : outColumn.put(OUTPUT_DATATYPE, IJLParameter.TYPE_STRING); break;
				}
				outColumns.add(outColumn);
			}
    		return out;
        }

        return null;
	}

	private Hashtable<String, Object> actionAddInst(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String instname = checkStringParameter(input, PARAM_INSTANCE, true);
        
        ftHandler.addInst(modelname, instname, sessionId);
        
       	return null;
	}

	private Hashtable<String, Object> actionReplace(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String oldname = checkStringParameter(input, PARAM_CURMODEL, true);
        String oldinst = checkStringParameter(input, PARAM_CURINST, false);
//        String newname = checkStringParameter(input, PARAM_NEWMODEL, true);
        String newinst = checkStringParameter(input, PARAM_NEWINST, true);
        Object pathObj = checkParameter(input, PARAM_PATH, false);
        List<Integer> path = getIntArray(PARAM_PATH, pathObj);

        if (oldinst==null && path==null)
            throw new JLIException("Must specify either " + PARAM_CURINST + " or " + PARAM_PATH + " arguments");
        
        ftHandler.replace(modelname, oldname, oldinst, newinst, path, sessionId);
        
       	return null;
	}

	private Hashtable<String, Object> actionList(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String namePattern = checkStringParameter(input, PARAM_INSTANCE, false);
        
        List<String> list = ftHandler.list(modelname, namePattern, sessionId);
        
        if (list!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
       		out.put(OUTPUT_INSTANCES, list);
        	return out;
        }
       	return null;
	}

	private Hashtable<String, Object> actionListTree(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        boolean erase = checkFlagParameter(input, PARAM_ERASE, false, false);
        
        FamTableInstance inst = ftHandler.listTree(modelname, erase, sessionId);
        
        if (inst!=null && inst.getTotal()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			getInstanceTree(inst, out);
        	return out;
        }
       	return null;
	}
	
	private void getInstanceTree(FamTableInstance inst, Map<String, Object> out) {
		if (inst.getName()!=null)
			out.put(OUTPUT_NAME, inst.getName());
		out.put(OUTPUT_TOTAL, inst.getTotal());
		if (inst.getChildren()!=null && inst.getChildren().size()>0) {
			List<Map<String, Object>> list = new Vector<Map<String, Object>>();
			out.put(OUTPUT_CHILDREN, list);
			Map<String, Object> rec;
			for (FamTableInstance child : inst.getChildren()) {
				rec = new Hashtable<String, Object>();
				list.add(rec);
				getInstanceTree(child, rec);
			}
		}
	}

	private Hashtable<String, Object> actionCreateInst(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String instname = checkStringParameter(input, PARAM_INSTANCE, true);
        
        String filename = ftHandler.createInstance(modelname, instname, sessionId);
        
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_NAME, filename);
       	return out;
	}

	private Hashtable<String, Object> actionGetParents(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        
        List<String> list = ftHandler.getParents(modelname, sessionId);
        
        if (list!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
       		out.put(OUTPUT_PARENTS, list);
        	return out;
        }
       	return null;
	}

}
