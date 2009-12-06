// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Extra Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class ExtraOperator extends PSOperator {

	public static Class<?>[] operators = { Break.class };
}

class Break extends ExtraOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.execStack().pop();
		throw new BreakException();
	}
}