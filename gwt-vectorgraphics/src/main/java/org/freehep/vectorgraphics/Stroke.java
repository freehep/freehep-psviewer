package org.freehep.vectorgraphics;

public interface Stroke {

    int JOIN_MITER = 0;
    int JOIN_ROUND = 1;
    int JOIN_BEVEL = 2;

    int CAP_BUTT = 0;
    int CAP_ROUND = 1;
    int CAP_SQUARE = 2;

	Shape createStrokedShape(Shape s);
}
