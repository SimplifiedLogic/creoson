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
import com.simplifiedlogic.nitro.jlink.data.DimData;
import com.simplifiedlogic.nitro.jlink.data.DimDetailData;
import com.simplifiedlogic.nitro.jlink.data.DimSelectData;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLDimension {

	public void set(String modelname, String dimname, Object value,
			boolean encoded, String sessionId) throws JLIException;
	public void set(String modelname, String dimname, Object value,
			boolean encoded, AbstractJLISession sess) throws JLIException;

	public void setText(String modelname, String dimname,
			Object value, //Object prefix, Object suffix, 
			boolean encoded, String sessionId) 
			throws JLIException;
	public void setText(String modelname, String dimname,
			Object value, //Object prefix, Object suffix, 
			boolean encoded, AbstractJLISession sess) 
			throws JLIException;

	public void copy(String modelname, String dimname, String to_name,
			String to_model, String sessionId) throws JLIException;
	public void copy(String modelname, String dimname, String to_name,
			String to_model, AbstractJLISession sess) throws JLIException;

	public List<DimData> list(String modelname, 
			String dimName, List<String> dimNames, String dimType, 
			boolean encoded, boolean select, String sessionId) throws JLIException;
	public List<DimData> list(String modelname, 
			String dimName, List<String> dimNames, String dimType, 
			boolean encoded, boolean select, AbstractJLISession sess) throws JLIException;

	public List<DimDetailData> listDetail(String modelname, 
			String dimName, List<String> dimNames, String dimType, 
			boolean encoded, boolean select, String sessionId) throws JLIException;
	public List<DimDetailData> listDetail(String modelname, 
			String dimName, List<String> dimNames, String dimType, 
			boolean encoded, boolean select, AbstractJLISession sess) throws JLIException;

	public void show(String modelname, String asmname, String dimname,
			int[] path, boolean show, String sessionId) throws JLIException;
	public void show(String modelname, String asmname, String dimname,
			int[] path, boolean show, AbstractJLISession sess) throws JLIException;

	public List<DimSelectData> userSelect(String modelname, int max, 
			String sessionId) throws JLIException;
	public List<DimSelectData> userSelect(String modelname, int max, 
			AbstractJLISession sess) throws JLIException;

}