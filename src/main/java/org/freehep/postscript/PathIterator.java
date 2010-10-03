package org.freehep.postscript;

public interface PathIterator {

	// TODO
	int SEG_LINETO = 0;
	int SEG_MOVETO = 1;
	int SEG_CUBICTO = 2;
	int SEG_CLOSE = 3;

	boolean isDone();

	void next();

	int currentSegment(float[] coord);

	int currentSegment(double[] coord);
}
