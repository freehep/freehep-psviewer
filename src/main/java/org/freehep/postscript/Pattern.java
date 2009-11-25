// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.awt.color.ColorSpace;

/**
 * Pattern colorspace.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/Pattern.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public class Pattern extends ColorSpace {

	private ColorSpace colorSpace;

	public Pattern(ColorSpace colorSpace) {
		super(TYPE_4CLR, 4);
		this.colorSpace = colorSpace;
	}

	@Override
	public final float[] fromCIEXYZ(float[] colorvalue) {
		System.out.println("fromCIEXYZ " + colorvalue.length);
		return new float[] { 0.7f, 0.7f, 0.7f, 0.7f, 0.7f };
	}

	@Override
	public final float[] fromRGB(float[] rgbvalue) {
		System.out.println("fromRGB");
		return colorSpace.fromRGB(rgbvalue);
	}

	@Override
	public final float[] toCIEXYZ(float[] colorvalue) {
		System.out.println("toCIEXYZ");
		return colorSpace.toCIEXYZ(colorvalue);
	}

	@Override
	public final float[] toRGB(float[] colorvalue) {
		System.out.println("toRGB");
		return colorSpace.toRGB(colorvalue);
	}

	@Override
	public final String getName(int index) {
		System.out.println("getName");
		return colorSpace.getName(index);
	}

	@Override
	public final int getNumComponents() {
		System.out.println("getNumComponents " + colorSpace.getNumComponents());
		return 4;
	}

	@Override
	public final int getType() {
		return super.getType();
	}

	@Override
	public final boolean isCS_sRGB() {
		System.out.println("isRGB");
		return colorSpace.isCS_sRGB();
	}

}