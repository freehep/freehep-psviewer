// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public abstract class PSNumber extends PSSimple implements Comparable<Object> {

	public PSNumber(String name) {
		super(name, true);
	}

	public float getFloat() {
		return (float) getDouble();
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(this);
		return true;
	}

	public abstract double getDouble();

	public abstract int getInt() throws RangeException;
}
