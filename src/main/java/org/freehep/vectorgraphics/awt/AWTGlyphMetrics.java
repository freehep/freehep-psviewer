package org.freehep.vectorgraphics.awt;

import java.awt.font.GlyphMetrics;

import org.freehep.vectorgraphics.Rectangle;

public class AWTGlyphMetrics implements org.freehep.vectorgraphics.GlyphMetrics {

	private GlyphMetrics gm;

	public AWTGlyphMetrics(GlyphMetrics gm) {
		this.gm = gm;
	}

	@Override
	public double getAdvance() {
		return gm.getAdvance();
	}

	@Override
	public Rectangle getBounds() {
		return new AWTRectangle(gm.getBounds2D());
	}

}
