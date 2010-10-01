// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.stacks;

import org.freehep.postscript.types.PSGState;

/**
 * OperandStack for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/GStateStack.java
 *          829a8d93169a 2006/12/08 09:03:07 duns $
 */
public class GStateStack extends PostScriptStack {

	private static final long serialVersionUID = 1L;

	public GStateStack() {
		super();
	}

	@Override
	public Object push(Object o) {
		throw new IllegalArgumentException("Only PSGState allowed on stack.");
	}

	public PSGState push(PSGState gs) {
		return (PSGState) super.push(gs);
	}

	public PSGState popGState() {
		return (PSGState) pop();
	}

	@Override
	public String toString() {
		return "GStateStack";
	}
}