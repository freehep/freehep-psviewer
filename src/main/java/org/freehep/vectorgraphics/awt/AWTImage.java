package org.freehep.vectorgraphics.awt;

import java.awt.image.BufferedImage;

import org.freehep.vectorgraphics.GraphicsContext;

public class AWTImage implements org.freehep.vectorgraphics.Image {

	BufferedImage img;
	
	public AWTImage(BufferedImage img) {
		this.img = img;
	}

	public AWTImage(int width, int height, int type) {
		img = new BufferedImage(width, height, type);
	}

	@Override
	public GraphicsContext getGraphics() {
		return new AWTGraphicsContext(img.getGraphics());
	}

	@Override
	public int getHeight() {
		return img.getHeight();
	}

	@Override
	public int getWidth() {
		return img.getWidth();
	}

	@Override
	public void setPixel(int x, int y, int[] pixels) {
		img.getRaster().setPixel(x, y, pixels);
	}

	@Override
	public void getDataElements(int x, int y, int w, int h, int[] pixels) {
		img.getRaster().getDataElements(x, y, w, h, pixels);
	}

	@Override
	public void setDataElements(int x, int y, int w, int h, int[] pixels) {
		img.getRaster().setDataElements(x, y, w, h, pixels);
	}

	public BufferedImage getImage() {
		return img;
	}
}
