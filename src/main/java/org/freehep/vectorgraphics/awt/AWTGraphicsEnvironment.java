package org.freehep.vectorgraphics.awt;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class AWTGraphicsEnvironment implements org.freehep.vectorgraphics.GraphicsEnvironment {

	GraphicsEnvironment e;
	
	@Override
	public org.freehep.vectorgraphics.Font[] getAllFonts() {
		Font[] fonts =  e.getAllFonts();
		org.freehep.vectorgraphics.Font[] vgf = new org.freehep.vectorgraphics.Font[fonts.length];
		
		for (int i=0; i<fonts.length; i++) {
			vgf[i] = new AWTFont(fonts[i]);
		}
		
		return vgf;
	}

}
