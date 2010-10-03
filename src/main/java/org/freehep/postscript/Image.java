package org.freehep.postscript;

public interface Image {

	// FIXME
	String TYPE_INT_ARGB = null;

	GraphicsContext getGraphics();

	int getHeight();

	int getWidth();

	// use getRaster().setPixel;
	void setPixel(int x, int y, int[] pixels);

}
