// Copyright 2001, FreeHEP.
package org.freehep.postscript.errors;

/**
 * Syntax Exception for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/SyntaxException.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class SyntaxException extends Exception {

	private int line;

	public SyntaxException(int line, String msg) {
		super(msg);
		this.line = line;
	}

	public int getLineNo() {
		return line;
	}
}
