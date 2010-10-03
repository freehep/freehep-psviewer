package org.freehep.postscript;

public interface Container {

	int getWidth();

	int getHeight();

	GraphicsConfiguration getGraphicsConfiguration();

	void repaint();

	Image createImage(int width, int height);

}
