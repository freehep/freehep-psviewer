// Copyright 2001, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.postscript.stacks.OperandStack;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSFontID.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public class PSFontID extends PSSimple {

	public PSFontID() {
		super("fontid", true);
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(this);
		return true;
	}

	@Override
	public String getType() {
		return "fonttype";
	}

	// FIXME
	@Override
	public int hashCode() {
		return 0;
	}

	// FIXME
	@Override
	public boolean equals(Object o) {
		if (o instanceof PSFontID) {
			return true;
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSFontID();
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return "--" + name + "--";
	}
}
