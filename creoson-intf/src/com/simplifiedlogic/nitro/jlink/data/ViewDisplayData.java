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

/**
 * Creo display parameters used to create drawing views
 * 
 * @author Adam Andrews
 *
 */
public class ViewDisplayData {

	public static final String CABLESTYLE_DEFAULT = "default";
	public static final String CABLESTYLE_CENTERLINE = "centerline";
	public static final String CABLESTYLE_THICK = "thick";
	
	public static final String STYLE_DEFAULT = "default";
	public static final String STYLE_WIREFRAME = "wireframe";
	public static final String STYLE_HIDDEN_LINE = "hidden_line";
	public static final String STYLE_NO_HIDDEN = "no_hidden";
	public static final String STYLE_SHADED = "shaded";
	public static final String STYLE_FOLLOW_ENV = "follow_environment";
	// this is creo only...
//	public static final String STYLE_SHADED_W_EDGES = "shaded_w_edges";
	
	public static final String TANGENT_DEFAULT = "default";
	public static final String TANGENT_NONE = "none";
	public static final String TANGENT_CENTERLINE = "centerline";
	public static final String TANGENT_PHANTOM = "phantom";
	public static final String TANGENT_DIMMED = "dimmed";
	public static final String TANGENT_SOLID = "solid";

	private String cableStyle;
	private boolean removeQuiltHiddenLines;
	private boolean showConceptModel;
	private boolean showWeldXSection;
	private String style;
	private String tangentStyle;
	
	/**
	 * @return Cable Style
	 */
	public String getCableStyle() {
		return cableStyle;
	}
	/**
	 * @param cableStyle Cable Style
	 */
	public void setCableStyle(String cableStyle) {
		this.cableStyle = cableStyle;
	}
	/**
	 * @return Whether to remove quilt hidden lines
	 */
	public boolean isRemoveQuiltHiddenLines() {
		return removeQuiltHiddenLines;
	}
	/**
	 * @param removeQuiltHiddenLines Whether to remove quilt hidden lines
	 */
	public void setRemoveQuiltHiddenLines(boolean removeQuiltHiddenLines) {
		this.removeQuiltHiddenLines = removeQuiltHiddenLines;
	}
	/**
	 * @return Whether to show the concept model
	 */
	public boolean isShowConceptModel() {
		return showConceptModel;
	}
	/**
	 * @param showConceptModel Whether to show the concept model
	 */
	public void setShowConceptModel(boolean showConceptModel) {
		this.showConceptModel = showConceptModel;
	}
	/**
	 * @return Whether to show weld cross-section
	 */
	public boolean isShowWeldXSection() {
		return showWeldXSection;
	}
	/**
	 * @param showWeldXSection Whether to show weld cross-section
	 */
	public void setShowWeldXSection(boolean showWeldXSection) {
		this.showWeldXSection = showWeldXSection;
	}
	/**
	 * @return Style
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * @param style Style
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * @return Tangent Style
	 */
	public String getTangentStyle() {
		return tangentStyle;
	}
	/**
	 * @param tangentStyle Tangent Style
	 */
	public void setTangentStyle(String tangentStyle) {
		this.tangentStyle = tangentStyle;
	}
	
}
