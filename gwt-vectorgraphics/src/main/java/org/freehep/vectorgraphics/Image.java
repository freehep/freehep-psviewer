package org.freehep.vectorgraphics;

public interface Image {

	// NOTE, take others from BufferedImage
	int TYPE_INT_ARGB = 2;

	GraphicsContext getGraphics();

	int getHeight();

	int getWidth();

	void setPixel(int x, int y, int[] pixels);

	void getDataElements(int x, int y, int w, int h, int[] pixels);

	void setDataElements(int x, int y, int w, int h, int[] pixels);
}
