package org.freehep.postscript.awt;

import java.awt.image.BufferedImage;

import org.freehep.postscript.GraphicsContext;

public class AWTImage implements org.freehep.postscript.Image {

	BufferedImage i;
	
	@Override
	public GraphicsContext getGraphics() {
		return new AWTGraphicsContext(i.getGraphics());
	}

	@Override
	public int getHeight() {
		return i.getHeight();
	}

	@Override
	public int getWidth() {
		return i.getWidth();
	}

	@Override
	public void setPixel(int x, int y, int[] pixels) {
		// TODO Auto-generated method stub
		
	}
}
