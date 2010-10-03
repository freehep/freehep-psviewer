// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.viewer;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.freehep.postscript.GraphicsContext;
import org.freehep.postscript.awt.AWTGraphicsContext;
import org.freehep.postscript.types.PSContainer;

/**
 * PostScript Panel for PostScript Processor,
 * 
 * @author Mark Donszelmann
 */
public class PSPanel extends BufferedPanel implements PSContainer {

	private static final long serialVersionUID = 1L;
	private List<RefreshListener> listeners = new ArrayList<RefreshListener>();
	
	public PSPanel() {
		super(false);
	}

	@Override
	public void repaintComponent(Graphics g) {
		for (Iterator<RefreshListener> i = listeners.iterator(); i.hasNext(); ) {
			i.next().componentRefreshed();
		}
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSContainer#addRefreshListener(org.freehep.postscript.RefreshListener)
	 */
	public void addRefreshListener(RefreshListener l) {
		listeners.add(l);
	}
	
	@Override
	public GraphicsContext getGraphicsContext() {
		return new AWTGraphicsContext(getOffscreenGraphics());
	}
}
