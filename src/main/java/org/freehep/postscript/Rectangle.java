package org.freehep.postscript;

public interface Rectangle extends Shape {

	void add(Rectangle bb);

	double getMinX();

	double getMinY();

	double getMaxX();

	double getMaxY();

}
