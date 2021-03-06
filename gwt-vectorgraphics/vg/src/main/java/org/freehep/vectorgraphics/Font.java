package org.freehep.vectorgraphics;

public interface Font {

	int PLAIN = 0;
	int BOLD = 1;
	int ITALIC = 2;

	String getPSName();

	Transform getTransform();

	Font deriveFont(Transform t);

	GlyphVector createGlyphVector(FontRenderContext fontRenderContext, char[] uc);

}
