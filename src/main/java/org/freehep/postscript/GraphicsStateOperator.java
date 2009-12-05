// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Color;

/**
 * Graphics State Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class GraphicsStateOperator extends PSOperator {

	public static Class<?>[] operators = { GSave.class, GRestore.class,
			ClipSave.class, ClipRestore.class, GRestoreAll.class,
			InitGraphics.class, GState.class, SetGState.class,
			CurrentGState.class, SetLineWidth.class, CurrentLineWidth.class,
			SetLineCap.class, CurrentLineCap.class, SetLineJoin.class,
			CurrentLineJoin.class, SetMiterLimit.class,
			CurrentMiterLimit.class, SetStrokeAdjust.class,
			CurrentStrokeAdjust.class, SetDash.class, CurrentDash.class,
			SetColorSpace.class, CurrentColorSpace.class, SetColor.class,
			CurrentColor.class, SetGray.class, CurrentGray.class,
			SetHSBColor.class, CurrentHSBColor.class, SetRGBColor.class,
			CurrentRGBColor.class, SetCMYKColor.class, CurrentCMYKColor.class };

	@Override
	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class GSave extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gsave();
		return true;
	}
}

class GRestore extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.grestore();
		return true;
	}
}

class ClipSave extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		// Level 3
		error(os, new Unimplemented());
		return true;
	}
}

class ClipRestore extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		// Level 3
		error(os, new Unimplemented());
		return true;
	}
}

class GRestoreAll extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-132: can only be implemented once we have Save and Restore
		error(os, new Unimplemented());
		return true;
	}
}

class InitGraphics extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().initGraphics();
		return true;
	}
}

class GState extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().copy());
		return true;
	}
}

class SetGState extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSGState.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSGState gs = os.popGState();
		gs.copyInto(os.gstate());
		return true;
	}
}

class CurrentGState extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSGState.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSGState gs = os.popGState();
		os.gstate().copyInto(gs);
		os.push(gs);
		return true;
	}
}

class SetLineWidth extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber width = os.popNumber();
		os.gstate().setLineWidth(width.getDouble());
		return true;
	}
}

class CurrentLineWidth extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().lineWidth());
		return true;
	}
}

class SetLineCap extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger n = os.popInteger();
		os.gstate().setLineCap(n.getValue());
		return true;
	}
}

class CurrentLineCap extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().lineCap());
		return true;
	}
}

class SetLineJoin extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger n = os.popInteger();
		os.gstate().setLineJoin(n.getValue());
		return true;
	}
}

class CurrentLineJoin extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().lineJoin());
		return true;
	}
}

class SetMiterLimit extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n = os.popNumber();
		os.gstate().setMiterLimit(n.getFloat());
		return true;
	}
}

class CurrentMiterLimit extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().miterLimit());
		return true;
	}
}

class SetStrokeAdjust extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSBoolean.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSBoolean b = os.popBoolean();
		os.gstate().setStrokeAdjust(b.getValue());
		return true;
	}
}

class CurrentStrokeAdjust extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().strokeAdjust());
		return true;
	}
}

class SetDash extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber offset = os.popNumber();
		PSPackedArray array = os.popPackedArray();
		os.gstate().setDash(array.toFloats(), offset.getFloat());
		return true;
	}
}

class CurrentDash extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		float[] dash = os.gstate().dash();
		PSArray array = new PSArray(dash);
		os.push(array);
		os.push(os.gstate().dashPhase());
		return true;
	}
}

class SetColorSpace extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		String name;
		Object[] params;
		if (os.checkType(PSPackedArray.class)) {
			PSPackedArray a = os.popPackedArray();
			name = a.get(0).cvs();
			params = a.toObjects();
		} else if (os.checkType(PSName.class)) {
			name = os.popName().cvs();
			params = null;
		} else {
			name = "Undefined";
			params = null;
		}

		if (!os.gstate().setColorSpace(name, params)) {
			error(os, new Undefined());
		}
		return true;
	}
}

class CurrentColorSpace extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		String space = os.gstate().colorSpace();
		// FIXME: handle pattern color space parameters
		PSArray a = new PSArray(1);
		a.set(0, space);
		// for (int i=0; i<color.length; i++) {
		// a.set(1+i, color[i]);
		// }
		os.push(a);
		return true;
	}
}

class SetColor extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	// FIXME: handles only RGB, GRAY and CMYK
	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class)) {
			String space = os.gstate().colorSpace();
			if (space.equals("DeviceGray")) {
				PSNumber g = os.popNumber();
				os.gstate().setColor(new float[] { g.getFloat() });
			} else if (space.equals("DeviceRGB")) {
				PSNumber blue = os.popNumber();
				PSNumber green = os.popNumber();
				PSNumber red = os.popNumber();
				os.gstate().setColor(
						new float[] { red.getFloat(), green.getFloat(),
								blue.getFloat() });
			} else if (space.equals("DeviceCMYK")) {
				PSNumber black = os.popNumber();
				PSNumber yellow = os.popNumber();
				PSNumber magenta = os.popNumber();
				PSNumber cyan = os.popNumber();
				os.gstate().setColor(
						new float[] { cyan.getFloat(), magenta.getFloat(),
								yellow.getFloat(), black.getFloat() });
			} else {
				error(os, new Undefined());
			}
		} else if (os.checkType(PSDictionary.class)) {
			PSDictionary pattern = os.popDictionary();
			PSPaint paint = (PSPaint) pattern.get("Implementation");
			int patternType = pattern.getInteger("PatternType");
			int paintType = pattern.getInteger("PaintType");
			if ((patternType == 1) && (paintType == 2)) {
				// FIXME: where is the color
				// FIXME: where is the pattern
				os.gstate().setColor(paint.getValue(), new PSObject[0]);
			} else {
				os.gstate().setColor(paint.getValue());
			}

		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class CurrentColor extends GraphicsStateOperator {

	// FIXME: this call should allow UCR and BG to be processed from DeviceCMYK
	@Override
	public boolean execute(OperandStack os) {
		float[] color = os.gstate().color();
		// FIXME: handle pattern color space
		for (int i = 0; i < color.length; i++) {
			os.push(color[i]);
		}
		return true;
	}
}

class SetGray extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber color = os.popNumber();
		os.gstate().setColorSpace("DeviceGray");
		os.gstate().setColor(new float[] { color.getFloat() });
		return true;
	}
}

class CurrentGray extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		float[] color = os.gstate().color("DeviceGray");

		for (int i = 0; i < color.length; i++) {
			os.push(color[i]);
		}
		return true;
	}
}

class SetHSBColor extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber b = os.popNumber();
		PSNumber s = os.popNumber();
		PSNumber h = os.popNumber();
		os.gstate().setColorSpace("DeviceRGB");
		Color color = new Color(Color.HSBtoRGB(h.getFloat(), s.getFloat(), b
				.getFloat()));
		os.gstate().setColor(color.getColorComponents(null));
		return true;
	}
}

class CurrentHSBColor extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		float[] color = os.gstate().color("DeviceRGB");
		float[] hsb = Color.RGBtoHSB((int) (color[0] * 255),
				(int) (color[1] * 255), (int) (color[2] * 255), null);

		for (int i = 0; i < hsb.length; i++) {
			os.push(hsb[i]);
		}
		return true;
	}
}

class SetRGBColor extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber blue = os.popNumber();
		PSNumber green = os.popNumber();
		PSNumber red = os.popNumber();
		os.gstate().setColorSpace("DeviceRGB");
		os.gstate()
				.setColor(
						new float[] { red.getFloat(), green.getFloat(),
								blue.getFloat() });
		return true;
	}
}

class CurrentRGBColor extends GraphicsStateOperator {

	@Override
	public boolean execute(OperandStack os) {
		float[] color = os.gstate().color("DeviceRGB");

		for (int i = 0; i < color.length; i++) {
			os.push(color[i]);
		}
		return true;
	}
}

class SetCMYKColor extends GraphicsStateOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		float k = os.popNumber().getFloat();
		float y = os.popNumber().getFloat();
		float m = os.popNumber().getFloat();
		float c = os.popNumber().getFloat();

		os.gstate().setColorSpace("DeviceCMYK");
		os.gstate().setColor(new float[] { c, m, y, k });
		return true;
	}
}

class CurrentCMYKColor extends GraphicsStateOperator {

	private float[] cmyk;
	private float bg;
	private float ucr;

	public CurrentCMYKColor() {
	}

	private CurrentCMYKColor(float[] color) {
		cmyk = new float[color.length];
		System.arraycopy(color, 0, cmyk, 0, color.length);
		bg = -1.0f;
		ucr = -1.0f;
	}

	@Override
	public boolean execute(OperandStack os) {
		if (cmyk == null) {
			float[] color = os.gstate().color("DeviceCMYK");

			os.execStack().pop();
			os.execStack().push(new CurrentCMYKColor(color));
			os.push(color[3]); // K
			os.execStack().push(os.gstate().blackGeneration().copy());
			return false;
		}

		if (bg < 0) {
			bg = (os.popNumber()).getFloat();

			os.push(cmyk[3]);
			os.execStack().push(os.gstate().underColorRemoval().copy());
			return false;
		}

		ucr = (os.popNumber()).getFloat();

		cmyk[0] = Math.min(1.0f, Math.max(0.0f, cmyk[0] - ucr));
		cmyk[1] = Math.min(1.0f, Math.max(0.0f, cmyk[1] - ucr));
		cmyk[2] = Math.min(1.0f, Math.max(0.0f, cmyk[2] - ucr));
		cmyk[3] = Math.min(1.0f, Math.max(0.0f, bg));

		for (int i = 0; i < cmyk.length; i++) {
			os.push(cmyk[i]);
		}
		return true;
	}
}
