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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transform getTransform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public org.freehep.postscript.Font deriveFont(Transform t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GlyphVector createGlyphVector(FontRenderContext fontRenderContext,
			char[] uc) {
		// TODO Auto-generated method stub
		return null;
	}

}
