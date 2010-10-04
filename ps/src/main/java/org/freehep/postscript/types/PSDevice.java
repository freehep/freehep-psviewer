// Copyright 2004-2010, FreeHEP.
package org.freehep.postscript.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.freehep.postscript.dsc.DSCEvent;
import org.freehep.postscript.dsc.DSCEventListener;
import org.freehep.postscript.viewer.FixedTexturePaint;
import org.freehep.postscript.viewer.RefreshListener;
import org.freehep.vectorgraphics.Device;
import org.freehep.vectorgraphics.GraphicsContext;
import org.freehep.vectorgraphics.Image;
import org.freehep.vectorgraphics.Rectangle;
import org.freehep.vectorgraphics.Transform;

/*
 * @author Mark Donszelmann
 */
public abstract class PSDevice implements Device, DSCEventListener {

	private boolean valid = false;
	private Transform mirror = null;
	private Transform boundingBox = null;
	private Transform pageBoundingBox = null;
	private Transform transform = null;
	private GraphicsContext graphics;

	private List<RefreshListener> listeners = new ArrayList<RefreshListener>();

	public PSDevice() {
		boundingBox = createTransform();
		transform = createTransform();
	}

	public void addComponentRefreshListener(RefreshListener l) {
		listeners.add(l);
	}

	public void removeComponentRefreshListener(RefreshListener l) {
		listeners.remove(l);
	}

	protected void fireComponentRefreshed() {
		valid = false;
		mirror = createTransform(1, 0, 0, -1, 0, getHeight());
		for (Iterator<RefreshListener> i = listeners.iterator(); i.hasNext();) {
			i.next().componentRefreshed();
		}
	}

	public void dscCommentFound(DSCEvent event) {
		if (event.getComment().equals("BoundingBox:")) {
			int[] bb = (int[]) event.getArgs();
			double s = Math.min(getWidth() / (bb[2] - bb[0]), getHeight() / (bb[3] - bb[1]));
			boundingBox = createTransform(s, 0, 0, s, -bb[0] * s, -bb[1] * s);
			pageBoundingBox = null;
			valid = false;
		}

		if (event.getComment().equals("PageBoundingBox:")) {
			int[] bb = (int[]) event.getArgs();
			double s = Math.min(getWidth() / (bb[2] - bb[0]), getHeight() / (bb[3] - bb[1]));
			pageBoundingBox = createTransform(s, 0, 0, s, -bb[0] * s, -bb[1] * s);
			valid = false;
		}

		if (event.getComment().equals("Page:")) {
			pageBoundingBox = null;
			valid = false;
		}
	}

	public void setTransform(Transform transform) {
		this.transform = (transform != null) ? transform : createTransform();
		valid = false;
	}

	public Transform getTransform() {
		return transform;
	}

	public Transform getMirrorTransform() {
		if (mirror == null) {
			mirror = createTransform(1, 0, 0, -1, 0, getHeight());
		}
		return mirror;
	}

	public Transform getBoundingBoxTransform() {
		return (pageBoundingBox != null) ? pageBoundingBox : boundingBox;
	}

	// FIXME this method and the next seem clumsy. There must be a way
	// for deviceGraphics to remain untouched, and to just add new transforms
	// to graphics, BUT getDeviceGraphics().create() does not seem to work,
	// presumably because BufferedPanel probably copies a different buffer...
	public GraphicsContext getGraphics() {
		if (!valid) {
			graphics = getDeviceGraphics();
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
		graphics = getDeviceGraphics();
		// WHITE
		graphics.setBackground(createColor(1.0f, 1.0f, 1.0f));
		graphics.setTransform(getDeviceTransform());
		graphics.clearRect(0, 0, (int) getWidth(), (int) getHeight());
		valid = false;
	}

	public abstract FixedTexturePaint createTexturePaint(Image changeColor, Rectangle anchor);
}
