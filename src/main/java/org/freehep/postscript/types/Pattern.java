// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

import java.util.logging.Logger;

import org.freehep.vectorgraphics.ColorSpace;

/**
 * Pattern colorspace.
 * 
 * @author Mark Donszelmann
 */
public class Pattern implements ColorSpace {
	protected Logger log = Logger.getLogger("org.freehep.postscript");
	private ColorSpace colorSpace;

	public Pattern(ColorSpace colorSpace) {
//		super(TYPE_4CLR, 4);
// TODO
		this.colorSpace = colorSpace;
	}

	@Override
	public final float[] fromCIEXYZ(float[] colorvalue) {
		return colorSpace.fromCIEXYZ(colorvalue);
	}

	@Override
	public final float[] fromRGB(float[] rgbvalue) {
		return colorSpace.fromRGB(rgbvalue);
	}

	@Override
	public final float[] toCIEXYZ(float[] colorvalue) {
		return colorSpace.toCIEXYZ(colorvalue);
	}

	@Override
	public final float[] toRGB(float[] colorvalue) {
		return colorSpace.toRGB(colorvalue);
	}

	@Override
	public final String getName(int index) {
		return colorSpace.getName(index);
	}

	@Override
	public final int getNumComponents() {
		return colorSpace.getNumComponents();
	}

	@Override
	public final int getType() {
		return colorSpace.getType();
	}

	@Override
	public final boolean isCS_sRGB() {
		return colorSpace.isCS_sRGB();
	}

}