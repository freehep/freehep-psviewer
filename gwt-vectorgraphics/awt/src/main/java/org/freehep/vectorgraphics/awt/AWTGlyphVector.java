package org.freehep.vectorgraphics.awt;

import java.awt.font.GlyphVector;

import org.freehep.vectorgraphics.GlyphMetrics;
import org.freehep.vectorgraphics.Shape;

public class AWTGlyphVector implements org.freehep.vectorgraphics.GlyphVector {

	GlyphVector gv;
	
	public AWTGlyphVector(GlyphVector gv) {
		this.gv = gv;
	}

	@Override
	public GlyphMetrics getGlyphMetrics(int i) {
		return new AWTGlyphMetrics(gv.getGlyphMetrics(i));
	}

	@Override
	public Shape getOutline(float x, float y) {
		return new AWTShape(gv.getOutline(x, y));
	}

	public GlyphVector getGlyphVector() {
		return gv;
	}

}
