// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Vritual Memory Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class MemoryOperator extends PSOperator {

	public static Class<?>[] operators = { Save.class, Restore.class,
			SetGlobal.class, CurrentGlobal.class, GCheck.class, StartJob.class,
			DefineUserObject.class, ExecUserObject.class,
			UndefineUserObject.class, UserObjects.class };

	@Override
	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
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
		PSString password = os.popString();
		PSBoolean state = os.popBoolean();
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
