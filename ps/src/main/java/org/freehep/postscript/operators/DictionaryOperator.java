// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import org.freehep.postscript.errors.Undefined;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSInteger;
import org.freehep.postscript.types.PSMark;
import org.freehep.postscript.types.PSObject;

/**
 * Dictionary Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class DictionaryOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { Dict.class,
				StartDictionary.class, EndDictionary.class, MaxLength.class,
				Begin.class, End.class, Def.class, Load.class, Store.class,
				Undef.class, Known.class, Where.class, CurrentDict.class,
				CountDictStack.class, DictStack.class, ClearDictStack.class });
	}
}

class Dict extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSInteger.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSInteger n = os.popInteger();
		if (n.getValue() < 0) {
			error(os, new RangeCheck());
		} else {
			os.push(new PSDictionary(n.getValue()));
		}
		return true;
	}
}

class StartDictionary extends DictionaryOperator {

	@Override
	public String getName() {
		return "<<";
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(new PSMark());
		return true;
	}
}

class EndDictionary extends DictionaryOperator {

	@Override
	public String getName() {
		return ">>";
	}

	// FREEHEP-145: nothing done about InvalidAccess
	@Override
	public boolean execute(OperandStack os) {
		int n = os.countToMark();
		if (n < 0) {
			error(os, new UnmatchedMark());
		} else {
			n = n / 2;
			PSDictionary d = new PSDictionary(n);
			for (int i = 0; i < n; i++) {
				if (os.size() < 2) {
					error(os, new RangeCheck());
				}
				PSObject value = os.popObject();
				PSObject key = os.popObject();

				d.put(key, value);
			}
			/* PSMark mark = */os.popMark();

			os.push(d);
		}
		return true;
	}
}

class MaxLength extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSDictionary d = os.popDictionary();
		os.push(d.capacity());
		return true;
	}
}

class Begin extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		os.dictStack().push(os.popDictionary());
		return true;
	}
}

class End extends DictionaryOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (os.dictStack().pop() == null) {
			error(os, new DictStackUnderflow());
		}
		return true;
	}
}

class Def extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject value = os.popObject();
		PSObject key = os.popObject();
		PSDictionary d = os.dictStack().currentDictionary();
		d.put(key, value);
		value.setName(key.cvs());
		return true;
	}
}

class Load extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject key = os.popObject();
		PSObject value = os.dictStack().lookup(key);
		if (value == null) {
			error(os, new Undefined());
		} else {
			os.push(value);
		}
		return true;
	}
}

class Store extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSObject.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject value = os.popObject();
		PSObject key = os.popObject();
		PSDictionary d = os.dictStack().get(key);
		if (d == null) {
			os.dictStack().currentDictionary().put(key, value);
		} else {
			d.put(key, value);
		}
		return true;
	}
}

class Undef extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSDictionary.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject key = os.popObject();
		PSDictionary d = os.popDictionary();
		d.remove(key);
		return true;
	}
}

class Known extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSDictionary.class, PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject key = os.popObject();
		PSDictionary d = os.popDictionary();
		if (d.get(key) != null) {
			os.push(true);
		} else {
			os.push(false);
		}
		return true;
	}
}

class Where extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSObject key = os.popObject();
		PSDictionary d = os.dictStack().get(key);
		if (d == null) {
			os.push(false);
		} else {
			os.push(d);
			os.push(true);
		}
		return true;
	}
}

class CurrentDict extends DictionaryOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.dictStack().currentDictionary());
		return true;
	}
}

class CountDictStack extends DictionaryOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.push(os.dictStack().size());
		return true;
	}
}

class DictStack extends DictionaryOperator {
	{
		operandTypes = new Class[] { PSArray.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		PSArray a = os.popArray();
		if (os.dictStack().size() > a.size()) {
			error(os, new RangeCheck());
		} else {
			os.dictStack().copyInto(a);
			os.push(a.subArray(0, os.dictStack().size()));
		}
		return true;
	}
}

class ClearDictStack extends DictionaryOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.dictStack().clear();
		return true;
	}
}
