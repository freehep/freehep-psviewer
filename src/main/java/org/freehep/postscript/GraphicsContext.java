// Copyright FreeHEP 2010
package org.freehep.postscript;

/**
 * 
 * @author duns
 */
public interface GraphicsContext {

	void setBackground(Color createColor);

	void setTransform(Transform deviceTransform);

	void clearRect(int x, int y, int width, int height);

	void transform(Transform transform);

	GraphicsContext create();

	Color getColor();

	Font getFont();

	Object getRenderingHint(RenderingHintKey key);

	void setRenderingHint(RenderingHintKey key, Object value);

	Stroke getStroke();

	void setClip(Shape shape);

	void setPaint(Color color);

	void setPaint(Paint paint);

	void setStroke(Stroke stroke);

	void fill(Shape s);

	void dispose();

	void draw(Shape s);

	void drawRenderedImage(Image image, Transform at);

	void drawGlyphVector(GlyphVector gv, float x, float y);

}
