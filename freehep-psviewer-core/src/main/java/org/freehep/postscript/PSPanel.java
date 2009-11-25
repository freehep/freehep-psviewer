// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * PostScript Panel for PostScript Processor,
 * 
 * @author Mark Donszelmann
 */
public class PSPanel extends JPanel {

	private Graphics2D mirroredGraphics;

	public PSPanel() {
		super(true);
	}

	@Override
	public final Graphics getGraphics() {
		if (mirroredGraphics == null) mirroredGraphics = (Graphics2D) super.getGraphics().create();
		return mirroredGraphics;
	}
	
/*
	@Override
	public final void setBounds(int x, int y, int w, int h) {
		if ((x == getX()) && (y == getY()) && (w == getWidth()) && (h == getHeight())) {
			return;
		}
		super.setBounds(x, y, w, h);
		if (mirroredGraphics != null) {
			mirroredGraphics.dispose();
		}
		mirroredGraphics = (Graphics2D) super.getGraphics().create();	
		System.err.println(w+" "+h+" "+((double)w)/h);
	}
*/
}
