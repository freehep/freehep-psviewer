package org.freehep.vectorgraphics.gwt;

import org.freehep.vectorgraphics.Point;

public class GWTPoint implements Point {
	
	private double x, y;
		
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}
}
