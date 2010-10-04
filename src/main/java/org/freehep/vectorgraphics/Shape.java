package org.freehep.vectorgraphics;

public interface Shape {

	PathIterator getPathIterator();
	PathIterator getPathIterator(Transform t);
	Rectangle getBounds();
}
