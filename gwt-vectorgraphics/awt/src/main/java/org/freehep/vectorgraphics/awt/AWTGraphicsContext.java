// Copyright FreeHEP 2010
package org.freehep.vectorgraphics.awt;

import java.awt.Graphics;
import java.awt.Graphics2D;

import org.freehep.vectorgraphics.Color;
import org.freehep.vectorgraphics.Font;
import org.freehep.vectorgraphics.GlyphVector;
import org.freehep.vectorgraphics.GraphicsContext;
import org.freehep.vectorgraphics.Image;
import org.freehep.vectorgraphics.Paint;
import org.freehep.vectorgraphics.RenderingHintKey;
import org.freehep.vectorgraphics.Shape;
import org.freehep.vectorgraphics.Stroke;
import org.freehep.vectorgraphics.Transform;

public class AWTGraphicsContext implements GraphicsContext {
	Graphics2D g;

	public AWTGraphicsContext(Graphics2D g) {
		this.g = g;
	}

	public AWTGraphicsContext(Graphics g) {
		this.g = (Graphics2D) g;
	}

	@Override
	public void setBackground(Color color) {
		g.setBackground(((AWTColor)color).getColor());
	}

	@Override
	public void setTransform(Transform transform) {
		g.setTransform(((AWTTransform)transform).getTransform());
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		g.clearRect(x, y, width, height);
	}

	@Override
	public void transform(Transform transform) {
		g.transform(((AWTTransform)transform).getTransform());
	}

	@Override
	public GraphicsContext create() {
		return new AWTGraphicsContext(g.create());
	}

	@Override
	public Color getColor() {
		return new AWTColor(g.getColor());
	}

	@Override
	public Font getFont() {
		return new AWTFont(g.getFont());
	}

	@Override
	public Object getRenderingHint(RenderingHintKey key) {
		return g.getRenderingHint(((AWTRenderingHintKey)key).getKey());
	}

	@Override
	public void setRenderingHint(RenderingHintKey key, Object value) {
		g.setRenderingHint(((AWTRenderingHintKey)key).getKey(), value);
	}

	@Override
	public Stroke getStroke() {
		return new AWTStroke(g.getStroke());
	}

	@Override
	public void setClip(Shape shape) {
		g.setClip(((AWTShape)shape).getShape());
	}

	@Override
	public void setPaint(Color color) {
		g.setPaint(((AWTColor)color).getColor());
	}

	@Override
	public void setPaint(Paint paint) {
		g.setPaint(((AWTPaint)paint).getPaint());
	}

	@Override
	public void setStroke(Stroke stroke) {
		g.setStroke(((AWTStroke)stroke).getStroke());
	}

	@Override
	public void fill(Shape s) {
		g.fill(((AWTShape)s).getShape());
	}

	@Override
	public void dispose() {
		g.dispose();
	}

	@Override
	public void draw(Shape s) {
		g.draw(((AWTShape)s).getShape());
	}

	@Override
	public void drawRenderedImage(Image i, Transform t) {
		g.drawRenderedImage(((AWTImage)i).getImage(), ((AWTTransform)t).getTransform());
	}

	@Override
	public void drawGlyphVector(GlyphVector gv, float x, float y) {
		g.drawGlyphVector(((AWTGlyphVector)gv).getGlyphVector(), x, y);
	}
}
