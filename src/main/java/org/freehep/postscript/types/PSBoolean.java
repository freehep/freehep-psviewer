// Copyright 2001, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.postscript.stacks.OperandStack;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSBoolean.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSBoolean extends PSSimple {
	protected boolean value;

	public PSBoolean(boolean b) {
		super("boolean", true);
		value = b;
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(this);
		return true;
	}

	@Override
	public String getType() {
		return "booleantype";
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSBoolean(value);
	}

	@Override
	public int hashCode() {
		return value ? 1231 : 1237;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSBoolean) {
			value = ((PSBoolean) o).getValue();
			return value;
		}
		return false;
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return (value) ? "true" : "false";
	}
}
