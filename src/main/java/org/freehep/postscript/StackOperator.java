// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Stack Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class StackOperator extends PSOperator {

	public static Class<?>[] operators = { Pop.class, Exch.class, Dup.class,
			Index.class, Roll.class, Clear.class, Count.class, Mark.class,
			ClearToMark.class, CountToMark.class };
}

class Pop extends StackOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		os.pop();
		return true;
	}
}

class Exch extends StackOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		os.exch();
		return true;
	}
}

class Dup extends StackOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		os.dup();
		return true;
	}
}

class Index extends StackOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
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

	@Override
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

	@Override
	public boolean execute(OperandStack os) {
		os.clear();
		return true;
	}
}

class Count extends StackOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.size());
		return true;
	}
}

class Mark extends StackOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(new PSMark());
		return true;
	}
}

class ClearToMark extends StackOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (!os.clearToMark()) {
			error(os, new UnmatchedMark());
		}
		return true;
	}
}

class CountToMark extends StackOperator {

	@Override
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
