package org.freehep.vectorgraphics;

public interface Container {

	int getWidth();

	int getHeight();

	GraphicsConfiguration getGraphicsConfiguration();

	void repaint();

	Image createImage(int width, int height);

}
