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
package com.simplifiedlogic.nitro.jlink.impl;

import java.util.ArrayList;
import java.util.List;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitAbort;
import com.ptc.pfc.pfcExceptions.XToolkitBadInputs;
import com.ptc.pfc.pfcExceptions.XToolkitInUse;
import com.ptc.pfc.pfcExceptions.XToolkitNotExist;
import com.ptc.pfc.pfcFamily.FamilyColumnType;
import com.ptc.pfc.pfcFeature.FeatureType;
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcModelItem.ParamValueType;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.DataUtils;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallCompModelReplace;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentFeat;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyMember;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableColumn;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableColumns;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableRow;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableRows;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatureOperations;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatures;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallRegenInstructions;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.FamTableInstance;
import com.simplifiedlogic.nitro.jlink.data.FamilyTableGetRowReturn;
import com.simplifiedlogic.nitro.jlink.data.FamilyTableRowColumns;
import com.simplifiedlogic.nitro.jlink.intf.IJLFamilyTable;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.InstanceLooper;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.ModelLooper;

/**
 * @author Adam Andrews
 */
public class JLFamilyTable implements IJLFamilyTable {

    private static final String TYPE_PARAM = "param";
    private static final String TYPE_DIMENSION = "dimension";
    private static final String TYPE_SYSTEM_PARAM = "param_sys";
    private static final String TYPE_UNSUPPORTED = "not_supported";
    private static final String TYPE_MASSPROPS_PARAM = "param_massprops";
    private static final String TYPE_FEATURE_PARAM = "param_feature";
    private static final String TYPE_EDGE_PARAM = "param_edge";
    private static final String TYPE_SURFACE_PARAM = "param_surface";
    private static final String TYPE_CURVE_PARAM = "param_curve";
    private static final String TYPE_COMP_CURVE_PARAM = "param_comp_curve";
    private static final String TYPE_QUILT_PARAM = "param_quilt";
    private static final String TYPE_ANNOT_ELEM_PARAM = "param_annot_elem";

/*
    public int actionCreate(Hashtable cmd, Hashtable out_cmd, JLISession sess) throws JLIException,Exception {
        if (sess.isNoConnect())
            return 0;
        
        String modelname = checkStringParameter(cmd, PARAM_MODEL, false);
        
        JLGlobal.loadLibrary();

        Session session = JLConnection.getJLSession(sess.getConnectionId());
        if (session == null)
            return 1;

//        Model m;
//        m = JlinkUtils.getFile(session, modelname, true);
//
//        if (!(m instanceof Solid))
//            throw new JLIException("File '" + m.getFileName() + "' must be a solid.");
//        
//        Solid solid = (Solid)m;
        Solid solid = getModelSolid(session, modelname);
        
        FamilyMember fmbr = solid;
        
        FamilyTableRows rows = fmbr.ListRows();
//        FamilyTableColumns cols = fmbr.ListColumns();
        if (rows!=null && rows.getarraysize()>0)
            throw new JLIException("A family table already exists for this part");

//        if (rows!=null) {
//            int len = rows.getarraysize();
//            FamilyTableRow onerow = null;
//            for (int i=0; i<len; i++) {
//                onerow = rows.get(i);
//                System.out.println("name: " + onerow.GetInstanceName());
//            }
//        }
        return 0;
    }
*/

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#exists(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Boolean exists(
			String modelname,
			String instname,
			String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

		return exists(modelname, instname, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#exists(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
	public Boolean exists(
			String modelname,
			String instname,
			AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.exists", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (instname==null || instname.trim().length()==0)
    		throw new JLIException("No instance name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	        
	        CallFamilyTableRows rows = fmbr.listRows();
	
	        if (rows!=null) {
	            int len = rows.getarraysize();
	            CallFamilyTableRow onerow = null;
	            for (int i=0; i<len; i++) {
	                onerow = rows.get(i);
	                if (onerow.getInstanceName().equals(instname)) {
	                    return Boolean.TRUE;
	                }
	            }
	        }
	        return Boolean.FALSE;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.exists", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#delete(java.lang.String, java.lang.String)
	 */
    public void delete(
    		String modelname,
    		String sessionId) throws JLIException {
    
        JLISession sess = JLISession.getSession(sessionId);

    	delete(modelname, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#delete(java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void delete(
    		String modelname,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.delete", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        DeleteLooper looper = new DeleteLooper();
        	looper.setNamePattern(modelname);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.loop();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.delete", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#deleteInst(java.lang.String, java.lang.String, java.lang.String)
	 */
    public void deleteInst(
    		String modelname,
    		String instname,
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	deleteInst(modelname, instname, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#deleteInst(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void deleteInst(
    		String modelname,
    		String instname,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.delete_inst: " + instname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (instname==null || instname.trim().length()==0)
    		throw new JLIException("No instance name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	
	        // find and remove row
	        CallFamilyTableRow row = fmbr.getRow(instname);
	        if (row==null)
	            throw new JLIException("Instance name '" + instname + "' was not found in family table");
	        
	        fmbr.removeRow(row);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.delete_inst,"+instname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#getRow(java.lang.String, java.lang.String, java.lang.String)
	 */
    public FamilyTableGetRowReturn getRow(
    		String modelname,
    		String instname, 
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	return getRow(modelname, instname, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#getRow(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public FamilyTableGetRowReturn getRow(
    		String modelname,
    		String instname, 
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.get_row: " + instname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (instname==null || instname.trim().length()==0)
    		throw new JLIException("No instance name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	
	        CallFamilyTableRow row = fmbr.getRow(instname);
	        if (row==null)
	            throw new JLIException("Instance name '" + instname + "' was not found in family table");
	        
	        List<FamilyTableRowColumns> instVector = new ArrayList<FamilyTableRowColumns>();
	        CallFamilyTableColumns cols = fmbr.listColumns();
	        FamilyTableRowColumns colvals = null;
	        if (cols!=null) {
	            int len = cols.getarraysize();
	            for (int i=0; i<len; i++) {
	                colvals = new FamilyTableRowColumns();
	                try {
	                    getCellInfo(fmbr, row, cols.get(i), colvals);
	                    instVector.add(colvals);
	                }
	                catch (jxthrowable jxe) {
	                    throw JlinkUtils.createException(jxe, "A PTC error has occurred when retrieving column " + cols.get(i).getSymbol());
	                }
	            }
	        }
	        
	        FamilyTableGetRowReturn result = new FamilyTableGetRowReturn();
	        result.setColumns(instVector);
	        result.setInstanceName(instname);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.get_row,"+instname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#getCell(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    public FamilyTableRowColumns getCell(
    		String modelname,
    		String instname,
    		String colid,
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	return getCell(modelname, instname, colid, sess);
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#getCell(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public FamilyTableRowColumns getCell(
    		String modelname,
    		String instname,
    		String colid,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.get_cell: " + instname + ", " + colid, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (instname==null || instname.trim().length()==0)
    		throw new JLIException("No instance name parameter given");
    	if (colid==null || colid.trim().length()==0)
    		throw new JLIException("No column id parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	
	        CallFamilyTableRow row = fmbr.getRow(instname);
	        if (row==null)
	            throw new JLIException("Instance name '" + instname + "' was not found in family table");
	        
	        CallFamilyTableColumn col = fmbr.getColumn(colid);
	        if (col==null)
	            throw new JLIException("Column '" + colid + "' was not found in family table");
	
	        FamilyTableRowColumns outdata = new FamilyTableRowColumns();
	        getCellInfo(fmbr, row, col, outdata);
	
	        return outdata;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.get_cell,"+instname+","+colid, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#setCell(java.lang.String, java.lang.String, java.lang.String, java.lang.Object, java.lang.String)
	 */
    public void setCell(
    		String modelname,
    		String instname,
    		String colid,
    		Object value,
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	setCell(modelname, instname, colid, value, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#setCell(java.lang.String, java.lang.String, java.lang.String, java.lang.Object, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void setCell(
    		String modelname,
    		String instname,
    		String colid,
    		Object value,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.set_cell: " + instname + ", " + colid + "=" + value, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (instname==null || instname.trim().length()==0)
    		throw new JLIException("No instance name parameter given");
    	if (colid==null || colid.trim().length()==0)
    		throw new JLIException("No column id parameter given");
    	if (value==null || value.toString().trim().length()==0)
    		throw new JLIException("No value parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	
	        CallFamilyTableRow row = fmbr.getRow(instname);
	        if (row==null)
	            throw new JLIException("Instance name '" + instname + "' was not found in family table");
	        
	        CallFamilyTableColumn col = fmbr.getColumn(colid);
	        if (col==null)
	            throw new JLIException("Column '" + colid + "' was not found in family table");
	
	        CallParamValue pval = fmbr.getCell(col, row);
	        if (pval==null)
	            throw new JLIException("Unable to get current value for column");
	        
	        int type = pval.getParamValueType();
	        try
	        {
		        switch (type) {
		            case ParamValueType._PARAM_STRING:
		                pval.setStringValue(DataUtils.getStringValue(value));
		                break;
		            case ParamValueType._PARAM_DOUBLE:
		                pval.setDoubleValue(DataUtils.getDoubleValue(value));
		                break;
		            case ParamValueType._PARAM_INTEGER:
		                pval.setIntValue(DataUtils.getIntValue(value));
		                break;
		            case ParamValueType._PARAM_BOOLEAN:
		                pval.setBoolValue(DataUtils.getBooleanValue(value));
		                break;
		//            case ParamValueType._PARAM_NOTE:
		//                break;
		        }
	        }
	    	catch (Exception e) {
	    		throw new JLIException("Unable to set data value " + value + " for type " + type + ": " + e.getMessage());
	    	}
	        fmbr.setCell(col, row, pval);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.set_cell,"+instname+","+colid+","+value, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#getHeader(java.lang.String, java.lang.String)
	 */
    public List<FamilyTableRowColumns> getHeader(
    		String modelname,
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	return getHeader(modelname, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#getHeader(java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public List<FamilyTableRowColumns> getHeader(
    		String modelname,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.get_header", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	
	        List<FamilyTableRowColumns> instVector = new ArrayList<FamilyTableRowColumns>();
	        CallFamilyTableColumns cols = fmbr.listColumns();
	        FamilyTableRowColumns colvals = null;
	        String coltype = null;
	        if (cols!=null) {
	            int len = cols.getarraysize();
	            CallFamilyTableColumn onecol = null;
	            for (int i=0; i<len; i++) {
	                colvals = new FamilyTableRowColumns();
	                onecol = cols.get(i);
	                try {
	                    colvals.setColid(onecol.getSymbol());
	                    int ptype = onecol.getType();
	                    coltype = columnTypeToString(ptype);
	                    colvals.setColumnType(coltype);
	                    instVector.add(colvals);
	                }
	                catch (jxthrowable jxe) {
	                    throw JlinkUtils.createException(jxe, "A PTC error has occurred when retrieving column " + onecol.getSymbol());
	                }
	            }
	        }
	        
	        return instVector;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.get_header", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#addInst(java.lang.String, java.lang.String, java.lang.String)
	 */
    public void addInst(
    		String modelname,
    		String newname,
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	addInst(modelname, newname, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#addInst(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void addInst(
    		String modelname,
    		String newname,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.add_inst: " + newname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (newname==null || newname.trim().length()==0)
    		throw new JLIException("No instance name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	        
	        CallFamilyTableRows rows = fmbr.listRows();
	        CallFamilyTableRow onerow = null;
	        if (rows!=null) {
		        int len = rows.getarraysize();
		        for (int i=0; i<len; i++) {
		            onerow = rows.get(i);
		            if (newname.equalsIgnoreCase(onerow.getInstanceName()))
		                throw new JLIException("Instance '" + newname + "' already exists in family table");  // TODO: or modify the existing one?
		        }
	        }
	        
	        onerow = fmbr.addRow(newname, null);
	        if (onerow==null)
	            throw new JLIException("Failed to create new instance " + newname);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.add_inst,"+newname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
/*    public int actionInstantiate(Hashtable cmd, Hashtable out_cmd, JLISession sess) throws JLIException,Exception {
        if (sess.isNoConnect())
            return 0;
        
        String modelname = checkStringParameter(cmd, PARAM_MODEL, false);
        String newname = checkStringParameter(cmd, PARAM_INSTANCE, true);
        
        JLGlobal.loadLibrary();

        Session session = JLConnection.getJLSession(sess.getConnectionId());
        if (session == null)
            return 1;

        FamilyMember fmbr = solid;
        
        FamilyTableRows rows = fmbr.ListRows();
    	  FamilyTableRow onerow = null;
    	  if (rows!=null) {
	          int len = rows.getarraysize();
	          for (int i=0; i<len; i++) {
	              onerow = rows.get(i);
	              if (newname.equalsIgnoreCase(onerow.GetInstanceName()))
	                  throw new JLIException("Instance '" + newname + "' already exists in family table");  // TODO: or modify the existing one?
	          }
		  }
        
        onerow = fmbr.AddRow(newname, null);
        
        FamilyTableColumns cols = fmbr.ListColumns();
    	  if (cols!=null) {
	        len = cols.getarraysize();
	        FamilyTableColumn onecol = null;
	        for (int i=0; i<len; i++) {
	            onecol = cols.get(i);
	            String sym = onecol.GetSymbol();
	            FamilyColumnType t = onecol.GetType();
	            switch (t.getValue()) {
	                case FamilyColumnType._FAM_USER_PARAM:
	                    Parameter param = solid.GetParam(sym);
	                    if (param==null)
	                        continue;
	                    int ptype = ((FamColParam)onecol).GetRefParam().GetValue().Getdiscr().getValue();
	                    ParamValue pval = null;
	                    switch (ptype) {
	                        case ParamValueType._PARAM_STRING:
	                            pval = pfcModelItem.CreateStringParamValue(param.GetValue().GetStringValue());
	                            System.out.println(sym + " = " + param.GetValue().GetStringValue());
	                            break;
	                        case ParamValueType._PARAM_DOUBLE:
	                            pval = pfcModelItem.CreateDoubleParamValue(param.GetValue().GetDoubleValue());
	                            System.out.println(sym + " = " + param.GetValue().GetDoubleValue());
	                            break;
	                        case ParamValueType._PARAM_INTEGER:
	                            pval = pfcModelItem.CreateIntParamValue(param.GetValue().GetIntValue());
	                            System.out.println(sym + " = " + param.GetValue().GetIntValue());
	                            break;
	                        case ParamValueType._PARAM_BOOLEAN:
	                            pval = pfcModelItem.CreateBoolParamValue(param.GetValue().GetBoolValue());
	                            System.out.println(sym + " = " + param.GetValue().GetBoolValue());
	                            break;
	                        case ParamValueType._PARAM_NOTE:
	                            pval = pfcModelItem.CreateNoteParamValue(param.GetValue().GetNoteId());
	                            System.out.println(sym + " = " + param.GetValue().GetNoteId());
	                            break;
	                    }
	                    if (pval!=null)
	                        fmbr.SetCell(onecol, onerow, pval);
	                    break;
	                case FamilyColumnType._FAM_DIMENSION:
	                    break;
	                case FamilyColumnType._FAM_IPAR_NOTE:
	                    break;
	                case FamilyColumnType._FAM_FEATURE:
	                    break;
	                case FamilyColumnType._FAM_ASMCOMP:
	                    break;
	                case FamilyColumnType._FAM_UDF:
	                    break;
	                case FamilyColumnType._FAM_ASMCOMP_MODEL:
	                    break;
	                case FamilyColumnType._FAM_GTOL:
	                    break;
	                case FamilyColumnType._FAM_TOL_PLUS:
	                    break;
	                case FamilyColumnType._FAM_TOL_MINUS:
	                    break;
	                case FamilyColumnType._FAM_TOL_PLUSMINUS:
	                    break;
	                case FamilyColumnType._FAM_SYSTEM_PARAM:
	                    break;
	                case FamilyColumnType._FAM_EXTERNAL_REFERENCE:
	                    break;
	                case FamilyColumnType._FAM_MERGE_PART_REF:
	                    break;
	                case FamilyColumnType._FAM_MASS_PROPS_USER_PARAM:
	                    break;
	                case FamilyColumnType._FAM_MASS_PROPS_SOURCE:
	                    break;
	                case FamilyColumnType._FAM_INH_PART_REF:
	                    break;
	                case FamilyColumnType._FAM_SIM_OBJ:
	                    break;
	                case FamilyColumnType._FAM_FEATURE_PARAM:
	                    break;
	                case FamilyColumnType._FAM_EDGE_PARAM:
	                    break;
	                case FamilyColumnType._FAM_SURFACE_PARAM:
	                    break;
	                case FamilyColumnType._FAM_CURVE_PARAM:
	                    break;
	                case FamilyColumnType._FAM_COMP_CURVE_PARAM:
	                    break;
	                case FamilyColumnType._FAM_QUILT_PARAM:
	                    break;
	                case FamilyColumnType._FAM_ANNOT_ELEM_PARAM:
	                    break;
	            }
	        }
		  }
        
        return 0;
    }
*/

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#replace(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.lang.String)
	 */
    public void replace(
    		String modelname,
    		String oldname,
    		String oldinst,
//    		String newname,
    		String newinst,
    		List<Integer> path,
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	replace(modelname, oldname, oldinst, newinst, path, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#replace(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void replace(
    		String modelname,
    		String oldname,
    		String oldinst,
//    		String newname,
    		String newinst,
    		List<Integer> path,
    		AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("familytable.replace: " + oldinst + " to " + newinst, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (oldname==null || oldname.trim().length()==0)
    		throw new JLIException("No original instance name parameter given");
    	if (newinst==null || newinst.trim().length()==0)
    		throw new JLIException("No new instance name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (oldinst==null && path==null)
	            throw new JLIException("Must specify either current instance or path arguments");
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallSolid wrapping_solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallSolid base_solid = (CallSolid)session.getModelFromFileName(oldname);
	        if (base_solid==null)
	            throw new JLIException("Cannot find model '" + oldname + "' in session");
	        
	        CallFamilyTableRow row = base_solid.getRow(newinst);
	        if (row==null)
	            throw new JLIException("No instance '" + newinst + "' in model " + oldname);
	        
	        CallSolid new_solid = (CallSolid)row.createInstance();
	
	        // required for WF5 and higher
	        boolean resolveMode = JlinkUtils.prefixResolveModeFix(session);

	        try {
		        if (path!=null)
		            navigateReplace(session, wrapping_solid, path, new_solid);
		        else
		        	walkReplace(session, wrapping_solid, oldinst, new_solid, "  ");
	        }
	        finally {
	        	JlinkUtils.postfixResolveModeFix(session, resolveMode);
	        }
	
	        CallWindow win = session.getModelWindow(wrapping_solid);
	        if (win!=null)
	            win.refresh();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.replace,"+oldinst+","+newinst, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /**
     * Recursively walk the assembly to replace one instance with another.  The walk is done postfix.  The window will be refreshed at the end.
     * @param session The Creo session
     * @param wrapping_solid The parent component whose children are being examined
     * @param oldinst The name of the old instance
     * @param new_solid The new solid to use to replace the old instance
     * @param indent The indentation level for debugging output
     * @throws JLIException
     * @throws jxthrowable
     */
    private void walkReplace(CallSession session, CallSolid wrapping_solid, String oldinst, CallSolid new_solid, String indent) throws JLIException, jxthrowable {
    	// list the child components of the current component
        CallFeatures components = wrapping_solid.listFeaturesByType(Boolean.FALSE, FeatureType.FEATTYPE_COMPONENT);
        if (components==null)
            return;
        int len = components.getarraysize();
        if (len==0) return;
        CallFeatureOperations replaceOps = null;

        CallComponentFeat component;
        CallCompModelReplace replace;
        String instname;
        // for each child component,
        for (int i=0; i<len; i++) {
            component = (CallComponentFeat)components.get(i);
            try {
                instname = component.getModelDescr().getInstanceName();
                int type = component.getModelDescr().getType();
                CallModel child = session.getModelFromDescr(component.getModelDescr());
    
                // recurse into the child components
                if (type==ModelType._MDL_ASSEMBLY) {
                    if (child!=null && child instanceof CallSolid) {
                        walkReplace(session, (CallSolid)child, oldinst, new_solid, indent+"  ");
                    }
                }
            
                // accumulate replace operations
                // FIXME: need to check that base name is same as input curmodel
                if (instname.equalsIgnoreCase(oldinst)) {
                    replace = component.createReplaceOp(new_solid);
                    if (replaceOps==null)
                        replaceOps = CallFeatureOperations.create();
                    replaceOps.insert(0, replace);
                }
            }
            catch (jxthrowable jxe) {
                throw JlinkUtils.createException(jxe, "A PTC error has occurred when retriving component " + component.getModelDescr().getFileName());
            }
        }
        // perform the replace operations that have been accumulated for the children of this solid
        if (replaceOps!=null && replaceOps.getarraysize()>0) {
            CallRegenInstructions inst = CallRegenInstructions.create(Boolean.FALSE, null, null); 
            wrapping_solid.executeFeatureOps(replaceOps, inst);
            
            CallWindow win = session.getModelWindow(wrapping_solid);
            if (win!=null)
            	win.refresh();
        }
    }
    
    /**
     * Navigate directly to a component specified by a component path and replace it with another instance.  The window will be refreshed at the end.
     * @param session The Creo session
     * @param wrapping_solid The parent component whose children are being examined
     * @param path The component path for the component to replace
     * @param new_solid The new solid to use to replace the old instance
     * @throws JLIException
     * @throws jxthrowable
     */
    private void navigateReplace(CallSession session, CallSolid wrapping_solid, List<Integer> path, CallSolid new_solid) throws JLIException,jxthrowable {
    	CallFeatureOperations replaceOps = null;
    	CallCompModelReplace replace;

        int len = path.size();
        Integer id;
        CallSolid parentSolid = wrapping_solid;
        CallComponentFeat componentF = null;
        CallModel child = null;
        CallFeature component;
        // find the component identified by the path
        for (int i=0; i<len; i++) {
            id = path.get(i);
            component = null;
            try {
            	component = parentSolid.getFeatureById(id.intValue());
            } 
            catch (XToolkitBadInputs e) {}  // ignore this exception because it's thrown when the ID is not found
            catch (XToolkitNotExist e) {}  // ignore this exception because it's thrown when the ID is not found
            if (component==null || !(component instanceof CallComponentFeat))
                throw new JLIException("Could not find child component #" + id +
                        (componentF==null ? " of root component" : " of component #" + componentF.getId()));
            componentF = (CallComponentFeat)component;
            if (i<len-1) {
                child = session.getModelFromDescr(componentF.getModelDescr());
                if (!(child instanceof CallSolid)) 
                    throw new JLIException("component #" + id + " is not a solid");
                parentSolid = (CallSolid)child;
            }
        }
        // create a replace operation
        replace = componentF.createReplaceOp(new_solid);
        if (replaceOps==null)
            replaceOps = CallFeatureOperations.create();
        replaceOps.insert(0, replace);
        
        // perform the replace operation
        if (replaceOps!=null && replaceOps.getarraysize()>0) {
            CallRegenInstructions inst = CallRegenInstructions.create(Boolean.FALSE, null, null); 
            parentSolid.executeFeatureOps(replaceOps, inst);
            
            CallWindow win = session.getModelWindow(parentSolid);
            if (win!=null)
                win.refresh();
        }
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#list(java.lang.String, java.lang.String)
	 */
    public List<String> list(
    		String modelname, 
    		String namePattern, 
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	return list(modelname, namePattern, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#list(java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public List<String> list(
    		String modelname,
    		String namePattern, 
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.list", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	//        FamilyMember fmbr = solid.GetParent();
	//        if (fmbr==null) fmbr = solid;
	        CallFamilyMember fmbr = solid;
	        
	        ListLooper looper = new ListLooper();
	        looper.setNamePattern(namePattern);
	        looper.setGetColumns(false);
	        
	        looper.loop(fmbr);
	        
	        if (looper.output==null)
	        	return new ArrayList<String>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.list", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFamilyTable#listTree(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public FamTableInstance listTree(String modelname, boolean erase, String sessionId)
			throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	return listTree(modelname, erase, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFamilyTable#listTree(java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public FamTableInstance listTree(String modelname, boolean erase, AbstractJLISession sess)
			throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.list_tree", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        FamTableInstance root = new FamTableInstance(solid.getFileName());
	        getInstanceTree(solid, root);
	        
	        if (erase) {
                solid.erase();
                session.eraseUndisplayedModels();
	        }
	        return root;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.list_tree", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
    
    /**
     * Recursively walk a nested family table to find all rows in the table
     * @param s The solid which owns the family table
     * @param parent The current parent instance in the walk
     * @throws jxthrowable
     */
    public static void getInstanceTree(CallSolid s, FamTableInstance parent) throws jxthrowable {
        CallFamilyTableRows rows = null;
        try {
            rows = s.listRows();
        }
        catch (XToolkitBadInputs e) {
            // maybe bad inputs just means that the table has been deleted?
            return;
        }
        if (rows!=null && rows.getarraysize()>0) {
            int len = rows.getarraysize();
            CallFamilyTableRow onerow = null;
            for (int i=0; i<len; i++) {
                onerow = rows.get(i);
                FamTableInstance newinst = new FamTableInstance(onerow.getInstanceName());
                parent.addChild(newinst);
                parent.setTotal(parent.getTotal()+1);
                
                CallModel child = onerow.createInstance();
                if (child instanceof CallSolid) {
                    getInstanceTree((CallSolid)child, newinst);
                }
                parent.setTotal(parent.getTotal()+newinst.getTotal());
                try {
                    child.erase();
                } 
                catch (XToolkitInUse e) { // caused when erasing a model that's a dependency of another
                }
            }
        }
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFamilyTable#createInstance(java.lang.String, java.lang.String, java.lang.String)
     */
    public String createInstance(
    		String modelname, 
    		String instname,
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	return createInstance(modelname, instname, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFamilyTable#createInstance(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public String createInstance(
    		String modelname,
    		String instname,
    		AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("familytable.create_instance: " + instname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (instname==null)
	            throw new JLIException("Must specify an instance argument");
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        
	        CallFamilyMember fmbr = solid;
	        
	        CallFamilyTableRows rows = fmbr.listRows();
	        if (rows!=null) {
		        int len = rows.getarraysize();
		        CallFamilyTableRow onerow = null;
		        for (int i=0; i<len; i++) {
		            onerow = rows.get(i);
		            if (instname.equalsIgnoreCase(onerow.getInstanceName())) {
		            	CallModel child = onerow.createInstance();
		            	return child.getFileName();
		            }
		        }
	        }
	        
	        return null;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.create_instance,"+instname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFamilyTable#getParents(java.lang.String, java.lang.String)
     */
    public List<String> getParents(
    		String modelname, 
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);

    	return getParents(modelname, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFamilyTable#list(java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public List<String> getParents(
    		String modelname, 
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("familytable.get_parents", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallModel m = JlinkUtils.getFile(session, modelname, true);
	        if (!(m instanceof CallFamilyMember))
	        	return null;
	        CallFamilyMember fmbr = (CallFamilyMember)m;

	        List<String> instVector = new ArrayList<String>();
	        CallModel parent;
	        try {
	        	CallModelDescriptor desc = fmbr.getImmediateGenericInfo();
		        while (desc!=null) {
		        	instVector.add(0, desc.getFileName());
		        	parent = session.getModelFromDescr(desc);
		        	if (!(parent instanceof CallFamilyMember))
		        		break;
		        	desc = ((CallFamilyMember)parent).getImmediateGenericInfo();
		        }
	        }
	        catch (XToolkitBadInputs e) {
	        	// end of chain
	        }
	        
	        if (instVector.size()==0)
	        	return null;
	        return instVector;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("familytable.get_parents", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /**
     * Retrieve data about one cell in a family table
     * @param fmbr The component that owns the family table
     * @param row The row object for the cell
     * @param col The column object for the cell
     * @param out The output object to receive the data
     * @throws JLIException
     * @throws jxthrowable
     */
    private void getCellInfo(CallFamilyMember fmbr, CallFamilyTableRow row, CallFamilyTableColumn col, FamilyTableRowColumns out) throws JLIException,jxthrowable {
        if (row==null || col==null || out==null)
            return;
        
        CallParamValue pval = fmbr.getCell(col, row);
        Object value = null;
        int type = 0;
        
        switch (pval.getParamValueType()) {
            case ParamValueType._PARAM_STRING: 
                value = pval.getStringValue();
                type = FamilyTableRowColumns.DATA_TYPE_STRING;
                break;
            case ParamValueType._PARAM_INTEGER: 
                value = new Integer(pval.getIntValue());
                type = FamilyTableRowColumns.DATA_TYPE_INTEGER;
                break;
            case ParamValueType._PARAM_BOOLEAN: 
                value = new Boolean(pval.getBoolValue());
                type = FamilyTableRowColumns.DATA_TYPE_BOOL;
                break;
            case ParamValueType._PARAM_DOUBLE: 
                value = new Double(pval.getDoubleValue());
                type = FamilyTableRowColumns.DATA_TYPE_DOUBLE;
                break;
            default:
                value = "";
                type = 0;
                break;
        }

        out.setColumnType(columnTypeToString(col.getType()));
        out.setColid(col.getSymbol());
        out.setValue(value);
        out.setDataType(type);
    }

    /**
     * Convert a Creo FamilyColumnType to a string equivalent.  Not all types are supported.
     * @param ptype The int value of a FamilyColumnType
     * @return A string representing the type
     */
    private String columnTypeToString(int ptype) {
        String coltype = null;
        switch (ptype) {

            case FamilyColumnType._FAM_USER_PARAM: coltype = TYPE_PARAM; break;
            case FamilyColumnType._FAM_DIMENSION: coltype = TYPE_DIMENSION; break;
            case FamilyColumnType._FAM_SYSTEM_PARAM: coltype = TYPE_SYSTEM_PARAM; break;
            case FamilyColumnType._FAM_MASS_PROPS_USER_PARAM: coltype = TYPE_MASSPROPS_PARAM; break;
            case FamilyColumnType._FAM_FEATURE_PARAM: coltype = TYPE_FEATURE_PARAM; break;
            case FamilyColumnType._FAM_EDGE_PARAM: coltype = TYPE_EDGE_PARAM; break;
            case FamilyColumnType._FAM_SURFACE_PARAM: coltype = TYPE_SURFACE_PARAM; break;
            case FamilyColumnType._FAM_CURVE_PARAM: coltype = TYPE_CURVE_PARAM; break;
            case FamilyColumnType._FAM_COMP_CURVE_PARAM: coltype = TYPE_COMP_CURVE_PARAM; break;
            case FamilyColumnType._FAM_QUILT_PARAM: coltype = TYPE_QUILT_PARAM; break;
            case FamilyColumnType._FAM_ANNOT_ELEM_PARAM: coltype = TYPE_ANNOT_ELEM_PARAM; break;

            case FamilyColumnType._FAM_IPAR_NOTE:
            case FamilyColumnType._FAM_FEATURE:
            case FamilyColumnType._FAM_ASMCOMP:
            case FamilyColumnType._FAM_UDF:
            case FamilyColumnType._FAM_ASMCOMP_MODEL:
            case FamilyColumnType._FAM_GTOL:
            case FamilyColumnType._FAM_TOL_PLUS:
            case FamilyColumnType._FAM_TOL_MINUS:
            case FamilyColumnType._FAM_TOL_PLUSMINUS:
            case FamilyColumnType._FAM_EXTERNAL_REFERENCE:
            case FamilyColumnType._FAM_MERGE_PART_REF:
            case FamilyColumnType._FAM_MASS_PROPS_SOURCE:
            case FamilyColumnType._FAM_INH_PART_REF:
            case FamilyColumnType._FAM_SIM_OBJ:
                coltype = TYPE_UNSUPPORTED;
                break;
        }
        return coltype;
    }

    /**
     * An implementation of ModelLooper which deletes any family table from matching models
     * 
     * @author Adam Andrews
     */
    public class DeleteLooper extends ModelLooper {

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			if (!(m instanceof CallSolid))
				return false;
			CallSolid solid = (CallSolid)m;

			CallFamilyMember fmbr = solid;
	    	
	        // remove rows
			CallFamilyTableRows rows = fmbr.listRows();
	        if (rows!=null) {
	            int len = rows.getarraysize();
	            CallFamilyTableRow onerow = null;
	            for (int i=0; i<len; i++) {
	                onerow = rows.get(i);
	                try {
	                	fmbr.removeRow(onerow);
	                }
	                catch (jxthrowable jxe) {
	            		throw JlinkUtils.createException(jxe, "A PTC error has occurred when removing instance " + onerow.getInstanceName());
	                }
	            }
	        }
	  
	        // remove cols
	        CallFamilyTableColumns cols = fmbr.listColumns();
	        if (cols!=null) {
	            int len = cols.getarraysize();
	            CallFamilyTableColumn onecol = null;
	            for (int i=0; i<len; i++) {
	                onecol = cols.get(i);
	                try {
	                	fmbr.removeColumn(onecol);
	                }
	                catch (jxthrowable jxe) {
	                    // ignore XToolkitAbort exceptions due to a bug in JLink for WF3.0
	                    if (!(jxe instanceof XToolkitAbort))
	                        throw JlinkUtils.createException(jxe, "A PTC error has occurred when removing column " + onecol.getSymbol());
	                }
	            }
	            // recheck count
	            cols = fmbr.listColumns();
	            if (cols!=null && cols.getarraysize()>0)
	                throw new JLIException("Unable to delete all columns of the table");
	        }
			return false;
		}
    }

    /**
     * An implementation of InstanceLooper which a list of instance names from a family table
     * 
     * @author Adam Andrews
     *
     */
    private class ListLooper extends InstanceLooper {
    	/**
    	 * The output list of instance names
    	 */
    	List<String> output = null;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.InstanceLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableRow, com.simplifiedlogic.nitro.jlink.calls.lists.CallFamilyTableColumns)
		 */
		@Override
		public boolean loopAction(CallFamilyTableRow onerow, CallFamilyTableColumns cols) throws JLIException, jxthrowable {
	        if (output==null)
	        	output = new ArrayList<String>();
            output.add(onerow.getInstanceName());
			return false;
		}
    	
    }
}
