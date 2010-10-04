package org.freehep.postscript;

public interface ColorSpace {

	// TODO
	int CS_GRAY = 0;
	int CS_sRGB = 1;

	int getNumComponents();

	float[] toRGB(float[] rgb);

	int getType();

	float[] fromCIEXYZ(float[] colorvalue);

	float[] fromRGB(float[] rgbvalue);

	float[] toCIEXYZ(float[] colorvalue);

	String getName(int index);

	boolean isCS_sRGB();

}
