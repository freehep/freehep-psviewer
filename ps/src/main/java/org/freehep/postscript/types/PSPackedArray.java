// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.postscript.errors.Unimplemented;
import org.freehep.postscript.stacks.OperandStack;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public class PSPackedArray extends PSComposite {
	protected PSObject[] array;
	protected int start = 0;
	protected int length = 0;

	protected PSPackedArray(String name, PSObject[] a, int index, int count) {
		this(name, a, index, count, READ_ONLY);
	}

	protected PSPackedArray(int n) {
		this(n, READ_ONLY);
	}

	public PSPackedArray(PSObject[] a) {
		this(a, READ_ONLY);
	}

	public PSPackedArray(float[] f) {
		this(f.length);
		for (int i = 0; i < f.length; i++) {
			array[i] = new PSReal(f[i]);
		}
	}

	public PSPackedArray(double[] d) {
		this(d.length);
		for (int i = 0; i < d.length; i++) {
			array[i] = new PSReal(d[i]);
		}
	}

	public PSPackedArray(String[] s) {
		this(s.length);
		for (int i = 0; i < s.length; i++) {
			array[i] = new PSName(s[i], true);
		}
	}

	protected PSPackedArray(String n, PSObject[] a, int index, int count,
			int access) {
		super(n, true, access);
		array = new PSObject[a.length];
		System.arraycopy(a, 0, array, 0, a.length);
		start = index;
		length = count;
	}

	protected PSPackedArray(int n, int access) {
		super("packedarray", true, access);
		array = new PSObject[n];
		for (int i = 0; i < array.length; i++) {
			array[i] = new PSNull();
		}
		start = 0;
		length = n;
	}

	protected PSPackedArray(PSObject[] a, int access) {
		super("packedarray", true, access);
		array = new PSObject[a.length];
		System.arraycopy(a, 0, array, 0, a.length);
		start = 0;
		length = a.length;
	}

	private int execIndex = -1;

	@Override
	public boolean execute(OperandStack os) {
		if (isLiteral()) {
			os.push(this);
			return true;
		}

		if (execIndex == -1) {
			// replace yourself with a copy, since the execIndex is a state
			// variable
			PSPackedArray copy;
			try {
				copy = (PSPackedArray) clone();
			} catch (CloneNotSupportedException e) {
				error(os, new Unimplemented());
				return true;
			}
			copy.setExecutable();
			copy.execIndex = 0;
			os.execStack().pop();
			os.execStack().push(copy);
			return false;
		}

		if (execIndex < size()) {
			PSObject obj = get(execIndex);
			if ((obj instanceof PSPackedArray)
					&& ((PSPackedArray) obj).isExecutable()) {
				// subarrays (exec) are just pushed on the stack.
				os.push(obj);
			} else {
				os.execStack().push(obj);
			}
			execIndex++;
			return false;
		} else {
			execIndex = -1;
			return true;
		}
	}

	@Override
	public String getType() {
		return "packedarraytype";
	}

	public PSObject get(int i) {
		return array[start + i];
	}

	public int getInteger(int i) {
		return ((PSInteger) array[start + i]).getValue();
	}

	public PSName getName(int i) {
		return (PSName) array[start + i];
	}

	public PSDictionary getDictionary(int i) {
		return (PSDictionary) array[start + i];
	}

	public void bind(int i, PSOperator op) {
		array[start + i] = op;
	}

	public int size() {
		return length;
	}

	public PSPackedArray subPackedArray(int index, int count) {
		if ((index < 0) || (index + count) > length) {
			throw new IllegalArgumentException(
					"Trying to create subPackedArray(" + index + ", " + count
							+ ") with length=" + length + ".");
		}
		return new PSPackedArray(name, array, index, count);
	}

	@Override
	public int hashCode() {
		return array.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSPackedArray) {
			return (array == ((PSPackedArray) o).array);
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		PSPackedArray p = new PSPackedArray(name, array, start, length);
		if (isExecutable()) {
			p.setExecutable();
		}
		return p;
	}

	@Override
	public PSObject copy() {
		PSPackedArray p = new PSPackedArray(toObjects());
		if (isExecutable()) {
			p.setExecutable();
		}
		return p;
	}

	public float[] toFloats() {
		float[] f = new float[length];
		for (int i = 0; i < length; i++) {
			f[i] = ((PSNumber) get(i)).getFloat();
		}
		return f;
	}

	public double[] toDoubles() {
		double[] d = new double[length];
		for (int i = 0; i < length; i++) {
			d[i] = ((PSNumber) get(i)).getDouble();
		}
		return d;
	}

	public PSObject[] toObjects() {
		PSObject[] o = new PSObject[length];
		for (int i = 0; i < length; i++) {
			o[i] = get(i);
		}
		return o;
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return "--" + ((isExecutable()) ? "*" : "") + name + " (" + start
				+ ".." + (start + length) + ", " + execIndex + ") --";
	}

	@Override
	public String toPrint() {
		StringBuffer s = new StringBuffer();
		s.append((isExecutable()) ? "{ " : "[ ");
		for (int i = 0; i < size(); i++) {
			s.append(get(i).toString());
			s.append(" ");
		}
		s.append((isExecutable()) ? "}" : "]");
		return s.toString();
	}

}
