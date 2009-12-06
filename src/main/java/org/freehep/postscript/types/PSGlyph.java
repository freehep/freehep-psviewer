// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;

import java.awt.geom.Rectangle2D;

import org.freehep.postscript.stacks.OperandStack;

/**
 * Object is only for storage and lookup in Dictionaries and Arrays, not to be
 * executed.
 * 
 * @author Mark Donszelmann
 */
public class PSGlyph extends PSSimple {
    private double wx, wy, llx, lly, urx, ury;

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
		return getWx();
	}

	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(llx, lly, urx - llx, ury - lly);
	}

	public double getLSB() {
		return getLLx();
	}

	public double getRSB() {
		return getWx() - getURx();
	}
	
	/**
	 * @return the wx
	 */
	public double getWx() {
		return wx;
	}

	/**
	 * @param wx the wx to set
	 */
	public void setWx(double wx) {
		this.wx = wx;
	}

	/**
	 * @return the wy
	 */
	public double getWy() {
		return wy;
	}

	/**
	 * @param wy the wy to set
	 */
	public void setWy(double wy) {
		this.wy = wy;
	}

	/**
	 * @return the llx
	 */
	public double getLLx() {
		return llx;
	}

	/**
	 * @param llx the llx to set
	 */
	public void setLLx(double llx) {
		this.llx = llx;
	}

	/**
	 * @return the lly
	 */
	public double getLLy() {
		return lly;
	}

	/**
	 * @param lly the lly to set
	 */
	public void setLLy(double lly) {
		this.lly = lly;
	}

	/**
	 * @return the urx
	 */
	public double getURx() {
		return urx;
	}

	/**
	 * @param urx the urx to set
	 */
	public void setURx(double urx) {
		this.urx = urx;
	}

	/**
	 * @return the ury
	 */
	public double getURy() {
		return ury;
	}
	
	/**
	 * @param ury the ury to set
	 */
	public void setURy(double ury) {
		this.ury = ury;
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
