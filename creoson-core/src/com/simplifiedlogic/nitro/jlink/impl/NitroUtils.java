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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Generally useful utility methods
 * 
 * @author Adam Andrews
 */
public class NitroUtils {

    /**
     * Add an extension to a file name, or replace an existing extension.
     * 
     * @param filename The file name to modify
     * @param ext The extension to add or update.  Must include the dot.
     * @return The updated file name
     */
    public static String setFileExtension(String filename, String ext) {
        int pos = findFileExtension(filename);
        if (pos>=0)
            return filename.substring(0, pos) + ext;
        else
            return filename + ext;
    }
    
    /**
     * Get the character position of a file extension in a file name.
     * @param filename The file name to search
     * @return The position of the last dot after the last slash in the name, or -1 if there is no extension
     */
    public static int findFileExtension(String filename) {
        for (int i=filename.length()-1; i>=0; i--) {
            char c = filename.charAt(i); 
            if (c=='/' || c=='\\') {
                return -1;
            }
            if (c=='.') return i;
        }
        return -1;
    }

    /**
     * Get the character position of the final name in a path.
     * @param filename The path/file name to search
     * @return The position right after the last slash in the name, or -1 if there are no slashes
     */
    public static int findFileBase(String filename) {
        for (int i=filename.length()-1; i>=0; i--) {
            char c = filename.charAt(i); 
            if (c=='/' || c=='\\') {
                return i+1;
            }
        }
        return -1;
    }

    public static String removeExtension(String filename) {
        int pos = NitroUtils.findFileExtension(filename);
        if (pos>=0)
        	filename = filename.substring(0, pos);
        return filename;
    }

    /**
     * Strip the Creo revision number from a file name
     * @param filename The file name to update
     * @return The updated file name
     */
    public static String removeNumericExtension(String filename) {
    	String subname;
        int extpos1 = NitroUtils.findFileExtension(filename);
        if (extpos1!=-1) {
        	subname = filename.substring(0, extpos1);
        	int extpos2 = NitroUtils.findFileExtension(subname);
        	if (extpos2!=-1) {
        		int len=filename.length();
        		int i;
        		for (i=extpos1+1; i<len; i++)
        			if (!Character.isDigit(filename.charAt(i))) 
        				break;
        		if (i>=len)
        			return subname;
        	}
        }
        return filename;
    }
    
    /**
     * Validate that a directory and file exist.  If file!=null and writable=true then it also checks whether the
     * file is writable.
     * @param dirname The directory name to check.  If null, then the application's execution directory (NOT the Creo working directory) is used
     * @param filename The file name to check.
     * @param writable Whether to check that the file is writable
     * @throws JLIException The validation failed
     */
    public static void validateDirFile(String dirname, String filename, boolean writable) throws JLIException {
        if (dirname != null && dirname.length()>0) {
            File dir = new File(dirname);
            if (!dir.exists() || !dir.isDirectory()) {
                throw new JLIException("Directory does not exist: " + dirname);
            }
            if (!dir.canRead()) {
                throw new JLIException("Directory is not readable: " + dirname);
            }
        }

        if (filename!=null) {
        	if (dirname!=null) {
                int len = dirname.length();
                if (dirname.charAt(len-1) != '\\' && dirname.charAt(len-1) != '/' && dirname.charAt(len-1) != ':')
                	filename = dirname + File.separatorChar + filename;
                else
                    filename = dirname + filename;
            }
            File f = new File(filename);
            if (writable) {
                if (f.exists() && !f.canWrite()) {
                    throw new JLIException("Output file exists but is not writeable: " + filename);
                }
            }
            else {
                if (!f.exists() || !f.isFile()) {
                    throw new JLIException("Input file does not exist: " + filename);
                }
                if (!f.canRead()) {
                    throw new JLIException("Input file is not readable: " + filename);
                }
            }
        }
    }
    
    /**
     * Compare two directory names to see if they are equivalent
     * @param dir1 
     * @param dir2
     * @return True if the directory names are equivalent
     */
    public static boolean compareDirNames(String dir1, String dir2) {
        if (dir1==dir2) return true;
        if (dir1==null || dir2==null) return false;
        if (dir1.equals(dir2)) return true;
        dir1 = dir1.replace('\\', '/');
        dir2 = dir2.replace('\\', '/');
        boolean hasDrive1 = false;
        boolean hasDrive2 = false;
        // remove drive letter
        if (dir1.length()>=2 && Character.isLetter(dir1.charAt(0)) && dir1.charAt(1)==':')
            hasDrive1=true;
        if (dir2.length()>=2 && Character.isLetter(dir2.charAt(0)) && dir2.charAt(1)==':')
            hasDrive2=true;
        
        // if only one directory has a drive letter, ignore it
        if (hasDrive1 && !hasDrive2)
            dir1 = dir1.substring(2);
        if (!hasDrive1 && hasDrive2)
            dir2 = dir2.substring(2);
        
        if (dir1.toLowerCase().equals(dir2.toLowerCase()))
            return true;
        return false;
    }
    
    /**
     * Check whether a string contains a pattern (wildcard) value.  This checks for the
     * characters *, ?, | or [ in the string.
     * @param pattern The pattern to check
     * @return Whether the string contains a pattern
     */
    public static boolean isPattern(String pattern) {
        if (pattern==null || pattern.length()==0) return false;
        if (pattern.indexOf('*') >= 0) return true;
        if (pattern.indexOf('?') >= 0) return true;
        if (pattern.indexOf('|') >= 0) return true;
        if (pattern.indexOf('[') >= 0) return true;
        return false;
    }
    
    /**
     * Transform a user-provided search pattern into a Java regular expression string.
     * 
     * @param pattern The user filter pattern
     * @return A Java regular expression string
     */
    public static String transformPattern(String pattern) {
        if (pattern==null || pattern.length()==0)
            return pattern;
        int len = pattern.length();
        StringBuffer buf = new StringBuffer(len);
        for (int i=0; i<len; i++) {
            switch (pattern.charAt(i)) {
                case '?': buf.append('.'); break;
                case '.': buf.append("\\x2e"); break;
                case '*': buf.append(".*"); break;
                case ',': buf.append("|"); break;
                default: buf.append(pattern.charAt(i)); break;
            }
        }
        return buf.toString();
    }
    
//    public static String transformValuePattern(String pattern) {
//        if (pattern==null || pattern.length()==0)
//            return pattern;
//        pattern = transformPattern(pattern);
////        pattern = SymbolUtils.translateTextToRegex(pattern);
//        return pattern;
//    }
    
    /**
     * Check whether a string contains non-ASCII data
     * @param val The string to check
     * @return Whether a string contains non-ASCII data
     */
    public static boolean hasBinary(String val) {
        if (val==null) return false;
        
        int len = val.length();
        if (val.length()==0) return false;
        char c;
        for (int i=0; i<len; i++) {
            c = val.charAt(i);
            //System.out.println("char " + i + ": " + c + ":" + (int)c);
            if ((int)c < (int)' ' && c!='\n' && c!='\r' && c!='\t')
                return true;
        }
        return false;
    }
    
    /**
     * Check whether a string contains any characters that would be invalid in a 
     * Creo object name.
     * 
     * @param name The name to validate.
     * @throws JLIException The validation failed.
     */
    public static void validateNameChars(String name) throws JLIException {
        if (name==null) return;
        
        char ch;
//        boolean nondigit=false;
        int len = name.length();
        for (int i=0; i<len; i++) {
            ch = name.charAt(i);
            if (!Character.isLetter(ch) && !Character.isDigit(ch) && ch!='-' && ch!='_')
                throw new JLIException("Invalid character '" + ch + "' in name");
//            if (!Character.isDigit(ch) && ch!='_')
//            	nondigit=true;
        }
//        if (!nondigit)
//        	throw new JLIException("Name must contain at least one letter or underscore");
        if (!Character.isLetter(name.charAt(0))) // this check makes the nondigit check unnecessary
        	throw new JLIException("Name must start with a letter");

    }
    
	/**
	 * Check whether a string contains a valid URL.
	 * @param urlStr The string to validate
	 * @return Whether the string contains a valid URL
	 */
	public static boolean isValidURL(String urlStr) {
	    try {
		    @SuppressWarnings("unused")
			URL url = new URL(urlStr);
		    return true;
	    }
	    catch (MalformedURLException e) {
	        return false;
	    }
	}
}
