package org.freehep.vectorgraphics.awt;

import java.awt.Container;
import java.awt.image.BufferedImage;

import org.freehep.vectorgraphics.GraphicsConfiguration;
import org.freehep.vectorgraphics.Image;

public class AWTContainer implements org.freehep.vectorgraphics.Container {

	Container c;
	
	public AWTContainer(Container c) {
		this.c = c;
	}

	@Override
	public int getWidth() {
		return c.getWidth();
	}

	@Override
	public int getHeight() {
		return c.getHeight();
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration() {
		return new AWTGraphicsConfiguration(c.getGraphicsConfiguration());
	}

	@Override
	public void repaint() {
		c.repaint();
	}

	@Override
	public Image createImage(int width, int height) {
		// NOTE BufferedImage assumed
		return new AWTImage((BufferedImage)c.createImage(width, height));
	}

}
