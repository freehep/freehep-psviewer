// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.errors.StackUnderflow;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSInteger;
import org.freehep.postscript.types.PSMark;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSOperator;

/**
 * Array Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class ArrayOperator extends PSOperator {

	public static Class<?>[] operators = { Array.class, ArrayBegin.class,
			ArrayEnd.class, AStore.class };
}

class Array extends ArrayOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger n = os.popInteger();
		if (n.getValue() < 0) {
			error(os, new RangeCheck());
		} else {
			os.push(new PSArray(n.getValue()));
		}
		return true;
	}
}

class ArrayBegin extends ArrayOperator {

	@Override
	public String getName() {
		return ("[");
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(new PSMark());
		return true;
	}
}

class ArrayEnd extends ArrayOperator {

	@Override
	public String getName() {
		return ("]");
	}

	// FREEHEP-139: nothing done about InvalidAccess
	@Override
	public boolean execute(OperandStack os) {
		int n = os.countToMark();
		if (n < 0) {
			error(os, new UnmatchedMark());
		} else {
			PSObject[] a = new PSObject[n];
			for (int i = n - 1; i >= 0; i--) {
				PSObject o = os.popObject();
				a[i] = o;
			}
			/* PSMark mark = */ os.popMark();

			os.push(new PSArray(a));
		}
		return true;
	}
}

class AStore extends ArrayOperator {
	{
		operandTypes = new Class[] { PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray a = os.popArray();
		int n = a.size();
		if (n > os.size()) {
			error(os, new StackUnderflow());
		} else {
			for (int i = n - 1; i >= 0; i--) {
				PSObject o = os.popObject();
				a.set(i, o);
			}
			os.push(a);
		}
		return true;
	}
}
