// Copyright 2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.freehep.postscript.PSContainer#addRefreshListener(org.freehep.postscript
	 * .RefreshListener)
	 */
	public void addRefreshListener(RefreshListener l) {
		listeners.add(l);
	}

	@Override
	public void repaintComponent(Graphics g) {
		for (Iterator<RefreshListener> i = listeners.iterator(); i.hasNext();) {
			i.next().componentRefreshed();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JApplet#update(java.awt.Graphics)
	 */
	@Override
	public void update(Graphics g) {
		paint(g);
	}

	private static final String FILE = "file";
	private static final String PAGE = "page";
	private static final String SX = "sx";
	private static final String SY = "sy";
	private static final String TX = "tx";
	private static final String TY = "ty";

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#getParameterInfo()
	 */
	@Override
	public String[][] getParameterInfo() {
		return new String[][] {
				{ FILE, "URL", "PostScript file to be displayed" },
				{ PAGE, "int", "Page Number to be displayed (1)" },
				{ SX, "double", "Scale in X direction (1.0)" },
				{ SY, "double", "Scale in Y direction (1.0)" },
				{ TX, "double", "Translate in X direction (0.0)" },
				{ TY, "double", "Translate in Y direction (0.0)" } };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		Processor processor = new Processor(this);
		try {
			String file = getParameter(FILE, "examples/cookbook/program_00.ps");
			int pageNo = getParameter(PAGE, 1);
			double sx = getParameter(SX, 1.0);
			double sy = getParameter(SY, 1.0);
			double tx = getParameter(TX, 0.0);
			double ty = getParameter(TY, 0.0);
			URL url = new URL(getDocumentBase(), file);
			new PSViewer(processor, url, pageNo, sx, sy, tx, ty, false);
		} catch (IOException e) {
			showStatus(e.getMessage());
		}
		repaint();
	}

	private String getParameter(String name, String defaultParameter) {
		String p = getParameter(name);
		return p != null ? p : defaultParameter;
	}

	private int getParameter(String name, int defaultParameter) {
		String p = getParameter(name);
		if (p == null)
			return defaultParameter;
		try {
			return Integer.parseInt(p);
		} catch (NumberFormatException e) {
			return defaultParameter;
		}
	}

	private double getParameter(String name, double defaultParameter) {
		String p = getParameter(name);
		if (p == null)
			return defaultParameter;
		try {
			return Double.parseDouble(p);
		} catch (NumberFormatException e) {
			return defaultParameter;
		}
	}

}
