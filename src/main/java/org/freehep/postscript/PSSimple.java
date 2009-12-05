// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.util.logging.Logger;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public abstract class PSSimple extends PSObject {
	protected Logger log = Logger.getLogger("org.freehep.postscript");

	public PSSimple(String name, boolean literal) {
		super(name, literal);
	}

	@Override
	public PSObject copy() {
		try {
			return (PSObject) clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
