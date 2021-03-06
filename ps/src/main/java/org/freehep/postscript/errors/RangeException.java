// Copyright 2004-2010, FreeHEP.
package org.freehep.postscript.errors;

/**
 * Range(Check) Exception for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/RangeException.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class RangeException extends Exception {
	private static final long serialVersionUID = 1L;

	public RangeException() {
		super();
	}

	public RangeException(String msg) {
		super(msg);
	}
}
