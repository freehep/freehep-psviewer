// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Device Setup and Output Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class OutputOperator extends PSOperator {

	public static Class<?>[] operators = { ShowPage.class, CopyPage.class,
			SetPageDevice.class, CurrentPageDevice.class, NullDevice.class };
}

class ShowPage extends OutputOperator {

	@Override
	public boolean execute(OperandStack os) {
		Object showPage = os.execStack().pop();
		os.execStack().push(new InitGraphics());
		os.execStack().push(new ErasePage());
		os.execStack().push(showPage);
		return true;
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