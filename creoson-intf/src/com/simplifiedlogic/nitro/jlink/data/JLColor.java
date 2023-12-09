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
package com.simplifiedlogic.nitro.jlink.data;

/**
 * Representation of an RGB color
 * @author Adam Andrews
 *
 */
public class JLColor {

	int red;
	int green;
	int blue;
	
	/**
	 * Constructor which takes standard RGB integer values (0-255)
	 * @param red The Red component of the color
	 * @param green The Green component of the color
	 * @param blue The Blue component of the color
	 */
	public JLColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	/**
	 * Constructor which takes intensity values.  An intensity is a double in the range 0.0-100.0 .
	 * @param red Red intensity
	 * @param green Green intensity
	 * @param blue Blue intensity
	 */
	public JLColor(double red, double green, double blue) {
		setRedIntensity(red);
		setGreenIntensity(green);
		setBlueIntensity(blue);
	}

	/**
	 * Constructor which takes a standard long color value
	 * @param color A long color value to convert to RGB
	 */
	public JLColor(long color) {
		this.red = (int)((color & 0xFF0000)>>16);
		this.green = (int)((color & 0x00FF00)>>8);
		this.blue = (int)(color & 0x0000FF);
	}

	/**
	 * Convert a color value to a color intensity
	 * @param value The color value (0-255)
	 * @return The intensity (0.0-100.0)
	 */
	public static double valueToIntensity(int value) {
		return (double)value/255.0 * 100.0;
	}
	
	/**
	 * Convert a color intensity to a color value
	 * @param intensity The intensity (0.0-100.0)
	 * @return The color value (0-255)
	 */
	public static int intensityToValue(double intensity) {
		return (int)(intensity / 100.0 * 255.0);
	}

	/**
	 * @return Red value
	 */
	public int getRed() {
		return red;
	}
	
	/**
	 * @return Red intensity
	 */
	public double getRedIntensity() {
		return valueToIntensity(red);
	}

	/**
	 * @param red Red value
	 */
	public void setRed(int red) {
		this.red = red;
	}
	
	/**
	 * @param intensity Red intensity
	 */
	public void setRedIntensity(double intensity) {
		this.red = intensityToValue(intensity);
	}

	/**
	 * @return Green value
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * @return Green intensity
	 */
	public double getGreenIntensity() {
		return valueToIntensity(green);
	}

	/**
	 * @param green Green value
	 */
	public void setGreen(int green) {
		this.green = green;
	}

	/**
	 * @param intensity Green intensity
	 */
	public void setGreenIntensity(double intensity) {
		this.green = intensityToValue(intensity);
	}

	/**
	 * @return Blue value
	 */
	public int getBlue() {
		return blue;
	}

	/**
	 * @return Blue intensity
	 */
	public double getBlueIntensity() {
		return valueToIntensity(blue);
	}

	/**
	 * @param blue Blue value
	 */
	public void setBlue(int blue) {
		this.blue = blue;
	}

	/**
	 * @param intensity Blue intensity
	 */
	public void setBlueIntensity(double intensity) {
		this.blue = intensityToValue(intensity);
	}

}
