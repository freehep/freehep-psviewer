// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSBoolean;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSNumber;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSPackedArray;

/**
 * Device Dependent Graphics Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class DeviceOperator extends AbstractOperator {

	// FIXME: most of these should be part of GState
	// FIXME: all are fake and ignored, just implemented as dummies
	PSDictionary halftone = new PSDictionary();
	{
		halftone.put("HalftoneType", 1);
		halftone.put("Frequency", 60);
		halftone.put("Angle", 0);
		halftone.put("SpotFunction", new PSPackedArray(new PSObject[0]));
	}
	boolean overprint = true;

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { SetHalftone.class,
				CurrentHalftone.class, SetScreen.class, CurrentScreen.class,
				SetColorScreen.class, CurrentColorScreen.class,
				SetTransfer.class, CurrentTransfer.class,
				SetColorTransfer.class, CurrentColorTransfer.class,
				SetBlackGeneration.class, CurrentBlackGeneration.class,
				SetUnderColorRemoval.class, CurrentUnderColorRemoval.class,
				SetColorRendering.class, CurrentColorRendering.class,
				SetFlat.class, CurrentFlat.class, SetOverprint.class,
				CurrentOverprint.class, SetSmoothness.class,
				CurrentSmoothness.class });
	}
}

class SetHalftone extends DeviceOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FIXME: ignored
		os.pop();
		return true;
	}
}

class CurrentHalftone extends DeviceOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(halftone);
		return true;
	}
}

class SetScreen extends DeviceOperator {
	// FIXME: ignored
	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class, PSPackedArray.class)) {
			os.pop();
			os.pop();
			os.pop();
		} else if (os.checkType(PSNumber.class, PSNumber.class,
				PSDictionary.class)) {
			os.pop();
			os.pop();
			os.pop();
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class CurrentScreen extends DeviceOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(halftone.get("Frequency"));
		os.push(halftone.get("Angle"));
		os.push(halftone);
		return true;
	}
}

class SetColorScreen extends DeviceOperator {

	// FIXME
	@Override
	public boolean execute(OperandStack os) {
		error(os, new Unimplemented());
		return true;
	}
}

class CurrentColorScreen extends DeviceOperator {

	// FIXME
	@Override
	public boolean execute(OperandStack os) {
		os.push(60);
		os.push(0);
		os.push(halftone);

		os.push(60);
		os.push(0);
		os.push(halftone);

		os.push(60);
		os.push(0);
		os.push(halftone);

		os.push(60);
		os.push(0);
		os.push(halftone);

		return true;
	}
}

class SetTransfer extends DeviceOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSPackedArray p = os.popPackedArray();
		os.gstate().setTransfer(p);
		return true;
	}
}

class CurrentTransfer extends DeviceOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().transfer());
		return true;
	}
}

class SetColorTransfer extends DeviceOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class, PSPackedArray.class,
				PSPackedArray.class, PSPackedArray.class };
	}

	// FIXME
	@Override
	public boolean execute(OperandStack os) {
		os.pop();
		os.pop();
		os.pop();
		PSPackedArray p = os.popPackedArray();
		os.gstate().setTransfer(p);
		return true;
	}
}

class CurrentColorTransfer extends DeviceOperator {

	// FIXME
	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().transfer());
		os.push(os.gstate().transfer());
		os.push(os.gstate().transfer());
		os.push(os.gstate().transfer());
		return true;
	}
}

class SetBlackGeneration extends DeviceOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSPackedArray p = os.popPackedArray();

		os.gstate().setBlackGeneration(p);
		return true;
	}
}

class CurrentBlackGeneration extends DeviceOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().blackGeneration());
		return true;
	}
}

class SetUnderColorRemoval extends DeviceOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSPackedArray p = os.popPackedArray();

		os.gstate().setUnderColorRemoval(p);
		return true;
	}
}

class CurrentUnderColorRemoval extends DeviceOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().underColorRemoval());
		return true;
	}
}

class SetColorRendering extends DeviceOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// FIXME
		os.pop();
		return true;
	}
}

class CurrentColorRendering extends DeviceOperator {

	// FIXME
	@Override
	public boolean execute(OperandStack os) {
		error(os, new Unimplemented());
		return true;
	}
}

class SetFlat extends DeviceOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSNumber n = os.popNumber();

		os.gstate().setFlat(n.getDouble());
		return true;
	}
}

class CurrentFlat extends DeviceOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.gstate().flat());
		return true;
	}
}

class SetOverprint extends DeviceOperator {
	{
		operandTypes = new Class[] { PSBoolean.class };
	}

	// FIXME
	@Override
	public boolean execute(OperandStack os) {
		overprint = os.popBoolean().getValue();
		return true;
	}
}

class CurrentOverprint extends DeviceOperator {

	// FIXME
	@Override
	public boolean execute(OperandStack os) {
		os.push(overprint);
		return true;
	}
}

class SetSmoothness extends DeviceOperator {
	{
		operandTypes = new Class[] { PSNumber.class };
	}

	// Level 3
	@Override
	public boolean execute(OperandStack os) {
		error(os, new Unimplemented());
		return true;
	}
}

class CurrentSmoothness extends DeviceOperator {

	// Level 3
	@Override
	public boolean execute(OperandStack os) {
		error(os, new Unimplemented());
		return true;
	}
}
