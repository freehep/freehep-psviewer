// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author Mark Donszelmann
 */
public class PanelDevice extends PSDevice {

	private final JPanel panel;
	private Graphics2D imageGraphics = null;

	public PanelDevice(JPanel panel) {
		this.panel = panel;

		// forward the resize
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				fireComponentResizedEvent(event);
			}
		});
	}

	@Override
	public double getWidth() {
		return panel.getWidth();
	}

	@Override
	public double getHeight() {
		return panel.getHeight();
	}

	@Override
	public AffineTransform getDeviceTransform() {
		return panel.getGraphicsConfiguration().getDefaultTransform();
	}

	@Override
	public Graphics getDeviceGraphics() {
		return panel.getGraphics();
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
		panel.repaint();
	}

	@Override
	public BufferedImage convertToImage(int width, int height) {
		BufferedImage image = (BufferedImage) panel.createImage(width, height);
		imageGraphics = (Graphics2D) image.getGraphics();
		return image;
	}
}
