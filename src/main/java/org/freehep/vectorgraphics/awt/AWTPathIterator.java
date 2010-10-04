package org.freehep.vectorgraphics.awt;

import java.awt.geom.PathIterator;

public class AWTPathIterator implements org.freehep.vectorgraphics.PathIterator {

	PathIterator p;
	
	public AWTPathIterator(PathIterator p) {
		this.p = p;
	}

	@Override
	public boolean isDone() {
		return p.isDone();
	}

	@Override
	public void next() {
		p.next();
	}

	@Override
	public int currentSegment(float[] coords) {
		return p.currentSegment(coords);
	}

	@Override
	public int currentSegment(double[] coords) {
		return p.currentSegment(coords);
	}

	public PathIterator getPathIterator() {
		return p;
	}

}
