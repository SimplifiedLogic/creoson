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
package com.simplifiedlogic.nitro.jlink.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.StdColor;
import com.ptc.pfc.pfcExceptions.XToolkitBadInputs;
import com.ptc.pfc.pfcExceptions.XToolkitNotFound;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.base.CallColorRGB;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.JLColor;
import com.simplifiedlogic.nitro.jlink.intf.IJLProe;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.FileListFilter;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;

/**
 * @author Adam Andrews
 *
 */
public class JLProe implements IJLProe {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#cd(java.lang.String, java.lang.String)
	 */
	@Override
	public String cd(String dirname, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return cd(dirname, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#cd(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String cd(String dirname, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.cd: " + dirname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (dirname==null || dirname.trim().length()==0)
    		throw new JLIException("No directory name parameter given");

		long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary(); 
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);
	
	        NitroUtils.validateDirFile(dirname, null, false);
	        
	        JlinkUtils.changeDirectory(session, dirname);

	        String result = session.getCurrentDirectory();
	
	        return result.replace('\\', '/');
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.cd,"+dirname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#mkdir(java.lang.String, java.lang.String)
	 */
	@Override
	public String mkdir(String dirname, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return mkdir(dirname, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#mkdir(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String mkdir(String dirname, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.mkdir: " + dirname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (dirname==null || dirname.trim().length()==0)
    		throw new JLIException("No directory name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
			JLGlobal.loadLibrary(); 
	
			CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);
	        
	        
	        File f = new File(dirname);
	        
	        if (!f.mkdirs())
	            throw new JLIException("Unable to create directory: " + dirname);
	        
	        return dirname.replace('\\', '/');
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.mkdir,"+dirname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#pwd(java.lang.String)
	 */
	@Override
	public String pwd(String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return pwd(sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#pwd(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String pwd(AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.pwd", NitroConstants.DEBUG_KEY);
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
	
	        String dirname = session.getCurrentDirectory();
	
	        return dirname.replace('\\', '/');
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.pwd", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#list_dirs(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> list_dirs(String filename, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return list_dirs(filename, sess);
	}

	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#list_dirs(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> list_dirs(String filename, AbstractJLISession sess) throws JLIException{
		DebugLogging.sendDebugMessage("proe.list_dirs: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        String dirname = session.getCurrentDirectory();
	        
	        File dir = new File(dirname);
	        if (!dir.exists() || !dir.isDirectory())
	        	throw new JLIException("Directory '" + dirname + "' does not exist");

	        FileListFilter filter = new FileListFilter();
	        filter.setNamePattern(filename);
	        filter.setListDirs(true);
	        filter.setListFiles(false);
	
	        File[] files = dir.listFiles(filter);
	        Vector<String> out_dirs = new Vector<String>();
	        int numfiles = files.length;
	        if (numfiles>0) {
		        String[] filenames = new String[files.length];
		        for (int i=0; i<numfiles; i++)
		        	filenames[i] = files[i].getName();
		        Arrays.sort(filenames);
		        
		        for (int i=0; i<numfiles; i++) {
	        		out_dirs.add(filenames[i]);
		        }
	        }
	        return out_dirs;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.list_dirs,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#list_files(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> list_files(String filename, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return list_files(filename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#list_files(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> list_files(String filename, AbstractJLISession sess) throws JLIException{
		DebugLogging.sendDebugMessage("proe.list_files: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        String dirname = session.getCurrentDirectory();
	        
	        File dir = new File(dirname);
	        if (!dir.exists() || !dir.isDirectory())
	        	throw new JLIException("Directory '" + dirname + "' does not exist");
	
	        FileListFilter filter = new FileListFilter();
	        filter.setNamePattern(filename);
	        filter.setListDirs(false);
	        filter.setListFiles(true);
	
	        File[] files = dir.listFiles(filter);
	        Vector<String> out_files = new Vector<String>();
	        int numfiles = files.length;
	        if (numfiles>0) {
		        String[] filenames = new String[files.length];
		        for (int i=0; i<numfiles; i++)
		        	filenames[i] = files[i].getName();
		        Arrays.sort(filenames);
		        
		        String last_subname = null;
		        String subname = null;
		        for (int i=0; i<numfiles; i++) {
		        	subname = NitroUtils.removeNumericExtension(filenames[i]);
		        	if (last_subname==null || !subname.equalsIgnoreCase(last_subname))
		        		out_files.add(subname);
		        	last_subname = subname;
		        }
	        }
	        return out_files;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.list_files,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#deleteFiles(java.lang.String, java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public List<String> deleteFiles(String dirname, String filename, List<String> filenames, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return deleteFiles(dirname, filename, filenames, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#deleteFiles(java.lang.String, java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> deleteFiles(String dirname, String filename, List<String> filenames, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.delete_files", NitroConstants.DEBUG_KEY);
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

	        if (dirname==null)
	        	dirname = session.getCurrentDirectory();
	        
	        File dir = new File(dirname);
	        if (!dir.exists() || !dir.isDirectory())
	        	throw new JLIException("Directory '" + dirname + "' does not exist");

	        FileListFilter filter = new FileListFilter();
	        if (filenames!=null)
		        filter.setNameList(filenames);
	        else
	        	filter.setNamePattern(filename);
	        filter.setListDirs(false);
	        filter.setListFiles(true);

	        File[] files = dir.listFiles(filter);

	        List<String> out_files = new Vector<String>();
	        for (int i=0; i<files.length; i++) {
	        	if (!files[i].delete() || files[i].exists())
	        		throw new JLIException("Unable to delete file " + files[i].getAbsolutePath());

	        	out_files.add(files[i].getName());
	        }
	        if (out_files.size()==0)
	        	return null;
	        return out_files;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.delete_files", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#rmdir(java.lang.String, java.lang.String)
	 */
	@Override
	public void rmdir(String dirname, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        rmdir(dirname, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#rmdir(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void rmdir(String dirname, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.rmdir: " + dirname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (dirname==null || dirname.trim().length()==0)
    		throw new JLIException("No directory name parameter given");

		long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary(); 
	
	        if (JlinkUtils.isRelativePath(dirname)) {
	            JLGlobal.loadLibrary(); 
	            CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());

	            dirname = JlinkUtils.resolveRelativePath(session, dirname);
	        }
	        
	        File dir = new File(dirname);
	        if (!dir.exists() || !dir.isDirectory())
	        	throw new JLIException("Directory '" + dirname + "' does not exist");
	        
	        String[] files = dir.list();
	        
	        if (files.length > 0)
	        	throw new JLIException("Directory '" + dirname + "' is not empty");
	        
	        dir.delete();
	        
	        if (dir.exists())
	        	throw new JLIException("Failed to delete directory " + dirname);
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.rmdir,"+dirname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#setConfig(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public void setConfig(String name, String value, boolean ignoreErrors, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        setConfig(name, value, ignoreErrors, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#setConfig(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void setConfig(String name, String value, boolean ignoreErrors, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.set_config", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (name==null || name.trim().length()==0)
    		throw new JLIException("No config option name parameter given");
//    	if (value==null || value.trim().length()==0)
//    		throw new JLIException("No config option value parameter given");

		long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary(); 
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        //System.out.println("Setting config option " + name + " = " + value);
	        session.setConfigOption(name, value);
    	}
    	catch (XToolkitNotFound e) {
    		if (ignoreErrors)
    			System.err.println(JlinkUtils.ptcError(e, "ERROR setting config option "+name));
    		else
    			throw new JLIException("Please check your config option name: " + name);
    	}
    	catch (XToolkitBadInputs e) {
    		if (ignoreErrors)
    			System.err.println(JlinkUtils.ptcError(e, "ERROR setting config option "+name));
    		else
    			throw new JLIException("Please check your config option value: " + value);
    	}
    	catch (jxthrowable e) {
    		if (ignoreErrors)
    			System.err.println(JlinkUtils.ptcError(e, "ERROR setting config option "+name));
    		else
    			throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.set_config", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#getConfig(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getConfig(String name, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return getConfig(name, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#getConfig(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> getConfig(String name, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.get_config", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (name==null || name.trim().length()==0)
    		throw new JLIException("No config option name parameter given");

		long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary(); 
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallStringSeq values = session.getConfigOptionValues(name);
	        if (values!=null) {
	            int len = values.getarraysize();
	            if (len>0) {
		            List<String> vals = new Vector<String>(len);
		            for (int i=0; i<len; i++) {
		                vals.add(values.get(i));
		            }
		            return vals;
	            }
	        }

	        return null;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.get_config", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#getStandardColor(int, java.lang.String)
	 */
	@Override
	public JLColor getStandardColor(int type, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return getStandardColor(type, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#getStandardColor(int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public JLColor getStandardColor(int type, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.get_standard_color", NitroConstants.DEBUG_KEY);
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
	
	        StdColor colorID = getStdColor(type);
	        if (colorID==null)
	        	throw new JLIException("Invalid color value: " + type);
	        
	        CallColorRGB rgb = session.getRGBFromStdColor(colorID);
	        if (rgb==null)
	        	return null;
	        JLColor out = new JLColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	        
	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.get_standard_color", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#setStandardColor(int, com.simplifiedlogic.nitro.jlink.data.JLColor, java.lang.String)
	 */
	@Override
	public void setStandardColor(int type, JLColor color, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        setStandardColor(type, color, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLProe#setStandardColor(int, com.simplifiedlogic.nitro.jlink.data.JLColor, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void setStandardColor(int type, JLColor color, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("proe.set_standard_color", NitroConstants.DEBUG_KEY);
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
	
	        StdColor colorID = getStdColor(type);
	        if (color==null)
	        	throw new JLIException("Invalid color value: " + type);
	        
	        CallColorRGB rgb = CallColorRGB.create(color.getRedIntensity(), color.getGreenIntensity(), color.getBlueIntensity());
	        if (rgb==null)
	        	return;

	        session.setStdColorFromRGB(colorID, rgb);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("proe.set_standard_color", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/**
	 * Get the Creo Color ID for a given color type
	 * @param type The JShell color code
	 * @return The Creo Standard Color ID
	 */
	private StdColor getStdColor(int type) {
		switch (type) {
			case STD_COLOR_LETTER: return StdColor.COLOR_LETTER;
			case STD_COLOR_HIGHLIGHT: return StdColor.COLOR_HIGHLIGHT;
			case STD_COLOR_DRAWING: return StdColor.COLOR_DRAWING;
			case STD_COLOR_BACKGROUND: return StdColor.COLOR_BACKGROUND;
			case STD_COLOR_HALF_TONE: return StdColor.COLOR_HALF_TONE;
			case STD_COLOR_EDGE_HIGHLIGHT: return StdColor.COLOR_EDGE_HIGHLIGHT;
			case STD_COLOR_DIMMED: return StdColor.COLOR_DIMMED;
			case STD_COLOR_ERROR: return StdColor.COLOR_ERROR;
			case STD_COLOR_WARNING: return StdColor.COLOR_WARNING;
			case STD_COLOR_SHEETMETAL: return StdColor.COLOR_SHEETMETAL;
			case STD_COLOR_CURVE: return StdColor.COLOR_CURVE;
			case STD_COLOR_PRESEL_HIGHLIGHT: return StdColor.COLOR_PRESEL_HIGHLIGHT;
			case STD_COLOR_SELECTED: return StdColor.COLOR_SELECTED;
			case STD_COLOR_SECONDARY_SELECTED: return StdColor.COLOR_SECONDARY_SELECTED;
			case STD_COLOR_PREVIEW: return StdColor.COLOR_PREVIEW;
			case STD_COLOR_SECONDARY_PREVIEW: return StdColor.COLOR_SECONDARY_PREVIEW;
			case STD_COLOR_DATUM: return StdColor.COLOR_DATUM;
			case STD_COLOR_QUILT: return StdColor.COLOR_QUILT;
		}
		return null;
	}
}
