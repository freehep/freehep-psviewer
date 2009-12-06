// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import java.util.logging.Logger;

import org.freehep.postscript.errors.IOError;
import org.freehep.postscript.errors.StackUnderflow;
import org.freehep.postscript.errors.SyntaxError;
import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.errors.Undefined;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.stacks.PostScriptStack;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSBoolean;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSName;
import org.freehep.postscript.types.PSNull;
import org.freehep.postscript.types.PSOperator;

/**
 * Error Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class ErrorOperator extends PSOperator {
	protected Logger log = Logger.getLogger("org.freehep.postscript");

	public static Class<?>[] operators = { ConfigurationError.class,
			DictFull.class, DictStackOverflow.class, DictStackUnderflow.class,
			ExecStackOverflow.class, HandleError.class, Interrupt.class,
			InvalidAccess.class, InvalidExit.class, InvalidFileAccess.class,
			InvalidFont.class, InvalidRestore.class, IOError.class,
			LimitCheck.class, NoCurrentPoint.class, RangeCheck.class,
			StackOverflow.class, StackUnderflow.class, SyntaxError.class,
			Timeout.class, TypeCheck.class, Undefined.class,
			UndefinedFileName.class, UndefinedResource.class,
			UndefinedResult.class, UnmatchedMark.class, Unregistered.class,
			VMError.class, Unimplemented.class };

	// default error handler
	@Override
	public boolean execute(OperandStack os) {
		// fill error dict
		PSDictionary error = os.dictStack().dollarError();
		error.put("newerror", new PSBoolean(true));
		error.put("errorname", new PSName(getName()));
		error.put("command", os.execStack().peekObject(1));
		if (error.get("recordstacks").equals(new PSBoolean(true))) {
			error.put("ostack", stackToArray(os));
			error.put("estack", stackToArray(os.execStack()));
			error.put("dstack", stackToArray(os.dictStack()));
		}

		// now execute stop
		os.execStack().pop();
		os.execStack().push("stop");

		return false;
	}

	private static PSArray stackToArray(PostScriptStack stack) {
		PSArray array = new PSArray(stack.size());
		stack.copyInto(array);
		return array;
	}
}

class ConfigurationError extends ErrorOperator {
}

class DictFull extends ErrorOperator {
}

class DictStackOverflow extends ErrorOperator {
}

class DictStackUnderflow extends ErrorOperator {
}

class ExecStackOverflow extends ErrorOperator {
}

class HandleError extends ErrorOperator {

	// FIXME, should print more
	@Override
	public boolean execute(OperandStack os) {
		// report on error
		PSDictionary error = os.dictStack().dollarError();
		if (error.get("newerror").equals(new PSBoolean(true))) {
			log.warning("\n\n%%[Error: " + error.get("errorname").toPrint()
					+ "; Offending Command: " + error.get("command").toPrint()
					+ "]%%");
			log.warning("");

			if (!error.get("errorinfo").equals(new PSNull())) {
				log.warning("Error Info: " + error.get("errorinfo").toPrint());
				log.warning("");
			}

			if (error.get("recordstacks").equals(new PSBoolean(true))) {
				log.warning("Operand Stack (bottom..top)");
				log.warning(error.get("ostack").toPrint());
				log.warning("");

				log.warning("Execution Stack (bottom..top)");
				log.warning(error.get("estack").toPrint());
				log.warning("");

				log.warning("Dictionary Stack (bottom..top)");
				log.warning(error.get("dstack").toPrint());
				log.warning("");
			}

			// reset error dict
			error.put("newerror", new PSBoolean(false));
			error.put("errorinfo", new PSNull());
		}

		return true;
	}
}

class Interrupt extends ErrorOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.execStack().pop();
		os.execStack().push("stop");
		return false;
	}
}

class InvalidAccess extends ErrorOperator {
}

class InvalidExit extends ErrorOperator {
}

class InvalidFileAccess extends ErrorOperator {
}

class InvalidFont extends ErrorOperator {
}

class InvalidRestore extends ErrorOperator {
}

class LimitCheck extends ErrorOperator {
}

class NoCurrentPoint extends ErrorOperator {
}

class RangeCheck extends ErrorOperator {
	private String msg;

    RangeCheck() {
		this.msg = null;
	}

    RangeCheck(String msg) {
		this.msg = msg;
	}

	@Override
	public boolean execute(OperandStack os) {
		if (msg != null) {
			log.warning(msg);
		}
		return super.execute(os);
	}
}

class StackOverflow extends ErrorOperator {
}

class Timeout extends ErrorOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.execStack().pop();
		os.execStack().push("stop");
		return false;
	}
}

class UndefinedFileName extends ErrorOperator {
}

class UndefinedResource extends ErrorOperator {
}

class UndefinedResult extends ErrorOperator {
}

class UnmatchedMark extends ErrorOperator {
}

class Unregistered extends ErrorOperator {
}

class VMError extends ErrorOperator {
	@Override
	public String getName() {
		return "VMerror";
	}
}

// Not part of standard, added while implementation is not complete
class Unimplemented extends ErrorOperator {
}
