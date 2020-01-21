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
package com.simplifiedlogic.nitro.jlink.intf;

import java.util.List;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.AssembleInstructions;
import com.simplifiedlogic.nitro.jlink.data.FileAssembleResults;
import com.simplifiedlogic.nitro.jlink.data.FileInfoResults;
import com.simplifiedlogic.nitro.jlink.data.FileListInstancesResults;
import com.simplifiedlogic.nitro.jlink.data.FileOpenResults;
import com.simplifiedlogic.nitro.jlink.data.JLConstraintInput;
import com.simplifiedlogic.nitro.jlink.data.JLTransform;
import com.simplifiedlogic.nitro.jlink.data.MasspropsData;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLFile {
	public static final boolean DISPLAY_YES = true;
	public static final boolean DISPLAY_NO = false;
	public static final boolean ACTIVATE_YES = true;
	public static final boolean ACTIVATE_NO = false;
	public static final boolean NEWWIN_YES = true;
	public static final boolean NEWWIN_NO = false;
	public static final boolean FORCEREGEN_YES = true;
	public static final boolean FORCEREGEN_NO = false;
	public static final boolean ERASECHILDREN_YES = true;
	public static final boolean ERASECHILDREN_NO = false;

	public static final char LINE_CONTINUE_CHAR = '\\';

	public FileOpenResults open(String dirname, String filename,
			List<String> filenames, String genericname, boolean display,
			boolean activate, boolean newwin, boolean forceRegen,
			String sessionId) throws JLIException;
	public FileOpenResults open(String dirname, String filename,
			List<String> filenames, String genericname, boolean display,
			boolean activate, boolean newwin, boolean forceRegen,
			AbstractJLISession sess) throws JLIException;

    public boolean openErrors(String filename, String sessionId) throws JLIException;
    public boolean openErrors(String filename, AbstractJLISession sess) throws JLIException;

	public void save(String filename, List<String> filenames, 
			String sessionId) throws JLIException;
	public void save(String filename, List<String> filenames, 
			AbstractJLISession sess) throws JLIException;

    public void backup(String filename, String targetdir,
            String sessionId) throws JLIException;
    public void backup(String filename, String targetdir,
    		AbstractJLISession sess) throws JLIException;
    
    public void erase(String filename, List<String> filenames,
            boolean eraseChildren, String sessionId) throws JLIException;
    public void erase(String filename, List<String> filenames,
            boolean eraseChildren, AbstractJLISession sess) throws JLIException;
    
    public void eraseNotDisplayed(String sessionId) throws JLIException;
    public void eraseNotDisplayed(AbstractJLISession sess) throws JLIException;
 
    public FileAssembleResults assemble(AssembleInstructions instro, String sessionId) throws JLIException;
    public FileAssembleResults assemble(AssembleInstructions instro, AbstractJLISession sess) throws JLIException;
    
    /** @deprecated - Use assemble(AssembleInstruction, sessionId) instead */
    public FileAssembleResults assemble(
    		String dirname,
    		String filename,
    		String genericName,
    		String intoAsm,
    		List<Integer> componentPath,
    		JLTransform transData,
    		List<JLConstraintInput> constraints,
    		boolean packageAssembly,
    		String refModel,
    		boolean walkChildren,
    		boolean assembleToRoot,
    		boolean suppress,
            String sessionId) throws JLIException;
    
    /** @deprecated - Use assemble(AssembleInstruction, sess) instead */
    public FileAssembleResults assemble(
    		String dirname,
    		String filename,
    		String genericName,
    		String intoAsm,
    		List<Integer> componentPath,
    		JLTransform transData,
    		List<JLConstraintInput> constraints,
    		boolean packageAssembly,
    		String refModel,
    		boolean walkChildren,
    		boolean assembleToRoot,
    		boolean suppress,
            AbstractJLISession sess) throws JLIException;
    
	public void regenerate(String filename, List<String> filenames, 
			boolean display, String sessionId) throws JLIException;
	public void regenerate(String filename, List<String> filenames, 
			boolean display, AbstractJLISession sess) throws JLIException;

	public void refresh(String filename, String sessionId) throws JLIException;
	public void refresh(String filename, AbstractJLISession sess) throws JLIException;

	public void repaint(String filename, String sessionId) throws JLIException;
	public void repaint(String filename, AbstractJLISession sess) throws JLIException;

	public FileOpenResults getActive(String sessionId)
			throws JLIException;
	public FileOpenResults getActive(AbstractJLISession sess)
			throws JLIException;

    public String rename(String filename, String newname,
	        boolean onlyInSession, String sessionId)
			throws JLIException;
    public String rename(String filename, String newname,
	        boolean onlyInSession, AbstractJLISession sess)
			throws JLIException;

	public List<String> list(String filename, List<String> filenames, String sessionId)
		throws JLIException;
	public List<String> list(String filename, List<String> filenames, AbstractJLISession sess)
		throws JLIException;

	public boolean exists(String filename, String sessionId) throws JLIException;
	public boolean exists(String filename, AbstractJLISession sess) throws JLIException;

	public FileInfoResults getFileinfo(String filename, String sessionId) throws JLIException;
	public FileInfoResults getFileinfo(String filename, AbstractJLISession sess) throws JLIException;

    public boolean hasInstances(String filename, String sessionId) throws JLIException;
    public boolean hasInstances(String filename, AbstractJLISession sess) throws JLIException;

    public FileListInstancesResults listInstances(String filename, String sessionId) throws JLIException;
    public FileListInstancesResults listInstances(String filename, AbstractJLISession sess) throws JLIException;

    public MasspropsData massprops(String filename, String sessionId) throws JLIException;
    public MasspropsData massprops(String filename, AbstractJLISession sess) throws JLIException;
    
    public List<String> getRelations(String filename, String sessionId) throws JLIException;
    public List<String> getRelations(String filename, AbstractJLISession sess) throws JLIException;
    
    public void setRelations(String filename, List<String> relations, String sessionId) throws JLIException;
    public void setRelations(String filename, List<String> relations, AbstractJLISession sess) throws JLIException;

    public List<String> getPostRegenRelations(String filename, String sessionId) throws JLIException;
    public List<String> getPostRegenRelations(String filename, AbstractJLISession sess) throws JLIException;
    
    public void setPostRegenRelations(String filename, List<String> relations, String sessionId) throws JLIException;
    public void setPostRegenRelations(String filename, List<String> relations, AbstractJLISession sess) throws JLIException;

    public String getMassUnits(String filename, String sessionId) throws JLIException;
    public String getMassUnits(String filename, AbstractJLISession sess) throws JLIException;

    public void setMassUnits(String filename, String units, boolean convert, String sessionId) throws JLIException;
    public void setMassUnits(String filename, String units, boolean convert, AbstractJLISession sess) throws JLIException;

    public String getLengthUnits(String filename, String sessionId) throws JLIException;
    public String getLengthUnits(String filename, AbstractJLISession sess) throws JLIException;

    public void setLengthUnits(String filename, String units, boolean convert, String sessionId) throws JLIException;
    public void setLengthUnits(String filename, String units, boolean convert, AbstractJLISession sess) throws JLIException;

    public JLTransform getTransform(String assembly, List<Integer> path, String csys, String sessionId) throws JLIException;
    public JLTransform getTransform(String assembly, List<Integer> path, String csys, AbstractJLISession sess) throws JLIException;

    public boolean isActive(String filename, String sessionId) throws JLIException;
    public boolean isActive(String filename, AbstractJLISession sess) throws JLIException;
    
    public void display(String filename, boolean activate, String sessionId) throws JLIException;
    public void display(String filename, boolean activate, AbstractJLISession sess) throws JLIException;

    public void closeWindow(String filename, String sessionId) throws JLIException;
    public void closeWindow(String filename, AbstractJLISession sess) throws JLIException;

    public List<String> listSimpReps(String filename, String name, String sessionId) throws JLIException;
    public List<String> listSimpReps(String filename, String name, AbstractJLISession sess) throws JLIException;

    public String getCurrentMaterial(String filename, String sessionId) throws JLIException;
	public String getCurrentMaterial(String filename, AbstractJLISession sess) throws JLIException;

	public List<String> listMaterials(String filename, String materialName, String sessionId) throws JLIException;
	public List<String> listMaterials(String filename, String materialName, AbstractJLISession sess) throws JLIException;

	public void loadMaterialFile(String filename, String dirname, String materialFile, String sessionId) throws JLIException;
	public void loadMaterialFile(String filename, String dirname, String materialFile, AbstractJLISession sess) throws JLIException;

	public void setCurrentMaterial(String filename, String materialName, String sessionId) throws JLIException;
	public void setCurrentMaterial(String filename, String materialName, AbstractJLISession sess) throws JLIException;

	public void deleteMaterial(String filename, String materialName, String sessionId) throws JLIException;
	public void deleteMaterial(String filename, String materialName, AbstractJLISession sess) throws JLIException;
}
