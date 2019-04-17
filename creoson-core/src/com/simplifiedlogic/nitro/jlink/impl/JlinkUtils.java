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
package com.simplifiedlogic.nitro.jlink.impl;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XInvalidFileName;
import com.ptc.pfc.pfcExceptions.XStringTooLong;
import com.ptc.pfc.pfcExceptions.XToolkitBadInputs;
import com.ptc.pfc.pfcExceptions.XToolkitCheckoutConflict;
import com.ptc.pfc.pfcExceptions.XToolkitCommError;
import com.ptc.pfc.pfcExceptions.XToolkitError;
import com.ptc.pfc.pfcExceptions.XToolkitInvalidName;
import com.ptc.pfc.pfcExceptions.XToolkitNotExist;
import com.ptc.pfc.pfcExceptions.XToolkitNotFound;
import com.ptc.pfc.pfcExceptions.XUnknownModelExtension;
import com.ptc.pfc.pfcFeature.FeatureStatus;
import com.ptc.pfc.pfcLayer.DisplayStatus;
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcModelItem.ParamValueType;
import com.ptc.pfc.pfcServer.Server;
import com.ptc.pfc.pfcSimpRep.SimpRepActionType;
import com.simplifiedlogic.nitro.jlink.DataUtils;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallAssembly;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallComponentPath;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentFeat;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailNoteItem;
import com.simplifiedlogic.nitro.jlink.calls.dimension.CallBaseDimension;
import com.simplifiedlogic.nitro.jlink.calls.dimension.CallDimension;
import com.simplifiedlogic.nitro.jlink.calls.dimension2d.CallDimension2D;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyMember;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.mfg.CallMFG;
import com.simplifiedlogic.nitro.jlink.calls.model.CallDependencies;
import com.simplifiedlogic.nitro.jlink.calls.model.CallDependency;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItems;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameterOwner;
import com.simplifiedlogic.nitro.jlink.calls.note.CallNote;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallIntSeq;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRep;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRepCompItemPath;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRepInstructions;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRepItem;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRepItemPath;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRepItems;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.data.JLTransform;
import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.jlink.data.SimpRepData;
import com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLFeature;
import com.simplifiedlogic.nitro.jlink.intf.IJLLayer;
import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.rpc.JlinkConnectException;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.JLMatrixMaker;

/**
 * Useful utility functions related to interacting with JLink.
 * 
 * @author Adam Andrews
 */
public class JlinkUtils {

    public static final int FILENAME_LIMIT 	= 31;

    /**
     * Get a model that is open in Creo
     * 
     * @param session The Creo session
     * @param dirname The directory name the model was loaded from
     * @param filename The filename for the model
     * @return The Model object for the file if it is in session, otherwise null
     * @throws jxthrowable
     */
    public static CallModel getFile(CallSession session, String dirname, String filename) throws jxthrowable {
    	CallModelDescriptor descr = CallModelDescriptor.createFromFileName(filename);
        descr.setPath(dirname);
        CallModel m = session.getModelFromDescr(descr);
        return m;
    }

    /**
     * Get a model that is open in Creo
     * 
     * @param session The Creo session
     * @param filename The filename for the model
     * @param errorOnNotFound Whether to throw an exception if the model was not in memory
     * @return The Model object for the file if it is in session, otherwise null
     * @throws JLIException
     * @throws jxthrowable
     */
    public static CallModel getFile(CallSession session, String filename, boolean errorOnNotFound) throws JLIException,jxthrowable {
        CallModel m = null;
        if (filename==null) {
            m = session.getCurrentModel();
            if (m==null && errorOnNotFound)
                throw new JLIException("No currently active model found");
        }
        else {
        	m = session.getModelFromFileName(filename);  // this crashes the server if you use a filename without an extension...
            if (m==null && errorOnNotFound)
                throw new JLIException("File '" + filename + "' was not open");
        }
        // TODO: check to see whether path name matches dirname?
        return m;
    }

    /**
     * Get a model that is open in Creo
     * 
     * @param sess The JShell session
     * @param filename The filename for the model
     * @param errorOnNotFound Whether to throw an exception if the model was not in memory
     * @return The Model object for the file if it is in session, otherwise null
     * @throws JLIException
     * @throws Exception
     */
    public static CallModel getFile(JLISession sess, String filename, boolean errorOnNotFound) throws JLIException,Exception {
        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
        if (session == null)
            return null;
        return getFile(session, filename, errorOnNotFound);
    }
    
	/**
	 * Get a model that is open in Creo.  Throw an exception if the model is not a Solid.
	 * @param session The Creo session
	 * @param modelname The filename for the model
	 * @return The Solid object for the file if it is in session, otherwise null
	 * @throws JLIException
	 * @throws jxthrowable
	 */
	public static CallSolid getModelSolid(CallSession session, String modelname) throws JLIException,jxthrowable {
        CallModel m;
        m = getFile(session, modelname, true);

        if (!(m instanceof CallSolid))
            throw new JLIException("File '" + m.getFileName() + "' must be a solid.");
        
        return (CallSolid)m;
    }
    
    /**
     * Get a model for a MFG model.  Throws an error if the MFG model has more 
     * than one assembly/part dependency.
     * @param session The Creo session
     * @param mfg The MFG object
     * @return The model for the MFG object
     * @throws jxthrowable
     * @throws JLIException
     */
    public static CallModel getMfgModel(CallSession session, CallMFG mfg) throws jxthrowable,JLIException {
    	CallModel result = null;
    	CallDependencies deps = mfg.listDependencies();
        if (deps!=null) {
            int len = deps.getarraysize();
            CallDependency dep;
            CallModelDescriptor desc;
            for (int i=0; i<len; i++) {
                dep = deps.get(i);
                desc = dep.getDepModel();
                //System.out.println(desc.GetFullName() + ": " + desc.GetType().getCipTypeName());
                if (desc.getType()==ModelType._MDL_ASSEMBLY || desc.getType()==ModelType._MDL_PART) {
                    if (result!=null)
                        throw new JLIException("Manufacturing Model has more than one assembly dependency.");
                    else 
                        result = session.getModelFromDescr(desc);
                }
            }
        }
        return result;
    }
    
    /**
     * Check whether a given path is relative or absolute
     * @param dirname The directory name to check (can also be a file name
     * @return True if the path is relative
     */
    public static boolean isRelativePath(String dirname) {
    	if (dirname==null)
    		return true;
        if ((Character.isLetter(dirname.charAt(0)) && dirname.charAt(1)==':') ||
                dirname.charAt(0)=='\\' ||
                dirname.charAt(0)=='/') {
        	return false;
        }
        else
        	return true;
    }
    
    /**
     * Takes a relative file/directory path, and prepends it with Creo's current working directory.
     * If the input path is not relative, then it is not modified.
     * 
     * @param session The Creo session
     * @param dirname The relative path
     * @return The absolute path generated
     * @throws JLIException
     * @throws jxthrowable
     */
    public static String resolveRelativePath(CallSession session, String dirname) throws JLIException,jxthrowable {
        if (dirname==null) {
            if (session!=null)
                return session.getCurrentDirectory();
            else
                return null;
        }
        
        if (isRelativePath(dirname)) {
            // if a relative path, get the session's current directory
            if (session != null) {
                String current = session.getCurrentDirectory();
                if (current.charAt(current.length()-1)==File.separatorChar)
                    dirname = session.getCurrentDirectory() + dirname;
                else
                	dirname = session.getCurrentDirectory() + File.separatorChar + dirname;
            }
        }
        return dirname;
    }

    /**
     * Change Creo's current working directory
     * @param session The Creo session
     * @param dir The target directory
     * @throws JLIException
     * @throws jxthrowable
     */
    public static void changeDirectory(CallSession session, String dir) throws JLIException,jxthrowable {
        if (session==null) return;
        if (dir==null || dir.length()==0) return;
        try {
        	session.changeDirectory(dir);
        }
        catch (XToolkitInvalidName jxe) {
            throw new JLIException("Unable to change to directory " + dir);
        }
    }
    
    /**
     * Determine the model type for a given file extension
     * @param ext The file extension
     * @return The Creo model type that matches the extension
     */
    public static ModelType getModelTypeForExtension(String ext) {
        if (ext==null)
            return null;
        ext = ext.toLowerCase();
        if (ext.charAt(0)=='.')
            ext = ext.substring(1);
        
        // mappings taken from pfcModel.ModelDescriptor.GetExtension() doc
             if (ext.equals("asm")) return ModelType.MDL_ASSEMBLY;
        else if (ext.equals("prt")) return ModelType.MDL_PART;
        else if (ext.equals("drw")) return ModelType.MDL_DRAWING;
        else if (ext.equals("sec")) return null; // ignore this because it's used by 2 model types
        else if (ext.equals("lay")) return ModelType.MDL_LAYOUT;
        else if (ext.equals("frm")) return ModelType.MDL_DWG_FORMAT;
        else if (ext.equals("mfg")) return ModelType.MDL_MFG;
        else if (ext.equals("rep")) return ModelType.MDL_REPORT;
        else if (ext.equals("mrk")) return ModelType.MDL_MARKUP;
        else if (ext.equals("dgm")) return ModelType.MDL_DIAGRAM;
        else return null;
    }
    
    /**
     * Create a new JLIException that wraps a regular Exception.  Creo exceptions
     * will be converted to a more friendly string format.
     * @param e The original Exception
     * @return The resulting JLIException.  Connection-related errors will be returned as JlinkConnectException
     */
    public static JLIException createException(Exception e) {
    	return createException(e, null);
    }
    
    /**
     * Create a new JLIException that wraps a regular Exception.  Creo exceptions
     * will be converted to a more friendly string format.
     * @param e The original Exception
     * @param msg A text message to go with the Exception.
     * @return The resulting JLIException.  Connection-related errors will be returned as JlinkConnectException
     */
    public static JLIException createException(Exception e, String msg) {
        if (e instanceof XToolkitNotFound || e instanceof XToolkitCommError)
        	return new JlinkConnectException(ptcError(e, msg), e);
        else
        	return new JLIException(ptcError(e, msg), e);
    }
    
    /**
     * Create a new JLIException that wraps a regular Exception.
     * @param e The original Exception
     * @param msg A text message to go with the Exception.
     * @return The resulting JLIException.  Connection-related errors will be returned as JlinkConnectException
     */
    public static JLIException createBareException(Exception e, String msg) {
        if (e instanceof XToolkitNotFound || e instanceof XToolkitCommError)
        	return new JlinkConnectException(msg);
        else
        	return new JLIException(msg);
    }
    
    /**
     * Try to generate a friendlier error message for a Creo exception.  
     * If the specified exception does not have a friendlier message, just
     * return the passed-in message.
     * @param e The original Exception
     * @param msg A text message to go with the Exception.
     * @return A friendlier text message for the Exception.
     */
    public static String ptcError(Exception e, String msg) {
    	// this if is here because this exception is a child of XToolkitError
    	if (e instanceof XToolkitCheckoutConflict) { 
    		msg = "Windchill Checkout Conflict";
    		try {
    			msg+=": "+((XToolkitCheckoutConflict)e).GetConflictDescription();
    		}
    		catch (Exception e2) {
    			// ignore
    		}
    		return msg;
     	}
        if (e instanceof XToolkitError) 
            return toolkitError((XToolkitError)e, msg);

        if (e instanceof XStringTooLong)
            msg = "Error: String too long";
        else if (e instanceof XUnknownModelExtension)
            msg = "Error: Unknown Model Extension";
        else if (e instanceof XInvalidFileName)
            msg = "Error: Invalid File Name";
//        else if (e instanceof XInAMethod)
//            msg = "Error occurred inside a PTC method";  // best just go with the default error

        if (msg==null)
            msg = "An error occurred which PTC does not explain; please review your latest actions for problems";
        return msg;
    }
    
    /**
     * Try to generate a friendlier error message for a Pro/TOOLKIT exception.
     * @param e The original Exception
     * @param msg A text message to go with the Exception
     * @return A friendlier text message for the Exception
     */
    public static String toolkitError(XToolkitError e, String msg) {
        String kitMsg = null;
        try {
            if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("XToolkitError,GetErrorCode", 0, NitroConstants.DEBUG_JLINK_KEY);
            kitMsg = getPtcErrorText(e.GetErrorCode());
        } catch (jxthrowable e2) {}
        if (msg==null)
            msg = "A Pro/TOOLKIT error has occurred";
        if (kitMsg!=null)
            msg += ": " + kitMsg;
        msg += ".  Check your model/drawing for accuracy.";
        return msg;
    }
    
    /**
     * Convert a PTC error code into text.
     * @param code The code to convert
     * @return The text resulting from a lookup
     */
    private static String getPtcErrorText(int code) {
        switch (code) {
            case -1: return "General Error";
            case -2: return "Bad Inputs";
            case -3: return "User Abort";
            case -4: return "Not Found";
            case -5: return "Found";
            case -6: return "Line Too Long";
            case -7: return "Continue";
            case -8: return "Bad Context";
            case -9: return "Not Implemented";
            case -10: return "Out of Memory";
            case -11: return "Communications Error";
            case -12: return "No Change";
            case -13: return "Suppressed Parents";
            case -14: return "Pick Above";
            case -15: return "Invalid Directory";
            case -16: return "Invalid File";
            case -17: return "Can't Write";
            case -18: return "Invalid Type";
            case -19: return "Invalid Pointer";
            case -20: return "Unavailable Section";
            case -21: return "Invalid Matrix";
            case -22: return "Invalid Name";
            case -23: return "Does Not Exist";
            case -24: return "Can't Open";
            case -25: return "Abort";
            case -26: return "Not Valid";
            case -27: return "Invalid Item";
            case -28: return "Message Not Found";
            case -29: return "Message Not Transmitted";
            case -30: return "Message Format Error";
            case -31: return "Message User Quit";
            case -32: return "Message Too Long";
            case -33: return "Can't Access";
            case -34: return "Obsolete Functionality";
            case -35: return "No Coord System";
            case -36: return "Ambiguous";
            case -37: return "Deadlock";
            case -38: return "Busy";
            case -39: return "In Use";
            case -40: return "No License";
            case -41: return "Bspl Unsuitable Degree";
            case -42: return "Bspl Non Standard End Knots";
            case -43: return "Bspl Multi Inner Knots";
            case -44: return "Bad Srf Crv";
            case -45: return "Empty";
            case -46: return "Bad Dim Attach";
            case -47: return "Not Displayed";
            case -48: return "Can't Modify";
            case -49: return "Checkout Conflict";
            case -50: return "Create View Bad Sheet";
            case -51: return "Create View Bad Model";
            case -52: return "Create View Bad Parent";
            case -53: return "Create View Bad Type";
            case -54: return "Create View Bad Explode";
            case -55: return "Unattached Features";
            case -56: return "Regenerate Again";
            case -57: return "Dwg Create Errors";
            case -58: return "Unsupported";
            case -59: return "No Permission";
            case -60: return "Authentication failure";
            case -61: return "Outdated";
            case -62: return "Incomplete";
            case -63: return "Check Omitted";
            case -64: return "Max Limit Reached";
            case -65: return "Out Of Range";
            case -66: return "Check Last Error";
            
            case -88: return "App: Creo Barred";
            case -89: return "App: Too Old";
            case -90: return "App: Bad Datapath";
            case -91: return "App: Bad Encoding";
            case -92: return "App: No License";
            case -93: return "App: XS Callbacks";
            case -94: return "App: Startup Fail";
            case -95: return "App: Init Fail";
            case -96: return "App: Version Mismatch";
            case -97: return "App: Comm Failure";
            case -98: return "App: New Version";
            case -99: return "App: Unlock";
            case -100: return "App: JLink Not Allowed";
            default: return "Unknown error " + code;
        }
    }
    
    /**
     * Display a model in Creo.  The model must already be in-session.
     * @param session The Creo session
     * @param m The model to display
     * @param activate Whether to activate the model's window
     * @throws jxthrowable
     */
    public static void displayModel(CallSession session, CallModel m, boolean activate) throws jxthrowable {
        CallWindow win = session.getModelWindow(m);
        CallWindow winActive = session.getCurrentWindow();
        if (win!=null && !win.equals(winActive)) {
//            session.SetCurrentWindow(win);
            m.display();
            if (activate)
            	win.activate();
        }
        else if (win==null) {
            m.display();
            if (activate)
                winActive.activate();
        }
    }

    /**
     * Check whether a model is the active model in Creo
     * @param session The Creo session
     * @param m The model to check
     * @return True if the model is active
     * @throws jxthrowable
     */
    public static boolean isModelActive(CallSession session, CallModel m) throws jxthrowable {
    	if (m==null)
    		return false;

    	CallModel am = session.getCurrentModel();
    	if (am==null)
    		return false;
    	if (am.getModel().equals(m.getModel()))
    		return true;
    	String amf = getPath(am) + am.getDescr().getFileName();
    	String mf = getPath(m) + m.getDescr().getFileName();
    	if (amf.equalsIgnoreCase(mf))
    		return true;

    	return false;
    }

    /**
     * Get the name for a feature.  If the feature is a Component, then it 
     * returns the filename of the component.
     * @param feat The Feature to check
     * @return The name of the feature
     * @throws jxthrowable
     */
    public static String getFeatureName(CallFeature feat) throws jxthrowable {
        if (feat instanceof CallComponentFeat)
            return ((CallComponentFeat)feat).getModelDescr().getFileName();
        else
            return feat.getName();
    }
    
    /**
     * Check the status of a feature to see whether it is suppressed
     * @param feat The Feature to check
     * @return True if the feature is suppressed
     * @throws jxthrowable
     */
    public static boolean isSuppressed(CallFeature feat) throws jxthrowable {
        if (feat==null) return false;
        int status = feat.getStatus();
        if (status==FeatureStatus._FEAT_FAMILY_TABLE_SUPPRESSED || 
            status==FeatureStatus._FEAT_SIMP_REP_SUPPRESSED || 
            status==FeatureStatus._FEAT_PROGRAM_SUPPRESSED || 
            status==FeatureStatus._FEAT_SUPPRESSED)
            return true;
        else
        	return false;
    }

    /**
     * Get a Model's directory path, including drive letter
     * @param m The model to check
     * @return The model's directory path
     * @throws jxthrowable
     */
    public static String getPath(CallModel m) throws jxthrowable {
    	CallModelDescriptor desc = m.getDescr();
    	String drive = desc.getDevice();
    	if (drive!=null && drive.length()>0)
    		return drive + ':' + desc.getPath();
    	else
    		return desc.getPath();
    }

    /**
     * Translate a Creo feature status into a JSHell feature status
     * @param status The Creo feature status
     * @return The JShell feature status
     */
    public static String translateFeatureStatus(int status) {
        String ret = null;
        switch (status) {
            case FeatureStatus._FEAT_ACTIVE: ret = IJLFeature.STATUS_ACTIVE; break;
            case FeatureStatus._FEAT_INACTIVE: ret = IJLFeature.STATUS_INACTIVE; break;
            case FeatureStatus._FEAT_FAMILY_TABLE_SUPPRESSED: ret = IJLFeature.STATUS_FAMILY_TABLE_SUPPRESSED; break;
            case FeatureStatus._FEAT_SIMP_REP_SUPPRESSED: ret = IJLFeature.STATUS_SIMP_REP_SUPPRESSED; break;
            case FeatureStatus._FEAT_PROGRAM_SUPPRESSED: ret = IJLFeature.STATUS_PROGRAM_SUPPRESSED; break;
            case FeatureStatus._FEAT_SUPPRESSED: ret = IJLFeature.STATUS_SUPPRESSED; break;
            case FeatureStatus._FEAT_UNREGENERATED: ret = IJLFeature.STATUS_UNREGENERATED; break;
        }
        return ret;
    }
    
    /**
     * Translate a Creo display status into a JSHell display status
     * @param status The Creo display status
     * @return The JShell display status
     */
    public static String translateDisplayStatus(int status) {
        String ret = null;
        switch (status) {
	    	case DisplayStatus._LAYER_BLANK: ret = IJLLayer.STATUS_BLANK; break; 
	    	case DisplayStatus._LAYER_DISPLAY: ret = IJLLayer.STATUS_DISPLAY; break; 
	    	case DisplayStatus._LAYER_HIDDEN: ret = IJLLayer.STATUS_HIDDEN; break; 
	    	case DisplayStatus._LAYER_NORMAL: ret = IJLLayer.STATUS_NORMAL; break; 
        }
        return ret;
    }
    
    /**
     * Translate a Creo parameter value type into a JSHell parameter data type
     * @param ptype The Creo parameter value type
     * @return The JShell parameter type
     */
    public static String getParamTypeString(int ptype) {
    	String type = null;
    	switch (ptype) {
	        case ParamValueType._PARAM_STRING: 
	            break;
	        case ParamValueType._PARAM_INTEGER: 
	            type = IJLParameter.TYPE_INTEGER;
	            break;
	        case ParamValueType._PARAM_BOOLEAN: 
	            type = IJLParameter.TYPE_BOOL;
	            break;
	        case ParamValueType._PARAM_DOUBLE: 
	            type = IJLParameter.TYPE_DOUBLE;
	            break;
	        case ParamValueType._PARAM_NOTE: 
	            type = IJLParameter.TYPE_NOTE;
	            break;
	        default:
	            type = "";
	            break;
    	}
    	return type;
    }
    
    /**
     * Retrieve a dimension from a model.  If the name contains a dimension ID (@see isDimId) then
     * it will find the dimension for that ID unless the noid flag is true.
     * 
     * <p>This method will handle both Solid and Drawing dimensions.
     * 
     * @param m The model which owns the dimension
     * @param name The name of the dimension, or a dimension ID in the form d#.
     * @param noid Whether to allow a dimension ID to be requested in the name field
     * @return The Dimension or Dimension2D that was found
     * @throws jxthrowable
     */
    public static CallBaseDimension getDimension(CallModel m, String name, boolean noid) throws jxthrowable {
        CallModelItem item;
        int id = isDimId(name);
        if (id>=0) {
            if (noid)
                return null;
            item = m.getItemById(ModelItemType.ITEM_DIMENSION, id);
            if (item==null)
            	return null;
        }
        else {
            item = m.getItemByName(ModelItemType.ITEM_DIMENSION, name);
        }
        if (item instanceof CallDimension)
            return (CallDimension)item;
        else if (item instanceof CallDimension2D)
            return (CallDimension2D)item;
        else 
            return null;
    }
    
    /**
     * Check whether a name is of the form that indicates a dimension ID.  A name
     * is a dimension ID if it is a 'd' followed by one or more numeric digits.
     * @param name The name to check
     * @return Whether the name is a dimension ID
     */
    public static int isDimId(String name) {
        if (name==null)
            return -1;
        if (name.charAt(0) == 'd' || name.charAt(0) == 'D') {
            int len = name.length();
            for (int i=1; i<len; i++) 
                if (!Character.isDigit(name.charAt(i)))
                    return -1;
            return Integer.parseInt(name.substring(1));
        }
        else
            return -1;
    }
    
    /**
     * Retrieve a note from a model.  If the name contains a note ID (@see isNoteId) then
     * it will find the note for that ID unless the noid flag is true.
     * 
     * <p>This method will handle both Solid and Drawing notes.
     * 
     * @param m The model or drawing which owns the note
     * @param name The name of the note, or a note ID in the form note_#.
     * @param noid Whether to allow a note ID to be requested in the name field
     * @return The Note or DetailNoteItem that was found
     * @throws jxthrowable
     */
    public static CallModelItem getNote(CallModel m, String name, boolean noid) throws jxthrowable {
    	ModelItemType type = JlinkUtils.getNoteType(m);
    	return getNote(m, name, type, noid);
    }

    /**
     * Retrieve a note from a model.  If the name contains a note ID (@see isNoteId) then
     * it will find the note for that ID unless the noid flag is true.
     * 
     * <p>This method will handle both Solid and Drawing notes.
     * 
     * @param m The model or drawing which owns the note
     * @param name The name of the note, or a note ID in the form note_#.
     * @param type The ModelItemType to find; this allows the method to handle both Solid and Drawing models
     * @param noid Whether to allow a note ID to be requested in the name field
     * @return The Note or DetailNoteItem that was found
     * @throws jxthrowable
     */
    public static CallModelItem getNote(CallModel m, String name, ModelItemType type, boolean noid) throws jxthrowable {
        CallModelItem item;
        int id = isNoteId(name);
        if (id>=0) {
            if (noid)
                return null;
            item = m.getItemById(type, id);
            if (item==null)
            	return null;
        }
        else {
        	item = getNote(m, name, type);
        }
        if (item instanceof CallNote || item instanceof CallDetailNoteItem)
            return item;
        else 
            return null;
    }
    
    /**
     * Retrieve a note from a model.
     * 
     * <p>This method will handle both Solid and Drawing notes.
     * 
     * @param m The model or drawing which owns the note
     * @param name The name of the note.
     * @return The note that was found
     * @throws jxthrowable
     */
    public static CallModelItem getNote(CallModel m, String name) throws jxthrowable {
    	ModelItemType type = JlinkUtils.getNoteType(m);
    	return getNote(m, name, type);
    }

    /**
     * Retrieve a note from a model.
     * 
     * <p>This method will handle both Solid and Drawing notes.
     * 
     * @param m The model or drawing which owns the note
     * @param name The name of the note.
     * @param type The ModelItemType to find; this allows the method to handle both Solid and Drawing models
     * @return The note that was found
     * @throws jxthrowable
     */
    public static CallModelItem getNote(CallModel m, String name, ModelItemType type) throws jxthrowable {
    	CallModelItem item = m.getItemByName(type, name);
        if (item==null) {
        	CallModelItems itemList = m.listItems(type);
            if (itemList!=null) {
                int len = itemList.getarraysize();
                CallModelItem tmp = null;
                for (int i=0; i<len; i++) {
                	tmp = itemList.get(i);

                    if (tmp.getName().toLowerCase().equals(name.toLowerCase())) { 
                        item = tmp;
                        break;
                    }
                }
            }
        }
        return item;
    }

    /**
     * Check whether a name is of the form that indicates a note ID.  A name
     * is a note ID if it is 'note_' followed by one or more numeric digits.
     * @param name The name to check
     * @return Whether the name is a note ID
     */
    public static int isNoteId(String name) {
        if (name==null)
            return -1;
        if (name.toLowerCase().startsWith("note_")) {
            int len = name.length();
            int prefixLen = "note_".length();
            for (int i=prefixLen; i<len; i++) 
                if (!Character.isDigit(name.charAt(i)))
                    return -1;
            return Integer.parseInt(name.substring(prefixLen));
        }
        else
            return -1;
    }
    
    /**
     * Retrieve a Layer from a Model.
     * @param m The Model which owns the Layer
     * @param name The layer name
     * @return The layer that was found
     * @throws jxthrowable
     */
    public static CallModelItem getLayer(CallModel m, String name) throws jxthrowable {
    	CallModelItem item = m.getItemByName(ModelItemType.ITEM_LAYER, name);
        if (item==null) {
        	CallModelItems itemList = m.listItems(ModelItemType.ITEM_LAYER);
            if (itemList!=null) {
                int len = itemList.getarraysize();
                CallModelItem tmp = null;
                for (int i=0; i<len; i++) {
                    tmp = itemList.get(i);

                    if (tmp.getName().toLowerCase().equals(name.toLowerCase())) { 
                        item = tmp;
                        break;
                    }
                }
            }
        }
        return item;
    }

    /**
     * Set or create a parameter value in a model
     * @param m The model containing the parameter
     * @param pval The Creo parameter value object to update
     * @param value The value to set
     * @param encoded True if the value is Base64-encoded
     * @throws JLIException
     * @throws jxthrowable
     */
    public static void setParamValue(CallModel m, CallParamValue pval, Object value, boolean encoded) throws JLIException, jxthrowable {
		int type = pval.getParamValueType();
    	try {
	        switch (type) {
	            case ParamValueType._PARAM_STRING:
	                pval.setStringValue(value==null?"":DataUtils.getStringValue(value, encoded));
	            	if (value==null)
	            		pval.setStringValue("");
	            	else {
	            		String val = DataUtils.getStringValue(value, encoded);
		                pval.setStringValue(val);
	            	}
	                break;
	            case ParamValueType._PARAM_DOUBLE:
	                pval.setDoubleValue(value==null?0.0:DataUtils.getDoubleValue(value, encoded));
	                break;
	            case ParamValueType._PARAM_INTEGER:
	                pval.setIntValue(value==null?0:DataUtils.getIntValue(value, encoded));
	                break;
	            case ParamValueType._PARAM_BOOLEAN:
	                pval.setBoolValue(value==null?false:DataUtils.getBooleanValue(value, encoded));
	                break;
	            case ParamValueType._PARAM_NOTE:
	            	CallModelItem item = getNote(m, value.toString());
	                if (item==null)
	                    throw new JLIException("Cannot find note:" + value.toString().toUpperCase());
	                pval.setNoteId(item.getId());
	                break;
	        }
    	}
    	catch (JLIException e) {
    		throw e;
    	}
    	catch (Exception e) {
    		throw new JLIException("Unable to set data value for type " + getParamTypeString(type) + ": " + e.getMessage());
    	}
    }

    /**
     * Copy a value from one parameter to another
     * @param target_pval The Creo parameter value object to copy to
     * @param source_pval The Creo parameter value object to copy from
     * @throws jxthrowable
     */
    public static void setParamValue(CallParamValue target_pval, CallParamValue source_pval) throws jxthrowable {
        int type = source_pval.getParamValueType();
        
        switch (type) {
            case ParamValueType._PARAM_STRING:
                target_pval.setStringValue(source_pval.getStringValue());
                break;
            case ParamValueType._PARAM_DOUBLE:
                target_pval.setDoubleValue(source_pval.getDoubleValue());
                break;
            case ParamValueType._PARAM_INTEGER:
                target_pval.setIntValue(source_pval.getIntValue());
                break;
            case ParamValueType._PARAM_BOOLEAN:
                target_pval.setBoolValue(source_pval.getBoolValue());
                break;
            case ParamValueType._PARAM_NOTE:
                target_pval.setNoteId(source_pval.getNoteId());
                break;
        }
    }
    
    /**
     * Retrieve a feature from a model given a component path
     * @param session The Creo session
     * @param wrapping_solid The solid containing the feature
     * @param path The component path to the feature
     * @return The feature, if found
     * @throws JLIException
     * @throws jxthrowable
     */
    public static CallFeature getFeatureForPath(CallSession session, CallSolid wrapping_solid, List<Integer> path) throws JLIException, jxthrowable {
        int len = path.size();
        Integer id;
        CallSolid parentSolid = wrapping_solid;
        CallComponentFeat componentF = null;
        CallModel child = null;
        CallFeature component = null;
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
        return component;
    }
    
    public static final String OPTION_REGEN_HNDLG = "regen_failure_handling";
    public static final String VALUE_RESOLVE = "resolve_mode";
    public static final String VALUE_NO_RESOLVE = "no_resolve_mode";
    
    /**
     * Workaround for Creo bug, where the "regen_failure_handling" config option
     * needs to be set to "resolve_mode" before attempting a regenerate, so that
     * failures will be handled properly.
     * 
     * <p>This method must be called before doing a regenerate, and will set
     * the config option to "resolve_mode".
     * 
     * <p>This workaround is needed for Creo 2 (WF5) and higher.
     * 
     * @param session The Creo session
     * @return True if the option was already set to "resolve_mode" before calling this method.  Needed when it comes to setting it back.
     * @throws jxthrowable
     */
    public static boolean prefixResolveModeFix(CallSession session) throws jxthrowable {
    	return prefixResolveModeFix(session, null);
    }
    
    /**
     * Workaround for Creo bug, where the "regen_failure_handling" config option
     * needs to be set to "resolve_mode" before attempting a regenerate, so that
     * failures will be handled properly.
     * 
     * <p>This method must be called before doing a regenerate, and will set
     * the config option to "resolve_mode".
     * 
     * <p>This workaround is needed for Creo 2 (WF5) and higher
     * @param session The Creo session
     * @param debugKey If not null, a debug log message is sent with this debug key
     * @return True if the option was already set to "resolve_mode" before calling this method.  Needed when it comes to setting it back.
     * @throws jxthrowable
     */
    public static boolean prefixResolveModeFix(CallSession session, String debugKey) throws jxthrowable {
        boolean resolveMode = false;
        // required for WF5 and higher
        try {
        	long start = System.currentTimeMillis();
            String value = session.getConfigOption(OPTION_REGEN_HNDLG);
            if (debugKey!=null)
            	DebugLogging.sendTimerMessage("jlink.GetConfigOption,"+OPTION_REGEN_HNDLG, start, debugKey);
            
            if (value!=null && value.equalsIgnoreCase(VALUE_RESOLVE))
                resolveMode = true;
            
            if (!resolveMode) {
            	start = System.currentTimeMillis();
                session.setConfigOption(OPTION_REGEN_HNDLG, VALUE_RESOLVE);
                if (debugKey!=null)
                	DebugLogging.sendTimerMessage("jlink.SetConfigOption,"+OPTION_REGEN_HNDLG+","+VALUE_RESOLVE, start, debugKey);
            }
        }
        catch (Exception e) {
            // ignore exceptions, which occur with that option in WF4
        }
        return resolveMode;
    }
    
    /**
     * Workaround for Creo bug, where the "regen_failure_handling" config option
     * needs to be set to "resolve_mode" before attempting a regenerate, so that
     * failures will be handled properly.
     * 
     * <p>This method must be called after doing a regenerate, and will set
     * the config option to "no_resolve_mode" if the resolveMode is false.
     * 
     * <p>This workaround is needed for Creo 2 (WF5) and higher
     * @param session The Creo session
     * @param resolveMode Whether the option was already set to "resolve_mode" before the 
     * @throws jxthrowable
     */
    public static void postfixResolveModeFix(CallSession session, boolean resolveMode) throws jxthrowable {
    	postfixResolveModeFix(session, resolveMode, null);
    }
    
    /**
     * Workaround for Creo bug, where the "regen_failure_handling" config option
     * needs to be set to "resolve_mode" before attempting a regenerate, so that
     * failures will be handled properly.
     * 
     * <p>This method must be called after doing a regenerate, and will set
     * the config option to "no_resolve_mode" if the resolveMode is false.
     * 
     * <p>This workaround is needed for Creo 2 (WF5) and higher
     * @param session The Creo session
     * @param resolveMode Whether the option was already set to "resolve_mode" before prefixResolveModeFix was called 
     * @param debugKey If not null, a debug log message is sent with this debug key
     * @throws jxthrowable
     */
    public static void postfixResolveModeFix(CallSession session, boolean resolveMode, String debugKey) throws jxthrowable {
        try {
            if (!resolveMode) {
            	long start = System.currentTimeMillis();
                session.setConfigOption(OPTION_REGEN_HNDLG, VALUE_NO_RESOLVE);
                if (debugKey!=null)
                	DebugLogging.sendTimerMessage("jlink.SetConfigOption,"+OPTION_REGEN_HNDLG+","+VALUE_NO_RESOLVE, start, debugKey);
            }
        }
        catch (Exception e) {
            // ignore exceptions, which occur with that option in WF4
        }
    }
    
    /**
     * Regenerate a Creo object
     * @param session The Creo session
     * @param regener The handler class which performs the regeneration
     * @param setConfig True to update the "regen_failure_handling" config option
     * @throws jxthrowable
     */
    public static void regenerate(CallSession session, Regenerator regener, boolean setConfig) throws jxthrowable {
    	regenerate(session, regener, setConfig, null);
    }
    
    /**
     * Regenerate a Creo object.  If the regeneration fails with a -56 
     * ("Regenerate Again") error, the regeneration will be tried again.
     * 
     * @param session The Creo session
     * @param regener The handler class which performs the regeneration
     * @param setConfig True to update the "regen_failure_handling" config option
     * @param debugKey If not null, a debug log message is sent with this debug key
     * @throws jxthrowable
     */
    public static void regenerate(CallSession session, Regenerator regener, boolean setConfig, String debugKey) throws jxthrowable {
        if (session==null)
            setConfig = false;
        boolean resolveMode = false;
        long start;
        if (setConfig)
        	resolveMode = prefixResolveModeFix(session, debugKey);
        
        try {
        	start = System.currentTimeMillis();
        	regener.regenerate();
            if (debugKey!=null)
            	DebugLogging.sendTimerMessage("jlink.Regenerate", start, debugKey);
        }
        catch (XToolkitError e) {
            // if the error is "Regen again", then regenerate again
            if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("XToolkitError,GetErrorCode", 0, NitroConstants.DEBUG_JLINK_KEY);
            int code = e.GetErrorCode();
            if (code == -56) {
                // only try once more
            	start = System.currentTimeMillis();
            	regener.regenerate();
                if (debugKey!=null)
                	DebugLogging.sendTimerMessage("jlink.Regenerate after -56", start, debugKey);
            }
            else
                throw e;
        }
        finally {
            if (setConfig)
            	postfixResolveModeFix(session, resolveMode, debugKey);
        }
    }

    /**
     * Get the 3D transform for a component in an assembly and convert it to a JLTransform object.
     *  
     * @param baseAssembly The assembly containing the component
     * @param curPath The component path for the component
     * @param pathlen The length of the component path; this allows you to use a portion of the component path
     * @return The 3D transform for the component
     * @throws jxthrowable
     * @throws JLIException
     */
    public static JLTransform genTransformTable(CallAssembly baseAssembly, List<Integer> curPath, int pathlen) throws jxthrowable,JLIException {
    	CallTransform3D featTrans = genTransformTable2(baseAssembly, curPath, pathlen);

        return JLMatrixMaker.writeTransformTable(featTrans);
    }

    /**
     * Get the 3D transform for a component in an assembly
     * @param baseAssembly The assembly containing the component
     * @param curPath The component path for the component
     * @param pathlen The length of the component path; this allows you to use a portion of the component path
     * @return The 3D transform for the component
     * @throws jxthrowable
     */
    public static CallTransform3D genTransformTable2(CallAssembly baseAssembly, List<Integer> curPath, int pathlen) throws jxthrowable {
        CallIntSeq seq = CallIntSeq.create();
        Integer val;
        for (int i=0; i<pathlen; i++) {
            val = (Integer)curPath.get(i);
            seq.set(i, val.intValue());
        }
        CallComponentPath path = CallComponentPath.create(baseAssembly, seq);
        CallTransform3D featTrans = path.getTransform(true);

        return featTrans;
    }
    
    /**
     * Get detailed information about a parameter.  The data is returned in the "out" argument to the method.
     * 
     * <p>If the parameter value is a String which contains binary data, then the encoded argument will
     * be set to true automatically.
     * 
     * <p>If the valuePtn argument is filled in, then the value is checked against the pattern.  If the
     * value doesn't match the pattern, the output is not updated and the method returns false.
     * 
     * @param model The model containing the parameter
     * @param m The owner of the parameter (may not be the same as the model; for example, it may be a feature)
     * @param param The Creo parameter data
     * @param out The JShell parameter data output by the method
     * @param encoded Whether to return the parameter value as a byte array
     * @param valuePtn A pattern to filter the value for
     * @return True if the parameter data was found, false if the parameter was not found or did not match the filter
     * @throws JLIException
     * @throws jxthrowable
     */
    public static boolean getParamInfo(CallModel model, CallParameterOwner m, CallParameter param, ParameterData out, boolean encoded, Pattern valuePtn) throws JLIException,jxthrowable {
        if (param==null || out==null)
            return false;
        
        CallParamValue pval = param.getValue();
        Object value = null;
        String type = null;
        
        boolean filterChecked=false;
        switch (pval.getParamValueType()) {
            case ParamValueType._PARAM_STRING: 
                value = pval.getStringValue();
                type = IJLParameter.TYPE_STRING;
                if (value!=null) {
                	filterChecked=true;
	                if (!checkValueFilter(value.toString(), valuePtn))
	                	return false;
	                if (!encoded) {
	                	String v = value.toString();
	                    if (NitroUtils.hasBinary(v)) {
//	                    	v = SymbolUtils.translateSymbolsToText(v);
	                        encoded = NitroUtils.hasBinary(v);
	                        value = v;
	                    }
	                }
                }
                break;
            case ParamValueType._PARAM_INTEGER: 
                value = new Integer(pval.getIntValue());
                type = IJLParameter.TYPE_INTEGER;
                break;
            case ParamValueType._PARAM_BOOLEAN: 
                value = new Boolean(pval.getBoolValue());
                type = IJLParameter.TYPE_BOOL;
                break;
            case ParamValueType._PARAM_DOUBLE: 
                value = new Double(pval.getDoubleValue());
                type = IJLParameter.TYPE_DOUBLE;
                break;
            case ParamValueType._PARAM_NOTE: 
                try {
                	CallModelItem item = null;
                	if (m instanceof CallModel) {
                		item = ((CallModel)m).getItemById(ModelItemType.ITEM_NOTE, pval.getNoteId());
                        if (item==null)
                        	item = ((CallModel)m).getItemById(ModelItemType.ITEM_DTL_NOTE, pval.getNoteId());
                	}
                	else if (model!=null) {
                		item = model.getItemById(ModelItemType.ITEM_NOTE, pval.getNoteId());
                        if (item==null)
                        	item = model.getItemById(ModelItemType.ITEM_DTL_NOTE, pval.getNoteId());
                	}
                    if (item==null)
                        value = "";
                    else
                        value = item.getName();
                } catch (Exception e) {
                    // if an item does not exist with the ID, an exception is thrown
                    value = "";
                }
                type = IJLParameter.TYPE_NOTE;
                break;
            default:
                value = "";
                type = "";
                break;
        }
        if (value==null)
            value="";
        else {
        	if (!filterChecked) {
	            if (!checkValueFilter(value.toString(), valuePtn))
	            	return false;
        	}
        }
        out.setName(param.getName().toUpperCase());
        if (encoded) {
            out.setValue(value.toString().getBytes(Charset.forName("UTF-8")));
        }
        else {
            out.setValue(value);
        }
        out.setType(type);
        out.setEncoded(encoded);
        out.setDesignate(param.getIsDesignated());
        return true;
    }

    /**
     * Make sure that the generic instance of a model is loaded into Creo.
     * 
     * @param session The Creo session
     * @param m The model which is an instance in a family table
     * @throws jxthrowable
     */
    public static void resolveGenerics(CallSession session, CallModel m) throws jxthrowable {
    	if (session==null)
    		return;
    	if (!(m instanceof CallFamilyMember))
    		return;
    	CallFamilyMember fm = (CallFamilyMember)m;
        CallModelDescriptor desc = fm.getImmediateGenericInfo();
        CallModel child = session.getModelFromDescr(desc);
        if (child==null)
            child = session.retrieveModel(desc);
    }

    /**
     * Call an externally-defined function which requires Creo (as opposed to 
     * older functions which work with Pro/E Wildfire 4).
     * 
     * @param className The class name for the function extension
     * @param args An argument list for the function
     * @return The object returned by the external function
     * @throws JLIException
     * @throws jxthrowable
     */
    @SuppressWarnings("unchecked")
	public static Object callCreoFunction(String className, Object[] args) throws JLIException,jxthrowable {
    	Class clazz = null;
        try {
        	System.out.println("Loading Creo class " + className);
        	clazz = JlinkUtils.class.getClassLoader().loadClass(className);
        }
        catch (Throwable e) {
        	System.out.println("Function is not available: " + className);
        	throw new JLIException("Function is not available");
        }
        
        if (clazz==null) {
        	System.out.println("Function is not available: " + className);
        	throw new JLIException("Function is not available");
        }
        
        try {
	    	CreoFunctionInterface instance = (CreoFunctionInterface)clazz.newInstance();
	    	return instance.execute(args);
        }
        catch (JLIException e) {
        	throw e;
        }
        catch (jxthrowable e) {
        	throw e;
        }
        catch (Throwable e) {
//        	e.printStackTrace();
    		System.err.println("Error executing CreoFunction: " + e.getLocalizedMessage());
        	throw new JLIException("Could not execute function, Creo is required");
        }
    }

	/**
	 * Get detailed information about a model's Simplified Representation
	 * @param solid The model with a Simp Rep
	 * @return The output data
	 * @throws JLIException
	 * @throws jxthrowable
	 */
	public static SimpRepData getSimpRepInfo(CallSolid solid) throws JLIException,jxthrowable {
		CallSimpRep rep = solid.getActiveSimpRep();
		if (rep==null)
			return null;
		
		SimpRepData data = new SimpRepData();
		data.setName(rep.getName());
		
		CallSimpRepInstructions inst = rep.getInstructions();
		if (inst!=null) {
			if (inst.getDefaultAction()==SimpRepActionType._SIMPREP_EXCLUDE)
				data.setDefaultExclude(true);
			CallSimpRepItems items = inst.getItems();
			if (items!=null) {
				int len = items.getarraysize();
				CallIntSeq seq;
				CallSimpRepItem item;
				CallSimpRepItemPath path;
				CallSimpRepCompItemPath cpath;
				for (int i=0; i<len; i++) {
					item = items.get(i);
					int actionType = item.getAction();

					boolean skip=false;
					switch (actionType) {
						// ignore "empty" action types
						case -1: 
						case SimpRepActionType._SIMPREP_NONE:
							skip=true; 
							break;
							
						// do NOT ignore reverse action types
						case SimpRepActionType._SIMPREP_REVERSE:
							break;
							
						// ignore action type which matches the default (exclude)
						case SimpRepActionType._SIMPREP_EXCLUDE:
							if (data.isDefaultExclude())
								skip=true;
							break;
							
						// ignore action type which matches the default (include)
						case SimpRepActionType._SIMPREP_INCLUDE:
							if (!data.isDefaultExclude())
								skip=true;
							break;
							
						// by default ignore all other action types unless the default type is Exclude
						default:
							if (!data.isDefaultExclude())
								skip=true;
							break;
					}
					
					if (skip) {
						//System.out.println("skipping action type: " + actionType);
						continue;
					}

					path = item.getItemPath();
					if (path instanceof CallSimpRepCompItemPath) {
						cpath = (CallSimpRepCompItemPath)path;
						seq = cpath.getItemPath();
						if (seq!=null) {
							//System.out.println("Simp Rep: " + item.getAction() + "  " + intSeqToList(seq));
							data.addItem(intSeqToList(seq));
						}
					}
				}
			}
		}
		
		return data;
	}
	
	/**
	 * Convert a JLink intseq (integer sequence) to a Java Integer list
	 * @param seq The integer sequence
	 * @return The integer list
	 * @throws JLIException
	 * @throws jxthrowable
	 */
	public static List<Integer> intSeqToList(CallIntSeq seq) throws JLIException,jxthrowable {
		if (seq==null)
			return null;
		int len = seq.getarraysize();
		List<Integer> ret = new ArrayList<Integer>();
		for (int i=0; i<len; i++) {
			ret.add(new Integer(seq.get(i)));
		}
		return ret;
	}
	
	/**
	 * Retrieve the appropriate ModelItemType for notes for a model.
	 * @param m A model to get the type for
	 * @return Either ModelItemType.ITEM_NOTE (for Solids) or ModelItemType.ITEM_DTL_NOTE (for drawings)
	 */
	public static ModelItemType getNoteType(CallModel m) {
    	ModelItemType type = ModelItemType.ITEM_NOTE;
    	try {
    		String fn = m.getFileName().toLowerCase();
    		int extpos = NitroUtils.findFileExtension(fn);
	        if (extpos!=-1 && fn.substring(extpos+1).equals("drw"))
	        	type = ModelItemType.ITEM_DTL_NOTE;
    	}
    	catch (jxthrowable e) {
//    		e.printStackTrace();
    		System.err.println("Error getting note type: " + e.getLocalizedMessage());
    	}
    	return type;
	}

    /**
     * Check whether a string matches a search pattern
     * @param text The string to validate
     * @param valuePtn The search pattern
     * @return true if the string matches the pattern, or if the pattern is null
     * @throws jxthrowable
     */
    public static boolean checkValueFilter(String text, Pattern valuePtn) throws jxthrowable {
        if (valuePtn!=null) {
			if (text==null || text.trim().length()==0)
				return false;
			String transText = text.toLowerCase().replace('\n', ' ');
			
			Matcher m = valuePtn.matcher(transText);
			if (!m.matches())
				return false;
        }
        return true;
    }

    /**
     * Find the top-level generic portion of a file name.  This is done strictly based
     * on the file name without reference to the physical model.
     * @param filename A file name which represents an instance in a family table
     * @return The top-level generic name, or null if the file is not a family table instance
     */
    public static String extractTopGeneric(String filename) {
    	String lastGeneric = extractNextGeneric(filename);
    	if (lastGeneric==null)
    		return null;
    	String generic = null;
    	while ((generic = extractNextGeneric(lastGeneric))!=null) {
    		lastGeneric = generic;
    	}
    	return lastGeneric;
    }

    /**
     * Find the immediate generic portion of a file name.  This is done strictly based
     * on the file name without reference to the physical model.
     * @param filename A file name which represents an instance in a family table
     * @return The generic name, or null if the file is not a family table instance
     */
    public static String extractNextGeneric(String filename) {
    	if (filename==null)
    		return null;
    	int pos1 = filename.indexOf('<');
    	if (pos1<0)
    		return null;
    	int pos2 = filename.lastIndexOf('>');
    	if (pos2<0 || pos2<pos1)
    		pos2 = filename.length();
    	String generic = filename.substring(pos1+1, pos2);
    	return generic;
    }

    /**
     * Find the instance portion of a file name.  This is done strictly based
     * on the file name without reference to the physical model.
     * @param filename A file name which represents an instance in a family table
     * @return The instance name, or null if the file is not a family table instance
     */
    public static String extractInstance(String filename) {
    	if (filename==null)
    		return null;
    	int pos1 = filename.indexOf('<');
    	if (pos1<0)
    		return null;
    	return filename.substring(0, pos1);
    }

    /**
     * Combine an instance filename (such as long_bolt.prt) and a generic name (such as bolt)
     * to a standard Creo filename (such as long_bolt<bolt>.prt).
     * @param instanceFilename The instance file name, including the file extension
     * @param generic The generic name, which does NOT include the file extension
     * @return The formatted, combined name
     */
    public static String instGenericToFilename(String instanceFilename, String generic) {
    	if (instanceFilename==null)
    		return generic;
    	if (generic==null)
    		return instanceFilename;
    	
    	String extension=null;;
    	int pos = NitroUtils.findFileExtension(instanceFilename);
    	if (pos>=0) {
    		extension = instanceFilename.substring(pos+1);
    		instanceFilename = instanceFilename.substring(0, pos);
    	}
    	return instGenericExtToFilename(instanceFilename, generic, extension);
    }

    /**
     * Combine a generic filename (such as bolt.prt) and an instance name (such as long_bolt)
     * to a standard Creo filename (such as long_bolt<bolt>.prt).
     * @param genericFilename The generic file name, including the file extension
     * @param instanceName The instance name, which does NOT include the file extension
     * @return The formatted, combined name
     */
    public static String fileInstanceToFilename(String genericFilename, String instanceName) {
    	if (genericFilename==null)
    		return instanceName;
    	if (instanceName==null)
    		return genericFilename;
    	
    	String extension=null;;
    	int pos = NitroUtils.findFileExtension(genericFilename);
    	if (pos>=0) {
    		extension = genericFilename.substring(pos+1);
    		genericFilename = genericFilename.substring(0, pos);
    	}
    	return instGenericExtToFilename(instanceName, genericFilename, extension);
    }

    /**
     * Combine an instance name (such as long_bolt), a generic name (such as bolt) and a file 
     * extension (such as .prt) to a standard Creo filename (such as long_bolt<bolt>.prt).
     * @param instanceName The instance name, which does NOT include the file extension
     * @param genericName The generic name, which does NOT include the file extension
     * @param extension The file extension, which may or may not include the dot
     * @return The formatted, combined name
     */
    public static String instGenericExtToFilename(String instanceName, String genericName, String extension) {
    	StringBuffer buf = new StringBuffer();
    	if (instanceName==null) {
    		if (genericName!=null)
    			buf.append(genericName);
    	}
    	else {
	    	buf.append(instanceName);
	    	if (genericName!=null) {
	    		buf.append("<");
	    		buf.append(genericName);
	    		buf.append(">");
	    	}
    	}
    	if (extension!=null) {
    		if (extension.charAt(0)!='.')
    			buf.append(".");
    		buf.append(extension);
    	}
    	return buf.toString();
    }

	/**
	 * Debugging function to print out a byte array
	 * @param data The array to print out
	 */
	public static void showHexBytes(byte[] data) {
		if (data==null) return;
		for (int i=0; i<data.length; i++) {
			System.out.print(data[i]);
			System.out.print(" ");
		}
		System.out.println();
	}

	/**
	 * Debugging function to print out the bytes in a string
	 * @param str The string to print out
	 */
	public static void showHexString(String str) {
		if (str==null) return;
		System.out.println(str);
		for (int i=0; i<str.length(); i++) {
			System.out.print((int)str.charAt(i));
			System.out.print(" ");
		}
		System.out.println();
	}

	/**
	 * Construct a URL for a Windchill workspace of the form wtws://<server-alias>/<workspace>/
	 * @param server The JLink server object
	 * @param workspace The Windchill workspace name
	 * @return The URL for the workspace
	 * @throws jxthrowable
	 */
	public static String makeWindchillUrl(Server server, String workspace) throws jxthrowable {
		return makeWindchillUrl(server.GetAlias(), workspace);
	}
	
	/**
	 * Construct a URL for a Windchill workspace of the form wtws://<server-alias>/<workspace>/
	 * @param serverAlias The Windchill server alias
	 * @param workspace The Windchill workspace name
	 * @return The URL for the workspace
	 * @throws jxthrowable
	 */
	public static String makeWindchillUrl(String serverAlias, String workspace) throws jxthrowable {
		StringBuffer buf = new StringBuffer();
		buf.append("wtws://");
		buf.append(serverAlias);
		buf.append("/");
		buf.append(workspace);
		buf.append("/");
		
		return buf.toString();
	}
	
	/**
	 * Take a Windchill workspace file URL and strip out file name.
	 * @param fileUrl The URL for the Windchill workspace file
	 * @return The file name portion of the fileUrl
	 */
	public static String stripWindchillUrl(String fileUrl) {
		int pos1 = fileUrl.lastIndexOf('/');
		int pos2 = fileUrl.lastIndexOf('\\');
		if (pos1<0 && pos2<0)
			return fileUrl;
		if (pos2>pos1)
			return fileUrl.substring(pos2+1);
		else
			return fileUrl.substring(pos1+1);
	}

    /**
     * Interface for the special handler used by the regenerate function
     * @author Adam Andrews
     *
     */
    public static interface Regenerator {
    	/**
    	 * Perform the regeneration
    	 * @throws jxthrowable
    	 */
    	public void regenerate() throws jxthrowable;
    }

    /**
     * Validates that a file name follows the rules for Creo file names.
     * Throws an exception if validation fails.
     * @param filename The name of the file to validate.
     * @throws JLIException If a validation error occurs, this contains the text of the failure.
     */
    public static void validateFilename(String filename) throws JLIException {
        // https://www.ptc.com/en/support/article?n=CS67337

    	int extpos = NitroUtils.findFileExtension(filename);
        String ext = "";
        String prefix = filename;
        if (extpos>=0) {
        	prefix = filename.substring(0, extpos);
            ext = filename.substring(extpos+1);
        }

    	// check for length
    	if (prefix.length()>FILENAME_LIMIT)
    		throw new JLIException("File name must be less than "+(FILENAME_LIMIT+1)+" characters before the extension");

    	// check for not starting with -
    	if (prefix.startsWith("-"))
    		throw new JLIException("File name must not start with hyphen");

    	// check for extra dots
    	if (prefix.indexOf('.')>=0)
    		throw new JLIException("File name may not contain more than one dot");

    	// check for alphanumeric only (except for hyphen and underscore)
    	// no unicode chars
    	int len = prefix.length();
    	for (int i=0; i<len; i++) {
    		char c=prefix.charAt(i);
    		if ((c>='0' && c<='9') ||
    			(c>='A' && c<='Z') || 
    			(c>='a' && c<='z') || 
    			c=='-' || 
    			c=='_')
    			;
    		else
    			throw new JLIException("File name contains an invalid character: "+c);
    	}

    }
}
