// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;

import org.freehep.util.io.EEXECConstants;
import org.freehep.util.io.EEXECDecryption;

/**
 * Control Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class ControlOperator extends PSOperator {

	public static Class<?>[] operators = { Exec.class, If.class, IfElse.class,
			For.class, Repeat.class, Loop.class, Exit.class, Stop.class,
			Stopped.class, CountExecStack.class, ExecStack.class, Quit.class,
			Start.class, EExec.class };

	@Override
	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class Exec extends ControlOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		os.execStack().pop();
		os.execStack().push(os.popObject());
		return false;
	}
}

class If extends ControlOperator {
	{
		operandTypes = new Class[] { PSBoolean.class, PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSPackedArray p = os.popPackedArray();
		PSBoolean b = os.popBoolean();
		if (b.getValue()) {
			os.execStack().pop();
			os.execStack().push(p);
			return false;
		}
		return true;
	}
}

class IfElse extends ControlOperator {
	{
		operandTypes = new Class[] { PSBoolean.class, PSPackedArray.class,
				PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSPackedArray p2 = os.popPackedArray();
		PSPackedArray p1 = os.popPackedArray();
		PSBoolean b = os.popBoolean();
		os.execStack().pop();
		if (b.getValue()) {
			os.execStack().push(p1);
		} else {
			os.execStack().push(p2);
		}
		return false;
	}
}

class For extends ControlOperator implements LoopingContext {

	private PSPackedArray procedure = null;
	private int iLimit = 0;
	private int iIncrement = 0;
	private int iCount = 0;
	private double rLimit = 0.0;
	private double rIncrement = 0.0;
	private double rCount = 0.0;
	private boolean real = false;

	public For() {
	}

	private For(int start, int inc, int limit, PSPackedArray p) {
		iCount = start;
		iIncrement = inc;
		iLimit = limit;
		procedure = p;
		real = false;
	}

	public For(double start, double inc, double limit, PSPackedArray p) {
		rCount = start;
		rIncrement = inc;
		rLimit = limit;
		procedure = p;
		real = true;
	}

	@Override
	public boolean execute(OperandStack os) {

		if (procedure == null) {
			if (!os.checkType(PSNumber.class, PSNumber.class, PSNumber.class,
					PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			PSPackedArray p = os.popPackedArray();
			PSNumber limit = os.popNumber();
			PSNumber inc = os.popNumber();
			PSNumber start = os.popNumber();
			os.execStack().pop();
			if ((limit instanceof PSReal) || (inc instanceof PSReal)
					|| (start instanceof PSReal)) {
				os.execStack().push(
						new For(start.getDouble(), inc.getDouble(), limit
								.getDouble(), p));
			} else {
				os.execStack().push(
						new For(((PSInteger) start).getValue(),
								((PSInteger) inc).getValue(),
								((PSInteger) limit).getValue(), p));
			}
			return false;
		}

		if (real) {
			if ((rIncrement >= 0.0) && (rCount <= rLimit)) {
				os.push(rCount);
				os.execStack().push(procedure);
				rCount += rIncrement;
				return false;
			} else if ((rIncrement < 0.0) && (rCount >= rLimit)) {
				os.push(rCount);
				os.execStack().push(procedure);
				rCount += rIncrement;
				return false;
			}
		} else {
			if ((iIncrement >= 0) && (iCount <= iLimit)) {
				os.push(iCount);
				os.execStack().push(procedure);
				iCount += iIncrement;
				return false;
			} else if ((iIncrement < 0) && (iCount >= iLimit)) {
				os.push(iCount);
				os.execStack().push(procedure);
				iCount += iIncrement;
				return false;
			}
		}
		return true;
	}
}

class Repeat extends ControlOperator implements LoopingContext {

	private PSPackedArray procedure = null;
	private int limit = 0;
	private int i = 0;

	public Repeat() {
	}

	private Repeat(int n, PSPackedArray p) {
		procedure = p;
		limit = n;
		i = 0;
	}

	@Override
	public boolean execute(OperandStack os) {

		if (procedure == null) {
			if (!os.checkType(PSInteger.class, PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			PSPackedArray p = os.popPackedArray();
			PSInteger n = os.popInteger();
			if (n.getValue() < 0) {
				error(os, new RangeCheck());
				return true;
			}
			os.execStack().pop();
			os.execStack().push(new Repeat(n.getValue(), p));
			return false;
		}

		if (i < limit) {
			os.execStack().push(procedure);
			i++;
			return false;
		}
		return true;
	}
}

class Loop extends ControlOperator implements LoopingContext {

	private PSPackedArray procedure = null;

	public Loop() {
	}

	private Loop(PSPackedArray p) {
		procedure = p;
	}

	@Override
	public boolean execute(OperandStack os) {
		if (procedure == null) {
			if (!os.checkType(PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			os.execStack().pop();
			os.execStack().push(new Loop(os.popPackedArray()));
			return false;
		}

		os.execStack().push(procedure);
		return false;
	}
}

class Exit extends ControlOperator {

	@Override
	public boolean execute(OperandStack os) {
		PSObject o = os.execStack().popObject();
		while ((o != null) && !(o instanceof LoopingContext)) {
			o = os.execStack().popObject();
		}
		if (o == null) {
			System.err
					.println("Error: No enclosing LoopingContext; Command: exit");
			os.execStack().push("quit");
			os.execStack().push(
					os.dictStack().errorDictionary().get("handleerror"));
			return false;
		}

		if ((o instanceof Stopped) || (o instanceof Run)) {
			os.execStack().push(o);
			os.execStack().push("invalidexit");
			return false;
		}

		// ok
		return false;
	}
}

class Stop extends ControlOperator {

	// Our stop operator will print the error message if not in a stopped
	// context
	@Override
	public boolean execute(OperandStack os) {

		PSObject o = os.execStack().popObject();
		while ((o != null) && !(o instanceof Stopped)) {
			o = os.execStack().popObject();
		}
		if (o == null) {
			// FIXME: this should be part of a standard stopped context
			// just a message written here
			os.execStack().push("quit");
			os.execStack().push(
					os.dictStack().errorDictionary().get("handleerror"));
		} else {
			// do as if stopped returned true
			os.push(true);
		}
		return false;
	}
}

class Stopped extends ControlOperator implements LoopingContext {

	private boolean stopped = true;

	public Stopped() {
	}

	private Stopped(boolean state) {
		stopped = state;
	}

	@Override
	public boolean execute(OperandStack os) {
		if (stopped) {
			if (!os.checkType(PSObject.class)) {
				error(os, new TypeCheck());
				return true;
			}
			PSObject obj = os.popObject();

			os.execStack().pop();
			os.execStack().push(new Stopped(false));
			os.execStack().push(obj);
			return false;
		}

		os.push(false);
		return true;
	}
}

class CountExecStack extends ControlOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.execStack().size());
		return true;
	}
}

class ExecStack extends ControlOperator {
	{
		operandTypes = new Class[] { PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray a = os.popArray();
		os.execStack().copyInto(a);
		os.push(a.subArray(0, os.execStack().size()));
		return true;
	}
}

class Quit extends ControlOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FIXME: look at 637, quit should also be in the userdict
		// empty the full execution stack
		os.execStack().clear();
		return false;
	}
}

class Start extends ControlOperator {

	@Override
	public boolean execute(OperandStack os) {
		// ignored
		return true;
	}
}

class EExec extends ControlOperator {
	{
		operandTypes = new Class[] { PSDataSource.class };
	}

	@Override
	public boolean execute(OperandStack os) {

		if (os.checkType(PSDataSource.class)) {
			try {
				PSDataSource source = os.popDataSource();
				os.execStack().pop();
				os.execStack().push(
						new PSInputFile(new EEXECDecryption(source
								.getInputStream(), EEXECConstants.EEXEC_R,
								EEXECConstants.N), source.getDSC()));
			} catch (IOException e) {
				error(os, new InvalidFileAccess());
			}
			return false;
		} else {
			error(os, new TypeCheck());
			return true;
		}
	}

}
