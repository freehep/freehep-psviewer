package org.freehep.vectorgraphics;

public interface ColorSpace {

	int CS_sRGB = 1000;
	int CS_CIEXYZ = 1001;
	int CS_PYCC = 1002;
	int CS_GRAY = 1003;
	int CS_LINEAR_RGB = 1004;

	int getNumComponents();

	float[] toRGB(float[] rgb);

	int getType();

	float[] fromCIEXYZ(float[] colorvalue);

	float[] fromRGB(float[] rgbvalue);

	float[] toCIEXYZ(float[] colorvalue);

	String getName(int index);

	boolean isCS_sRGB();

}
