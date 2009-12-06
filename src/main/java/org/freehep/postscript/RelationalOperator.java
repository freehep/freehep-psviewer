// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Relational, Boolean and Bitwise Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class RelationalOperator extends PSOperator {

	public static Class<?>[] operators = { EQ.class, NE.class, GE.class,
			GT.class, LE.class, LT.class, And.class, Not.class, Or.class,
			Xor.class, True.class, False.class, BitShift.class };
}

class EQ extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject o2 = os.popObject();
		PSObject o1 = os.popObject();
		os.push(o1.equals(o2));
		return true;
	}
}

class NE extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject o2 = os.popObject();
		PSObject o1 = os.popObject();
		os.push(!o1.equals(o2));
		return true;
	}
}

class GE extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			PSNumber n2 = os.popNumber();
			PSNumber n1 = os.popNumber();
			os.push(n1.compareTo(n2) >= 0);
		} else if (os.checkType(PSString.class, PSString.class)) {
			PSString s2 = os.popString();
			PSString s1 = os.popString();
			os.push(s1.compareTo(s2) >= 0);
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class GT extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			PSNumber n2 = os.popNumber();
			PSNumber n1 = os.popNumber();
			os.push(n1.compareTo(n2) > 0);
		} else if (os.checkType(PSString.class, PSString.class)) {
			PSString s2 = os.popString();
			PSString s1 = os.popString();
			os.push(s1.compareTo(s2) > 0);
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class LE extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			PSNumber n2 = os.popNumber();
			PSNumber n1 = os.popNumber();
			os.push(n1.compareTo(n2) <= 0);
		} else if (os.checkType(PSString.class, PSString.class)) {
			PSString s2 = os.popString();
			PSString s1 = os.popString();
			os.push(s1.compareTo(s2) <= 0);
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class LT extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			PSNumber n2 = os.popNumber();
			PSNumber n1 = os.popNumber();
			os.push(n1.compareTo(n2) < 0);
		} else if (os.checkType(PSString.class, PSString.class)) {
			PSString s2 = os.popString();
			PSString s1 = os.popString();
			os.push(s1.compareTo(s2) < 0);
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class And extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSBoolean.class, PSBoolean.class)) {
			PSBoolean b2 = os.popBoolean();
			PSBoolean b1 = os.popBoolean();
			os.push(b1.getValue() & b2.getValue());
		} else if (os.checkType(PSInteger.class, PSInteger.class)) {
			PSInteger i2 = os.popInteger();
			PSInteger i1 = os.popInteger();
			os.push(i1.getValue() & i2.getValue());
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Not extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSBoolean.class)) {
			PSBoolean b1 = os.popBoolean();
			os.push(!b1.getValue());
		} else if (os.checkType(PSInteger.class)) {
			PSInteger i1 = os.popInteger();
			os.push(~i1.getValue());
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Or extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSBoolean.class, PSBoolean.class)) {
			PSBoolean b2 = os.popBoolean();
			PSBoolean b1 = os.popBoolean();
			os.push(b1.getValue() | b2.getValue());
		} else if (os.checkType(PSInteger.class, PSInteger.class)) {
			PSInteger i2 = os.popInteger();
			PSInteger i1 = os.popInteger();
			os.push(i1.getValue() | i2.getValue());
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Xor extends RelationalOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSBoolean.class, PSBoolean.class)) {
			PSBoolean b2 = os.popBoolean();
			PSBoolean b1 = os.popBoolean();
			os.push(b1.getValue() ^ b2.getValue());
		} else if (os.checkType(PSInteger.class, PSInteger.class)) {
			PSInteger i2 = os.popInteger();
			PSInteger i1 = os.popInteger();
			os.push(i1.getValue() ^ i2.getValue());
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class True extends RelationalOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(true);
		return true;
	}
}

class False extends RelationalOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(false);
		return true;
	}
}

class BitShift extends RelationalOperator {
	{
		operandTypes = new Class[] { PSInteger.class, PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		int shift = os.popInteger().getValue();
		int i = os.popInteger().getValue();
		if (shift < 0) {
			os.push(i >> Math.abs(shift));
		} else {
			os.push(i << shift);
		}
		return true;
	}
}
