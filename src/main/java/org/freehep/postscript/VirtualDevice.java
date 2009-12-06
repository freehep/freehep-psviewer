// Copyright 2004-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/*
 * @author Mark Donszelmann
 */
public class VirtualDevice extends PSDevice {
	private Graphics2D graphics;
	private Dimension dimension;
	private AffineTransform device = new AffineTransform();

	public VirtualDevice(Graphics2D graphics, Dimension dimension) {
		this.graphics = graphics;
		this.dimension = dimension;
		fireComponentRefreshed();
	}

	@Override
	public double getWidth() {
		return dimension.width;
	}

	@Override
	public double getHeight() {
		return dimension.height;
	}

	@Override
	public AffineTransform getDeviceTransform() {
		return device;
	}

	@Override
	public Graphics getDeviceGraphics() {
		return graphics;
	}

	@Override
	public void refresh() {
		// ignored
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#createImageDevice(int, int)
	 */
	@Override
	public ImageDevice createImageDevice(int width, int height) {
		return new ImageDevice(width, height);
	}
}
