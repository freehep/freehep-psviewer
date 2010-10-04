package org.freehep.vectorgraphics;

public interface PathIterator {

	int SEG_MOVETO = 0;
	int SEG_LINETO = 1;
	int SEG_QUADTO = 2;
	int SEG_CUBICTO = 3;
	int SEG_CLOSE = 4;

	int WIND_EVEN_ODD = 0;
	int WIND_NON_ZERO	= 1;

	boolean isDone();

	void next();

	int currentSegment(float[] coords);

	int currentSegment(double[] coords);
}
