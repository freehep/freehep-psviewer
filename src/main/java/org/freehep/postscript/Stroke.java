package org.freehep.postscript;

public interface Stroke {

	// TODO
	int CAP_BUTT = 0;
	int JOIN_MITER = 0;

	Shape createStrokedShape(Shape path);

}
