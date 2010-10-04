package org.freehep.postscript.awt;

import java.awt.geom.AffineTransform;

import org.freehep.postscript.NoninvertibleTransformException;
import org.freehep.postscript.Point;
import org.freehep.postscript.Transform;

public class AWTTransform implements Transform {

	private static final long serialVersionUID = 1L;

	private AffineTransform t;
	
	public AWTTransform(AffineTransform transform) {
		t = transform;
	}

	public AWTTransform(double m00, double m10, double m01, double m11,
			double m02, double m12) {
		t = new AffineTransform(m00, m10, m01, m11, m02, m12);
	}

	public AffineTransform getTransform() {
		return t;
	}

	@Override
	public Transform createInverse() throws NoninvertibleTransformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point inverseTransform(Point createPoint, Object todo)
			throws NoninvertibleTransformException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void concatenate(Transform m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transform getTranslateInstance(double tx, double ty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void translate(double tx, double ty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(double angle) {
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
	public Point deltaTransform(Point createPoint, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point transform(Point createPoint, Object object) {
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
	public Transform copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void preConcatenate(Transform xform) {
		// TODO Auto-generated method stub
	}	
}
