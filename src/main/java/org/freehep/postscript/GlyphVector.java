package org.freehep.postscript;

public interface GlyphVector {

	GlyphMetrics getGlyphMetrics(int i);

	Shape getOutline(float x, float y);

}
