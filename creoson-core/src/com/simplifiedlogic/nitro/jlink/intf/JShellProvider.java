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
package com.simplifiedlogic.nitro.jlink.intf;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.impl.JLBom;
import com.simplifiedlogic.nitro.jlink.impl.JLConnection;
import com.simplifiedlogic.nitro.jlink.impl.JLDimension;
import com.simplifiedlogic.nitro.jlink.impl.JLDrawing;
import com.simplifiedlogic.nitro.jlink.impl.JLFamilyTable;
import com.simplifiedlogic.nitro.jlink.impl.JLFeature;
import com.simplifiedlogic.nitro.jlink.impl.JLFile;
import com.simplifiedlogic.nitro.jlink.impl.JLGeometry;
import com.simplifiedlogic.nitro.jlink.impl.JLGlobal;
import com.simplifiedlogic.nitro.jlink.impl.JLLayer;
import com.simplifiedlogic.nitro.jlink.impl.JLNote;
import com.simplifiedlogic.nitro.jlink.impl.JLParameter;
import com.simplifiedlogic.nitro.jlink.impl.JLProe;
import com.simplifiedlogic.nitro.jlink.impl.JLTransfer;
import com.simplifiedlogic.nitro.jlink.impl.JLView;
import com.simplifiedlogic.nitro.jlink.impl.JLWindchill;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;

/**
 * Single point of entry; class which provides JShell handlers to outside 
 * Java libraries.
 * @author Adam Andrews
 *
 */
public class JShellProvider implements IJShellProvider {
	
	private IJLBom bomHandler = null;
	private IJLConnection connHandler = null;
	private IJLDimension dimHandler = null;
	private IJLFamilyTable ftHandler = null;
	private IJLFile fileHandler = null;
	private IJLParameter paramHandler = null;
	private IJLProe proeHandler = null;
	private IJLTransfer transferHandler = null;
	private IJLFeature featureHandler = null;
	private IJLDrawing drawingHandler = null;
	private IJLNote noteHandler = null;
	private IJLLayer layerHandler = null;
	private IJLWindchill windchillHandler = null;
	private IJLGeometry geomHandler = null;
	private IJLView viewHandler = null;

	private static JShellProvider instance = new JShellProvider();

	/**
	 * @return the singleton instance of JShellProvider
	 */
	public static JShellProvider getInstance() {
		return instance;
	}
	
	/**
	 * Initialize when running as part of an OSGi application
	 */
	public void initializeOSGi() {
		JLGlobal.libraryLoaded=true;
	}
	
	/**
	 * Initialize when running as part of a console application
	 */
	public void initializeStandalone() {
	}

	public IJLBom getJLBom() {
		if (instance.bomHandler==null) {
			instance.bomHandler = new JLBom();
		}
		return instance.bomHandler;
	}

	public IJLConnection getJLConnection() {
		if (instance.connHandler==null) {
			instance.connHandler = new JLConnection();
		}
		return instance.connHandler;
	}

	public IJLDimension getJLDimension() {
		if (instance.dimHandler==null) {
			instance.dimHandler = new JLDimension();
		}
		return instance.dimHandler;
	}

	public IJLFamilyTable getJLFamilyTable() {
		if (instance.ftHandler==null) {
			instance.ftHandler = new JLFamilyTable();
		}
		return instance.ftHandler;
	}

	public IJLFile getJLFile() {
		if (instance.fileHandler==null) {
			instance.fileHandler = new JLFile();
		}
		return instance.fileHandler;
	}

	public IJLParameter getJLParameter() {
		if (instance.paramHandler==null) {
			instance.paramHandler = new JLParameter();
		}
		return instance.paramHandler;
	}

	public IJLProe getJLProe() {
		if (instance.proeHandler==null) {
			instance.proeHandler = new JLProe();
		}
		return instance.proeHandler;
	}

	public IJLTransfer getJLTransfer() {
		if (instance.transferHandler==null) {
			instance.transferHandler = new JLTransfer();
		}
		return instance.transferHandler;
	}

	public IJLFeature getJLFeature() {
		if (instance.featureHandler==null) {
			instance.featureHandler = new JLFeature();
		}
		return instance.featureHandler;
	}

	public IJLDrawing getJLDrawing() {
		if (instance.drawingHandler==null) {
			instance.drawingHandler = new JLDrawing();
		}
		return instance.drawingHandler;
	}

	public IJLNote getJLNote() {
		if (instance.noteHandler==null) {
			instance.noteHandler = new JLNote();
		}
		return instance.noteHandler;
	}
	
	public IJLLayer getJLLayer() {
		if (instance.layerHandler==null) {
			instance.layerHandler = new JLLayer();
		}
		return instance.layerHandler;
	}
	
	public IJLWindchill getJLWindchill() {
		if (instance.windchillHandler==null) {
			instance.windchillHandler = new JLWindchill();
		}
		return instance.windchillHandler;
	}

	public IJLGeometry getJLGeometry() {
		if (instance.geomHandler==null) {
			instance.geomHandler = new JLGeometry();
		}
		return instance.geomHandler;
	}

	public IJLView getJLView() {
		if (instance.viewHandler==null) {
			instance.viewHandler = new JLView();
		}
		return instance.viewHandler;
	}

	public static AbstractJLISession createSession() {
		return JLISession.createSession();
	}
	
    public static void deleteSession(AbstractJLISession sess) throws JLIException {
    	JLISession.deleteSession(sess);
    }

	public static AbstractJLISession getSession(String sessionId) {
		return JLISession.getSession(sessionId);
	}

}
