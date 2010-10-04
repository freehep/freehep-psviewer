package org.freehep.vectorgraphics.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.freehep.vectorgraphics.NoninvertibleTransformException;
import org.freehep.vectorgraphics.Point;
import org.freehep.vectorgraphics.Transform;

public class AWTTransform implements Transform {

	private AffineTransform t;

	public AWTTransform(AffineTransform transform) {
		t = transform;
	}

	public AWTTransform(double m00, double m10, double m01, double m11,
			double m02, double m12) {
		t = new AffineTransform(m00, m10, m01, m11, m02, m12);
	}

	public AWTTransform(double[] m) {
		t = new AffineTransform(m);
	}

	public AffineTransform getTransform() {
		return t;
	}

	@Override
	public Transform createInverse() throws NoninvertibleTransformException {
		try {
			return new AWTTransform(t.createInverse());
		} catch (java.awt.geom.NoninvertibleTransformException e) {
			throw new NoninvertibleTransformException(e.getMessage(),
					e.getCause());
		}
	}

	@Override
	public Point inverseTransform(Point p1, Point p2)
			throws NoninvertibleTransformException {
		try {
			return new AWTPoint(t.inverseTransform(((AWTPoint) p1).getPoint(),
					((AWTPoint) p2).getPoint()));
		} catch (java.awt.geom.NoninvertibleTransformException e) {
			throw new NoninvertibleTransformException(e.getMessage(),
					e.getCause());
		}
	}

	@Override
	public void concatenate(Transform m) {
		t.concatenate(((AWTTransform)m).getTransform());
	}

	@Override
	public void translate(double tx, double ty) {
		t.translate(tx, ty);
	}

	@Override
	public void rotate(double theta) {
		t.rotate(theta);
	}

	@Override
	public void scale(double sx, double sy) {
		t.scale(sx, sy);
	}

	@Override
	public void setTransform(Transform at) {
		t.setTransform(((AWTTransform)at).getTransform());
	}

	@Override
	public void getMatrix(double[] m) {
		t.getMatrix(m);
	}

	@Override
	public double getScaleX() {
		return t.getScaleX();
	}

	@Override
	public double getScaleY() {
		return t.getScaleY();
	}

	@Override
	public Point deltaTransform(Point src, Point dst) {
		Point2D p = dst == null ?  null : ((AWTPoint)dst).getPoint();
		Point2D r = t.deltaTransform(((AWTPoint)src).getPoint(), p);
		return p == null ? new AWTPoint(r) : dst;
	}

	@Override
	public Point transform(Point src, Point dst) {
		Point2D p = dst == null ?  null : ((AWTPoint)dst).getPoint();
		Point2D r = t.transform(((AWTPoint)src).getPoint(), p);
		return p == null ? new AWTPoint(r) : dst;
	}

	@Override
	public void setToTranslation(double tx, double ty) {
		t.setToTranslation(tx, ty);
	}

	@Override
	public void setToRotation(double theta) {
		t.setToRotation(theta);
	}

	@Override
	public void setToScale(double sx, double sy) {
		t.setToScale(sx, sy);
	}

	@Override
	public Transform copy() {
		return new AWTTransform((AffineTransform)t.clone());
	}

	@Override
	public void preConcatenate(Transform m) {
		t.preConcatenate(((AWTTransform)m).getTransform());
	}
}
