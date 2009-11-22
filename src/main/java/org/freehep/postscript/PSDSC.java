// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, for the Document Structure Conventions
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSDSC.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
// Not strictly part of the PostScript standard
public class PSDSC extends PSSimple {
	protected String comment;
	protected DSC dsc;

	public PSDSC(String comment, DSC dsc) {
		super("DSC", false);
		this.comment = comment;
		this.dsc = dsc;
	}

	public boolean execute(OperandStack os) {
		if (!dsc.parse(getValue(), os)) {
			System.out.println("DSC unrecognized: " + this);
		}
		// ignore dsc
		return true;
	}

	public String getType() {
		return "DSCtype";
	}

	public String getValue() {
		return comment;
	}

	public int hashCode() {
		return comment.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof PSDSC) {
			return (comment == ((PSDSC) o).comment);
		}
		return false;
	}

	public Object clone() {
		return new PSDSC(comment, dsc);
	}

	public String cvs() {
		return comment;
	}

	public String toString() {
		return "%%" + comment;
	}
}
