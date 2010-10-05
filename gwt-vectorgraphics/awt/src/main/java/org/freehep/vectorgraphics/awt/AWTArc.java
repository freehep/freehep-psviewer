package org.freehep.vectorgraphics.awt;

import java.awt.geom.Arc2D;

import org.freehep.vectorgraphics.Point;

public class AWTArc extends AWTShape implements org.freehep.vectorgraphics.Arc {
	
	public AWTArc(Arc2D a) {
		super(a);
	}
	
	public AWTArc() {
		super(new Arc2D.Double());
	}

	public AWTArc(int type) {
		super(new Arc2D.Double(type));
	}

	@Override
	public void setArcByCenter(double x, double y, double r, double sa,
			double ea, int type) {
		((Arc2D)s).setArcByCenter(x, y, r, sa, ea, type);
	}

	@Override
	public void setArcByTangent(Point p0, Point p1, Point p2, double r) {
		((Arc2D)s).setArcByTangent(((AWTPoint)p0).getPoint(), ((AWTPoint)p1).getPoint(), ((AWTPoint)p2).getPoint(), r);
	}

	@Override
	public Point getStartPoint() {
		return new AWTPoint(((Arc2D)s).getStartPoint());
	}

	@Override
	public Point getEndPoint() {
		return new AWTPoint(((Arc2D)s).getEndPoint());
	}

}
