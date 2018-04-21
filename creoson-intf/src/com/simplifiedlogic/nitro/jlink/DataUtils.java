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
package com.simplifiedlogic.nitro.jlink;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

/**
 * Utility methods for data value conversions
 * 
 * @author Adam Andrews
 *
 */
public class DataUtils {

    /**
     * Convert a generic object into an int value.
     * 
     * @param in The object to convert
     * @return The int value of the object
     * @throws NumberFormatException
     * @throws IOException
     */
    public static int getIntValue(Object in) throws NumberFormatException, IOException {
        return getIntValue(in, false);
    }

    /**
     * Convert a generic object into an int value.
     * 
     * @param in The object to convert
     * @param encoded Whether the object is Base64-encoded
     * @return The int value of the object
     * @throws NumberFormatException
     * @throws IOException
     */
    public static int getIntValue(Object in, boolean encoded) throws NumberFormatException, IOException {
        if (in instanceof Integer) return ((Integer)in).intValue();
        if (in instanceof Short) return ((Short)in).intValue();
        if (in instanceof Long) return ((Long)in).intValue();
        if (in instanceof Double) return ((Double)in).intValue();
        if (in instanceof Float) return ((Float)in).intValue();
        
        if (encoded) {
            in = decodeBase64(in);
        }
        String val;
        if (in instanceof byte[])
            val = new String((byte[])in);
        else
            val = in.toString();
        if (val.endsWith(".0"))
        	val = val.substring(0, val.length()-2);

        // Integer's value parsing can only handle up to 1E6, but an Integer can hold up to ~1E9 ??
//        return Integer.valueOf(val).intValue();
        return new BigDecimal(val).intValueExact();

    }

    /**
     * Convert a generic object into a double value
     * 
     * @param in The object to convert
     * @return The double value of the object
     * @throws NumberFormatException
     * @throws IOException
     */
    public static double getDoubleValue(Object in) throws NumberFormatException, IOException {
        return getDoubleValue(in, false);
    }
    
    /**
     * Convert a generic object into a double value
     * 
     * @param in The object to convert
     * @param encoded Whether the object is Base64-encoded
     * @return The double value of the object
     * @throws NumberFormatException
     * @throws IOException
     */
    public static double getDoubleValue(Object in, boolean encoded) throws NumberFormatException, IOException {
        if (in instanceof Integer) return ((Integer)in).doubleValue();
        if (in instanceof Short) return ((Short)in).doubleValue();
        if (in instanceof Long) return ((Long)in).doubleValue();
        if (in instanceof Double) return ((Double)in).doubleValue();
        if (in instanceof Float) return ((Float)in).doubleValue();
        
        if (encoded) {
            in = decodeBase64(in);
        }
        String val;
        if (in instanceof byte[])
            val = new String((byte[])in);
        else
            val = in.toString();
        return Double.valueOf(val).doubleValue();
    }

    /**
     * Convert a generic object into a boolean value.
     * 
     * <p>This function will also interpret the string values "1", "y", "yes", 
     * "true", and "on" as a boolean true value.
     *  
     * @param in The object to convert
     * @return The boolean value of the object
     */
    public static boolean getBooleanValue(Object in) {
        return getBooleanValue(in, false);
    }
    
    /**
     * Convert a generic object into a boolean value.
     * 
     * <p>This function will also interpret the string values "1", "y", "yes", 
     * "true", and "on" as a boolean true value.
     *  
     * @param in The object to convert
     * @param encoded Whether the object is Base64-encoded
     * @return The boolean value of the object
     */
    public static boolean getBooleanValue(Object in, boolean encoded) {
        if (in instanceof Boolean)
            return ((Boolean)in).booleanValue();

        if (encoded) {
            in = decodeBase64(in);
        }
        String val;
        if (in instanceof byte[])
            val = new String((byte[])in);
        else
            val = in.toString();
        if (val.equals("1") || 
        	val.equals("1.0") ||
            val.equalsIgnoreCase("y") || 
            val.equalsIgnoreCase("yes") || 
            val.equalsIgnoreCase("true") || 
            val.equalsIgnoreCase("on"))
            return true;
        else
            return false;
    }
    
    /**
     * Convert a generic object into a String value.  The reason not to 
     * just use toString() on the object is that this function also
     * handles input in the form of a byte array, which toString() does
     * not do well.
     * 
     * @param in The object to convert
     * @return The String value of the object
     */
    public static String getStringValue(Object in) {
        return getStringValue(in, false);
    }
    
    /**
     * Convert a generic object into a String value.  The reason not to 
     * just use toString() on the object is that this function also
     * handles input in the form of a byte array, which toString() does
     * not do well.
     * 
     * @param in The object to convert
     * @param encoded Whether the object is Base64-encoded
     * @return The String value of the object
     */
    public static String getStringValue(Object in, boolean encoded) {
        if (encoded) {
            in = decodeBase64(in);
        }
        if (in instanceof byte[])
            return new String((byte[])in, Charset.forName("UTF-8"));
        else
            return in.toString();
    }
    
    /**
     * Converts a String containing a set of comma-separated values
     * and returns it as a java.util.List object.  The list can contain
     * both String and Integer objects; if an item is detected to be numeric
     * then it is converted to an Integer; otherwise it is converted to a
     * String.
     * 
     * <p>Needs to be modified to handle decimal (Double) values as well.
     * 
     * @param csv The comma-separated list of values
     * @return The values as a list of objects
     */
    public static List<Object> getCsvList(String csv) {
    	if (csv==null || csv.trim().length()==0)
    		return null;
    	List<Object> vals = new ArrayList<Object>();
    	int len = csv.length();
    	StringBuffer entry = new StringBuffer();
    	char c;
    	boolean inquote=false;
    	boolean number=false;
    	for (int i=0; i<len; i++) {
    		c = csv.charAt(i);
    		if (inquote) {
    			if (c=='"')
    				inquote=false;
    			else
    				entry.append(c);
    		}
    		else {
    			if (c=='"') {
    				inquote=true;
    				number=false;
    			}
    			else if (c==',') {
    				if (number)
    					vals.add(new Integer(entry.toString()));
    				else
    					vals.add(entry.toString());
    				entry.setLength(0);
    			}
    			else {
    				number=true;
    				entry.append(c);
    			}
    		}
    	}
//		if (entry.length()>0) { // comment out because even if the last character is a comma, need to add a blank entry
			if (number)
				vals.add(new Integer(entry.toString()));
			else
				vals.add(entry.toString());
//		}
    	return vals;
    }

    /**
     * Base64-encode an object and return it as a byte array
     * @param input The object to encode
     * @return The Base64-encoded value
     */
    public static byte[] encodeBase64(Object input) {
        return Base64.encodeBase64(input.toString().getBytes(Charset.forName("UTF-8")));
    }
    
    /**
     * Base64-decode a string and return it as a decoded String.
     * 
     * <p>If anything other than a String is passed in, then assume
     * the object is already decoded and just return it.
     * 
     * @param input The Base64-encoded string to decode
     * @return The decoded string, or the input object if it was not a string.
     */
    public static Object decodeBase64(Object input) {
        // assume that a byte array came from java and is already decoded
        if (input instanceof String)
            return new String(Base64.decodeBase64((String)input), Charset.forName("UTF-8"));
        else 
            return input;
    }

}
