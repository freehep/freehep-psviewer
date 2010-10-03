package org.freehep.postscript;

public interface ColorSpace {

	// TODO
	int CS_GRAY = 0;
	int CS_sRGB = 1;

	int getNumComponents();

	float[] toRGB(float[] rgb);

	int getType();

}
