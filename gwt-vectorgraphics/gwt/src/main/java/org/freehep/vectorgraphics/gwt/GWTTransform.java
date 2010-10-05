package org.freehep.vectorgraphics.gwt;

import org.freehep.vectorgraphics.NoninvertibleTransformException;
import org.freehep.vectorgraphics.Point;
import org.freehep.vectorgraphics.Transform;

public class GWTTransform implements Transform {

	@Override
	public Transform copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transform createInverse() throws NoninvertibleTransformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point inverseTransform(Point createPoint, Point todo)
			throws NoninvertibleTransformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void concatenate(Transform m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void translate(double tx, double ty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotate(double theta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scale(double sx, double sy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTransform(Transform at) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getMatrix(double[] m) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getScaleX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getScaleY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point deltaTransform(Point src, Point dst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point transform(Point src, Point dst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setToTranslation(double tx, double ty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setToRotation(double theta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setToScale(double sx, double sy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preConcatenate(Transform xform) {
		// TODO Auto-generated method stub

	}

}
