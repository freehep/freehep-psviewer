package org.freehep.postscript;

public interface Shape {

	PathIterator getPathIterator();
	PathIterator getPathIterator(Transform t);
	Rectangle getBounds();
}
