// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.postscript.dsc.DSC;
import org.freehep.postscript.stacks.OperandStack;

/**
 * Objects for PostScript Processor, for the Document Structure Conventions
 * 
 * @author Mark Donszelmann
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

	@Override
	public boolean execute(OperandStack os) {
		if (!dsc.parse(getValue(), os)) {
			log.warning("DSC unrecognized: " + this);
		}
		// ignore dsc
		return true;
	}

	@Override
	public String getType() {
		return "DSCtype";
	}

	public String getValue() {
		return comment;
	}

	@Override
	public int hashCode() {
		return comment.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSDSC) {
			return (comment == ((PSDSC) o).comment);
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSDSC(comment, dsc);
	}

	@Override
	public String cvs() {
		return comment;
	}

	@Override
	public String toString() {
		return "%%" + comment;
	}
}
