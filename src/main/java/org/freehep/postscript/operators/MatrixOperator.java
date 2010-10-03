// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.NoninvertibleTransformException;
import org.freehep.postscript.Point;
import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSNumber;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSPackedArray;

/**
 * Matrix Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class MatrixOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { Matrix.class,
				InitMatrix.class, IdentMatrix.class, DefaultMatrix.class,
				CurrentMatrix.class, SetMatrix.class, Translate.class,
				Scale.class, Rotate.class, Concat.class, ConcatMatrix.class,
				Transform.class, DTransform.class, ITransform.class,
				IDTransform.class, InvertMatrix.class });
	}
}

class Matrix extends MatrixOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(new PSArray(new double[] { 1, 0, 0, 1, 0, 0 }));
		return true;
	}
}

class InitMatrix extends MatrixOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().setTransform(os.gstate().device().createTransform());

		return true;
	}
}

class IdentMatrix extends MatrixOperator {
	{
		operandTypes = new Class[] { PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray a = os.popArray();
		a.set(new double[] { 1, 0, 0, 1, 0, 0 });
		os.push(a);
		return true;
	}
}

class DefaultMatrix extends MatrixOperator {
	{
		operandTypes = new Class[] { PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray a = os.popArray();
		double[] d = { 1, 0, 0, 1, 0, 0 };
		a.set(d);
		os.push(a);
		return true;
	}
}

class CurrentMatrix extends MatrixOperator {
	{
		operandTypes = new Class[] { PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray a = os.popArray();
		if (a.size() < 6) {
			error(os, new RangeCheck());
		} else {
			org.freehep.postscript.Transform ctm = os.gstate().getTransform();
			double[] m = new double[6];
			ctm.getMatrix(m);
			a.set(m);
			os.push(a);
		}
		return true;
	}
}

class SetMatrix extends MatrixOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSPackedArray a = os.popPackedArray();
		org.freehep.postscript.Transform ctm = os.gstate().device().createTransform(a.toDoubles());
		os.gstate().setTransform(ctm);

		return true;
	}
}

class Translate extends MatrixOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			double ty = os.popNumber().getDouble();
			double tx = os.popNumber().getDouble();
			os.gstate().translate(tx, ty);
		} else if (os.checkType(PSNumber.class, PSNumber.class, PSArray.class)) {
			PSArray a = os.popArray();
			if (a.size() >= 6) {
				PSNumber ty = os.popNumber();
				PSNumber tx = os.popNumber();
				a.set(0, 1);
				a.set(1, 0);
				a.set(2, 0);
				a.set(3, 1);
				a.set(4, tx);
				a.set(5, ty);
				os.push(a);
			} else {
				error(os, new RangeCheck());
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Scale extends MatrixOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			double sy = os.popNumber().getDouble();
			double sx = os.popNumber().getDouble();
			os.gstate().scale(sx, sy);
		} else if (os.checkType(PSNumber.class, PSNumber.class, PSArray.class)) {
			PSArray a = os.popArray();
			if (a.size() >= 6) {
				PSNumber sy = os.popNumber();
				PSNumber sx = os.popNumber();
				a.set(0, sx);
				a.set(1, 0);
				a.set(2, 0);
				a.set(3, sy);
				a.set(4, 0);
				a.set(5, 0);
				os.push(a);
			} else {
				error(os, new RangeCheck());
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Rotate extends MatrixOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class)) {
			double angle = os.popNumber().getDouble();
			os.gstate().rotate(Math.toRadians(angle));
		} else if (os.checkType(PSNumber.class, PSArray.class)) {
			PSArray a = os.popArray();
			if (a.size() >= 6) {
				double theta = Math.toRadians(os.popNumber().getDouble());
				double sinTheta = Math.sin(theta);
				double cosTheta = Math.cos(theta);
				a.set(0, cosTheta);
				a.set(1, sinTheta);
				a.set(2, -sinTheta);
				a.set(3, cosTheta);
				a.set(4, 0);
				a.set(5, 0);
				os.push(a);
			} else {
				error(os, new RangeCheck());
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Concat extends MatrixOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		org.freehep.postscript.Transform m = os.gstate().device().createTransform(os.popPackedArray().toDoubles());
		os.gstate().transform(m);
		return true;
	}
}

class ConcatMatrix extends MatrixOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class, PSPackedArray.class,
				PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray m3 = os.popArray();
		PSPackedArray m2 = os.popPackedArray();
		PSPackedArray m1 = os.popPackedArray();

		org.freehep.postscript.Transform t = os.gstate().device().createTransform(m1.toDoubles());
		t.concatenate(os.gstate().device().createTransform(m2.toDoubles()));
		double[] d = new double[6];
		t.getMatrix(d);
		m3.set(d);
		os.push(m3);
		return true;
	}
}

class Transform extends MatrixOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		org.freehep.postscript.Transform transform;
		double dx, dy;
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().getTransform();
		} else if (os.checkType(PSNumber.class, PSNumber.class,
				PSPackedArray.class)) {
			double[] m = os.popPackedArray().toDoubles();
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().device().createTransform(m);
		} else {
			error(os, new TypeCheck());
			return true;
		}
		Point d = transform.transform(os.gstate().device().createPoint(dx, dy), null);
		os.push(d.getX());
		os.push(d.getY());
		return true;
	}
}

class DTransform extends MatrixOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		org.freehep.postscript.Transform transform;
		double dx, dy;
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().getTransform();
		} else if (os.checkType(PSNumber.class, PSNumber.class,
				PSPackedArray.class)) {
			double[] m = os.popPackedArray().toDoubles();
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().device().createTransform(m);
		} else {
			error(os, new TypeCheck());
			return true;
		}
		Point d = transform.deltaTransform(os.gstate().device().createPoint(dx, dy), null);
		os.push(d.getX());
		os.push(d.getY());
		return true;
	}
}

class ITransform extends MatrixOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		org.freehep.postscript.Transform transform;
		double dx, dy;
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().getTransform();
		} else if (os.checkType(PSNumber.class, PSNumber.class,
				PSPackedArray.class)) {
			double[] m = os.popPackedArray().toDoubles();
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().device().createTransform(m);
		} else {
			error(os, new TypeCheck());
			return true;
		}
		try {
			Point d = transform.inverseTransform(os.gstate().device().createPoint(dx, dy),
					null);
			os.push(d.getX());
			os.push(d.getY());
		} catch (NoninvertibleTransformException e) {
			error(os, new UndefinedResult());
		}
		return true;
	}
}

class IDTransform extends MatrixOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		org.freehep.postscript.Transform transform;
		double dx, dy;
		if (os.checkType(PSNumber.class, PSNumber.class)) {
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().getTransform();
		} else if (os.checkType(PSNumber.class, PSNumber.class,
				PSPackedArray.class)) {
			double[] m = os.popPackedArray().toDoubles();
			dy = os.popNumber().getDouble();
			dx = os.popNumber().getDouble();
			transform = os.gstate().device().createTransform(m);
		} else {
			error(os, new TypeCheck());
			return true;
		}
		try {
			org.freehep.postscript.Transform inverse = transform.createInverse();
			Point d = inverse
					.deltaTransform(os.gstate().device().createPoint(dx, dy), null);
			os.push(d.getX());
			os.push(d.getY());
		} catch (NoninvertibleTransformException e) {
			error(os, new UndefinedResult());
		}
		return true;
	}
}

class InvertMatrix extends MatrixOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class, PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray m2 = os.popArray();
		PSPackedArray m1 = os.popPackedArray();

		try {
			org.freehep.postscript.Transform transform = os.gstate().device().createTransform(m1.toDoubles());
			org.freehep.postscript.Transform inverse = transform.createInverse();

			double[] d = new double[6];
			inverse.getMatrix(d);
			m2.set(d);
			os.push(m2);
		} catch (NoninvertibleTransformException e) {
			error(os, new UndefinedResult());
		}
		return true;
	}
}
