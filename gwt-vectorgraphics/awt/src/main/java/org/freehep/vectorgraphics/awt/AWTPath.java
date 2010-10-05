package org.freehep.vectorgraphics.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

import org.freehep.vectorgraphics.Path;
import org.freehep.vectorgraphics.PathIterator;
import org.freehep.vectorgraphics.Point;
import org.freehep.vectorgraphics.Rectangle;
import org.freehep.vectorgraphics.Shape;
import org.freehep.vectorgraphics.Transform;

public class AWTPath implements Path {

	private Path2D p;
	
	public AWTPath(Path2D p) {
		this.p = p;
	}

	public AWTPath() {
		p = new GeneralPath();
	}
	
	public AWTPath(Shape s) {
		p = new GeneralPath(((AWTShape)s).getShape());
	}

	@Override
	public PathIterator getPathIterator() {
		return new AWTPathIterator(p.getPathIterator(new AffineTransform()));
	}

	@Override
	public PathIterator getPathIterator(Transform t, double flatness) {
		return new AWTPathIterator(p.getPathIterator(((AWTTransform)t).getTransform(), flatness));
	}

	@Override
	public PathIterator getPathIterator(Transform t) {
		return new AWTPathIterator(p.getPathIterator(((AWTTransform)t).getTransform()));
	}

	@Override
	public Rectangle getBounds() {
		return new AWTRectangle(p.getBounds2D());
	}

	@Override
	public void transform(Transform t) {
		p.transform(((AWTTransform)t).getTransform());
	}

	@Override
	public Point getCurrentPoint() {
		return new AWTPoint(p.getCurrentPoint());
	}

	@Override
	public void lineTo(float x, float y) {
		p.lineTo(x, y);
	}

	@Override
	public void moveTo(float x, float y) {
		p.moveTo(x, y);
	}

	@Override
	public void curveTo(float cx1, float cy1, float cx2, float cy2, float x,
			float y) {
		p.curveTo(cx1, cy1, cx2, cy2, x, y);
	}

	@Override
	public void closePath() {
		p.closePath();
	}

	@Override
	public Shape createTransformedShape(Transform t) {
		return new AWTShape(p.createTransformedShape(((AWTTransform)t).getTransform()));
	}

	@Override
	public void setWindingRule(int rule) {
		p.setWindingRule(rule);
	}

	@Override
	public void append(Shape s, boolean connect) {
		p.append(((AWTShape)s).getShape(), connect);
	}

	@Override
	public void append(PathIterator iterator, boolean connect) {
		p.append(((AWTPathIterator)iterator).getPathIterator(), connect);
	}


	@Override
	public Path copy() {
		return new AWTPath((Path2D)p.clone());
	}

}
