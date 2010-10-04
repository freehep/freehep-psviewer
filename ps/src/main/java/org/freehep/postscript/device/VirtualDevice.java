// Copyright 2004-2010, FreeHEP.
package org.freehep.postscript.device;

import org.freehep.postscript.types.PSDevice;
import org.freehep.vectorgraphics.Dimension;
import org.freehep.vectorgraphics.GraphicsContext;
import org.freehep.vectorgraphics.Transform;

/*
 * @author Mark Donszelmann
 */
public abstract class VirtualDevice extends PSDevice {
	private GraphicsContext graphics;
	private Dimension dimension;
	private Transform device;

	public VirtualDevice(GraphicsContext graphics, Dimension dimension) {
		this.graphics = graphics;
		this.dimension = dimension;
		this.device = createTransform();
		fireComponentRefreshed();
	}

	@Override
	public double getWidth() {
		return dimension.getWidth();
	}

	@Override
	public double getHeight() {
		return dimension.getHeight();
	}

	@Override
	public Transform getDeviceTransform() {
		return device;
	}

	@Override
	public GraphicsContext getDeviceGraphics() {
		return graphics;
	}

	@Override
	public void refresh() {
		// ignored
	}
}
