// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.errors.Undefined;
import org.freehep.postscript.stacks.DictionaryStack;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSBoolean;
import org.freehep.postscript.types.PSComposite;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSName;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSOperator;
import org.freehep.postscript.types.PSPackedArray;
import org.freehep.postscript.types.PSString;

/**
 * Miscellaneous Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class MiscellaneousOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { Bind.class,
				Version.class, RealTime.class, UserTime.class, Product.class,
				Revision.class, SerialNumber.class, Executive.class,
				Echo.class, Prompt.class });
	}
}

class Bind extends MiscellaneousOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSPackedArray p = os.popPackedArray();
		if (p.isLiteral()) {
			error(os, new TypeCheck());
		} else {
			bind(p, os.dictStack());
			os.push(p);
		}
		return true;
	}

	private void bind(PSPackedArray p, DictionaryStack ds) {
		if (p instanceof PSArray) {
			if (p.accessWrite()) {
				// Change access
				p.changeAccess(PSComposite.READ_ONLY);
			} else {
				// Do not change READ_ONLY Arrays
				return;
			}
		}

		// bind
		for (int i = 0; i < p.size(); i++) {
			PSObject key = p.get(i);
			if ((key.isExecutable()) && (key instanceof PSName)) {
				PSObject value = ds.lookup(key);
				if (value instanceof PSOperator) {
					// set operators
					p.bind(i, (PSOperator) value);
				}
			} else if (key instanceof PackedArray) {
				// recurse into nested procedures
				bind((PSPackedArray) key, ds);
			}
		}
	}
}

class Version extends MiscellaneousOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.push(new PSString(2000)); // FIXME: look up what level we really
		// support
		return true;
	}
}

class RealTime extends MiscellaneousOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.push(System.currentTimeMillis());
		return true;
	}
}

class UserTime extends MiscellaneousOperator {

	// FREEHEP-154: a better time measurement
	@Override
	public boolean execute(OperandStack os) {
		os.push(System.currentTimeMillis());
		return true;
	}
}

class Product extends MiscellaneousOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.push(new PSString("FreeHEP Java PostScript Interpreter"));
		return true;
	}
}

class Revision extends MiscellaneousOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.push(new PSString("1.0.0"));
		return true;
	}
}

class SerialNumber extends MiscellaneousOperator {
	@Override
	public boolean execute(OperandStack os) {
		// any number would do...
		os.push(9265294);
		return true;
	}
}

class Executive extends MiscellaneousOperator {
	@Override
	public boolean execute(OperandStack os) {
		error(os, new Undefined());
		return true;
	}
}

class Echo extends MiscellaneousOperator {
	{
		operandTypes = new Class[] { PSBoolean.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		error(os, new Undefined());
		return true;
	}
}

class Prompt extends MiscellaneousOperator {
	@Override
	public boolean execute(OperandStack os) {
		error(os, new Undefined());
		return true;
	}
}
