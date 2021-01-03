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
package com.simplifiedlogic.nitro.jlink.intf;

import java.util.List;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.FamTableInstance;
import com.simplifiedlogic.nitro.jlink.data.FamilyTableGetRowReturn;
import com.simplifiedlogic.nitro.jlink.data.FamilyTableRowColumns;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLFamilyTable {

	public Boolean exists(String modelname, String instname,
			String sessionId) throws JLIException;

	public Boolean exists(String modelname, String instname,
			AbstractJLISession sess) throws JLIException;

	public void delete(String modelname, String sessionId)
			throws JLIException;

	public void delete(String modelname, AbstractJLISession sess)
			throws JLIException;

	public void deleteInst(String modelname, String instname,
			String sessionId) throws JLIException;

	public void deleteInst(String modelname, String instname,
			AbstractJLISession sess) throws JLIException;

	public FamilyTableGetRowReturn getRow(String modelname, String instname,
			String sessionId) throws JLIException;

	public FamilyTableGetRowReturn getRow(String modelname, String instname,
			AbstractJLISession sess) throws JLIException;

	public FamilyTableRowColumns getCell(String modelname, String instname,
			String colid, String sessionId) throws JLIException;

	public FamilyTableRowColumns getCell(String modelname, String instname,
			String colid, AbstractJLISession sess) throws JLIException;

	public void setCell(String modelname, String instname,
			String colid, Object value, String sessionId) throws JLIException;

	public void setCell(String modelname, String instname,
			String colid, Object value, AbstractJLISession sess) throws JLIException;

	public List<FamilyTableRowColumns> getHeader(String modelname,
			String sessionId) throws JLIException;

	public List<FamilyTableRowColumns> getHeader(String modelname, AbstractJLISession sess)
			throws JLIException;

	public void addInst(String modelname, String newname,
			String sessionId) throws JLIException;

	public void addInst(String modelname, String newname,
			AbstractJLISession sess) throws JLIException;

	public void replace(String modelname, String oldname,
			String oldinst,
			String newinst, List<Integer> path, String sessionId)
			throws JLIException;

	public void replace(String modelname, String oldname,
			String oldinst,
			String newinst, List<Integer> path, AbstractJLISession sess)
			throws JLIException;

	public List<String> list(String modelname, String namePattern, String sessionId)
			throws JLIException;

	public List<String> list(String modelname, String namePattern, AbstractJLISession sess)
			throws JLIException;

	public FamTableInstance listTree(String modelname, boolean erase, String sessionId)
		throws JLIException;
	
	public FamTableInstance listTree(String modelname, boolean erase, AbstractJLISession sess)
		throws JLIException;

    public List<String> getParents(String modelname, String sessionId) throws JLIException;

    public List<String> getParents(String modelname, AbstractJLISession sess) throws JLIException;
    
    public String createInstance(String modelname, String instname,
    		String sessionId) throws JLIException;

    public String createInstance(String modelname, String instname,
    		AbstractJLISession sess) throws JLIException;

}
