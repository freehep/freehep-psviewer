package org.freehep.vectorgraphics.awt;

import java.awt.geom.Point2D;

public class AWTPoint implements org.freehep.vectorgraphics.Point {

	java.awt.geom.Point2D p;
	
	public AWTPoint(Point2D p) {
		this.p = p;
	}

	public AWTPoint(double x, double y) {
		p = new Point2D.Double(x, y);
	}

	@Override
	public double getX() {
		return p.getX();
	}

	@Override
	public double getY() {
		return p.getY();
	}
	
	public Point2D getPoint() {
		return p;
	}

}
