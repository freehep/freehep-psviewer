// Copyright 2001, FreeHEP.
package org.freehep.postscript.types;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSArray.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public class PSArray extends PSPackedArray {

	private PSArray(String n, PSObject[] a, int index, int count) {
		super(n, a, index, count, UNLIMITED);
	}

	public PSArray(int n) {
		super(n, UNLIMITED);
		setName("array");
	}

	public PSArray(PSObject[] a) {
		super(a, UNLIMITED);
		setName("array");
	}

	public PSArray(float[] f) {
		super(f);
	}

	public PSArray(double[] d) {
		super(d);
	}

	public PSArray(String[] s) {
		super(s);
	}

	@Override
	public String getType() {
		return "arraytype";
	}

	public void set(int i, PSObject o) {
		array[start + i] = o;
	}

	public void set(int i, String s) {
		set(i, new PSName(s));
	}

	public void set(int i, double d) {
		set(i, new PSReal(d));
	}

	public void set(int i, int d) {
		set(i, new PSInteger(d));
	}

	public void set(double[] d) {
		for (int i = 0; i < d.length; i++) {
			set(i, d[i]);
		}
	}

	public PSArray subArray(int index, int count) {
		if ((index < 0) || (index + count) > length) {
			throw new IllegalArgumentException("Trying to create subArray("
					+ index + ", " + count + ") with length=" + length + ".");
		}
		return new PSArray(name, array, index, count);
	}

	@Override
	public int hashCode() {
		return array.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSArray) {
			return (array == ((PSArray) o).array);
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSArray(name, array, start, length);
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return "--" + ((isExecutable()) ? "*" : "") + name + " (" + start
				+ ".." + (start + length) + ") --";
	}
}
