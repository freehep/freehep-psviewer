// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.viewer;

import java.awt.Color;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.freehep.postscript.Paint;
import org.freehep.postscript.stacks.OperandStack;

/**
 * Texture Paint for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class FixedTexturePaint extends TexturePaint {
	private Logger log = Logger.getLogger("org.freehep.postscript");

	private OperandStack os;
	private AffineTransform matrix;
	private Map<Color, TexturePaint> byColor = new HashMap<Color, TexturePaint>(
			2);

	public FixedTexturePaint(OperandStack os, AffineTransform m,
			BufferedImage texture, Rectangle2D anchor) {
		super(texture, anchor);
		this.os = os;
		matrix = m;
	}

	@Override
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
		org.freehep.postscript.Transform mirror;
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

	public TexturePaint inColor(Color color) {
		TexturePaint paint = byColor.get(color);
		if (paint == null) {
			paint = new TexturePaint(changeColor(super.getImage(), 0xFF000000,
					color.getRGB()), getAnchorRect());
			byColor.put(color, paint);
		}
		return paint;
	}

	private BufferedImage changeColor(BufferedImage src, int oldColor,
			int newColor) {
		int width = src.getWidth();
		int height = src.getHeight();
//		int type = src.getType();
		WritableRaster srcRaster = src.getRaster();

//		ColorModel dstColorModel = src.getColorModel();
		BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		WritableRaster dstRaster = dst.getRaster();

		// replace oldcolor with newcolor and make others transparent
		int[] inPixels = new int[width];
		for (int y = 0; y < height; y++) {
			srcRaster.getDataElements(0, y, width, 1, inPixels);
			for (int x = 0; x < width; x++) {
				inPixels[x] = inPixels[x] == oldColor ? newColor
						: inPixels[x] & 0x00FFFFFF;
			}
			dstRaster.setDataElements(0, y, width, 1, inPixels);
		}

		return dst;
	}

}
