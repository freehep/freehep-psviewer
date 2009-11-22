// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Stack Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/StackOperator.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class StackOperator extends PSOperator {

	public static Class[] operators = { Pop.class, Exch.class, Dup.class,
			Index.class, Roll.class, Clear.class, Count.class, Mark.class,
			ClearToMark.class, CountToMark.class };

	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class Pop extends StackOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	public boolean execute(OperandStack os) {
		os.pop();
		return true;
	}
}

class Exch extends StackOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	public boolean execute(OperandStack os) {
		os.exch();
		return true;
	}
}

class Dup extends StackOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	public boolean execute(OperandStack os) {
		os.dup();
		return true;
	}
}

class Index extends StackOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	public boolean execute(OperandStack os) {
		int n = os.popInteger().getValue();
		if ((n < 0) || (n > os.size())) {
			error(os, new RangeCheck());
		} else {
			os.index(n);
		}
		return true;
	}
}

class Roll extends StackOperator {
	{
		operandTypes = new Class[] { PSInteger.class, PSInteger.class };
	}

	public boolean execute(OperandStack os) {
		PSInteger j = os.popInteger();
		PSInteger n = os.popInteger();

		if (n.getValue() == 0) {
			// ignore
			return true;
		} else if ((n.getValue() < 0)
				|| (Math.abs(j.getValue()) > n.getValue())) {
			error(os, new RangeCheck());
		} else if (n.getValue() > os.size()) {
			error(os, new StackUnderflow());
		} else {
			os.roll(n.getValue(), j.getValue());
		}
		return true;
	}
}

class Clear extends StackOperator {

	public boolean execute(OperandStack os) {
		os.clear();
		return true;
	}
}

class Count extends StackOperator {

	public boolean execute(OperandStack os) {
		os.push(os.size());
		return true;
	}
}

class Mark extends StackOperator {

	public boolean execute(OperandStack os) {
		os.push(new PSMark());
		return true;
	}
}

class ClearToMark extends StackOperator {

	public boolean execute(OperandStack os) {
		if (!os.clearToMark()) {
			error(os, new UnmatchedMark());
		}
		return true;
	}
}

class CountToMark extends StackOperator {

	public boolean execute(OperandStack os) {
		int n = os.countToMark();
		if (n < 0) {
			error(os, new UnmatchedMark());
		} else {
			os.push(n);
		}
		return true;
	}
}
