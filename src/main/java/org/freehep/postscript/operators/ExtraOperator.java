// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.errors.BreakException;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSOperator;

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