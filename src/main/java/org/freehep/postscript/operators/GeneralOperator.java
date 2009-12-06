// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.operators;

import java.io.IOException;

import org.freehep.postscript.errors.IOError;
import org.freehep.postscript.errors.NameNotFoundException;
import org.freehep.postscript.errors.SyntaxError;
import org.freehep.postscript.errors.SyntaxException;
import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.errors.Undefined;
import org.freehep.postscript.processor.LoopingContext;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSFile;
import org.freehep.postscript.types.PSGState;
import org.freehep.postscript.types.PSInputFile;
import org.freehep.postscript.types.PSInteger;
import org.freehep.postscript.types.PSName;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSOperator;
import org.freehep.postscript.types.PSPackedArray;
import org.freehep.postscript.types.PSString;

/**
 * General Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class GeneralOperator extends PSOperator {

	public static Class<?>[] operators = { Length.class, Get.class, Put.class,
			GetInterval.class, PutInterval.class, ALoad.class, Copy.class,
			ForAll.class, Token.class };
}

class Length extends GeneralOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSPackedArray.class)) {
			PSPackedArray a = os.popPackedArray();
			os.push(a.size());
		} else if (os.checkType(PSDictionary.class)) {
			PSDictionary d = os.popDictionary();
			os.push(d.size());
		} else if (os.checkType(PSString.class)) {
			PSString s = os.popString();
			os.push(s.size());
		} else if (os.checkType(PSName.class)) {
			PSName n = os.popName();
			os.push(n.size());
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Get extends GeneralOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSArray.class, PSInteger.class)
				|| os.checkType(PSPackedArray.class, PSInteger.class)) {
			PSInteger i = os.popInteger();
			PSArray a = os.popArray();
			if ((i.getValue() < 0) || (i.getValue() >= a.size())) {
				error(os, new RangeCheck());
			} else {
				os.push(a.get(i.getValue()));
			}
		} else if (os.checkType(PSDictionary.class, PSObject.class)) {
			PSObject key = os.popObject();
			PSDictionary d = os.popDictionary();
			PSObject o = d.get(key);
			if (o == null) {
				error(os, new Undefined());
			} else {
				os.push(o);
			}
		} else if (os.checkType(PSString.class, PSInteger.class)) {
			PSInteger i = os.popInteger();
			PSString s = os.popString();
			if ((i.getValue() < 0) || (i.getValue() >= s.size())) {
				error(os, new RangeCheck());
			} else {
				os.push(s.get(i.getValue()));
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class Put extends GeneralOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSArray.class, PSInteger.class, PSObject.class)) {
			PSObject o = os.popObject();
			PSInteger i = os.popInteger();
			PSArray a = os.popArray();
			if ((i.getValue() < 0) || (i.getValue() >= a.size())) {
				error(os, new RangeCheck());
			} else {
				a.set(i.getValue(), o);
			}
		} else if (os.checkType(PSDictionary.class, PSObject.class,
				PSObject.class)) {
			PSObject o = os.popObject();
			PSObject key = os.popObject();
			PSDictionary d = os.popDictionary();
			d.put(key, o);
			o.setName(key.cvs());
		} else if (os.checkType(PSString.class, PSInteger.class,
				PSInteger.class)) {
			PSInteger o = os.popInteger();
			PSInteger i = os.popInteger();
			PSString s = os.popString();
			if ((i.getValue() < 0) || (i.getValue() >= s.size())) {
				error(os, new RangeCheck());
			} else {
				s.set(i.getValue(), o.getValue());
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class GetInterval extends GeneralOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSArray.class, PSInteger.class, PSInteger.class)) {
			PSInteger c = os.popInteger();
			PSInteger i = os.popInteger();
			PSArray a = os.popArray();
			PSArray s = a.subArray(i.getValue(), c.getValue());
			if (s == null) {
				error(os, new RangeCheck());
			} else {
				os.push(s);
			}
		} else if (os.checkType(PSPackedArray.class, PSInteger.class,
				PSInteger.class)) {
			PSInteger c = os.popInteger();
			PSInteger i = os.popInteger();
			PSPackedArray a = os.popPackedArray();
			PSPackedArray s = a.subPackedArray(i.getValue(), c.getValue());
			if (s == null) {
				error(os, new RangeCheck());
			} else {
				os.push(s);
			}
		} else if (os.checkType(PSString.class, PSInteger.class,
				PSInteger.class)) {
			PSInteger c = os.popInteger();
			PSInteger i = os.popInteger();
			PSString s = os.popString();
			PSString ss = s.subString(i.getValue(), c.getValue());
			if (ss == null) {
				error(os, new RangeCheck());
			} else {
				os.push(ss);
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class PutInterval extends GeneralOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSArray.class, PSInteger.class, PSPackedArray.class)) {
			PSPackedArray a2 = os.popPackedArray();
			PSInteger i = os.popInteger();
			PSArray a1 = os.popArray();
			if ((i.getValue() < 0) || (i.getValue() + a2.size() > a1.size())) {
				error(os, new RangeCheck());
			} else {
				for (int j = 0; j < a2.size(); j++) {
					a1.set(i.getValue() + j, a2.get(j));
				}
			}
		} else if (os
				.checkType(PSString.class, PSInteger.class, PSString.class)) {
			PSString s2 = os.popString();
			PSInteger i = os.popInteger();
			PSString s1 = os.popString();
			if ((i.getValue() < 0) || (i.getValue() + s2.size() > s1.size())) {
				error(os, new RangeCheck());
			} else {
				for (int j = 0; j < s2.size(); j++) {
					s1.set(i.getValue() + j, s2.get(j));
				}
			}
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class ALoad extends GeneralOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class };
	}

	// FIXME nothing done about InvalidAccess
	@Override
	public boolean execute(OperandStack os) {
		PSArray a = os.popArray();
		for (int i = 0; i < a.size(); i++) {
			os.push(a.get(i));
		}
		os.push(a);
		return true;
	}
}

class Copy extends GeneralOperator {

	// FIXME: access checks not done
	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSInteger.class)) {
			int n = os.popInteger().getValue();
			if ((n < 0) || (n > os.size())) {
				error(os, new RangeCheck());
			} else {
				os.copy(n);
			}
		} else {
			if (os.checkType(PSArray.class, PSArray.class)
					|| os.checkType(PSPackedArray.class, PSArray.class)) {
				PSArray a2 = os.popArray();
				PSPackedArray a1 = os.popPackedArray();
				if (a2.size() < a1.size()) {
					error(os, new RangeCheck());
				} else {
					for (int i = 0; i < a1.size(); i++) {
						a2.set(i, a1.get(i));
					}
					os.push(a2.subArray(0, a1.size()));
				}
			} else if (os.checkType(PSDictionary.class, PSDictionary.class)) {
				PSDictionary d2 = os.popDictionary();
				PSDictionary d1 = os.popDictionary();
				d1.copyInto(d2);
				os.push(d2);
			} else if (os.checkType(PSString.class, PSString.class)) {
				PSString s2 = os.popString();
				PSString s1 = os.popString();
				if (s2.size() < s1.size()) {
					error(os, new RangeCheck());
				} else {
					for (int i = 0; i < s1.size(); i++) {
						s2.set(i, s1.get(i));
					}
					os.push(s2.subString(0, s1.size()));
				}
			} else if (os.checkType(PSGState.class, PSGState.class)) {
				PSGState g2 = os.popGState();
				PSGState g1 = os.popGState();
				g1.copyInto(g2);
				os.push(g2);
			} else {
				error(os, new TypeCheck());
			}
		}
		return true;
	}
}

class ForAll extends GeneralOperator implements LoopingContext {

	private int index;
	private PSPackedArray array;
	private Object[] keys;
	private PSDictionary dictionary;
	private PSString string;
	private PSPackedArray proc;

	private ForAll(PSPackedArray a, PSPackedArray p) {
		array = a;
		proc = p;
		index = 0;
	}

	private ForAll(PSDictionary d, PSPackedArray p) {
		dictionary = d;
		proc = p;
		keys = dictionary.keys();
		index = 0;
	}

	private ForAll(PSString s, PSPackedArray p) {
		string = s;
		proc = p;
		index = 0;
	}

	public ForAll() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (proc == null) {
			if (os.checkType(PSPackedArray.class, PSPackedArray.class)) {
				PSPackedArray p = os.popPackedArray();
				PSPackedArray a = os.popPackedArray();
				os.execStack().pop();
				os.execStack().push(new ForAll(a, p));
				return false;
			} else if (os.checkType(PSDictionary.class, PSPackedArray.class)) {
				PSPackedArray p = os.popPackedArray();
				PSDictionary d = os.popDictionary();
				os.execStack().pop();
				os.execStack().push(new ForAll(d, p));
				return false;
			} else if (os.checkType(PSString.class, PSPackedArray.class)) {
				PSPackedArray p = os.popPackedArray();
				PSString s = os.popString();
				os.execStack().pop();
				os.execStack().push(new ForAll(s, p));
				return false;
			} else {
				error(os, new TypeCheck());
			}
		} else {
			if (array != null) {
				if (index < array.size()) {
					os.push(array.get(index));
					os.execStack().push(proc);
					index++;
					return false;
				} else {
					return true;
				}
			} else if (dictionary != null) {
				if (index < keys.length) {
					Object value = dictionary.get((PSObject) keys[index]);
					if (value != null) {
						os.push(keys[index]);
						os.push(value);
						os.execStack().push(proc);
					}
					index++;
					return false;
				} else {
					return true;
				}
			} else if (string != null) {
				if (index < string.size()) {
					os.push(string.get(index));
					os.execStack().push(proc);
					index++;
					return false;
				} else {
					return true;
				}
			} else {
				log.severe("Fatal Error in ForAll");
			}
		}
		return false;
	}
}

class Token extends GeneralOperator {

	@Override
	public boolean execute(OperandStack os) {
		try {
			if (os.checkType(PSString.class)) {
				PSString string = os.popString();
				PSObject obj = string.token(os.packingMode(), os.dictStack());
				if (obj != null) {
					os.push(string);
					os.push(obj);
					os.push(true);
				} else {
					os.push(false);
				}
			} else if (os.checkType(PSFile.class)) {
				PSInputFile file = (PSInputFile) os.popFile();
				PSObject obj = file.token(os.packingMode(), os.dictStack());
				if (obj != null) {
					os.push(obj);
					os.push(true);
				} else {
					os.push(false);
				}
			} else {
				error(os, new TypeCheck());
			}
		} catch (ClassCastException e) {
			error(os, new InvalidAccess());
		} catch (IOException e) {
			error(os, new IOError(e));
		} catch (SyntaxException e) {
			error(os, new SyntaxError());
		} catch (NameNotFoundException e) {
			error(os, new Undefined());
		}
		return true;
	}
}
