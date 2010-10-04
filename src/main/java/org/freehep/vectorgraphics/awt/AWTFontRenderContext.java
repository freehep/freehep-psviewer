package org.freehep.vectorgraphics.awt;

import java.awt.font.FontRenderContext;

import org.freehep.vectorgraphics.Transform;

public class AWTFontRenderContext implements org.freehep.vectorgraphics.FontRenderContext {

	FontRenderContext c;

	public AWTFontRenderContext(Transform t, boolean isAntiAliased, boolean usesFractionalMetrics) {
		c = new FontRenderContext(((AWTTransform)t).getTransform(), isAntiAliased, usesFractionalMetrics);
	}

	public FontRenderContext getFontRenderContext() {
		return c;
	}
	
}
