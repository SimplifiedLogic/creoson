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
package com.simplifiedlogic.nitro.util;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.base.CallMatrix3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallVector3D;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallInertia;
import com.simplifiedlogic.nitro.jlink.data.JLInertia;
import com.simplifiedlogic.nitro.jlink.data.JLMatrix;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.data.JLTransform;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Utility class for creating JLMatrix and JLTransform objects
 * 
 * @author Adam Andrews
 *
 */
public class JLMatrixMaker {

	/**
	 * Create a JLMatrix from a Creo Matrix3D object
	 * @param matrix The Creo matrix
	 * @return The resulting JShell matrix
	 * @throws JLIException
	 */
	public static JLMatrix create(CallMatrix3D matrix) throws JLIException {
    	try {
	    	return loadMatrix(matrix);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    }

	/**
	 * Create a JLMatrix from a Creo Transform3D object
	 * @param trans The Creo transform
	 * @return The resulting JShell matrix
	 * @throws JLIException
	 */
	public static JLMatrix create(CallTransform3D trans) throws JLIException {
    	try {
	    	return loadMatrix(trans.getMatrix());
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    }
    
	/**
	 * Create a JLMatrix from a Creo Matrix3D object
	 * @param matrix The Creo matrix
	 * @return The resulting JShell matrix
	 * @throws JLIException
	 */
    private static JLMatrix loadMatrix(CallMatrix3D matrix) throws jxthrowable {
        JLMatrix m = new JLMatrix();
        for (int i=0; i<4; i++) {
            for (int k=0; k<4; k++) {
                m.set(i,k, matrix.get(i, k));
            }
        }
        return m;
    }
    
    /**
     * Create a JLMatrix from a JShell JLTransform object
     * @param xf The JShell transform object
     * @return The resulting JShell matrix
     * @throws JLIException
     */
    public static JLMatrix create(JLTransform xf) throws JLIException {
        JLMatrix m = new JLMatrix();
        init(m);
        if (xf!=null) {
	        if (xf.xvector!=null && (xf.xrot!=null || xf.yrot!=null || xf.zrot!=null))
	            throw new JLIException("Cannot specify both rotation angles and transform matrix on assembly");
	        if (xf.xvector!=null) {
	            setXVector(m, xf.xvector.getX(), xf.xvector.getY(), xf.xvector.getZ());
	            setYVector(m, xf.yvector.getX(), xf.yvector.getY(), xf.yvector.getZ());
	            setZVector(m, xf.zvector.getX(), xf.zvector.getY(), xf.zvector.getZ());
	        }
	        else {
	            if (xf.yrot!=null)
	                rotateY(m, degreesToRadians(xf.yrot.doubleValue()));
	            if (xf.zrot!=null)
	                rotateZ(m, degreesToRadians(xf.zrot.doubleValue()));
	            if (xf.xrot!=null)
	                rotateX(m, degreesToRadians(xf.xrot.doubleValue()));
	        }
	        if (xf.origin!=null)
	        	setOrigin(m, xf.origin.getX(), xf.origin.getY(), xf.origin.getZ());
	        else
	        	setOrigin(m, 0.0, 0.0, 0.0);
        }
        
        return m;
    }

    /**
     * Initialize a JLMatrix to an origin matrix
     * @param m The matrix to initialize
     */
    public static void init(JLMatrix m) {
        m.set(0, 0, 1.0);
        m.set(1, 1, 1.0);
        m.set(2, 2, 1.0);
        m.set(3, 3, 1.0);
    }

    /**
     * Create a Creo Matrix3D object from a JLMatrix
     * @param m The JShell matrix to export
     * @return The resulting Creo matrix
     * @throws jxthrowable
     */
    public static CallMatrix3D export(JLMatrix m) throws jxthrowable {
        CallMatrix3D matrix = CallMatrix3D.create();
        for (int i=0; i<4; i++) {
            for (int k=0; k<4; k++) {
                matrix.set(i,k, m.get(i, k));
            }
        }
        return matrix;
    }
    
    /**
     * Set the X-Vector of a matrix
     * @param m The JShell matrix to update
     * @param x Vector's X coordinate
     * @param y Vector's Y coordinate
     * @param z Vector's Z coordinate
     */
    public static void setXVector(JLMatrix m, double x, double y, double z) {
    	m.set(0, 0, x);
        m.set(0, 1, y);
        m.set(0, 2, z);
    }

    /**
     * Set the Y-Vector of a matrix
     * @param m The JShell matrix to update
     * @param x Vector's X coordinate
     * @param y Vector's Y coordinate
     * @param z Vector's Z coordinate
     */
    public static void setYVector(JLMatrix m, double x, double y, double z) {
    	m.set(1, 0, x);
        m.set(1, 1, y);
        m.set(1, 2, z);
    }

    /**
     * Set the Z-Vector of a matrix
     * @param m The JShell matrix to update
     * @param x Vector's X coordinate
     * @param y Vector's Y coordinate
     * @param z Vector's Z coordinate
     */
    public static void setZVector(JLMatrix m, double x, double y, double z) {
    	m.set(2, 0, x);
        m.set(2, 1, y);
        m.set(2, 2, z);
    }

    /**
     * Set the Origin of a matrix
     * @param m The JShell matrix to update
     * @param x Origin's X coordinate
     * @param y Origin's Y coordinate
     * @param z Origin's Z coordinate
     */
    public static void setOrigin(JLMatrix m, double x, double y, double z) {
        m.set(3, 0, x);
        m.set(3, 1, y);
        m.set(3, 2, z);
    }

    /**
     * Perform a dot-product of two matrices.  The first matrix will be updated with the result
     * @param m1 The first matrix.  This one will be updated.
     * @param m2 The second matrix.  This one will NOT be updated.
     */
    public static void dot(JLMatrix m1, JLMatrix m2) {
        double[][] newval = new double[4][4];
        
        for (int col=0; col<4; col++) {
            for (int row=0; row<4; row++) {
                newval[col][row] = 
                    m2.get(col,0)*m1.get(0,row) +
                    m2.get(col,1)*m1.get(1,row) +
                    m2.get(col,2)*m1.get(2,row) +
                    m2.get(col,3)*m1.get(3,row);
            }
        }
        
//            newval[0][0] = m2.get(0,0)*m1.get(0,0) + m2.get(0,1)*m1.get(1,0) + m2.get(0,2)*m1.get(2,0) + m2.get(0,3)*m1.get(3,0);
//            newval[0][1] = m2.get(0,0)*m1.get(0,1) + m2.get(0,1)*m1.get(1,1) + m2.get(0,2)*m1.get(2,1) + m2.get(0,3)*m1.get(3,1);
        
        for (int col=0; col<4; col++) {
            for (int row=0; row<4; row++) {
                m1.set(col, row, newval[col][row]);
            }
        }
    }
    
    /**
     * Rotate a matrix on the X axis
     * <pre>
     * x-axis rotation:
     * [  1       0       0     ]
     * [  0       cos(a)  sin(a)]
     * [  0      -sin(a)  cos(a)]
     * </pre>
     * 
     * @param old The matrix to update
     * @param angle The angle to rotate, in radians
     */
    public static void rotateX(JLMatrix old, double angle) {
        // assume in radians
        //System.out.println("rotating X degrees=" + radiansToDegrees(angle) + ", radians=" + angle);
        
        double sinval = Math.sin(-angle);
        double cosval = Math.cos(-angle);
        
        JLMatrix m = new JLMatrix();
        m.set(0, 0, 1.0);
        m.set(1, 1, cosval);
        m.set(2, 1, sinval);
        m.set(1, 2, -sinval);
        m.set(2, 2, cosval);
        m.set(3, 3, 1.0);
        
        dot(old, m);
    }

    /**
     * Rotate a matrix on the Y axis
     * <pre>
     * y-axis rotation:
     * [  cos(a)  0      -sin(a)]
     * [  0       1       0     ]
     * [  sin(a)  0       cos(a)]
     * </pre>
     * 
     * @param old The matrix to update
     * @param angle The angle to rotate, in radians
     */
    public static void rotateY(JLMatrix old, double angle) {
        // assume in radians
        //System.out.println("rotating Y degrees=" + radiansToDegrees(angle) + ", radians=" + angle);
        
        double sinval = Math.sin(-angle);
        double cosval = Math.cos(-angle);
        
        JLMatrix m = new JLMatrix();
        m.set(0, 0, cosval);
        m.set(2, 0, -sinval);
        m.set(1, 1, 1.0);
        m.set(0, 2, sinval);
        m.set(2, 2, cosval);
        m.set(3, 3, 1.0);
        
        dot(old, m);
    }

    /**
     * Rotate a matrix on the Z axis
     * <pre>
     * z-axis rotation:
     * [  cos(a)  sin(a)  0     ]
     * [ -sin(a)  cos(a)  0     ]
     * [  0       0       1     ]
     * </pre>
     * 
     * @param old The matrix to update
     * @param angle The angle to rotate, in radians
     */
    public static void rotateZ(JLMatrix old, double angle) {
        // assume in radians
        //System.out.println("rotating Z degrees=" + radiansToDegrees(angle) + ", radians=" + angle);
        
        double sinval = Math.sin(-angle);
        double cosval = Math.cos(-angle);
        
        JLMatrix m = new JLMatrix();
        m.set(0, 0, cosval);
        m.set(0, 1, sinval);
        m.set(1, 0, -sinval);
        m.set(1, 1, cosval);
        m.set(2, 2, 1.0);
        m.set(3, 3, 1.0);
        
        dot(old, m);
    }

    /**
     * Convert degrees to radians
     * @param degrees An angle in degrees
     * @return An angle in radians
     */
    public static double degreesToRadians(double degrees) {
        return (degrees * Math.PI) / (double)180.0;
    }
    
    /**
     * Convert radians to degrees
     * @param radians An angle in radians
     * @return An angle in degrees
     */
    public static double radiansToDegrees(double radians) {
        return (radians * (double)180.0) / Math.PI; 
    }

    /**
     * Print out a matrix (for debugging)
     * @param m The matrix to print
     */
    public static void print(JLMatrix m) {
        for (int row=0; row<4; row++) {
            for (int col=0; col<4; col++) {
                System.out.print(" " + m.get(col, row));
            }
            System.out.println("");
        }
    }

    /**
     * Convert a JLMatrix to a JLTransform
     * @param m The JShell matrix to convert
     * @return The JShell transform created
     * @throws JLIException
     */
    public static JLTransform writeTransformTable(JLMatrix m) throws JLIException {
    	try {
	    	CallMatrix3D matrix = export(m);
	    	CallTransform3D trans = CallTransform3D.create(matrix);
	    	return writeTransformTable(trans);
        }
        catch (jxthrowable jxe) {
        	throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving a Transform matrix"), jxe);
        }
    }

    /**
     * Convert a Creo Transform3D object to a JLTransform
     * @param featTrans The Creo transform
     * @return The JShell transform created
     * @throws JLIException
     */
    public static JLTransform writeTransformTable(CallTransform3D featTrans) throws JLIException {
        JLTransform xf = new JLTransform();
        try {
	        CallVector3D v;
	        v = featTrans.getXAxis();
	        xf.xvector = new JLPoint(v.get(0), v.get(1), v.get(2));
	        double m00 = v.get(0);
	        double m10 = v.get(1);
	        double m20 = v.get(2);
	        
	        v = featTrans.getYAxis();
	        xf.yvector = new JLPoint(v.get(0), v.get(1), v.get(2));
	        double m11 = v.get(1);
	
	        v = featTrans.getZAxis();
	        xf.zvector = new JLPoint(v.get(0), v.get(1), v.get(2));
	        double m02 = v.get(0);
	        double m12 = v.get(1);
	        double m22 = v.get(2);
	        
	        CallPoint3D origin = featTrans.getOrigin();
	        xf.origin = new JLPoint(origin.get(0), origin.get(1), origin.get(2));
	        
	        // calculate angles
	        double heading, attitude, bank;
	        if (m10 > 0.998) { // singularity at north pole
	            heading = Math.atan2(m02,m22);
	            attitude = Math.PI/2;
	            bank = 0;
	        }
	        else if (m10 < -0.998) { // singularity at south pole
	            heading = Math.atan2(m02,m22);
	            attitude = -Math.PI/2;
	            bank = 0;
	        }
	        else {
	            heading = Math.atan2(-m20,m00);
	            bank = Math.atan2(-m12,m11);
	            attitude = Math.asin(m10);
	        }
	        attitude = -attitude; // negative sign because that's how pro/e does it...
	        xf.xrot = new Double(radiansToDegrees(bank));
	        xf.yrot = new Double(radiansToDegrees(heading));
	        xf.zrot = new Double(radiansToDegrees(attitude));
        
	        return xf;
        }
        catch (jxthrowable jxe) {
        	throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving a Transform matrix"), jxe);
        }
    }
    
    /**
     * Convert a Creo Inertia object to a JLInertia
     * @param inertia The Creo inertia object
     * @return The JShell inertia object created
     * @throws JLIException
     */
    public static JLInertia writeInertia(CallInertia inertia) throws jxthrowable {
        JLInertia xf = new JLInertia();

        xf.xvector = new JLPoint(
        		inertia.get(0, 0),
        		inertia.get(0, 1),
        		inertia.get(0, 2)
        		);
        xf.yvector = new JLPoint(
        		inertia.get(1, 0),
        		inertia.get(1, 1),
        		inertia.get(1, 2)
        		);
        xf.zvector = new JLPoint(
        		inertia.get(2, 0),
        		inertia.get(2, 1),
        		inertia.get(2, 2)
        		);
    
        return xf;
    }
    
    /**
     * Print out a Creo Transform3D object (for debugging)
     * @param trans The Creo transform to print
     * @throws jxthrowable
     */
    public static void showTransform(CallTransform3D trans) throws jxthrowable {
    	CallVector3D x = trans.getXAxis();
    	CallVector3D y = trans.getYAxis();
    	CallVector3D z = trans.getZAxis();
        System.out.println("    X-axis: " + JLPointMaker.toString(x));
        System.out.println("    Y-axis: " + JLPointMaker.toString(y));
        System.out.println("    Z-axis: " + JLPointMaker.toString(z));
        System.out.println("    Origin: " + JLPointMaker.toString(trans.getOrigin()));
    }
    
}
