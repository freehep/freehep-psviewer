// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.postscript.stacks.OperandStack;
import org.freehep.vectorgraphics.Paint;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSPaint.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public class PSPaint extends PSSimple {
	private Paint value;

	public PSPaint(Paint v) {
		super("paint", false);
		value = v;
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(this);
		return true;
	}

	@Override
	public String getType() {
		return "painttype";
	}

	public Paint getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSPaint) {
			return value.equals(((PSPaint) o).getValue());
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSPaint(value);
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return "paint: " + value;
	}
}
