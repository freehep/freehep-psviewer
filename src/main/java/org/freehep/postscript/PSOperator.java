// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public abstract class PSOperator extends PSSimple {

	protected Class<?>[] operandTypes = new Class[0];

	public PSOperator() {
		super("operator", false);
	}

	// lowercased name of the final part of the Class Name
	public String getName() {
		String name = getClass().getName();
		int pos = name.lastIndexOf('.');
		if (pos >= 0) {
			name = name.substring(pos + 1);
		}
		return name.toLowerCase();
	}

	@Override
	public boolean checkAndExecute(OperandStack os) {
		// set the mark for errors
		os.mark();

		// Check for StackUnderflow
		if (operandTypes.length > os.size()) {
			error(os, new StackUnderflow());
			return true;
		}

		// Check for TypeCheck
		if (!os.checkType(operandTypes)) {
			error(os, new TypeCheck());
			return true;
		}

		// excecute operator implementation
		return execute(os);
	}

	@Override
	public String getType() {
		return "operatortype";
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSOperator) {
			return getClass().equals(o.getClass());
		}
		return false;
	}

	// careful: do not add any instance variables
	@Override
	public Object clone() throws CloneNotSupportedException {
		return this;
	}

	// careful: do not add any instance variables
	@Override
	public PSObject copy() {
		return this;
	}

	@Override
	public String cvs() {
		return getName();
	}

	@Override
	public String toString() {
		return "//" + getName();
	}
}
