package org.freehep.vectorgraphics;

public interface Path extends Shape {

    int WIND_EVEN_ODD = PathIterator.WIND_EVEN_ODD;
    int WIND_NON_ZERO = PathIterator.WIND_NON_ZERO;
    
    int SEG_MOVETO  = PathIterator.SEG_MOVETO;
    int SEG_LINETO  = PathIterator.SEG_LINETO;
    int SEG_QUADTO  = PathIterator.SEG_QUADTO;
    int SEG_CUBICTO = PathIterator.SEG_CUBICTO;
    int SEG_CLOSE   = PathIterator.SEG_CLOSE;

	Path copy();

	void transform(Transform pt);

	Point getCurrentPoint();

	void lineTo(float x, float y);

	void moveTo(float x, float y);

	void curveTo(float cx1, float cy1, float cx2, float cy2, float x, float y);

	void closePath();

	Shape createTransformedShape(Transform t);

	void setWindingRule(int rule);

	void append(Shape s, boolean connect);

	void append(PathIterator iterator, boolean connect);

	PathIterator getPathIterator(Transform t, double flat);

}
