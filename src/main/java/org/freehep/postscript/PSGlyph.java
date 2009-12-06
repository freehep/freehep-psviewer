// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.geom.Rectangle2D;

/**
 * Object is only for storage and lookup in Dictionaries and Arrays, not to be
 * executed.
 * 
 * @author Mark Donszelmann
 */
public class PSGlyph extends PSSimple {
    double wx, wy, llx, lly, urx, ury;

	public PSGlyph() {
		super("glyph", true);
	}

	@Override
	public boolean execute(OperandStack os) {
		// no-op
		return true;
	}

	@Override
	public String getType() {
		return "glyph";
	}

	public double getWidth() {
		return wx;
	}

	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(llx, lly, urx - llx, ury - lly);
	}

	public double getLSB() {
		return llx;
	}

	public double getRSB() {
		return wx - urx;
	}

	// FIXME
	@Override
	public int hashCode() {
		return 0;
	}

	// FIXME
	@Override
	public boolean equals(Object o) {
		return false;
	}

	// FIXME
	@Override
	public Object clone() throws CloneNotSupportedException {
		return null;
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
