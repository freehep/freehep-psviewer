// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.device;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.freehep.postscript.types.PSContainer;
import org.freehep.postscript.types.PSDevice;
import org.freehep.postscript.viewer.RefreshListener;

/**
 * @author Mark Donszelmann
 */
public class PanelDevice extends PSDevice {

	private final Container container;
	private Graphics2D imageGraphics = null;

	public PanelDevice(Container container) {
		this.container = container;
				
		((PSContainer)container).addRefreshListener(new RefreshListener() {
			public void componentRefreshed() {
				fireComponentRefreshed();
			}
		});
	}

	/* FIXME Does not seem to work, gives 2 images, one up-side-down, while resizing, no image in stand-still
	public PanelDevice(JApplet applet) {
		this.container = applet;
		
		((PSContainer)applet).addRefreshListener(new RefreshListener() {
			public void componentRefreshed() {
				fireComponentRefreshed();
			}
		});
	} */
		
	@Override
	public double getWidth() {
		return container.getWidth();
	}

	@Override
	public double getHeight() {
		return container.getHeight();
	}

	@Override
	public AffineTransform getDeviceTransform() {
		return container.getGraphicsConfiguration().getDefaultTransform();
	}

	@Override
	public Graphics getDeviceGraphics() {
		return ((PSContainer)container).getOffscreenGraphics();
	}

	@Override
	public Graphics2D getGraphics() {
		if (imageGraphics != null) {
			return imageGraphics;
		}
		return super.getGraphics();
	}

	@Override
	public void refresh() {
		container.repaint();
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#createImageDevice(int, int)
	 */
	@Override
	public ImageDevice createImageDevice(int width, int height) {
		return new ImageDevice(container, width, height);
	}
}
