package org.freehep.vectorgraphics.awt;

import java.awt.Font;

import org.freehep.vectorgraphics.FontRenderContext;
import org.freehep.vectorgraphics.GlyphVector;
import org.freehep.vectorgraphics.Transform;

public class AWTFont implements org.freehep.vectorgraphics.Font {

	private Font f;

	public AWTFont(Font font) {
		f = font;
	}

	public AWTFont(String name, int style, float ptSize) {
		f = new Font(name, style, (int)(ptSize + 0.5));
	}

	@Override
	public String getPSName() {
		return f.getPSName();
	}

	@Override
	public Transform getTransform() {
		return new AWTTransform(f.getTransform());
	}

	@Override
	public org.freehep.vectorgraphics.Font deriveFont(Transform t) {
		return new AWTFont(f.deriveFont(((AWTTransform) t).getTransform()));
	}

	@Override
	public GlyphVector createGlyphVector(FontRenderContext ctx, char[] uc) {
		return new AWTGlyphVector(f.createGlyphVector(((AWTFontRenderContext)ctx).getFontRenderContext(), uc));
	}

}
