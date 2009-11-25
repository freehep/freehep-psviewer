// Copyright 2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PostScript Applet for PostScript Processor,
 * 
 * @author Mark Donszelmann
 */
public class PSApplet extends BufferedApplet implements PSContainer {

	private List<RefreshListener> listeners = new ArrayList<RefreshListener>();
	
	public PSApplet() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSContainer#addRefreshListener(org.freehep.postscript.RefreshListener)
	 */
	public void addRefreshListener(RefreshListener l) {
		listeners.add(l);
	}
	
	@Override
	public void repaintComponent(Graphics g) {
		for (Iterator<RefreshListener> i = listeners.iterator(); i.hasNext(); ) {
			i.next().componentRefreshed();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JApplet#update(java.awt.Graphics)
	 */
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		Processor processor = new Processor(this);
		try {
			new PSViewer(processor, "../../c1.eps", 1, 1.0, 1.0, 0.0, 0.0, false);
		} catch (IOException e) {
			System.err.println(e);
		}
		repaint();
	}	
}
