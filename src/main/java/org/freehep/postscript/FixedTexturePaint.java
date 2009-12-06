// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.logging.Logger;

/**
 * Texture Paint for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class FixedTexturePaint extends TexturePaint {
	private Logger log = Logger.getLogger("org.freehep.postscript");

	private OperandStack os;
	private AffineTransform matrix;

	public FixedTexturePaint(OperandStack os, AffineTransform m,
			BufferedImage texture, Rectangle2D anchor) {
		super(texture, anchor);
		this.os = os;
		matrix = m;
	}

	@Override
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
		AffineTransform mirror;
		try {
			// calculate Mirror: x = M*ctm --> M = x*ctm-1
			mirror = os.gstate().getTransform();
			mirror = mirror.createInverse();
			mirror.preConcatenate(xform);
		} catch (NoninvertibleTransformException e) {
			log.warning("Pattern problem: could not invert matrix");
			mirror = xform;
		}
		AffineTransform at = new AffineTransform();
		at.preConcatenate(matrix);
		at.preConcatenate(mirror);
		return super.createContext(cm, deviceBounds, userBounds, at, hints);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.TexturePaint#getImage()
	 */
	@Override
	public BufferedImage getImage() {
		System.err.println("getImage");
		return changeColor(super.getImage(), 0xFF000000, 0xFFFFFFFF);
	}

	private BufferedImage changeColor(BufferedImage src, int oldColor,
			int newColor) {
		int width = src.getWidth();
		int height = src.getHeight();
		int type = src.getType();
		WritableRaster srcRaster = src.getRaster();

		ColorModel dstColorModel = src.getColorModel();
		BufferedImage dst = new BufferedImage(dstColorModel,
				dstColorModel.createCompatibleWritableRaster(src.getWidth(),
						src.getHeight()), dstColorModel.isAlphaPremultiplied(),
				null);
		WritableRaster dstRaster = dst.getRaster();

		int[] inPixels = new int[width];
		for (int y = 0; y < height; y++) {
			// We try to avoid calling getRGB on images as it causes them to
			// become unmanaged, causing horrible performance problems.
			if ((type == BufferedImage.TYPE_INT_ARGB)
					|| (type == BufferedImage.TYPE_INT_RGB)) {
				srcRaster.getDataElements(0, y, width, 1, inPixels);
				for (int x = 0; x < width; x++) {
					inPixels[x] = inPixels[x] == oldColor ? newColor : inPixels[x];
				}
				dstRaster.setDataElements(0, y, width, 1, inPixels);
			} else {
				src.getRGB(0, y, width, 1, inPixels, 0, width);
				for (int x = 0; x < width; x++) {
					inPixels[x] = inPixels[x] == oldColor ? newColor : inPixels[x];
				}
				dst.setRGB(0, y, width, 1, inPixels, 0, width);
			}
		}

		return dst;
	}

}
