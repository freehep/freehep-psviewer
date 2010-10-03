package org.freehep.postscript;

public interface Arc extends Shape {

	// TODO
	int OPEN = 0;

	void setArcByCenter(double x, double y, double r, double d, double e,
			int type);

	void setArcByTangent(Point p0, Point p1, Point p2, double r);

	Point getStartPoint();

	Point getEndPoint();

}
