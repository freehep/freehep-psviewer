package org.freehep.postscript.awt;

import java.awt.Container;
import java.awt.geom.AffineTransform;

import org.freehep.postscript.device.ImageDevice;
import org.freehep.postscript.device.PanelDevice;
import org.freehep.postscript.operators.FixedTexturePaint;
import org.freehep.vectorgraphics.Arc;
import org.freehep.vectorgraphics.Color;
import org.freehep.vectorgraphics.ColorModel;
import org.freehep.vectorgraphics.ColorSpace;
import org.freehep.vectorgraphics.Font;
import org.freehep.vectorgraphics.FontRenderContext;
import org.freehep.vectorgraphics.GraphicsEnvironment;
import org.freehep.vectorgraphics.Image;
import org.freehep.vectorgraphics.PaintContext;
import org.freehep.vectorgraphics.Path;
import org.freehep.vectorgraphics.Point;
import org.freehep.vectorgraphics.Rectangle;
import org.freehep.vectorgraphics.RenderingHints;
import org.freehep.vectorgraphics.Shape;
import org.freehep.vectorgraphics.Stroke;
import org.freehep.vectorgraphics.Transform;
import org.freehep.vectorgraphics.awt.AWTArc;
import org.freehep.vectorgraphics.awt.AWTColor;
import org.freehep.vectorgraphics.awt.AWTColorSpace;
import org.freehep.vectorgraphics.awt.AWTContainer;
import org.freehep.vectorgraphics.awt.AWTFont;
import org.freehep.vectorgraphics.awt.AWTFontRenderContext;
import org.freehep.vectorgraphics.awt.AWTGraphicsEnvironment;
import org.freehep.vectorgraphics.awt.AWTImage;
import org.freehep.vectorgraphics.awt.AWTPaintContext;
import org.freehep.vectorgraphics.awt.AWTPath;
import org.freehep.vectorgraphics.awt.AWTPoint;
import org.freehep.vectorgraphics.awt.AWTRectangle;
import org.freehep.vectorgraphics.awt.AWTStroke;
import org.freehep.vectorgraphics.awt.AWTTransform;

public class AWTPanelDevice extends PanelDevice {

	public AWTPanelDevice(Container container) {
		super(new AWTContainer(container));
	}

	@Override
	public Transform createTransform() {
		return new AWTTransform(new AffineTransform());
	}

	@Override
	public Transform createTransform(double m00, double m10, double m01,
			double m11, double m02, double m12) {
		return new AWTTransform(m00, m10, m01, m11, m02, m12);
	}

	@Override
	public Color createColor(float r, float g, float b) {
		return new AWTColor(r, g, b);
	}

	@Override
	public Image createImage(int width, int height, int type) {
		return new AWTImage(width, height, type);
	}

	@Override
	public Stroke createStroke(float lineWidth, int cap, int join,
			float miterLimit, float[] dash, float dashPhase) {
		return new AWTStroke(lineWidth, cap, join, miterLimit, dash, dashPhase);
	}

	@Override
	public Path createPath(Shape shape) {
		return new AWTPath(shape);
	}

	@Override
	public Path createPath() {
		return new AWTPath();
	}

	@Override
	public Rectangle createRectangle(double x, double y, double width,
			double height) {
		return new AWTRectangle(x, y, width, height);
	}

	@Override
	public Arc createArc() {
		return new AWTArc();
	}

	@Override
	public Arc createArc(int type) {
		return new AWTArc(type);
	}

	@Override
	public Point createPoint(double x, double y) {
		return new AWTPoint(x, y);
	}

	@Override
	public Transform createTransform(double[] m) {
		return new AWTTransform(m);
	}

	@Override
	public ColorSpace createColorSpace(int type) {
		return new AWTColorSpace(type);
	}

	@Override
	public ImageDevice createImageDevice(int width, int height) {
// TODO
		return null;
	}

	@Override
	public GraphicsEnvironment getLocalGraphicsEnvironment() {
		return new AWTGraphicsEnvironment();
	}

	@Override
	public Font createFont(String name, int type, float ptSize) {
		return new AWTFont(name, type, ptSize);
	}

	@Override
	public FontRenderContext createFontRenderContext(Transform t,
			boolean isAntiAliased, boolean usesFractionalMetrics) {
		return new AWTFontRenderContext(t, isAntiAliased, usesFractionalMetrics);
	}

	@Override
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle userBounds, Transform at, RenderingHints hints) {
		return new AWTPaintContext(cm, deviceBounds, userBounds, at, hints);
	}

	@Override
	public FixedTexturePaint createTexturePaint(Image image,
			Rectangle anchor) {
		// TODO
		return null;
	}

}
