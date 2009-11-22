// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

/**
 * Vritual Memory Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/MemoryOperator.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class MemoryOperator extends PSOperator {

	public static Class[] operators = { Save.class, Restore.class,
			SetGlobal.class, CurrentGlobal.class, GCheck.class, StartJob.class,
			DefineUserObject.class, ExecUserObject.class,
			UndefineUserObject.class, UserObjects.class };

	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class Save extends MemoryOperator {

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

	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class CurrentGlobal extends MemoryOperator {

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

	public boolean execute(OperandStack os) {
		// FREEHEP-153
		error(os, new Unimplemented());
		return true;
	}
}

class UserObjects extends MemoryOperator {
	// FREEHEP-153: not an operator

	public String getName() {
		return "UserObjects";
	}

	public boolean execute(OperandStack os) {
		// FREEHEP-153
		error(os, new Unimplemented());
		return true;
	}
}
