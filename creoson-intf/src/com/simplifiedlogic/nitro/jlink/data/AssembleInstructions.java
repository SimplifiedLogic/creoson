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
package com.simplifiedlogic.nitro.jlink.data;

import java.util.List;

/**
 * Input values for the file-assemble function.
 * 
 * @author Adam Andrews
 *
 */
public class AssembleInstructions {

	private String dirname;
	private String filename;
	private String genericName;
	private String intoAsm;
	private List<Integer> componentPath;
	private JLTransform transData;
	private List<JLConstraintInput> constraints;
	private boolean packageAssembly;
	private String refModel;
	private boolean walkChildren;
	private boolean assembleToRoot;
	private boolean suppress;
	
	public String getDirname() {
		return dirname;
	}
	public void setDirname(String dirname) {
		this.dirname = dirname;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public String getIntoAsm() {
		return intoAsm;
	}
	public void setIntoAsm(String intoAsm) {
		this.intoAsm = intoAsm;
	}
	public List<Integer> getComponentPath() {
		return componentPath;
	}
	public void setComponentPath(List<Integer> componentPath) {
		this.componentPath = componentPath;
	}
	public JLTransform getTransData() {
		return transData;
	}
	public void setTransData(JLTransform transData) {
		this.transData = transData;
	}
	public List<JLConstraintInput> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<JLConstraintInput> constraints) {
		this.constraints = constraints;
	}
	public boolean isPackageAssembly() {
		return packageAssembly;
	}
	public void setPackageAssembly(boolean packageAssembly) {
		this.packageAssembly = packageAssembly;
	}
	public String getRefModel() {
		return refModel;
	}
	public void setRefModel(String refModel) {
		this.refModel = refModel;
	}
	public boolean isWalkChildren() {
		return walkChildren;
	}
	public void setWalkChildren(boolean walkChildren) {
		this.walkChildren = walkChildren;
	}
	public boolean isAssembleToRoot() {
		return assembleToRoot;
	}
	public void setAssembleToRoot(boolean assembleToRoot) {
		this.assembleToRoot = assembleToRoot;
	}
	public boolean isSuppress() {
		return suppress;
	}
	public void setSuppress(boolean suppress) {
		this.suppress = suppress;
	}
	
}
