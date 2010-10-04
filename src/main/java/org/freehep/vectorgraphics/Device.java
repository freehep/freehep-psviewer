package org.freehep.vectorgraphics;

import org.freehep.postscript.device.ImageDevice;

public interface Device {
	Transform getDeviceTransform();

	GraphicsContext getDeviceGraphics();

	double getWidth();

	double getHeight();

	void refresh();

	ImageDevice createImageDevice(int width, int height);

	Transform createTransform();

	Transform createTransform(double m00, double m10, double m01, double m11,
			double m02, double m12);

	Color createColor(float f, float g, float h);

	Image createImage(int width, int height, int type);

	Stroke createStroke(float lineWidth, int cap, int join, float miterLimit,
			float[] dash, float dashPhase);

	Path createPath(Shape shape);

	Path createPath();

	Rectangle createRectangle(double x, double y, double width, double height);

	Arc createArc();

	Arc createArc(int type);

	Point createPoint(double x, double y);

	Transform createTransform(double[] m);

	ColorSpace createColorSpace(int csGray);

	GraphicsEnvironment getLocalGraphicsEnvironment();

	Font createFont(String name, int type, float ptSize);

	FontRenderContext createFontRenderContext(Transform t,
			boolean isAntiAliased, boolean useFractionalMetrics);

    PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle userBounds, Transform at, RenderingHints hints);

}
