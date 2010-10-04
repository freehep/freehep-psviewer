// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.viewer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.freehep.postscript.Color;
import org.freehep.postscript.ColorModel;
import org.freehep.postscript.Image;
import org.freehep.postscript.NoninvertibleTransformException;
import org.freehep.postscript.Paint;
import org.freehep.postscript.PaintContext;
import org.freehep.postscript.Rectangle;
import org.freehep.postscript.RenderingHints;
import org.freehep.postscript.Transform;
import org.freehep.postscript.stacks.OperandStack;

/**
 * Texture Paint for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class FixedTexturePaint implements Paint {
	private Logger log = Logger.getLogger("org.freehep.postscript");

	private OperandStack os;
	private Image texture;
	private Rectangle anchor;
	private Transform matrix;
	private Map<Color, FixedTexturePaint> byColor = new HashMap<Color, FixedTexturePaint>(
			2);


	public FixedTexturePaint(OperandStack os, Transform m,
			Image texture, Rectangle anchor) {
		this.texture = texture;
		this.anchor = anchor;
		this.os = os;
		matrix = m;
	}

	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle userBounds, Transform xform, RenderingHints hints) {
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
		Transform at = os.gstate().device().createTransform();
		at.preConcatenate(matrix);
		at.preConcatenate(mirror);
		return os.gstate().device().createContext(cm, deviceBounds, userBounds, at, hints);
	}

	public FixedTexturePaint inColor(Color color) {
		FixedTexturePaint paint = byColor.get(color);
		if (paint == null) {
			paint = os.gstate().device().createTexturePaint(changeColor(texture, 0xFF000000,
					color.getRGB()), anchor);
			byColor.put(color, paint);
		}
		return paint;
	}

	private Image changeColor(Image src, int oldColor,
			int newColor) {
		int width = src.getWidth();
		int height = src.getHeight();
//		int type = src.getType();

//		ColorModel dstColorModel = src.getColorModel();
		Image dst = os.gstate().device().createImage(src.getWidth(), src.getHeight(),
				Image.TYPE_INT_ARGB);

		// replace oldcolor with newcolor and make others transparent
		int[] inPixels = new int[width];
		for (int y = 0; y < height; y++) {
			src.getDataElements(0, y, width, 1, inPixels);
			for (int x = 0; x < width; x++) {
				inPixels[x] = inPixels[x] == oldColor ? newColor
						: inPixels[x] & 0x00FFFFFF;
			}
			dst.setDataElements(0, y, width, 1, inPixels);
		}

		return dst;
	}

}
