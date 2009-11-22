// Copyright 2004, FreeHEP.
package org.freehep.postscript;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/*
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/VirtualDevice.java 829a8d93169a 2006/12/08 09:03:07 duns $
 */
public class VirtualDevice extends PSDevice {

	private Graphics2D imageGraphics = null;
	private Graphics2D graphics;
	private Dimension dimension;
	private AffineTransform device = new AffineTransform();

	public VirtualDevice(Graphics2D graphics, Dimension dimension) {
		this.graphics = graphics;
		this.dimension = dimension;
		fireComponentResizedEvent(new ComponentEvent(null,
				ComponentEvent.COMPONENT_RESIZED));
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
	public BufferedImage convertToImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		imageGraphics = (Graphics2D) image.getGraphics();
		return image;
	}

	@Override
	public Graphics2D getGraphics() {
		if (imageGraphics != null) {
			return imageGraphics;
		}
		return super.getGraphics();
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
}
