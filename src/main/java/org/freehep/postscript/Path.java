package org.freehep.postscript;


public interface Path extends Shape {

	// TODO
	int WIND_EVEN_ODD = 0;

	Path clone();

	void transform(Transform pt);

	Point getCurrentPoint();

	void lineTo(float x, float y);

	void moveTo(float x, float y);

	void curveTo(float cx1, float cy1, float cx2, float cy2, float x, float y);

	void closePath();

	Shape createTransformedShape(Transform t);

	void setWindingRule(int rule);

	void append(Shape s, boolean b);

	void append(PathIterator iterator, boolean b);

	PathIterator getPathIterator(Transform t, double flat);

}
