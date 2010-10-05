package org.freehep.vectorgraphics;

public interface Color extends Paint {

	float[] getColorComponents(float[] array);

	int getRGB();
}
