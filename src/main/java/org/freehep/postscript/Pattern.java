// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.color.ColorSpace;
import java.util.logging.Logger;

/**
 * Pattern colorspace.
 * 
 * @author Mark Donszelmann
 */
public class Pattern extends ColorSpace {
	private Logger log = Logger.getLogger("org.freehep.postscript");
	private ColorSpace colorSpace;

	public Pattern(ColorSpace colorSpace) {
		super(TYPE_4CLR, 4);
		this.colorSpace = colorSpace;
	}

	@Override
	public final float[] fromCIEXYZ(float[] colorvalue) {
		log.info("fromCIEXYZ " + colorvalue.length);
		return new float[] { 0.7f, 0.7f, 0.7f, 0.7f, 0.7f };
	}

	@Override
	public final float[] fromRGB(float[] rgbvalue) {
		log.info("fromRGB");
		return colorSpace.fromRGB(rgbvalue);
	}

	@Override
	public final float[] toCIEXYZ(float[] colorvalue) {
		log.info("toCIEXYZ");
		return colorSpace.toCIEXYZ(colorvalue);
	}

	@Override
	public final float[] toRGB(float[] colorvalue) {
		log.info("toRGB");
		return colorSpace.toRGB(colorvalue);
	}

	@Override
	public final String getName(int index) {
		log.info("getName");
		return colorSpace.getName(index);
	}

	@Override
	public final int getNumComponents() {
		log.info("getNumComponents " + colorSpace.getNumComponents());
		return 4;
	}

	@Override
	public final int getType() {
		return super.getType();
	}

	@Override
	public final boolean isCS_sRGB() {
		log.info("isRGB");
		return colorSpace.isCS_sRGB();
	}

}