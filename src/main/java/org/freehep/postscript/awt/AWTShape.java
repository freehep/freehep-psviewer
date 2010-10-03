package org.freehep.postscript.awt;

import java.awt.Shape;

import org.freehep.postscript.PathIterator;
import org.freehep.postscript.Rectangle;
import org.freehep.postscript.Transform;

public class AWTShape implements org.freehep.postscript.Shape {

	private Shape s;

	public Shape getShape() {
		return s;
	}

	@Override
	public PathIterator getPathIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator getPathIterator(Transform t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
