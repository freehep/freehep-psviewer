// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSDictionary;

/**
 * Device Setup and Output Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class OutputOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { ShowPage.class,
				CopyPage.class, SetPageDevice.class, CurrentPageDevice.class,
				NullDevice.class });
	}
}

class CopyPage extends OutputOperator {

	@Override
	public boolean execute(OperandStack os) {
		// no-op
		return true;
	}
}

class SetPageDevice extends OutputOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class CurrentPageDevice extends OutputOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class NullDevice extends OutputOperator {

	@Override
	public boolean execute(OperandStack os) {
		// no-op
		return true;
	}
}