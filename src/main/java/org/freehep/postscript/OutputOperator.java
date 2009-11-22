// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

/**
 * Device Setup and Output Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/OutputOperator.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class OutputOperator extends PSOperator {

	public static Class[] operators = { ShowPage.class, CopyPage.class,
			SetPageDevice.class, CurrentPageDevice.class, NullDevice.class };

	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class ShowPage extends OutputOperator {

	public boolean execute(OperandStack os) {
		Object showPage = os.execStack().pop();
		os.execStack().push(new InitGraphics());
		os.execStack().push(new ErasePage());
		os.execStack().push(showPage);
		return true;
	}
}

class CopyPage extends OutputOperator {

	public boolean execute(OperandStack os) {
		// no-op
		return true;
	}
}

class SetPageDevice extends OutputOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class CurrentPageDevice extends OutputOperator {

	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class NullDevice extends OutputOperator {

	public boolean execute(OperandStack os) {
		// no-op
		return true;
	}
}