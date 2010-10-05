package org.freehep.vectorgraphics;

public interface Transform {

	Transform copy();

	Transform createInverse() throws NoninvertibleTransformException;

	Point inverseTransform(Point createPoint, Point todo) throws NoninvertibleTransformException;

	void concatenate(Transform m);

	void translate(double tx, double ty);

	void rotate(double theta);

	void scale(double sx, double sy);

	void setTransform(Transform at);

	void getMatrix(double[] m);

	double getScaleX();

	double getScaleY();

	Point deltaTransform(Point src, Point dst);
	
	Point transform(Point src, Point dst);

	void setToTranslation(double tx, double ty);

	void setToRotation(double theta);

	void setToScale(double sx, double sy);

	void preConcatenate(Transform xform);

}
