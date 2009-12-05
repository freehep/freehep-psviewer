// Copyright 2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Delegates all calls (except create) to hostGraphics. You can now overload one of them and do
 * something special.
 * 
 * @author Mark Donszelmann
 */
public abstract class DelegateGraphics2D extends Graphics2D {

	// The host graphics context.
	protected Graphics2D hostGraphics;

	public DelegateGraphics2D(Graphics2D graphics) {
		hostGraphics = graphics;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#create()
	 */
	@Override
	public abstract Graphics create();
	
	/* (non-Javadoc)
	 * @see java.awt.Graphics#create(int, int, int, int)
	 */
	@Override
	public abstract Graphics create(int x, int y, int width, int height);

	@Override
	public void clearRect(int x, int y, int width, int height) {
		hostGraphics.clearRect(x, y, width, height);
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		hostGraphics.clipRect(x, y, width, height);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		hostGraphics.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public void dispose() {
		hostGraphics.dispose();
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		hostGraphics.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor,
			ImageObserver observer) {
		return hostGraphics.drawImage(img, x, y, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return hostGraphics.drawImage(img, x, y, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		return hostGraphics.drawImage(img, x, y, width, height, bgcolor,
				observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height,
			ImageObserver observer) {
		return hostGraphics.drawImage(img, x, y, width, height, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor,
			ImageObserver observer) {
		return hostGraphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2,
				sy2, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		return hostGraphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2,
				sy2, observer);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		hostGraphics.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		hostGraphics.drawOval(x, y, width, height);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		hostGraphics.drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawPolygon(Polygon p) {
		hostGraphics.drawPolygon(p);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		hostGraphics.drawPolyline(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		hostGraphics.drawRect(x, y, width, height);
	}

	@Override
	public void drawString(String str, int x, int y) {
		hostGraphics.drawString(str, x, y);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		hostGraphics.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		hostGraphics.fillOval(x, y, width, height);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		hostGraphics.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void fillPolygon(Polygon p) {
		hostGraphics.fillPolygon(p);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		hostGraphics.fillRect(x, y, width, height);
	}

	@Override
	public Shape getClip() {
		return hostGraphics.getClip();
	}

	@Override
	public Rectangle getClipBounds() {
		return hostGraphics.getClipBounds();
	}

	@Override
	public Rectangle getClipBounds(Rectangle r) {
		return hostGraphics.getClipBounds(r);
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return hostGraphics.getFontMetrics(f);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		hostGraphics.setClip(x, y, width, height);
	}

	@Override
	public void setClip(Shape clip) {
		hostGraphics.setClip(clip);
	}

	@Override
	public void setFont(Font font) {
		hostGraphics.setFont(font);
	}

	@Override
	public void setColor(Color color) {
		hostGraphics.setColor(color);
	}

	@Override
	public void setPaint(Paint paint) {
		hostGraphics.setPaint(paint);
	}

	@Override
	public void setPaintMode() {
		hostGraphics.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		hostGraphics.setXORMode(c1);
	}

	@Override
	public void translate(int x, int y) {
		hostGraphics.translate(x, y);
	}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		hostGraphics.addRenderingHints(hints);
	}

	@Override
	public void clip(Shape clip) {
		hostGraphics.clip(clip);
	}

	@Override
	public void draw(Shape s) {
		hostGraphics.draw(s);
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		hostGraphics.drawGlyphVector(g, x, y);
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		hostGraphics.drawImage(img, op, x, y);
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return hostGraphics.drawImage(img, xform, obs);
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		hostGraphics.drawRenderableImage(img, xform);
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		hostGraphics.drawRenderedImage(img, xform);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x,
			float y) {
		hostGraphics.drawString(iterator, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		hostGraphics.drawString(iterator, x, y);
	}

	@Override
	public void drawString(String str, float x, float y) {
		hostGraphics.drawString(str, x, y);
	}

	@Override
	public void fill(Shape s) {
		hostGraphics.fill(s);
	}

	@Override
	public Composite getComposite() {
		return hostGraphics.getComposite();
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return hostGraphics.getDeviceConfiguration();
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return hostGraphics.getFontRenderContext();
	}

	@Override
	public Object getRenderingHint(RenderingHints.Key hintKey) {
		return hostGraphics.getRenderingHint(hintKey);
	}

	@Override
	public RenderingHints getRenderingHints() {
		return hostGraphics.getRenderingHints();
	}

	@Override
	public Stroke getStroke() {
		return hostGraphics.getStroke();
	}

	@Override
	public AffineTransform getTransform() {
		return hostGraphics.getTransform();
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return hostGraphics.hit(rect, s, onStroke);
	}

	@Override
	public void rotate(double theta) {
		hostGraphics.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		hostGraphics.rotate(theta, x, y);
	}

	@Override
	public void scale(double sx, double sy) {
		hostGraphics.scale(sx, sy);
	}

	@Override
	public void setBackground(Color color) {
		hostGraphics.setBackground(color);
	}

	@Override
	public void setComposite(Composite comp) {
		hostGraphics.setComposite(comp);
	}

	@Override
	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
		hostGraphics.setRenderingHint(hintKey, hintValue);
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		hostGraphics.setRenderingHints(hints);
	}

	@Override
	public void setStroke(Stroke s) {
		hostGraphics.setStroke(s);
	}

	@Override
	public void setTransform(AffineTransform t) {
		hostGraphics.setTransform(t);
	}

	@Override
	public void shear(double shx, double shy) {
		hostGraphics.shear(shx, shy);
	}

	@Override
	public void transform(AffineTransform t) {
		hostGraphics.transform(t);
	}

	@Override
	public void translate(double tx, double ty) {
		hostGraphics.translate(tx, ty);
	}

	@Override
	public String toString() {
		return "DelegateGraphics2D[" + hostGraphics.toString() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getBackground()
	 */
	@Override
	public Color getBackground() {
		return hostGraphics.getBackground();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getPaint()
	 */
	@Override
	public Paint getPaint() {
		return hostGraphics.getPaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawRoundRect(int, int, int, int, int, int)
	 */
	@Override
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		hostGraphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#fillRoundRect(int, int, int, int, int, int)
	 */
	@Override
	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		hostGraphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#getColor()
	 */
	@Override
	public Color getColor() {
		return hostGraphics.getColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#getFont()
	 */
	@Override
	public Font getFont() {
		return hostGraphics.getFont();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#draw3DRect(int, int, int, int, boolean)
	 */
	@Override
	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		hostGraphics.draw3DRect(x, y, width, height, raised);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawBytes(byte[], int, int, int, int)
	 */
	@Override
	public void drawBytes(byte[] data, int offset, int length, int x, int y) {
		hostGraphics.drawBytes(data, offset, length, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawChars(char[], int, int, int, int)
	 */
	@Override
	public void drawChars(char[] data, int offset, int length, int x, int y) {
		hostGraphics.drawChars(data, offset, length, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#fill3DRect(int, int, int, int, boolean)
	 */
	@Override
	public void fill3DRect(int x, int y, int width, int height, boolean raised) {
		hostGraphics.fill3DRect(x, y, width, height, raised);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#getClipRect()
	 */
	@Override
	@Deprecated
	public Rectangle getClipRect() {
		return hostGraphics.getClipRect();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Graphics#getFontMetrics()
	 */
	@Override
	public FontMetrics getFontMetrics() {
		return hostGraphics.getFontMetrics();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Graphics#hitClip(int, int, int, int)
	 */
	@Override
	public boolean hitClip(int x, int y, int width, int height) {
		return hostGraphics.hitClip(x, y, width, height);
	}
	
	
}
