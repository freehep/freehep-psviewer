package org.freehep.vectorgraphics;

public interface GlyphVector {

	GlyphMetrics getGlyphMetrics(int i);

	Shape getOutline(float x, float y);

}
