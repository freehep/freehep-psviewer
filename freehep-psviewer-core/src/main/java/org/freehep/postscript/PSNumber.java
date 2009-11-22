// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSNumber.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public abstract class PSNumber extends PSSimple implements Comparable {

	public PSNumber(String name) {
		super(name, true);
	}

	public float getFloat() {
		return (float) getDouble();
	}

	public boolean execute(OperandStack os) {
		os.push(this);
		return true;
	}

	public abstract double getDouble();

	public abstract int getInt() throws RangeException;
}
