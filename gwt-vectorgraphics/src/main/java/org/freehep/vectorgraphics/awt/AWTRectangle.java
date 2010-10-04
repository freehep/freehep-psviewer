package org.freehep.vectorgraphics.awt;

import java.awt.geom.Rectangle2D;

import org.freehep.vectorgraphics.Rectangle;

public class AWTRectangle extends AWTShape implements Rectangle {

	public AWTRectangle(Rectangle2D r) {
		super(r);
	}

	public AWTRectangle(double x, double y, double width, double height) {
		super(new Rectangle2D.Double(x, y, width, height));
	}

	@Override
	public void add(Rectangle b) {
		((Rectangle2D)s).add(((AWTRectangle)b).getRectangle());
	}

	@Override
	public double getMinX() {
		return ((Rectangle2D)s).getMinX();
	}

	@Override
	public double getMinY() {
		return ((Rectangle2D)s).getMinY();
	}

	@Override
	public double getMaxX() {
		return ((Rectangle2D)s).getMaxX();
	}

	@Override
	public double getMaxY() {
		return ((Rectangle2D)s).getMaxY();
	}

	public Rectangle2D getRectangle() {
		return ((Rectangle2D)s);
	}
}
