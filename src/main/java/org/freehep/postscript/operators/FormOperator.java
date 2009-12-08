// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSPackedArray;
import org.freehep.postscript.types.PSPaint;
import org.freehep.postscript.viewer.FixedTexturePaint;

/**
 * Form and Pattern Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class FormOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { MakePattern.class,
				SetPattern.class, ExecForm.class });
	}

}

class MakePattern extends FormOperator {

	private PSDictionary pattern;
	private Paint paint;

	private MakePattern(PSDictionary d, Paint p) {
		pattern = d;
		paint = p;
	}

	public MakePattern() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (pattern == null) {
			if (!os.checkType(PSDictionary.class, PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			AffineTransform m = new AffineTransform(os.popPackedArray()
					.toDoubles());
			PSDictionary d = os.popDictionary();

			// FIXME: no checking
			// FIXME: no copy
			int type = d.getInteger("PatternType");
			switch (type) {
			case 1: // tiling pattern
				PSPackedArray proc = (PSPackedArray) d.getPackedArray(
						"PaintProc").copy();
				double[] bbox = d.getPackedArray("BBox").toDoubles();
				double xStep = d.getNumber("XStep").getDouble();
				double yStep = d.getNumber("YStep").getDouble();
				int paintType = d.getInteger("PaintType");
				// FIXME: ignored
				/* int tilingType = */
				d.getInteger("TilingType");

				// gsave, create image, set modified gstate
				os.push(d);
				os.gsave();

				AffineTransform inverse = new AffineTransform();
				try {
					inverse = m.createInverse();
				} catch (NoninvertibleTransformException e) {
					log.warning("Internal MakePattern Error");
				}

				Point2D bb = inverse.deltaTransform(
						new Point2D.Double(1.0, 1.0), null);
				double w = bbox[2] - bbox[0] + bb.getX();
				double h = bbox[3] - bbox[1] + bb.getY();

				Point2D biSize = m.deltaTransform(new Point2D.Double(xStep,
						yStep), null);
				int biWidth = (int) biSize.getX();
				int biHeight = (int) biSize.getY();
				BufferedImage bi = os.gstate()
						.convertToImage(biWidth, biHeight);

				AffineTransform ctm = os.gstate().getTransform();

				os.gstate().setTransform(m);
				// FIXME: offset may not be completely correct (HEART2)
				os.gstate().translate(-bbox[0], -bbox[1]);

				os.gstate().newPath();
				os.gstate()
						.clip(new Rectangle2D.Double(bbox[0], bbox[1], w, h));
				Point2D offset = inverse.transform(new Point2D.Double(bbox[0],
						bbox[1]), null);
				Rectangle2D box = new Rectangle2D.Double(offset.getX(), offset
						.getY(), biWidth, biHeight);
				Paint p = new FixedTexturePaint(os, ctm, bi, box);

				os.execStack().pop();
				os.execStack().push(new MakePattern(d, p));
				os.execStack().push(proc);

				switch (paintType) {
				case 1:
					break;
				case 2:
					os.gstate().setColorSpace("DeviceRGB");
					os.gstate().setColor(new float[] { 1.0f, 0.0f, 0.0f });
					break;
				default:
					error(os, new RangeCheck());
					return true;
				}
				return false;
			default:
				// FIXME
				error(os, new RangeCheck());
				return true;
			}
		}

		os.grestore();

		// retrieve paint and save as implementation
		pattern.put("Implementation", new PSPaint(paint));

		os.push(pattern);
		return true;
	}
}

class SetPattern extends FormOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSDictionary pattern = os.popDictionary();

		// FIXME: no checking
		int type = pattern.getInteger("PatternType");
		switch (type) {
		case 1: // tiling
			PSPaint implementation = (PSPaint) pattern.get("Implementation");
			Paint paint = implementation.getValue();
			int paintType = pattern.getInteger("PaintType");
			switch (paintType) {
			case 1:
				os.gstate().setColor(paint);
				break;
			case 2:
				int n = os.gstate().getNumberOfColorSpaceComponents();
				PSObject[] params = new PSObject[n];
				for (int i = n - 1; i >= 0; i--) {
					params[i] = os.popNumber();
				}
				os.gstate().setColor(paint, params);
				break;
			default:
				error(os, new RangeCheck());
				return true;
			}
			break;
		default:
			error(os, new RangeCheck());
			break;
		}
		return true;
	}
}

class ExecForm extends FormOperator {
	private boolean done;

	private ExecForm(boolean d) {
		done = d;
	}

	public ExecForm() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!done) {
			if (!os.checkType(PSDictionary.class)) {
				error(os, new TypeCheck());
				return true;
			}
			PSDictionary form = os.popDictionary();

			try {
				if (form.getInteger("FormType") != 1) {
					error(os, new RangeCheck());
					return true;
				}

				AffineTransform matrix = new AffineTransform(form
						.getPackedArray("Matrix").toDoubles());
				double[] bbox = form.getPackedArray("BBox").toDoubles();
				PSPackedArray proc = (PSPackedArray) form.getPackedArray(
						"PaintProc").clone();
				proc.setExecutable();

				// gsave, concat matrix, rectclip bbox
				os.gsave();
				os.gstate().transform(matrix);
				os.gstate().clip(
						new Rectangle2D.Double(bbox[0], bbox[1], bbox[2]
								- bbox[0], bbox[3] - bbox[1]));

				os.execStack().pop();
				os.execStack().push(new ExecForm(true));
				os.execStack().push(proc);
				os.push(form);
				return false;
			} catch (ClassCastException e) {
				error(os, new TypeCheck());
				return true;
			} catch (CloneNotSupportedException e) {
				error(os, new Unimplemented());
				return true;
			}
		}

		os.grestore();
		return true;
	}
}
