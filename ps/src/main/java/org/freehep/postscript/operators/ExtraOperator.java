// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.errors.BreakException;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSDictionary;

/**
 * Extra Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class ExtraOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { Break.class });
	}
}

class Break extends ExtraOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.execStack().pop();
		throw new BreakException();
	}
}