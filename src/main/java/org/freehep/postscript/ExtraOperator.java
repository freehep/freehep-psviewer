// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Extra Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class ExtraOperator extends PSOperator {

	public static Class<?>[] operators = { Break.class };

	@Override
	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class Break extends ExtraOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.execStack().pop();
		throw new BreakException();
	}
}