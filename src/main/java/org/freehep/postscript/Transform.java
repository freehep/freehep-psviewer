package org.freehep.postscript;

public interface Transform {

	Transform copy();

	Transform createInverse() throws NoninvertibleTransformException;

	// TODO
	Point inverseTransform(Point createPoint, Object todo) throws NoninvertibleTransformException;

	void concatenate(Transform m);

	Transform getTranslateInstance(double tx, double ty);

	void translate(double tx, double ty);

	void rotate(double angle);

	void scale(double sx, double sy);

	void setTransform(Transform at);

	void getMatrix(double[] m);

	double getScaleX();

	double getScaleY();

	// TODO
	Point deltaTransform(Point createPoint, Object object);
	Point transform(Point createPoint, Object object);

	void setToTranslation(double tx, double ty);

	void setToRotation(double theta);

	void setToScale(double sx, double sy);


}
