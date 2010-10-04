package org.freehep.postscript.awt;

import java.awt.Container;
import java.awt.geom.AffineTransform;

import org.freehep.postscript.Arc;
import org.freehep.postscript.Color;
import org.freehep.postscript.ColorModel;
import org.freehep.postscript.ColorSpace;
import org.freehep.postscript.Font;
import org.freehep.postscript.FontRenderContext;
import org.freehep.postscript.GraphicsEnvironment;
import org.freehep.postscript.Image;
import org.freehep.postscript.PaintContext;
import org.freehep.postscript.Path;
import org.freehep.postscript.Point;
import org.freehep.postscript.Rectangle;
import org.freehep.postscript.RenderingHints;
import org.freehep.postscript.Shape;
import org.freehep.postscript.Stroke;
import org.freehep.postscript.Transform;
import org.freehep.postscript.device.ImageDevice;
import org.freehep.postscript.device.PanelDevice;
import org.freehep.postscript.viewer.FixedTexturePaint;

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
	public Image createImage(int width, int height, String imageType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stroke createStroke(float lineWidth, int cap, int join,
			float miterLimit, float[] dash, float dashPhase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path createPath(Shape shape) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path createPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle createRectangle(double x, double y, double width,
			double height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Arc createArc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Arc createArc(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point createPoint(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transform createTransform(double[] m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColorSpace createColorSpace(int csGray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageDevice createImageDevice(int width, int height) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphicsEnvironment getLocalGraphicsEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Font createFont(String name, int type, float ptSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontRenderContext createFontRenderContext(Object object,
			boolean antiAliasing, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle userBounds, Transform at, RenderingHints hints) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FixedTexturePaint createTexturePaint(Image changeColor,
			Rectangle anchor) {
		// TODO Auto-generated method stub
		return null;
	}

}
