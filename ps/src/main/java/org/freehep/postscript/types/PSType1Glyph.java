// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

/**
 * Object is only for storage and lookup in Dictionaries and Arrays, not to be
 * executed.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSType1Glyph.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSType1Glyph extends PSGlyph {
	private PSPackedArray proc;

	public PSType1Glyph(PSDevice device, PSPackedArray proc, double wx, double lsb) {
		super(device);
		this.proc = proc;
		setWx(wx);
		setLLx(lsb);
	}

	public PSPackedArray getProc() {
		return proc;
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
		throw new CloneNotSupportedException(getClass()+" not implemented");
	}
}
