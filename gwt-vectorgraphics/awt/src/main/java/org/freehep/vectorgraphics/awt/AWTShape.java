package org.freehep.vectorgraphics.awt;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

import org.freehep.vectorgraphics.PathIterator;
import org.freehep.vectorgraphics.Rectangle;
import org.freehep.vectorgraphics.Transform;

public class AWTShape implements org.freehep.vectorgraphics.Shape {

	protected Shape s;

	public AWTShape(Shape s) {
		this.s = s;
	}

	public Shape getShape() {
		return s;
	}

	@Override
	public PathIterator getPathIterator() {
		return new AWTPathIterator(s.getPathIterator(new AffineTransform()));
	}

	@Override
	public PathIterator getPathIterator(Transform t) {
		return new AWTPathIterator(s.getPathIterator(((AWTTransform)t).getTransform()));
	}

	@Override
	public Rectangle getBounds() {
		return new AWTRectangle(s.getBounds2D());
	}
}
