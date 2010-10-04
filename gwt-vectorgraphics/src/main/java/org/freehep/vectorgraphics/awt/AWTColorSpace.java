package org.freehep.vectorgraphics.awt;

import java.awt.color.ColorSpace;

public class AWTColorSpace implements org.freehep.vectorgraphics.ColorSpace {

	ColorSpace cs;
	
	public AWTColorSpace(int type) {
		cs = ColorSpace.getInstance(type);
	}

	@Override
	public int getNumComponents() {
		return cs.getNumComponents();
	}

	@Override
	public float[] toRGB(float[] colorvalue) {
		return cs.toRGB(colorvalue);
	}

	@Override
	public int getType() {
		return cs.getType();
	}

	@Override
	public float[] fromCIEXYZ(float[] colorvalue) {
		return cs.fromCIEXYZ(colorvalue);
	}

	@Override
	public float[] fromRGB(float[] rgbvalue) {
		return cs.fromRGB(rgbvalue);
	}

	@Override
	public float[] toCIEXYZ(float[] colorvalue) {
		return cs.toCIEXYZ(colorvalue);
	}

	@Override
	public String getName(int index) {
		return cs.getName(index);
	}

	@Override
	public boolean isCS_sRGB() {
		return cs.isCS_sRGB();
	}

}
