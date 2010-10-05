package org.freehep.vectorgraphics.gwt;

import org.freehep.vectorgraphics.PathIterator;
import org.freehep.vectorgraphics.Point;
import org.freehep.vectorgraphics.Shape;
import org.freehep.vectorgraphics.Transform;
import org.vaadin.gwtgraphics.client.shape.Path;

public class GWTPath extends GWTShape implements org.freehep.vectorgraphics.Path {

	Path p;
	
	@Override
	public org.freehep.vectorgraphics.Path copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transform(Transform pt) {
		// TODO Auto-generated method stub

	}

	@Override
	public Point getCurrentPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lineTo(float x, float y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveTo(float x, float y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void curveTo(float cx1, float cy1, float cx2, float cy2, float x,
			float y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closePath() {
		// TODO Auto-generated method stub

	}

	@Override
	public Shape createTransformedShape(Transform t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWindingRule(int rule) {
		// TODO Auto-generated method stub

	}

	@Override
	public void append(Shape s, boolean connect) {
		// TODO Auto-generated method stub

	}

	@Override
	public void append(PathIterator iterator, boolean connect) {
		// TODO Auto-generated method stub

	}

	@Override
	public PathIterator getPathIterator(Transform t, double flat) {
		// TODO Auto-generated method stub
		return null;
	}

}
