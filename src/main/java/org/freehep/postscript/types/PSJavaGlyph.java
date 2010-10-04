// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.vectorgraphics.GlyphMetrics;
import org.freehep.vectorgraphics.GlyphVector;
import org.freehep.vectorgraphics.Rectangle;

/**
 * Object is only for storage and lookup in Dictionaries and Arrays, not to be
 * executed.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSJavaGlyph.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSJavaGlyph extends PSGlyph {
	private GlyphVector gv;

	public PSJavaGlyph(PSDevice device, GlyphVector gv) {
		super(device);
		this.gv = gv;
		GlyphMetrics gm = gv.getGlyphMetrics(0);
		setWx(gm.getAdvance());
		Rectangle r = gm.getBounds();
		setLLx(r.getMinX());
		setLLy(r.getMinY());
		setURx(r.getMaxX());
		setURy(r.getMaxY());
	}

	public GlyphVector getGlyph() {
		return gv;
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
