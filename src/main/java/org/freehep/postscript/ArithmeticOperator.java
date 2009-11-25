// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Arithmetic Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class ArithmeticOperator extends PSOperator {
	protected PSRandom random = new PSRandom();

	public static Class<?>[] operators = { Add.class, Div.class, IDiv.class,
			Mod.class, Mul.class, Sub.class, Abs.class, Neg.class,
			Ceiling.class, Floor.class, Round.class, Truncate.class,
			Sqrt.class, Atan.class, Cos.class, Sin.class, Exp.class, Ln.class,
			Log.class };

	@Override
	public boolean execute(OperandStack os) {
		throw new IllegalArgumentException("Cannot execute class: " + getClass());
	}
}

class Add extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSInteger.class, PSInteger.class)) {
			PSInteger n2 = os.popInteger();
			PSInteger n1 = os.popInteger();
			long s = (long) n1.getValue() + (long) n2.getValue();
			if ((Integer.MIN_VALUE <= s) && (s <= Integer.MAX_VALUE)) {
				os.push((int) s);
			} else {
				os.push(s);
			}
		} else {
			PSNumber n2 = os.popNumber();
			PSNumber n1 = os.popNumber();
			double s = n1.getDouble() + n2.getDouble();
			os.push(s);
		}
		return true;
	}
}

class Div extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n2 = os.popNumber();
		PSNumber n1 = os.popNumber();
		if (n2.getDouble() == 0.0) {
			error(os, new UndefinedResult());
		} else {
			double q = n1.getDouble() / n2.getDouble();
			os.push(q);
		}
		return true;
	}
}

class IDiv extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSInteger.class, PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger i2 = os.popInteger();
		PSInteger i1 = os.popInteger();
		if (i2.getValue() == 0) {
			error(os, new UndefinedResult());
		} else {
			int q = i1.getValue() / i2.getValue();
			os.push(q);
		}
		return true;
	}
}

class Mod extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSInteger.class, PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger i2 = os.popInteger();
		PSInteger i1 = os.popInteger();
		if (i2.getValue() == 0) {
			error(os, new UndefinedResult());
		} else {
			int r = i1.getValue() % i2.getValue();
			os.push(r);
		}
		return true;
	}
}

class Mul extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSInteger.class, PSInteger.class)) {
			PSInteger n2 = os.popInteger();
			PSInteger n1 = os.popInteger();
			long p = (long) n1.getValue() * (long) n2.getValue();
			if ((Integer.MIN_VALUE <= p) && (p <= Integer.MAX_VALUE)) {
				os.push((int) p);
			} else {
				os.push(p);
			}
		} else {
			PSNumber n2 = os.popNumber();
			PSNumber n1 = os.popNumber();
			double p = n1.getDouble() * n2.getDouble();
			os.push(p);
		}
		return true;
	}
}

class Sub extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSInteger.class, PSInteger.class)) {
			PSInteger n2 = os.popInteger();
			PSInteger n1 = os.popInteger();
			long d = (long) n1.getValue() - (long) n2.getValue();
			if ((Integer.MIN_VALUE <= d) && (d <= Integer.MAX_VALUE)) {
				os.push((int) d);
			} else {
				os.push(d);
			}
		} else {
			PSNumber n2 = os.popNumber();
			PSNumber n1 = os.popNumber();
			double d = n1.getDouble() - n2.getDouble();
			os.push(d);
		}
		return true;
	}
}

class Abs extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSInteger.class)) {
			PSInteger n1 = os.popInteger();
			int n2 = Math.abs(n1.getValue());
			os.push(n2);
		} else {
			PSReal n1 = os.popReal();
			double n2 = Math.abs(n1.getValue());
			os.push(n2);
		}
		return true;
	}
}

class Neg extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSInteger.class)) {
			PSInteger n1 = os.popInteger();
			int n2 = -n1.getValue();
			os.push(n2);
		} else {
			PSReal n1 = os.popReal();
			double n2 = -n1.getValue();
			os.push(n2);
		}
		return true;
	}
}

class Ceiling extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n1 = os.popNumber();
		double n2 = Math.ceil(n1.getDouble());
		if (os.checkType(PSInteger.class)) {
			os.push((int) n2);
		} else {
			os.push(n2);
		}
		return true;
	}
}

class Floor extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n1 = os.popNumber();
		double n2 = Math.floor(n1.getDouble());
		if (os.checkType(PSInteger.class)) {
			os.push((int) n2);
		} else {
			os.push(n2);
		}
		return true;
	}
}

class Round extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n1 = os.popNumber();
		long n2 = Math.round(n1.getDouble());
		if (os.checkType(PSInteger.class)) {
			os.push((int) n2);
		} else {
			os.push(n2);
		}
		return true;
	}
}

class Truncate extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n1 = os.popNumber();
		double n2 = Math.rint(n1.getDouble());
		if (os.checkType(PSInteger.class)) {
			os.push((int) n2);
		} else {
			os.push(n2);
		}
		return true;
	}
}

class Sqrt extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n1 = os.popNumber();
		if (n1.getDouble() < 0.0) {
			error(os, new RangeCheck());
		} else {
			double n2 = Math.sqrt(n1.getDouble());
			os.push(n2);
		}
		return true;
	}
}

class Atan extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber den = os.popNumber();
		PSNumber num = os.popNumber();
		if ((den.getDouble() == 0.0) && (num.getDouble() == 0.0)) {
			error(os, new UndefinedResult());
		} else {
			double angle = Math.toDegrees(Math.atan2(num.getDouble(), den
					.getDouble()));
			os.push(angle);
		}
		return true;
	}
}

class Cos extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber angle = os.popNumber();
		double r = Math.cos(Math.toRadians(angle.getDouble()));
		os.push(r);
		return true;
	}
}

class Sin extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber angle = os.popNumber();
		double r = Math.sin(Math.toRadians(angle.getDouble()));
		os.push(r);
		return true;
	}
}

class Exp extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber exponent = os.popNumber();
		PSNumber base = os.popNumber();
		if (((exponent.getDouble() % 1) != 0.0) && (base.getDouble() < 0.0)) {
			error(os, new UndefinedResult());
		} else {
			double r = Math.pow(base.getDouble(), exponent.getDouble());
			os.push(r);
		}
		return true;
	}
}

class Ln extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber num = os.popNumber();
		if (num.getDouble() <= 0.0) {
			error(os, new RangeCheck());
		} else {
			double r = Math.log(num.getDouble());
			os.push(r);
		}
		return true;
	}
}

class Log extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber num = os.popNumber();
		if (num.getDouble() <= 0.0) {
			error(os, new RangeCheck());
		} else {
			double r = Math.log(num.getDouble()) / Math.log(10.0);
			os.push(r);
		}
		return true;
	}
}

class Rand extends ArithmeticOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.push(random.nextInt());
		return true;
	}
}

class SRand extends ArithmeticOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger seed = os.popInteger();
		random.setSeed(seed.getValue());
		return true;
	}
}

class RRand extends ArithmeticOperator {
	@Override
	public boolean execute(OperandStack os) {
		int seed = random.getSeed();
		os.push(seed);
		return true;
	}
}
