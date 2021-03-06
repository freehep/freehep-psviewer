// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.errors.Undefined;
import org.freehep.postscript.processor.LoopingContext;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSBoolean;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSGState;
import org.freehep.postscript.types.PSName;
import org.freehep.postscript.types.PSNumber;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSPackedArray;
import org.freehep.postscript.types.PSReal;
import org.freehep.postscript.types.PSSimple;
import org.freehep.postscript.types.PSString;
import org.freehep.vectorgraphics.NoninvertibleTransformException;
import org.freehep.vectorgraphics.Path;
import org.freehep.vectorgraphics.PathIterator;
import org.freehep.vectorgraphics.Point;
import org.freehep.vectorgraphics.Rectangle;
import org.freehep.vectorgraphics.Shape;

/**
 * Path Construction Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class PathOperator extends AbstractOperator {

	static final int MATRIX_SIZE = 6;
	static final int PATH_SIZE = 6;
	static final int FULL_CIRCLE = 360;

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { NewPath.class,
				CurrentPoint.class, MoveTo.class, RMoveTo.class, LineTo.class,
				RLineTo.class, Arc.class, ArcN.class, ArcT.class, ArcTo.class,
				CurveTo.class, RCurveTo.class, ClosePath.class,
				FlattenPath.class, ReversePath.class, StrokePath.class,
				UStrokePath.class, CharPath.class, UAppend.class,
				ClipPath.class, SetBBox.class, PathBBox.class,
				PathForAll.class, UPath.class, InitClip.class, Clip.class,
				EOClip.class, RectClip.class, UCache.class,
				// type 1 font
				HLineTo.class, VLineTo.class, HMoveTo.class, VMoveTo.class,
				RRCurveTo.class, VHCurveTo.class, HVCurveTo.class });
	}
}

class NewPath extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().newPath();
		return true;
	}
}

class CurrentPoint extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			os.push(point.getX());
			os.push(point.getY());
		}
		return true;
	}
}

class MoveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber y = os.popNumber();
		PSNumber x = os.popNumber();
		os.gstate().path().moveTo(x.getFloat(), y.getFloat());
		return true;
	}
}

class RMoveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			PSNumber dy = os.popNumber();
			PSNumber dx = os.popNumber();
			os.gstate().path().moveTo((float) point.getX() + dx.getFloat(),
					(float) point.getY() + dy.getFloat());
		}
		return true;
	}
}

class LineTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			PSNumber y = os.popNumber();
			PSNumber x = os.popNumber();
			os.gstate().path().lineTo(x.getFloat(), y.getFloat());
		}
		return true;
	}
}

class RLineTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			PSNumber dy = os.popNumber();
			PSNumber dx = os.popNumber();
			os.gstate().path().lineTo((float) point.getX() + dx.getFloat(),
					(float) point.getY() + dy.getFloat());
		}
		return true;
	}
}

class Arc extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		double a2 = os.popNumber().getDouble();
		double a1 = os.popNumber().getDouble();
		double r = os.popNumber().getDouble();
		double y = os.popNumber().getDouble();
		double x = os.popNumber().getDouble();

		while (a2 < a1) {
			a2 += FULL_CIRCLE;
		}

		org.freehep.vectorgraphics.Arc arc = os.gstate().device().createArc();
		arc.setArcByCenter(x, y, r, -a1, -(a2 - a1), org.freehep.vectorgraphics.Arc.OPEN);
		os.gstate().path().append(arc, true);

		return true;
	}
}

class ArcN extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		double a2 = os.popNumber().getDouble();
		double a1 = os.popNumber().getDouble();
		double r = os.popNumber().getDouble();
		double y = os.popNumber().getDouble();
		double x = os.popNumber().getDouble();

		while (a2 > a1) {
			a2 -= FULL_CIRCLE;
		}

		org.freehep.vectorgraphics.Arc arc = os.gstate().device().createArc();

		arc.setArcByCenter(x, y, r, -a1, -(a2 - a1), org.freehep.vectorgraphics.Arc.OPEN);
		os.gstate().path().append(arc, true);

		return true;
	}
}

class ArcT extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			double r = os.popNumber().getDouble();
			double y2 = os.popNumber().getDouble();
			double x2 = os.popNumber().getDouble();
			double y1 = os.popNumber().getDouble();
			double x1 = os.popNumber().getDouble();

			org.freehep.vectorgraphics.Arc arc = os.gstate().device().createArc(org.freehep.vectorgraphics.Arc.OPEN);
			arc.setArcByTangent(p0, os.gstate().device().createPoint(x1, y1),
					os.gstate().device().createPoint(x2, y2), r);
			os.gstate().path().append(arc, true);
		}
		return true;
	}
}

class ArcTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			double r = os.popNumber().getDouble();
			double y2 = os.popNumber().getDouble();
			double x2 = os.popNumber().getDouble();
			double y1 = os.popNumber().getDouble();
			double x1 = os.popNumber().getDouble();

			org.freehep.vectorgraphics.Arc arc = os.gstate().device().createArc(org.freehep.vectorgraphics.Arc.OPEN);
			arc.setArcByTangent(p0, os.gstate().device().createPoint(x1, y1),
					os.gstate().device().createPoint(x2, y2), r);
			os.gstate().path().append(arc, true);
			Point p1t = arc.getStartPoint();
			os.push(p1t.getX());
			os.push(p1t.getY());
			Point p2t = arc.getEndPoint();
			os.push(p2t.getX());
			os.push(p2t.getY());
		}
		return true;
	}
}

class CurveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			float y3 = os.popNumber().getFloat();
			float x3 = os.popNumber().getFloat();
			float y2 = os.popNumber().getFloat();
			float x2 = os.popNumber().getFloat();
			float y1 = os.popNumber().getFloat();
			float x1 = os.popNumber().getFloat();

			os.gstate().path().curveTo(x1, y1, x2, y2, x3, y3);
		}
		return true;
	}
}

class RCurveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			float y0 = (float) p0.getY();
			float x0 = (float) p0.getX();
			float y3 = os.popNumber().getFloat() + y0;
			float x3 = os.popNumber().getFloat() + x0;
			float y2 = os.popNumber().getFloat() + y0;
			float x2 = os.popNumber().getFloat() + x0;
			float y1 = os.popNumber().getFloat() + y0;
			float x1 = os.popNumber().getFloat() + x0;

			os.gstate().path().curveTo(x1, y1, x2, y2, x3, y3);
		}
		return true;
	}
}

class ClosePath extends PathOperator {
	@Override
	public boolean execute(OperandStack os) {
		os.gstate().path().closePath();
		return true;
	}
}

class FlattenPath extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().flattenPath();
		return true;
	}
}

class ReversePath extends PathOperator {
	private float coord[] = new float[PATH_SIZE];
	private Stack<Object> stack;

	private void createSubPath(Path reverse) {
		while (!stack.empty()) {
			String s = (String) stack.pop();
			if (s.equals("moveto")) {
				float x = ((Float) stack.pop()).floatValue();
				float y = ((Float) stack.pop()).floatValue();
				reverse.moveTo(x, y);
			} else if (s.equals("lineto")) {
				float x = ((Float) stack.pop()).floatValue();
				float y = ((Float) stack.pop()).floatValue();
				reverse.lineTo(x, y);
			} else {
				float cx1 = ((Float) stack.pop()).floatValue();
				float cy1 = ((Float) stack.pop()).floatValue();
				float cx2 = ((Float) stack.pop()).floatValue();
				float cy2 = ((Float) stack.pop()).floatValue();
				float x = ((Float) stack.pop()).floatValue();
				float y = ((Float) stack.pop()).floatValue();
				reverse.curveTo(cx1, cy1, cx2, cy2, x, y);
			}
		}
	}

	@Override
	public boolean execute(OperandStack os) {
		stack = new Stack<Object>();
		Path path = os.gstate().path().copy();
		PathIterator iterator = path.getPathIterator();
		Path reverse = os.gstate().newPath();

		while (!iterator.isDone()) {
			switch (iterator.currentSegment(coord)) {
			case PathIterator.SEG_MOVETO:
				createSubPath(reverse);
				stack.push(new Float(coord[1]));
				stack.push(new Float(coord[0]));
				break;

			case PathIterator.SEG_LINETO:
				stack.push("lineto");
				stack.push(new Float(coord[1]));
				stack.push(new Float(coord[0]));
				break;

			case PathIterator.SEG_CUBICTO:
				stack.push(new Float(coord[1]));
				stack.push(new Float(coord[0]));
				stack.push(new Float(coord[3]));
				stack.push(new Float(coord[2]));
				stack.push("curveto");
				stack.push(new Float(coord[5]));
				stack.push(new Float(coord[4]));
				break;

			case PathIterator.SEG_CLOSE:
				stack.push("moveto");
				createSubPath(reverse);
				reverse.closePath();
				break;

			default:
				error(os, new RangeCheck());
				return true;
			}
			iterator.next();
		}
		return true;
	}
}

class StrokePath extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().strokePath();
		return true;
	}
}

class UStrokePath extends PathOperator {
	private boolean done;
	private org.freehep.vectorgraphics.Transform matrix;

	private UStrokePath(boolean d, org.freehep.vectorgraphics.Transform m) {
		done = d;
		matrix = m;
	}

	public UStrokePath() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!done) {
			if (!os.checkType(PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			org.freehep.vectorgraphics.Transform m = null;
			PSPackedArray proc = os.popPackedArray();
			if (proc.size() == MATRIX_SIZE) {
				try {
					m = os.gstate().device().createTransform(proc.toDoubles());
					if (!os.checkType(PSPackedArray.class)) {
						error(os, new TypeCheck());
						return true;
					}
					proc = os.popPackedArray();
				} catch (ClassCastException e) {
					// no matrix
				}
			}

			// newpath, systemdict, begin
			os.gstate().newPath();
			os.dictStack().push(os.dictStack().systemDictionary());
			PSPackedArray upath = (PSPackedArray) proc.copy();
			upath.setExecutable();

			os.execStack().pop();
			os.execStack().push(new UStrokePath(true, m));
			os.execStack().push(upath);
			return false;
		}

		// upath was executed, end, strokepath
		os.dictStack().pop();
		if (matrix != null) {
			org.freehep.vectorgraphics.Transform ctm = os.gstate().getTransform();
			os.gstate().transform(matrix);
			os.gstate().strokePath();
			os.gstate().setTransform(ctm);
		} else {
			os.gstate().strokePath();
		}
		// FIXME: where is the gsave???
		os.grestore();
		return true;
	}
}

class CharPath extends PathOperator {
	{
		operandTypes = new Class[] { PSString.class, PSBoolean.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		boolean strokePath = os.popBoolean().getValue();
		String text = os.popString().getValue();
		PSGState gs = os.gstate();

		float x = 0.0f;
		float y = 0.0f;
		Point point = gs.position();
		if (point != null) {
			x = (float) point.getX();
			y = (float) point.getY();
		}

		for (int i = 0; i < text.length(); i++) {
			int ch = text.charAt(i);
			Shape outline = gs.charPath(ch, x, y, strokePath);
			// FIXME: not necessary if fonts are all implemented in charPath
			if (outline != null) {
				gs.path().append(outline, false);
			}

			x += FontOperator.stringWidth(os, ch);
			gs.path().moveTo(x, y);
		}
		return true;
	}
}

class UAppend extends PathOperator {
	private boolean done;

	private UAppend(boolean d) {
		done = d;
	}

	public UAppend() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!done) {
			if (!os.checkType(PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			PSPackedArray proc = os.popPackedArray();

			// systemdict, begin
			os.dictStack().push(os.dictStack().systemDictionary());
			PSPackedArray upath = (PSPackedArray) proc.copy();
			upath.setExecutable();

			os.execStack().pop();
			os.execStack().push(new UAppend(true));
			os.execStack().push(upath);
			return false;
		}

		// upath was executed, end
		os.dictStack().pop();
		return true;
	}
}

class ClipPath extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().clipPath();
		return true;
	}
}

class SetBBox extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		double ury = ((PSNumber) os.pop()).getDouble();
		double urx = ((PSNumber) os.pop()).getDouble();
		double lly = ((PSNumber) os.pop()).getDouble();
		double llx = ((PSNumber) os.pop()).getDouble();
		os.gstate().setBoundingBox(os.gstate().device().createRectangle(llx, lly, urx - llx, ury - lly));
		return true;
	}
}

class PathBBox extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			Rectangle bb = (os.gstate().boundingBox() != null) ? os.gstate()
					.boundingBox() : os.gstate().path().getBounds();
			os.push(bb.getMinX());
			os.push(bb.getMinY());
			os.push(bb.getMaxX());
			os.push(bb.getMaxY());
		}
		return true;
	}
}

class PathForAll extends PathOperator implements LoopingContext {

	private Shape path = null;
	private PSPackedArray moveProc;
	private PSPackedArray lineProc;
	private PSPackedArray curveProc;
	private PSPackedArray closeProc;
	private PathIterator iterator;
	private double[] coord;

	private PathForAll(Shape shape, PSPackedArray move, PSPackedArray line,
			PSPackedArray curve, PSPackedArray close) {
		path = shape;
		moveProc = move;
		lineProc = line;
		curveProc = curve;
		closeProc = close;
		iterator = path.getPathIterator();
		coord = new double[PATH_SIZE];
	}

	public PathForAll() {
	}

	@Override
	public boolean execute(OperandStack os) {

		if (path == null) {
			if (!os.checkType(PSPackedArray.class, PSPackedArray.class,
					PSPackedArray.class, PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			PSPackedArray close = os.popPackedArray();
			PSPackedArray curve = os.popPackedArray();
			PSPackedArray line = os.popPackedArray();
			PSPackedArray move = os.popPackedArray();
			Shape shape = (Shape) os.gstate().path().copy();

			os.execStack().pop();
			os.execStack()
					.push(new PathForAll(shape, move, line, curve, close));
			return false;
		}

		if (iterator.isDone()) {
			return true;
		}

		switch (iterator.currentSegment(coord)) {
		case PathIterator.SEG_MOVETO:
			os.push(coord[0]);
			os.push(coord[1]);
			os.execStack().push(moveProc);
			break;

		case PathIterator.SEG_LINETO:
			os.push(coord[0]);
			os.push(coord[1]);
			os.execStack().push(lineProc);
			break;

		case PathIterator.SEG_CUBICTO:
			os.push(coord[0]);
			os.push(coord[1]);
			os.push(coord[2]);
			os.push(coord[3]);
			os.push(coord[4]);
			os.push(coord[5]);
			os.execStack().push(curveProc);
			break;

		case PathIterator.SEG_CLOSE:
			os.execStack().push(closeProc);
			break;

		default:
			error(os, new RangeCheck());
			return true;
		}
		iterator.next();
		return false;
	}
}

class UPath extends PathOperator {
	{
		operandTypes = new Class[] { PSBoolean.class };
	}
	private double coord[] = new double[PATH_SIZE];

	@Override
	public boolean execute(OperandStack os) {
		boolean ucache = os.popBoolean().getValue();

		org.freehep.vectorgraphics.Transform inverse;
		try {
			inverse = os.gstate().getTransform().createInverse();
		} catch (NoninvertibleTransformException e) {
			error(os, new Undefined());
			return true;
		}

		PathIterator iterator = os.gstate().path().getPathIterator(inverse);
		List<PSSimple> path = new ArrayList<PSSimple>();

		if (ucache) {
			path.add(new PSName("ucache", true));
		}

		Rectangle bb = os.gstate().path().getBounds();
		path.add(new PSReal(bb.getMinX()));
		path.add(new PSReal(bb.getMinY()));
		path.add(new PSReal(bb.getMaxX()));
		path.add(new PSReal(bb.getMaxY()));
		path.add(new PSName("setbbox", true));

		while (!iterator.isDone()) {
			switch (iterator.currentSegment(coord)) {
			case PathIterator.SEG_MOVETO:
				path.add(new PSReal(coord[0]));
				path.add(new PSReal(coord[1]));
				path.add(new PSName("moveto", true));
				break;

			case PathIterator.SEG_LINETO:
				path.add(new PSReal(coord[0]));
				path.add(new PSReal(coord[1]));
				path.add(new PSName("lineto", true));
				break;

			case PathIterator.SEG_CUBICTO:
				path.add(new PSReal(coord[0]));
				path.add(new PSReal(coord[1]));
				path.add(new PSReal(coord[2]));
				path.add(new PSReal(coord[3]));
				path.add(new PSReal(coord[4]));
				path.add(new PSReal(coord[5]));
				path.add(new PSName("curveto", true));
				break;

			case PathIterator.SEG_CLOSE:
				path.add(new PSName("closepath", true));
				break;

			default:
				error(os, new RangeCheck());
				return true;
			}
			iterator.next();
		}

		PSObject[] obj = path.toArray(new PSObject[path.size()]);
		PSPackedArray upath = new PSPackedArray(obj);
		upath.setExecutable();
		os.push(upath);

		return true;
	}
}

class InitClip extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().initClip();
		return true;
	}
}

class Clip extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().clip(os.gstate().path());
		return true;
	}
}

class EOClip extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		Path eoPath = os.gstate().path().copy();
		eoPath.setWindingRule(Path.WIND_EVEN_ODD);
		os.gstate().clip(eoPath);
		return true;
	}
}

class RectClip extends PathOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class, PSNumber.class,
				PSNumber.class)) {
			double h = os.popNumber().getDouble();
			double w = os.popNumber().getDouble();
			double y = os.popNumber().getDouble();
			double x = os.popNumber().getDouble();
			Rectangle r = os.gstate().device().createRectangle(x, y, w, h);
			os.gstate().clip(r);
			os.gstate().newPath();
		} else if (os.checkType(PSPackedArray.class)) {
			PSPackedArray a = os.popPackedArray();
			Path p = os.gstate().device().createPath();
			for (int i = 0; i < a.size() / 4; i++) {
				double x = ((PSNumber) a.get(i * 4)).getDouble();
				double y = ((PSNumber) a.get(i * 4 + 1)).getDouble();
				double w = ((PSNumber) a.get(i * 4 + 2)).getDouble();
				double h = ((PSNumber) a.get(i * 4 + 3)).getDouble();
				Rectangle r = os.gstate().device().createRectangle(x, y, w, h);
				p.append(r, false);
			}
			os.gstate().clip(p);
			os.gstate().newPath();
		} else {
			// FIXME: encoded number string not handled
			error(os, new TypeCheck());
		}
		return true;
	}
}

class UCache extends PathOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FIXME: ignored
		return true;
	}
}

// --------------------------------------------------------------------------------
// Type 1 Font Operators
// --------------------------------------------------------------------------------

class HLineTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			PSNumber dx = os.popNumber();
			os.gstate().path().lineTo((float) point.getX() + dx.getFloat(),
					(float) point.getY());
		}
		return true;
	}
}

class VLineTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			PSNumber dy = os.popNumber();
			os.gstate().path().lineTo((float) point.getX(),
					(float) point.getY() + dy.getFloat());
		}
		return true;
	}
}

class HMoveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			PSNumber dx = os.popNumber();
			os.gstate().path().moveTo((float) point.getX() + dx.getFloat(),
					(float) point.getY());
		}
		return true;
	}
}

class VMoveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point point = os.gstate().position();
		if (point == null) {
			error(os, new NoCurrentPoint());
		} else {
			PSNumber dy = os.popNumber();
			os.gstate().path().moveTo((float) point.getX(),
					(float) point.getY() + dy.getFloat());
		}
		return true;
	}
}

class RRCurveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			float y0 = (float) p0.getY();
			float x0 = (float) p0.getX();
			float dy3 = os.popNumber().getFloat();
			float dx3 = os.popNumber().getFloat();
			float dy2 = os.popNumber().getFloat();
			float dx2 = os.popNumber().getFloat();
			float dy1 = os.popNumber().getFloat();
			float dx1 = os.popNumber().getFloat();

			os.gstate().path().curveTo(x0 + dx1, y0 + dy1, x0 + dx1 + dx2,
					y0 + dy1 + dy2, x0 + dx1 + dx2 + dx3, y0 + dy1 + dy2 + dy3);
		}
		return true;
	}
}

class HVCurveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			float y0 = (float) p0.getY();
			float x0 = (float) p0.getX();
			float dy3 = os.popNumber().getFloat();
			float dx3 = 0;
			float dy2 = os.popNumber().getFloat();
			float dx2 = os.popNumber().getFloat();
			float dy1 = 0;
			float dx1 = os.popNumber().getFloat();

			os.gstate().path().curveTo(x0 + dx1, y0 + dy1, x0 + dx1 + dx2,
					y0 + dy1 + dy2, x0 + dx1 + dx2 + dx3, y0 + dy1 + dy2 + dy3);
		}
		return true;
	}
}

class VHCurveTo extends PathOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		Point p0 = os.gstate().position();
		if (p0 == null) {
			error(os, new NoCurrentPoint());
		} else {
			float y0 = (float) p0.getY();
			float x0 = (float) p0.getX();
			float dy3 = 0;
			float dx3 = os.popNumber().getFloat();
			float dy2 = os.popNumber().getFloat();
			float dx2 = os.popNumber().getFloat();
			float dy1 = os.popNumber().getFloat();
			float dx1 = 0;

			os.gstate().path().curveTo(x0 + dx1, y0 + dy1, x0 + dx1 + dx2,
					y0 + dy1 + dy2, x0 + dx1 + dx2 + dx3, y0 + dy1 + dy2 + dy3);
		}
		return true;
	}
}
