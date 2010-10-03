// Copyright 2009-2010, FreeHEP.
package org.freehep.postscript.viewer;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.freehep.postscript.GraphicsContext;
import org.freehep.postscript.awt.AWTGraphicsContext;
import org.freehep.postscript.processor.Processor;
import org.freehep.postscript.types.PSContainer;

/**
 * PostScript Applet for PostScript Processor,
 * 
 * @author Mark Donszelmann
 */
public class PSApplet extends BufferedApplet implements PSContainer {
	private static final long serialVersionUID = 1L;
	private static final String FILE = "file";
	private static final String PAGE = "page";
	private static final String SX = "sx";
	private static final String SY = "sy";
	private static final String TX = "tx";
	private static final String TY = "ty";
	private static final String BUFFER = "buffer";

	private List<RefreshListener> listeners = new ArrayList<RefreshListener>();
	private PopupMenu menu;

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
				{ TY, "double", "Translate in Y direction (0.0)" },
				{ BUFFER, "int", "Size of the buffer" } };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		try {
			String version = PSViewer.getVersion();
			// Popup Menu
			final URL aboutURL = new URL(
					"http://freehep.github.com/freehep-psviewer");
			menu = new PopupMenu();
			add(menu);

			// about
			MenuItem aboutMenu = new MenuItem(
					"About the FreeHEP Postscript Viewer " + version + "...");
			menu.add(aboutMenu);
			aboutMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getAppletContext().showDocument(aboutURL);
				}
			});
			enableEvents(AWTEvent.MOUSE_EVENT_MASK);

			String file = getParameter(FILE, "examples/cookbook/program_00.ps");
			int pageNo = getParameter(PAGE, 1);
			double sx = getParameter(SX, 1.0);
			double sy = getParameter(SY, 1.0);
			double tx = getParameter(TX, 0.0);
			double ty = getParameter(TY, 0.0);
			int buffer = getParameter(BUFFER, 0x8000);
			final URL url = new URL(getDocumentBase(), file);

			MenuItem urlMenu = new MenuItem(url.toExternalForm());
			menu.add(urlMenu);
			urlMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getAppletContext().showDocument(url);
				}
			});

			Processor processor = new Processor(this);
			new PSViewer(processor, url, pageNo, sx, sy, tx, ty, buffer, false);
		} catch (IOException e) {
			showStatus(e.getMessage());
		}
		repaint();
	}
	
	@Override
	public GraphicsContext getGraphicsContext() {
		return new AWTGraphicsContext(getOffscreenGraphics());
	}

	@Override
	public void processMouseEvent(MouseEvent event) {
		if (event.isPopupTrigger()) {
			menu.show(event.getComponent(), event.getX(), event.getY());
		}
		super.processMouseEvent(event);
	}

	private String getParameter(String name, String defaultParameter) {
		String p = getParameter(name);
		return p != null ? p : defaultParameter;
	}

	private int getParameter(String name, int defaultParameter) {
		String p = getParameter(name);
		if (p == null) {
			return defaultParameter;
		}
		try {
			return Integer.parseInt(p);
		} catch (NumberFormatException e) {
			return defaultParameter;
		}
	}

	private double getParameter(String name, double defaultParameter) {
		String p = getParameter(name);
		if (p == null) {
			return defaultParameter;
		}
		try {
			return Double.parseDouble(p);
		} catch (NumberFormatException e) {
			return defaultParameter;
		}
	}
}
