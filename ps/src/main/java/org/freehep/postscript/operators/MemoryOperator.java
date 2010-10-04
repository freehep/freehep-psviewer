// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSBoolean;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSInteger;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSSave;
import org.freehep.postscript.types.PSString;

/**
 * Virtual Memory Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class MemoryOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { Save.class,
				Restore.class, SetGlobal.class, CurrentGlobal.class,
				GCheck.class, StartJob.class, DefineUserObject.class,
				ExecUserObject.class, UndefineUserObject.class,
				UserObjects.class });
	}

}

class Save extends MemoryOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-152: ignored
		os.push(new PSSave());
		return true;
	}
}

class Restore extends MemoryOperator {
	{
		operandTypes = new Class[] { PSSave.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-152: ignored
		os.popSave();
		return true;
	}
}

class SetGlobal extends MemoryOperator {
	{
		operandTypes = new Class[] { PSBoolean.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class CurrentGlobal extends MemoryOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class GCheck extends MemoryOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-152
		error(os, new Unimplemented());
		return true;
	}
}

class StartJob extends MemoryOperator {
	{
		operandTypes = new Class[] { PSBoolean.class, PSString.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// ignored
		/* PSString password = */os.popString();
		/* PSBoolean state = */os.popBoolean();
		os.push(false);
		return true;
	}
}

class DefineUserObject extends MemoryOperator {
	{
		operandTypes = new Class[] { PSInteger.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-153
		error(os, new Unimplemented());
		return true;
	}
}

class ExecUserObject extends MemoryOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-153
		error(os, new Unimplemented());
		return true;
	}
}

class UndefineUserObject extends MemoryOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-153
		error(os, new Unimplemented());
		return true;
	}
}

class UserObjects extends MemoryOperator {
	// FREEHEP-153: not an operator

	@Override
	public String getName() {
		return "UserObjects";
	}

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-153
		error(os, new Unimplemented());
		return true;
	}
}
