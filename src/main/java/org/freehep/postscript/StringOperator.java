// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * String Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class StringOperator extends PSOperator {

	public static Class<?>[] operators = { StringString.class,
			AnchorSearch.class, Search.class };
}

class StringString extends StringOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
	public String getName() {
		return ("string");
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger n = os.popInteger();
		if (n.getValue() < 0) {
			error(os, new RangeCheck());
		} else {
			os.push(new PSString(n.getValue()));
		}
		return true;
	}
}

class AnchorSearch extends StringOperator {
	{
		operandTypes = new Class[] { PSString.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString seek = os.popString();
		PSString string = os.popString();
		if (string.indexOf(seek) != 0) {
			os.push(string);
			os.push(false);
		} else {
			os.push(string.subString(seek.size()));
			os.push(string.subString(0, seek.size()));
			os.push(true);
		}
		return true;
	}
}

class Search extends StringOperator {
	{
		operandTypes = new Class[] { PSString.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSString seek = os.popString();
		PSString string = os.popString();
		int pos = string.indexOf(seek);
		if (pos < 0) {
			os.push(string);
			os.push(false);
		} else {
			os.push(string.subString(pos + seek.size()));
			os.push(string.subString(pos, seek.size()));
			os.push(string.subString(0, pos));
			os.push(true);
		}
		return true;
	}
}
