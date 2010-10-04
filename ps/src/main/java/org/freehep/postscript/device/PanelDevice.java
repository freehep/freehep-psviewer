// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.device;

import org.freehep.postscript.types.PSContainer;
import org.freehep.postscript.types.PSDevice;
import org.freehep.vectorgraphics.Container;
import org.freehep.vectorgraphics.GraphicsContext;
import org.freehep.vectorgraphics.Transform;

/**
 * @author Mark Donszelmann
 */
public abstract class PanelDevice extends PSDevice {

	private final Container container;
	private GraphicsContext imageGraphics = null;

	public PanelDevice(Container container) {
		this.container = container;
			
		// TODO
/*
		((PSContainer)container).addRefreshListener(new RefreshListener() {
			public void componentRefreshed() {
				fireComponentRefreshed();
			}
		});
*/
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
	public Transform getDeviceTransform() {
		return container.getGraphicsConfiguration().getDefaultTransform();
	}

	@Override
	public GraphicsContext getDeviceGraphics() {
		return ((PSContainer)container).getGraphicsContext();
	}

	@Override
	public GraphicsContext getGraphics() {
		if (imageGraphics != null) {
			return imageGraphics;
		}
		return super.getGraphics();
	}

	@Override
	public void refresh() {
		container.repaint();
	}
}
