package org.freehep.postscript;

public interface Color extends Paint {

	float[] getColorComponents(float[] array);

	int getRGB();
}
