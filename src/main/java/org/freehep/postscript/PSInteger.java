// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSInteger.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSInteger extends PSNumber {
	private int value;

	public PSInteger(double v) {
		this((int) v);
	}

	public PSInteger(long v) {
		this((int) v);
	}

	public PSInteger(int v) {
		super("integer");
		value = v;
	}

	public String getType() {
		return "integertype";
	}

	public int getValue() {
		return value;
	}

	public double getDouble() {
		return value;
	}

	public int getInt() throws RangeException {
		return getValue();
	}

	public int compareTo(Object o) {
		int i1 = getValue();
		if (o instanceof PSReal) {
			double d2 = ((PSReal) o).getValue();
			return (i1 > d2) ? +1 : (i1 < d2) ? -1 : 0;
		} else {
			int i2 = ((PSInteger) o).getValue();
			return (i1 > i2) ? +1 : (i1 < i2) ? -1 : 0;
		}
	}

	public int hashCode() {
		return value;
	}

	public boolean equals(Object o) {
		if (o instanceof PSInteger) {
			return (value == ((PSInteger) o).getValue());
		}
		return false;
	}

	public Object clone() {
		return new PSInteger(value);
	}

	public String cvs() {
		return toString();
	}

	public String toString() {
		return "" + value;
	}
}
