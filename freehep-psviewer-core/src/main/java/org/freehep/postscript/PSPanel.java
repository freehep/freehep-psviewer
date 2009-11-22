// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * PostScript Panel for PostScript Processor,
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSPanel.java 829a8d93169a
 *          2006/12/08 09:03:07 duns $
 */
public class PSPanel extends JPanel {

	private Graphics2D mirroredGraphics;

	public PSPanel() {
		super(false);
	}

	@Override
	public Graphics getGraphics() {
		return mirroredGraphics;
	}

	@Override
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		if (mirroredGraphics != null) {
			mirroredGraphics.dispose();
		}
		mirroredGraphics = (Graphics2D) super.getGraphics().create();
	}

}
