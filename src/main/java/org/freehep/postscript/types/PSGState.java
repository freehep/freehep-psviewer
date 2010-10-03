// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.postscript.Color;
import org.freehep.postscript.ColorSpace;
import org.freehep.postscript.GlyphVector;
import org.freehep.postscript.GraphicsContext;
import org.freehep.postscript.Image;
import org.freehep.postscript.NoninvertibleTransformException;
import org.freehep.postscript.Paint;
import org.freehep.postscript.Path;
import org.freehep.postscript.PathIterator;
import org.freehep.postscript.Point;
import org.freehep.postscript.Rectangle;
import org.freehep.postscript.RenderingHints;
import org.freehep.postscript.Shape;
import org.freehep.postscript.Stroke;
import org.freehep.postscript.Transform;
import org.freehep.postscript.device.ImageDevice;
import org.freehep.postscript.stacks.DictionaryStack;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.viewer.FixedTexturePaint;

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
	private Transform ctm;
	private Path path;
	private Path clipPath;
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
	private Rectangle boundingBox;

	private PSGState() {
		super("gstate", true);
	}

	public PSGState(PSDevice device, DictionaryStack dictStack) {
		super("gstate", true);
		this.device = device;

		font = new PSFontDictionary(device, device.getGraphics().getFont(), dictStack
				.systemDictionary().getArray(
						DictionaryStack.standardEncoding.getValue()));
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
		ctm = device.createTransform();
		newPath();
		initClip();
		setColorSpace(Constants.DEVICE_GRAY);
		setColor(new float[] { 0.0f });
		lineWidth = 1.0f;
		cap = Stroke.CAP_BUTT;
		join = Stroke.JOIN_MITER;
		miterLimit = 10.0f;
		dash = null;
		dashPhase = 0.0f;
		setStroke();
	}
	
	public PSDevice device() {
		return device;
	}

	public Image convertToImage(int width, int height) {
		if (!(device instanceof ImageDevice)) {
			device = device.createImageDevice(width, height);
		}
		return ((ImageDevice) device).getImage();
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
		copy.ctm = (Transform) ctm.copy();
		copy.path = (Path) path.clone();
		copy.clipPath = (clipPath == null) ? null : (Path) clipPath
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
		Stroke stroke = device.createStroke(lineWidth, cap, join, miterLimit,
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
		path = device.createPath(device.getGraphics().getStroke()
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
			charPath = device.createPath(device.getGraphics().getStroke()
					.createStrokedShape(charPath));
		}
		return charPath;
	}

	public void fill(Shape s) {
		GraphicsContext g = device.getGraphics().create();
		g.transform(ctm);
		g.fill(s);
		g.dispose();
	}

	public void stroke(Shape s) {
		stroke(s, null);
	}

	public void stroke(Shape s, Transform m) {
		Transform at = ctm.copy();
		Path p = device.createPath(s);

		if (m != null) {
			// apply AT-1 to s
			try {
				Transform pt = m.createInverse();
				p.transform(pt);
			} catch (NoninvertibleTransformException e) {
				log.warning(Constants.INTERNAL_GSTATE_ERROR);
			}

			// add new transform
			at.concatenate(m);
		}

		GraphicsContext g = device.getGraphics().create();
		g.transform(at);
		g.draw(p);
		g.dispose();
	}

	public void show(GlyphVector gv, float x, float y) {
		GraphicsContext g = device.getGraphics().create();
		g.transform(ctm);

		g.drawGlyphVector(gv, x, y);
		g.dispose();
	}

	public void image(Image image, Transform at) {
		GraphicsContext g = device.getGraphics().create();
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

	public Point position() {
		return (path == null) ? null : path.getCurrentPoint();
	}

	public void translate(double tx, double ty) {
		Transform at = device.createTransform();
		at.setToTranslation(-tx, -ty);
		path.transform(at);
		ctm.translate(tx, ty);
	}

	public void rotate(double angle) {
		Transform at = device.createTransform();
		at.setToRotation(-angle);
		path.transform(at);
		ctm.rotate(angle);		
	}

	public void scale(double sx, double sy) {
		Transform at = device.createTransform();
		at.setToScale(1 / sx, 1 / sy);
		path.transform(at);
		ctm.scale(sx, sy);
	}

	public void setTransform(Transform at) {

		// apply AT-1 * CTM to path
		try {
			Transform pt = at.createInverse();
			pt.concatenate(ctm);
			path.transform(pt);
		} catch (NoninvertibleTransformException e) {
			log.warning(Constants.INTERNAL_GSTATE_ERROR);
		}

		// set new transform
		ctm.setTransform(at);
	}

	public void transform(Transform at) {

		// apply AT-1 to path
		try {
			Transform pt = at.createInverse();
			path.transform(pt);
		} catch (NoninvertibleTransformException e) {
			log.warning(Constants.INTERNAL_GSTATE_ERROR);
		}

		// add new transform
		ctm.concatenate(at);
	}

	public Transform getTransform() {
		return ctm.copy();
	}

	public Path path() {
		return path;
	}

	public Path newPath() {
		path = device.createPath();
		path.transform(ctm);
		return path;
	}

	public void initClip() {
		device.getGraphics().setClip(null);
		try {
			Transform inverse = ctm.createInverse();

			clipPath = device.createPath(device.createRectangle(0, 0, device
					.getWidth(), device.getHeight()));
			clipPath.transform(inverse);
		} catch (NoninvertibleTransformException e) {
			log.warning("Internal error in GState");
		}
	}

	public void clip(Shape p) {
		clipPath = device.createPath(p);
		Shape clip = clipPath.createTransformedShape(ctm);
		device.getGraphics().setClip(clip);
	}

	public void clipPath() {
		path = clipPath.clone();
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
		PathIterator iterator = path.getPathIterator(device.createTransform(),
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

	private ColorSpace toColorSpace(String name) {
		if (name.equals(Constants.DEVICE_GRAY)) {
			return device.createColorSpace(ColorSpace.CS_GRAY);
		} else if (name.equals(Constants.DEVICE_RGB)) {
			return device.createColorSpace(ColorSpace.CS_sRGB);
		} else if (name.equals(Constants.DEVICE_CMYK)) {
			return device.createColorSpace(ColorSpace.CS_sRGB);
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
		if (params != null && params.length > 0) {
			// rebuild paint with new color
			Color c;
			switch (params.length) {
			case 1:
				float d = ((PSNumber)params[0]).getFloat();
				c = device.createColor(d, d, d);
				break;
			case 3:
				float r = ((PSNumber)params[0]).getFloat();
				float g = ((PSNumber)params[0]).getFloat();
				float b = ((PSNumber)params[0]).getFloat();
				c = device.createColor(r, g, b);
				break;
			default:
				log.warning("Number of params not handled " + params.length);
				c = device.createColor(0.0f, 0.0f, 0.0f);	// BLACK
				break;
			}
			paint = ((FixedTexturePaint) paint).inColor(c);
		}
		device.getGraphics().setPaint(paint);
	}

	public void setColor(float[] color) {
		float[] rgb = toRGB(color, colorSpaceName);
		if (rgb != null) {
			device.getGraphics().setPaint(device.createColor(rgb[0], rgb[1], rgb[2]));
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

	public void setBoundingBox(Rectangle bb) {
		if (boundingBox != null) {
			boundingBox.add(bb);
		} else {
			boundingBox = bb;
		}
	}

	public Rectangle boundingBox() {
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
		throw new CloneNotSupportedException(getClass() + " Not implemented");
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
