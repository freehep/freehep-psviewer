// Copyright 2004-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * @author Mark Donszelmann
 */
public abstract class PSDevice implements DSCEventListener {

	private boolean valid = false;
	private AffineTransform mirror = null;
	private AffineTransform boundingBox = new AffineTransform();
	private AffineTransform pageBoundingBox = null;
	private AffineTransform transform = new AffineTransform();
	private Graphics2D graphics;

	private List<RefreshListener> listeners = new ArrayList<RefreshListener>();

	public void addComponentRefreshListener(RefreshListener l) {
		listeners.add(l);
	}

	public void removeComponentRefreshListener(RefreshListener l) {
		listeners.remove(l);
	}

	protected void fireComponentRefreshed() {
		valid = false;
		mirror = new AffineTransform(1, 0, 0, -1, 0, getHeight());
		for (Iterator<RefreshListener> i = listeners.iterator(); i.hasNext();) {
			i.next().componentRefreshed();
		}
	}

	public void dscCommentFound(DSCEvent event) {
		if (event.getComment().equals("BoundingBox:")) {
			Rectangle bb = (Rectangle) event.getArgs();
			double s = Math.min(getWidth() / bb.width, getHeight() / bb.height);
			boundingBox = new AffineTransform(s, 0, 0, s, -bb.x * s, -bb.y * s);
			pageBoundingBox = null;
			valid = false;
		}

		if (event.getComment().equals("PageBoundingBox:")) {
			Rectangle bb = (Rectangle) event.getArgs();
			double s = Math.min(getWidth() / bb.width, getHeight() / bb.height);
			pageBoundingBox = new AffineTransform(s, 0, 0, s, -bb.x * s, -bb.y
					* s);
			valid = false;
		}

		if (event.getComment().equals("Page:")) {
			pageBoundingBox = null;
			valid = false;
		}
	}

	public void setTransform(AffineTransform transform) {
		this.transform = (transform != null) ? transform
				: new AffineTransform();
		valid = false;
	}

	public AffineTransform getTransform() {
		return transform;
	}

	public AffineTransform getMirrorTransform() {
		if (mirror == null) {
			mirror = new AffineTransform(1, 0, 0, -1, 0, getHeight());
		}
		return mirror;
	}

	public AffineTransform getBoundingBoxTransform() {
		return (pageBoundingBox != null) ? pageBoundingBox : boundingBox;
	}

	// FIXME this method and the next seem clumsy. There must be a way
	// for deviceGraphics to remain untouched, and to just add new transforms
	// to graphics, BUT getDeviceGraphics().create() does not seem to work,
	// presumably because BufferedPanel probably copies a different buffer...
	public Graphics2D getGraphics() {
		if (!valid) {
			graphics = (Graphics2D) getDeviceGraphics();
			if (graphics != null) {
				graphics.setTransform(getDeviceTransform());
				graphics.transform(getTransform());
				graphics.transform(getMirrorTransform());
				graphics.transform(getBoundingBoxTransform());
				valid = true;
			}			
		}
		
		return graphics;
	}

	public void erasePage() {
		graphics = (Graphics2D) getDeviceGraphics();
		graphics.setBackground(Color.WHITE);
		graphics.setTransform(getDeviceTransform());
		graphics.clearRect(0, 0, (int) getWidth(), (int) getHeight());
		valid = false;
	}

	public abstract AffineTransform getDeviceTransform();

	public abstract Graphics getDeviceGraphics();

	public abstract double getWidth();

	public abstract double getHeight();

	public abstract BufferedImage convertToImage(int width, int height);

	public abstract void refresh();

}
