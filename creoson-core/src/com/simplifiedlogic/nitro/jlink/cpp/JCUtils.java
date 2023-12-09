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
package com.simplifiedlogic.nitro.jlink.cpp;

import java.util.List;

import com.ptc.pfc.pfcExceptions.XToolkitCommError;
import com.ptc.pfc.pfcExceptions.XToolkitNotFound;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.rpc.JCException;
import com.simplifiedlogic.nitro.rpc.JCToolkitException;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JlinkConnectException;

/**
 * Utility methods related to the native library.
 *
 */
public class JCUtils {

    /**
     * Convert a JCException to a JLIException
     * @param e
     * @return
     */
    public static JLIException createException(JCException e) {
    	return createException(e, null);
    }
    
    /**
     * Convert a JCException to a JLIException, with a message
     * @param e
     * @param msg
     * @return
     */
    public static JLIException createException(JCException e, String msg) {
    	if (e instanceof JCToolkitException && 
    			(((JCToolkitException)e).isInstanceOf(XToolkitNotFound.class) || 
    			 ((JCToolkitException)e).isInstanceOf(XToolkitCommError.class))) 
        	return new JlinkConnectException(ptcError(e, msg), e);
        else
        	return new JLIException(ptcError(e, msg), e);
    }

    /**
     * Return appropriate error text for a JCException
     * @param e
     * @param msg
     * @return
     */
    public static String ptcError(JCException e, String msg) {
    	// this if is here because this exception is a child of XToolkitError
        if (e instanceof JCToolkitException) {
        	JCToolkitException tke = (JCToolkitException)e;
        	// TODO:
//        	if (tke.isInstanceOf(XToolkitCheckoutConflict.class)) {
//        		msg = "Windchill Checkout Conflict";
//        		try {
//        			msg+=": "+((XToolkitCheckoutConflict)e).GetConflictDescription();
//        		}
//        		catch (Exception e2) {
//        			// ignore
//        		}
//        		return msg;
//        	}
//        	else
        		return toolkitError(tke, msg);
        }
/*
 TODO: need to find out how to cause these, so know how to handle
        if (e instanceof XStringTooLong)
            msg = "Error: String too long";
        else if (e instanceof XUnknownModelExtension)
            msg = "Error: Unknown Model Extension";
        else if (e instanceof XInvalidFileName)
            msg = "Error: Invalid File Name";
//        else if (e instanceof XInAMethod)
//            msg = "Error occurred inside a PTC method";  // best just go with the default error
        else if (e instanceof XUnimplemented)
            msg = "Error: Unimplemented function";
*/
        msg = e.getMessage();

        if (msg==null)
            msg = "An error occurred which PTC does not explain; please review your latest actions for problems";
        return msg;
    }
    
    /**
     * Return appropriate error text for a JCToolkitException
     * @param e
     * @param msg
     * @return
     */
    public static String toolkitError(JCToolkitException e, String msg) {
        String kitMsg = null;
        kitMsg = JlinkUtils.getPtcErrorText(e.getErrorCode());
        if (msg==null)
            msg = "A Pro/TOOLKIT error has occurred";
        if (kitMsg!=null)
            msg += ": " + kitMsg;
        msg += ".  Check your model/drawing for accuracy.";
        return msg;
    }

    /**
     * Convert an integer collection to an int array so that the array can be passed to a native function.
     * @param ids
     * @return
     */
    public static int[] intListToArray(List<Integer> ids) {
    	if (ids==null)
    		return null;
    	int[] arr = new int[ids.size()];
    	for (int i=0; i<arr.length; i++) 
    		arr[i] = ids.get(i).intValue();
    	return arr;
    }
}
