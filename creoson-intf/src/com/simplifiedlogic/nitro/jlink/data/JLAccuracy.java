package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;

public class JLAccuracy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The accuracy value from Creo
	 */
	private double accuracy;
	/**
	 * True if relative accuracy, false if absolute accuracy
	 */
	private boolean relative;
	
	public double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	public boolean isRelative() {
		return relative;
	}
	public void setRelative(boolean relative) {
		this.relative = relative;
	}
}
