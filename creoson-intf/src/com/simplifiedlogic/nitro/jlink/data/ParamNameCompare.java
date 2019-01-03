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
package com.simplifiedlogic.nitro.jlink.data;


/**
 * Utility class for comparing parameter names
 * @author Adam Andrews
 *
 */
public class ParamNameCompare {

    /**
     * Compare two string for sorting.  
     * 
     * <p>If both strings are numeric (ex: 15 and 110), sort them numerically rather than alphabetically.
     * <p>If both strings start with the same alphabetic prefix and end with a number (ex: PARM_15 and PARM_110), 
     * then sort the last part numerically.
     * <p>Otherwise, sort the two strings alphabetically.
     * @param a1 The first string to compare
     * @param a2 The second string to compare
	 * @return the value 0 if a1 is equal to a2; a value less than 0 if a1 is lexicographically less than a2; and a value greater than 0 if a1 is lexicographically greater than a2.
     */
    public static int compareStrings(String a1, String a2) {
        boolean a1num = isNumber(a1);
        boolean a2num = isNumber(a2);
        if (a1num && a2num) {
        	// if both are numbers, sort numerically 
            double d1 = Double.parseDouble(a1);
            double d2 = Double.parseDouble(a2);
            return (d1 < d2) ? -1 : (d1 == d2 ? 0 : 1);
        }
        else if (!a1num && !a2num) {
        	// if neither is a number, find the first occurrence of a number in each string
        	int n1 = findNumber(a1);
        	int n2 = findNumber(a2);
        	if (n1==n2 && n1>0) {
        		// if both strings contain a number, check whether the alpha portions are equal
        		String s1 = a1.substring(0, n1);
        		String s2 = a2.substring(0, n2);
        		if (s1.equals(s2)) {
            		s1 = a1.substring(n1);
            		s2 = a2.substring(n2);
            		// if both strings end with a number, sort numerically
        			if (findNonNumber(s1)<0 && findNonNumber(s2)<0) {
		                double d1 = Double.parseDouble(s1);
		                double d2 = Double.parseDouble(s2);
		                return (d1 < d2) ? -1 : (d1 == d2 ? 0 : 1);
        			}
        		}
        	}
        	// sort alphabetically
            return a1.compareTo(a2);
        }
        else if (a1num) {
        	// if only the first one is a number, sort it first
            return -1;
        }
        else if (a2num) {
        	// if only the second one is a number, sort it last
            return 1;
        }
        return 0;
    }

    /**
     * Check whether a string contains only digits
     * @param val The input string
     * @return Whether a string contains only digits
     */
    public static boolean isNumber(String val) {
    	return (findNonNumber(val)<0);
    }

    /**
     * Find the first numeric digit in a string
     * @param val The input string
     * @return The character position of the first numeric digit
     */
    public static int findNumber(String val) {
        if (val==null) return -1;
        val = val.trim();
        int len = val.length();
        if (len==0) return -1;
        char c;
        for (int i=0; i<len; i++) {
            c = val.charAt(i);
            if (Character.isDigit(c) || c=='.')
                return i;
        }
        return -1;
    }

    /**
     * Find the first non-numeric character in a string
     * @param val The input string
     * @return The character position of the first non-numeric character
     */
    public static int findNonNumber(String val) {
        if (val==null) return -1;
        val = val.trim();
        int len = val.length();
        if (len==0) return -1;
        char c;
        for (int i=0; i<len; i++) {
            c = val.charAt(i);
            if (!Character.isDigit(c) && c!='.')
                return i;
        }
        return -1;
    }

//	public static String[] splitAlpha(String input) {
//		if (input==null || input.length()==0)
//			return null;
//		boolean inAlpha=false;
//		List<String> results = new ArrayList<String>();
//		StringBuffer curString = new StringBuffer();
//		int len = input.length();
//		for (int i=0; i<len; i++) {
//			if (Character.isLetter(input.charAt(i))) {
//				if (!inAlpha) {
//					if (curString.length()>0) {
//						results.add(curString.toString());
//						curString.setLength(0);
//					}
//					inAlpha=true;
//				}
//			}
//			else {
//				if (inAlpha) {
//					if (curString.length()>0) {
//						results.add(curString.toString());
//						curString.setLength(0);
//					}
//					inAlpha=false;
//				}
//			}
//			curString.append(input.charAt(i));
//		}
//		if (curString.length()>0) {
//			results.add(curString.toString());
//		}
//		return (String[])results.toArray(new String[]{});
//	}

}
