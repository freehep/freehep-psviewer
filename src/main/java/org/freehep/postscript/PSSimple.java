// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSSimple.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public abstract class PSSimple extends PSObject {

	public PSSimple(String name, boolean literal) {
		super(name, literal);
	}

	public PSObject copy() {
		return (PSObject) clone();
	}
}
