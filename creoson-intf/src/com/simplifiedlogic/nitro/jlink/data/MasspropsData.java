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
 * Results from the file.massprops function
 * @author Adam Andrews
 *
 */
public class MasspropsData {

	private double volume;
	private double mass;
	private double density;
	private double surfaceArea;
	private JLInertia centerGravityInertiaTensor;
	private JLInertia coordSysInertia;
	private JLInertia coordSysInertiaTensor;
	private JLPoint gravityCenter;

	/**
	 * @return Model volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @param volume Model volume
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * @return Model mass
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * @param mass Model mass
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}

	/**
	 * @return Model density
	 */
	public double getDensity() {
		return density;
	}

	/**
	 * @param density Model density
	 */
	public void setDensity(double density) {
		this.density = density;
	}

	/**
	 * @return Model surface area
	 */
	public double getSurfaceArea() {
		return surfaceArea;
	}

	/**
	 * @param surfaceArea Model surface area
	 */
	public void setSurfaceArea(double surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	/**
	 * @return Model Center of Gravity Inertia Tensor
	 */
	public JLInertia getCenterGravityInertiaTensor() {
		return centerGravityInertiaTensor;
	}

	/**
	 * @param density Model Center of Gravity Inertia Tensor
	 */
	public void setCenterGravityInertiaTensor(JLInertia centerGravityInertiaTensor) {
		this.centerGravityInertiaTensor = centerGravityInertiaTensor;
	}

	/**
	 * @return Model Coordinate System Inertia
	 */
	public JLInertia getCoordSysInertia() {
		return coordSysInertia;
	}

	/**
	 * @param density Model Coordinate System Inertia
	 */
	public void setCoordSysInertia(JLInertia coordSysInertia) {
		this.coordSysInertia = coordSysInertia;
	}

	/**
	 * @return Model Coordinate System Inertia Tensor
	 */
	public JLInertia getCoordSysInertiaTensor() {
		return coordSysInertiaTensor;
	}

	/**
	 * @param density Model Coordinate System Inertia Tensor
	 */
	public void setCoordSysInertiaTensor(JLInertia coordSysInertiaTensor) {
		this.coordSysInertiaTensor = coordSysInertiaTensor;
	}

	/**
	 * @return Center of Gravity
	 */
	public JLPoint getGravityCenter() {
		return gravityCenter;
	}

	/**
	 * @param gravityCenter Center of Gravity
	 */
	public void setGravityCenter(JLPoint gravityCenter) {
		this.gravityCenter = gravityCenter;
	}

}
