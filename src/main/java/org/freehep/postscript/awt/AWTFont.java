package org.freehep.postscript.awt;

import java.awt.Font;

import org.freehep.postscript.FontRenderContext;
import org.freehep.postscript.GlyphVector;
import org.freehep.postscript.Transform;

public class AWTFont implements org.freehep.postscript.Font {

	private Font f;
	
	public AWTFont(Font font) {
		f = font;
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
	public org.freehep.postscript.Font deriveFont(Transform t) {
		return new AWTFont(f.deriveFont(((AWTTransform)t).getTransform()));
	}

	@Override
	public GlyphVector createGlyphVector(FontRenderContext fontRenderContext,
			char[] uc) {
		// TODO Auto-generated method stub
		return null;
	}

}
