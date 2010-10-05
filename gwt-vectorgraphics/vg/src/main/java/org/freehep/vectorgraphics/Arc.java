package org.freehep.vectorgraphics;

public interface Arc extends Shape {

	int OPEN = 0;
	int CHORD = 1;
	int PIE = 2;

	void setArcByCenter(double x, double y, double r, double d, double e,
			int type);

	void setArcByTangent(Point p0, Point p1, Point p2, double r);

	Point getStartPoint();

	Point getEndPoint();

}
