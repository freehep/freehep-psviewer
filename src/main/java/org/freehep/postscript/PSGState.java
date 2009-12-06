// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 * Graphics State Object for PostScript Processor, as defined in 4.2 Graphics
 * State.
 * 
 * Some of the state is kept in Graphics2D, some of it in BasicStroke
 * 
 * @author Mark Donszelmann
 */
public class PSGState extends PSComposite {

	private PSDevice device;
	private AffineTransform ctm;
	private GeneralPath path;
	private GeneralPath clipPath;
	private float lineWidth;
	private int cap;
	private int join;
	private float miterLimit;
	private float[] dash;
	private float dashPhase;
	private PSDictionary font;
	private double flat;
	private PSPackedArray transfer;
	private ColorSpace colorSpace;
	private String colorSpaceName;
	private PSPackedArray blackGeneration;
	private PSPackedArray underColorRemoval;
	private Rectangle2D boundingBox;

	private PSGState() {
		super("gstate", true);
	}

	public PSGState(PSDevice device) {
		super("gstate", true);
		this.device = device;

		font = new PSFontDictionary(device.getGraphics().getFont(), "STDLatin");
		flat = 0.5; // FIXME: is device dependent
		transfer = new PSPackedArray(new PSObject[0]);
		transfer.setExecutable();
		blackGeneration = new PSPackedArray(0);
		blackGeneration.setExecutable();
		underColorRemoval = new PSPackedArray(0);
		underColorRemoval.setExecutable();
		boundingBox = null;
		initGraphics();
		erasePage();
	}

	public void initGraphics() {
		ctm = new AffineTransform();
		newPath();
		initClip();
		setColorSpace(Constants.DEVICE_GRAY);
		setColor(new float[] { 0.0f });
		lineWidth = 1.0f;
		cap = BasicStroke.CAP_BUTT;
		join = BasicStroke.JOIN_MITER;
		miterLimit = 10.0f;
		dash = null;
		dashPhase = 0.0f;
		setStroke();
	}
	
	public BufferedImage convertToImage(int width, int height) {
		if (!(device instanceof ImageDevice)) {
			device = device.createImageDevice(width, height);
		}
		return ((ImageDevice)device).getImage();
	}

	public void erasePage() {
		String oldSpace = colorSpace();
		float[] oldColor = color();
		setColorSpace(Constants.DEVICE_GRAY);
		setColor(new float[] { 1.0f });
		device.erasePage();
		setColorSpace(oldSpace);
		setColor(oldColor);
	}

	public void copyInto(PSGState copy) {
		copy.device = device;
		copy.ctm = (AffineTransform) ctm.clone();
		copy.path = (GeneralPath) path.clone();
		copy.clipPath = (clipPath == null) ? null : (GeneralPath) clipPath
				.clone();
		copy.lineWidth = lineWidth;
		copy.cap = cap;
		copy.join = join;
		copy.miterLimit = miterLimit;
		if (dash == null) {
			copy.dash = null;
		} else {
			copy.dash = new float[dash.length];
			System.arraycopy(dash, 0, copy.dash, 0, dash.length);
		}
		copy.dashPhase = dashPhase;
		copy.font = new PSDictionary();
		font.copyInto(copy.font);
		copy.flat = flat;
		copy.transfer = (PSPackedArray) transfer.copy();
		copy.colorSpace = colorSpace;
		copy.colorSpaceName = colorSpaceName;
		copy.blackGeneration = (PSPackedArray) blackGeneration.copy();
		copy.underColorRemoval = (PSPackedArray) underColorRemoval.copy();
		copy.boundingBox = boundingBox;
	}

	@Override
	public PSObject copy() {
		PSGState copy = new PSGState();
		copyInto(copy);
		return copy;
	}

	private void setStroke() {
		BasicStroke stroke = new BasicStroke(lineWidth, cap, join, miterLimit,
				dash, dashPhase);
		device.getGraphics().setStroke(stroke);
	}

	public void fill() {
		fill(path);
	}

	public void stroke() {
		stroke(path);
	}

	public void strokePath() {
		path = new GeneralPath(device.getGraphics().getStroke()
				.createStrokedShape(path));
	}

	public Shape charPath(int cc, float x, float y, boolean strokePath) {
		Shape charPath;
		// FIXME: works only for Java type 1 fonts
		switch (font.getInteger("FontType")) {
		case 1:
			PSName name = font.getPackedArray("Encoding").getName(cc);
			PSObject obj = font.getDictionary("CharStrings").get(name);
			// FIXME: obj may be PSPackedArray
			GlyphVector glyph = ((PSJavaGlyph) obj).getGlyph();
			charPath = glyph.getOutline(x, y);
			break;

		default:
			log.warning(getClass() + ": CharPath failed for fonttype: "
					+ font.getInteger("FontType"));
			return null;
			// FontRenderContext fontRenderContext = new FontRenderContext(null,
			// false, true);
			// GlyphVector boxGlyph = font.createGlyphVector(fontRenderContext,
			// '\u25a1');
			// return boxGlyph.getOutline(x, y);
		}

		if (strokePath) {
			charPath = new GeneralPath(device.getGraphics().getStroke()
					.createStrokedShape(charPath));
		}
		return charPath;
	}

	public void fill(Shape s) {
		Graphics2D g = (Graphics2D) device.getGraphics().create();
		g.transform(ctm);
		g.fill(s);
		g.dispose();
	}

	public void stroke(Shape s) {
		stroke(s, null);
	}

	public void stroke(Shape s, AffineTransform m) {
		AffineTransform at = (AffineTransform) ctm.clone();
		GeneralPath p = new GeneralPath(s);

		if (m != null) {
			// apply AT-1 to s
			try {
				AffineTransform pt = m.createInverse();
				p.transform(pt);
			} catch (NoninvertibleTransformException e) {
				log.warning(Constants.INTERNAL_GSTATE_ERROR);
			}

			// add new transform
			at.concatenate(m);
		}

		Graphics2D g = (Graphics2D) device.getGraphics().create();
		g.transform(at);
		g.draw(p);
		g.dispose();
	}

	public void show(GlyphVector gv, float x, float y) {
		Graphics2D g = (Graphics2D) device.getGraphics().create();
		g.transform(ctm);

		g.drawGlyphVector(gv, x, y);
		g.dispose();
	}

	public void image(RenderedImage image, AffineTransform at) {
		Graphics2D g = (Graphics2D) device.getGraphics().create();
		g.transform(ctm);

		// map to unit space!
		try {
			at = at.createInverse();
		} catch (NoninvertibleTransformException e) {
			log.warning(Constants.INTERNAL_GSTATE_ERROR);
		}

		g.drawRenderedImage(image, at);
		g.dispose();
	}

	public Point2D position() {
		return (path == null) ? null : path.getCurrentPoint();
	}

	public void translate(double tx, double ty) {
		AffineTransform at = AffineTransform.getTranslateInstance(-tx, -ty);
		path.transform(at);
		ctm.translate(tx, ty);
	}

	public void rotate(double angle) {
		AffineTransform at = AffineTransform.getRotateInstance(-angle);
		path.transform(at);
		ctm.rotate(angle);
	}

	public void scale(double sx, double sy) {
		AffineTransform at = AffineTransform.getScaleInstance(1 / sx, 1 / sy);
		path.transform(at);
		ctm.scale(sx, sy);
	}

	public void setTransform(AffineTransform at) {

		// apply AT-1 * CTM to path
		try {
			AffineTransform pt = at.createInverse();
			pt.concatenate(ctm);
			path.transform(pt);
		} catch (NoninvertibleTransformException e) {
			log.warning(Constants.INTERNAL_GSTATE_ERROR);
		}

		// set new transform
		ctm.setTransform(at);
	}

	public void transform(AffineTransform at) {

		// apply AT-1 to path
		try {
			AffineTransform pt = at.createInverse();
			path.transform(pt);
		} catch (NoninvertibleTransformException e) {
			log.warning(Constants.INTERNAL_GSTATE_ERROR);
		}

		// add new transform
		ctm.concatenate(at);
	}

	public AffineTransform getTransform() {
		return (AffineTransform) ctm.clone();
	}

	public GeneralPath path() {
		return path;
	}

	public GeneralPath newPath() {
		path = new GeneralPath();
		path.transform(ctm);
		return path;
	}

	public void initClip() {
		device.getGraphics().setClip(null);
		try {
			AffineTransform inverse = ctm.createInverse();

			clipPath = new GeneralPath(new Rectangle(0, 0, (int) device
					.getWidth(), (int) device.getHeight()));
			clipPath.transform(inverse);
		} catch (NoninvertibleTransformException e) {
			log.warning("Internal error in GState");
		}
	}

	public void clip(Shape p) {
		clipPath = new GeneralPath(p);
		Shape clip = clipPath.createTransformedShape(ctm);
		device.getGraphics().setClip(clip);
	}

	public void clipPath() {
		path = (GeneralPath) clipPath.clone();
		path.transform(ctm);
	}

	public float lineWidth() {
		return lineWidth;
	}

	public void setLineWidth(double width) {
		lineWidth = (float) width;
		setStroke();
	}

	public float dashPhase() {
		return dashPhase;
	}

	public float[] dash() {
		return dash;
	}

	public void setDash(float[] dash, float dashPhase) {
		if (dash.length == 0) {
			this.dash = null;
			this.dashPhase = 0;
		} else {
			this.dash = new float[dash.length];
			System.arraycopy(dash, 0, this.dash, 0, dash.length);
			this.dashPhase = dashPhase;
		}
		setStroke();
	}

	public void setLineCap(int cap) {
		this.cap = cap;
		setStroke();
	}

	public int lineCap() {
		return cap;
	}

	public void setLineJoin(int join) {
		this.join = join;
		setStroke();
	}

	public int lineJoin() {
		return join;
	}

	public void setMiterLimit(float miterLimit) {
		this.miterLimit = miterLimit;
		setStroke();
	}

	public float miterLimit() {
		return miterLimit;
	}

	public void setStrokeAdjust(boolean adjust) {
		device.getGraphics().setRenderingHint(
				RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
	}

	public boolean strokeAdjust() {
		Object value = device.getGraphics().getRenderingHint(
				RenderingHints.KEY_STROKE_CONTROL);
		return (value == RenderingHints.VALUE_STROKE_PURE);
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(this);
		return true;
	}

	public void setFont(PSDictionary font) {
		this.font = font;
	}

	public PSDictionary font() {
		return font;
	}

	public void setFlat(double f) {
		flat = f;
	}

	public double flat() {
		return flat;
	}

	public void flattenPath() {
		PathIterator iterator = path.getPathIterator(new AffineTransform(),
				flat);
		newPath();
		path.append(iterator, true);
	}

	public void setTransfer(PSPackedArray proc) {
		// FREEHEP-164: transfer is ignored
		transfer = proc;
	}

	public PSPackedArray transfer() {
		return transfer;
	}

	private static ColorSpace toColorSpace(String name) {
		if (name.equals(Constants.DEVICE_GRAY)) {
			return ColorSpace.getInstance(ColorSpace.CS_GRAY);
		} else if (name.equals(Constants.DEVICE_RGB)) {
			return ColorSpace.getInstance(ColorSpace.CS_sRGB);
		} else if (name.equals(Constants.DEVICE_CMYK)) {
			return ColorSpace.getInstance(ColorSpace.CS_sRGB);
		}
		return null;
	}

	public boolean setColorSpace(String name) {
		return setColorSpace(name, null);
	}

	public boolean setColorSpace(String name, Object[] params) {
		ColorSpace space = toColorSpace(name);
		if (space == null) {
			if (name.equals(Constants.PATTERN)) {
				if (params == null) {
					space = new Pattern(toColorSpace(Constants.DEVICE_RGB));
				} else {
					space = new Pattern(
							toColorSpace(((PSName) params[1]).cvs()));
				}
			}
		}

		if (space != null) {
			colorSpaceName = name;
			colorSpace = space;
			return true;
		}
		return false;
	}

	public int getNumberOfColorSpaceComponents() {
		if (colorSpaceName.equals(Constants.DEVICE_GRAY)) {
			return 1;
		} else if (colorSpaceName.equals(Constants.DEVICE_RGB)) {
			return 3;
		} else if (colorSpaceName.equals(Constants.DEVICE_CMYK)) {
			return 4;
		} else if (colorSpaceName.equals(Constants.PATTERN)) {
			return colorSpace.getNumComponents();
		}
		return 0;
	}

	public String colorSpace() {
		return colorSpaceName;
	}

	private static String toColorSpaceName(ColorSpace space) {
		switch (space.getType()) {
		// FIXME: does not work for CMYK
		case ColorSpace.CS_GRAY:
			return Constants.DEVICE_GRAY;
		case ColorSpace.CS_sRGB:
			return Constants.DEVICE_RGB;
		default:
			return "Unknown";
		}
	}

	public void setColor(Paint paint) {
		setColorSpace(Constants.PATTERN);
		setColor(paint, null);
	}

	public void setColor(Paint paint, Object[] params) {
/*
		if (params != null) {
			FixedTexturePaint ftp = (FixedTexturePaint) paint;
			BufferedImage image = ftp.getImage();
			ColorConvertOp convert = new ColorConvertOp(ColorSpace
					.getInstance(ColorSpace.CS_sRGB), null);
			// SinglePixelPackedSampleModel sm =
			// (SinglePixelPackedSampleModel)image.getSampleModel();
			// sm = sm.createCompatibleSampleModel(sm.getWidth(),
			// sm.getHeight());
			// ColorModel cm = new ComponentColorModel(colorSpace, );
			// BufferedImage dstImage = convert.createCompatibleDestImage(image,
			// null);
  		    log.warning("PSGState.setColor(): "+paint.getClass()+" "+image.getSampleModel().getNumBands());
			BufferedImage filteredImage = convert.filter(image, null);
			// FIXME: copy & set this into paint
		}
*/
		log.warning("PSGState.setColor(): "+paint.getClass());
		device.getGraphics().setPaint(paint);
	}

	public void setColor(float[] color) {
		float[] rgb = toRGB(color, colorSpaceName);
		if (rgb != null) {
			device.getGraphics().setPaint(new Color(rgb[0], rgb[1], rgb[2]));
		} else {
			log.warning("Unknown colorspace: " + colorSpaceName);
		}
	}

	public static float[] toRGB(float[] color, String space) {
		if (space.equals(Constants.DEVICE_GRAY)) {
			float c = Math.max(0.0f, Math.min(1.0f, color[0]));
			return new float[] { c, c, c };

		} else if (space.equals(Constants.DEVICE_RGB)) {
			float r = Math.max(0.0f, Math.min(1.0f, color[0]));
			float g = Math.max(0.0f, Math.min(1.0f, color[1]));
			float b = Math.max(0.0f, Math.min(1.0f, color[2]));
			return new float[] { r, g, b };

		} else if (space.equals(Constants.DEVICE_CMYK)) {
			float r = Math
					.max(0.0f, 1.0f - Math.min(1.0f, color[0] + color[3]));
			float g = Math
					.max(0.0f, 1.0f - Math.min(1.0f, color[1] + color[3]));
			float b = Math
					.max(0.0f, 1.0f - Math.min(1.0f, color[2] + color[3]));
			return new float[] { r, g, b };
		}
		return null;
	}

	public float[] color() {
		return color(colorSpaceName);
	}

	public float[] color(String space) {
		float[] rgb;
		rgb = device.getGraphics().getColor().getColorComponents(null);

		if (space.equals(Constants.PATTERN)) {
			rgb = colorSpace.toRGB(rgb);
			space = toColorSpaceName(colorSpace);
		}

		rgb = toColor(space, rgb);
		if (rgb == null) {
			log.warning("Unknown colorspace: " + colorSpaceName);
		}
		return rgb;
	}

	public static float[] toColor(String space, float[] rgb) {
		float[] color;
		if (space.equals(Constants.DEVICE_GRAY)) {
			color = new float[1];
			color[0] = (float) (0.30 * rgb[0] + 0.59 * rgb[1] + 0.11 * rgb[2]);
			return color;

		} else if (space.equals(Constants.DEVICE_RGB)) {
			return rgb;

		} else if (space.equals(Constants.DEVICE_CMYK)) {
			float c = 1.0f - rgb[0];
			float m = 1.0f - rgb[1];
			float y = 1.0f - rgb[2];
			float k = Math.min(c, Math.min(m, y));

			color = new float[4];
			color[0] = Math.min(1.0f, Math.max(0.0f, c));
			color[1] = Math.min(1.0f, Math.max(0.0f, m));
			color[2] = Math.min(1.0f, Math.max(0.0f, y));
			color[3] = Math.min(1.0f, Math.max(0.0f, k));
			return color;
		} else {
			return null;
		}
	}

	public void setBlackGeneration(PSPackedArray p) {
		blackGeneration = p;
	}

	public PSPackedArray blackGeneration() {
		return blackGeneration;
	}

	public void setUnderColorRemoval(PSPackedArray p) {
		underColorRemoval = p;
	}

	public PSPackedArray underColorRemoval() {
		return underColorRemoval;
	}

	public void setBoundingBox(Rectangle2D bb) {
		if (boundingBox != null) {
			boundingBox.add(bb);
		} else {
			boundingBox = bb;
		}
	}

	public Rectangle2D boundingBox() {
		return boundingBox;
	}

	@Override
	public String getType() {
		return "gstatetype";
	}

	// FIXME: maybe not correct
	@Override
	public int hashCode() {
		return device.hashCode();
	}

	// FIXME: there may be no equal
	@Override
	public boolean equals(Object o) {
		if (o instanceof PSGState) {
			return (device == ((PSGState) o).device);
		}
		return false;
	}

	// FIXME: not implemented
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(getClass()+" Not implemented");
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return "--" + name + "--";
	}
}
