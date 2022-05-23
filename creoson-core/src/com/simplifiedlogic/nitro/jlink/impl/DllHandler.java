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
package com.simplifiedlogic.nitro.jlink.impl;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ptc.pfc.pfcArgument.ArgValueType;
import com.simplifiedlogic.nitro.jlink.calls.argument.CallArgValue;
import com.simplifiedlogic.nitro.jlink.calls.argument.CallArgument;
import com.simplifiedlogic.nitro.jlink.calls.argument.CallArguments;
import com.simplifiedlogic.nitro.jlink.calls.protoolkit.CallDll;
import com.simplifiedlogic.nitro.jlink.calls.protoolkit.CallFunctionReturn;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;


/**
 * Utility class for handling the execution of external Pro/TOOLKIT DLL functions.
 * 
 * <p>The normal workflow would be:<br>
 * 1) Call dllLoad() to load the library<br>
 * 2) Call dllFuncExec() to execute one or more functions<br>
 * 3) Call dllUnload() to remove the library from memory<br>
 *  
 * @author Adam Andrews
 *
 */
public class DllHandler {

    /**
     * Load an external Pro/TOOLKIT DLL into memory
     * 
     * @param sessionId The JShell session ID
     * @param dllname The name of the DLL
     * @param path The system path to the DLL
     * @param textpath The path to the directory containing text, menu, and resource files needed by the DLL, if any
     * @param userDisplay Whether to add the library to the "Auxiliary Applications" dialog in Creo
     * @return The ID of the loaded library, which is needed for other functions.
     * @throws JLIException
     * @throws Exception
     */
    public String dllLoad(String sessionId, String dllname, String path, String textpath, boolean userDisplay) throws JLIException,Exception {
        JLGlobal.loadLibrary();

        CallSession session = JLConnectionUtil.getJLSession(sessionId);
        if (session == null)
            return null;

        CallDll dllRef = session.loadProToolkitDll(
                dllname, 
                path + "/" + dllname,
                textpath, 
                userDisplay);
        
        if (dllRef!=null)
            return dllRef.getId();
        else
            throw new JLIException("Unable to load DLL");
    }
    
    /**
     * Remove an external Pro/TOOLKIT DLL from memory
     * 
     * @param sessionId The JShell session ID
     * @param dllid The DLL's ID, gotten when dllLoad() was called
     * @throws JLIException
     * @throws Exception
     */
    public void dllUnload(String sessionId, String dllid) throws JLIException,Exception {
        JLGlobal.loadLibrary();

        CallSession session = JLConnectionUtil.getJLSession(sessionId);
        if (session == null)
            return;

        CallDll dllRef = session.getProToolkitDll(dllid);

        if (dllRef!=null) {
            dllRef.unload();
        }
        else
            throw new JLIException("DLL has not been loaded");
    }
    
    /**
     * Check whether a DLL is still in memory.
     * 
     * @param sessionId The JShell session ID
     * @param dllid The DLL's ID, gotten when dllLoad() was called
     * @return Whether the DLL is still in memory
     * @throws JLIException
     * @throws Exception
     */
    public boolean dllLoaded(String sessionId, String dllid) throws JLIException,Exception {
        JLGlobal.loadLibrary();

        CallSession session = JLConnectionUtil.getJLSession(sessionId);
        if (session == null)
            return false;

        CallDll dllRef = session.getProToolkitDll(dllid);

        if (dllRef!=null)
            return true;
        else
            return false;
    }
    
    /**
     * Execute an function in an external Pro/TOOLKIT DLL.
     * 
     * @param sessionId The JShell session ID
     * @param dllid The DLL's ID, gotten when dllLoad() was called
     * @param funcname The function name to execute
     * @param args A set of named arguments to be passed to the function
     * @return A set of return values that were returned by the function, as well as a "dll_return" value which contains the integer returned by Creo itself.
     * @throws JLIException
     * @throws Exception
     */
    public Map<String, Object> dllFuncExec(String sessionId, String dllid, String funcname, Map<String, Object> args) throws JLIException,Exception {
        JLGlobal.loadLibrary();

        CallSession session = JLConnectionUtil.getJLSession(sessionId);
        if (session == null)
            return null;

        
        CallDll dllRef = session.getProToolkitDll(dllid);

        if (dllRef!=null) {
            CallArguments dllArgs = CallArguments.create();
            CallArgument arg;
            
            Set<String> set = args.keySet();
            Iterator<String> iter = set.iterator();
            String key;
            Object val;
            while (iter.hasNext()) {
                key = iter.next();
                val = args.get(key);
                
                arg = CallArgument.create(
                        key.toString(), 
                        CallArgValue.createASCIIStringArgValue(val.toString()));
                dllArgs.append(arg);
            }

            CallFunctionReturn ret = dllRef.executeFunction(funcname, dllArgs);

            Map<String, Object> retVals = new Hashtable<String, Object>();
            retVals.put("dll_return", new Integer(ret.getFunctionReturn()));
            
            CallArguments dllReturn = ret.getOutputArguments();
            if (dllReturn!=null) {
	            int len = dllReturn.getarraysize();
	            String label;
	            CallArgValue rval;
	            for (int i=0; i<len; i++) {
	                arg = dllReturn.get(i);
	                label = arg.getLabel();
	                rval = arg.getValue();
	                
	                int type = rval.getArgValueType();
	                switch (type) {
	                    case ArgValueType._ARG_V_INTEGER: retVals.put(label, new Integer(rval.getIntValue())); break;
	                    case ArgValueType._ARG_V_DOUBLE: retVals.put(label, new Double(rval.getDoubleValue())); break;
	                    case ArgValueType._ARG_V_BOOLEAN: retVals.put(label, new Boolean(rval.getBoolValue())); break;
	                    case ArgValueType._ARG_V_ASCII_STRING: retVals.put(label, rval.getASCIIStringValue()); break;
	                    case ArgValueType._ARG_V_STRING: retVals.put(label, rval.getStringValue()); break;
	                }
	            }
            }
            
            return retVals;
        }
        else
            throw new JLIException("DLL has not been loaded");
    }
    
}
