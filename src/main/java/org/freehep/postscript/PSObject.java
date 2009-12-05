// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.util.logging.Logger;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public abstract class PSObject implements Cloneable {
	protected Logger log = Logger.getLogger("org.freehep.postscript");
	
	private boolean literal;
	protected String name;

	public PSObject(String name, boolean literal) {
		this.name = name;
		this.literal = literal;
	}

	public void setName(String s) {
		name = s;
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract Object clone() throws CloneNotSupportedException;

	public abstract PSObject copy();

	public abstract String getType();

	// returns true if the execStack needs to be popped after execute returns
	public abstract boolean execute(OperandStack os);

	public boolean checkAndExecute(OperandStack os) {
		return execute(os);
	}

	public static void error(OperandStack os, ErrorOperator error) {
		// set the stack back to where the operands started
		os.reset();
		// FIXME, we could report errors by string
		// In the original code we looked up the error by name in the dictStack and reported that one. However, since the errors may contain
		// extra info (Exceptions), we report them directly
//		DictionaryStack ds = os.dictStack();
//		ErrorOperator e = (ErrorOperator) ds.errorDictionary().get(
//				error.getName());
		os.execStack().push(error);

		throw new PostScriptError();
	}

	public boolean isLiteral() {
		return literal;
	}

	public void setLiteral() {
		literal = true;
	}

	public void setExecutable() {
		literal = false;
	}

	public boolean isExecutable() {
		return !literal;
	}

	public abstract String cvs();

	@Override
	public String toString() {
		return "--nostringval--";
	}

	public String toPrint() {
		return toString();
	}
}
