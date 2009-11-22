// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.lang.reflect.Field;

/**
 * OperandStack for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class DictionaryStack extends PostScriptStack implements NameLookup {

	private Class<?>[] operators = { GeneralOperator.class,
			StackOperator.class, ArithmeticOperator.class, ArrayOperator.class,
			PackedArrayOperator.class, DictionaryOperator.class,
			StringOperator.class, RelationalOperator.class,
			ControlOperator.class, ConversionOperator.class,
			FileOperator.class, GraphicsStateOperator.class,
			DeviceOperator.class, MatrixOperator.class, MemoryOperator.class,
			MiscellaneousOperator.class, PathOperator.class,
			PaintingOperator.class, FormOperator.class, OutputOperator.class,
			FontOperator.class, ExtraOperator.class };

	private static String[] standardEncodingArray = { ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			"space", "exclam", "quotedbl", "numbersign", "dollar", "percent",
			"ampersand", "quoteright", "parenleft", "parenright", "asterisk",
			"plus", "comma", "minus", "period", "slash", "zero", "one", "two",
			"three", "four", "five", "six", "seven", "eight", "nine", "colon",
			"semicolon", "less", "equal", "greater", "question", "at", "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"bracketleft", "backslash", "bracketright", "asciicircum",
			"underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", "braceleft", "bar", "braceright",
			"asciitilde", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			"exclamdown", "cent", "sterling", "fraction", "yen", "florin",
			"section", "currency", "quotesingle", "quotedblleft",
			"guillemotleft", "guilsinglleft", "guilsinglright", "fi", "fl",
			".notdef", "endash", "dagger", "daggerdbl", "periodcentered",
			".notdef", "paragraph", "bullet", "quotesinglbase", "quotedblbase",
			"quotedblright", "guillemotright", "ellipsis", "perthousand",
			".notdef", "questiondown", ".notdef", "grave", "acute",
			"circonflex", "tilde", "macron", "breve", "dotaccent", "dieresis",
			".notdef", "ring", "cedilla", ".notdef", "hungarumlaut", "ogonek",
			"caron", "emdash", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			"AE", ".notdef", "ordfeminine", ".notdef", ".notdef", ".notdef",
			".notdef", "Lslash", "Oslash", "OE", "ordmasculine", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", "ae", ".notdef",
			".notdef", ".notdef", "dotlessi", ".notdef", ".notdef", "lslash",
			"oslash", "oe", "germandbls", ".notdef", ".notdef", ".notdef",
			".notdef" };

	private static String[] isoLatin1EncodingArray = { ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			"space", "exclam", "quotedbl", "numbersign", "dollar", "percent",
			"ampersand", "quoteright", "parenleft", "parenright", "asterisk",
			"plus", "comma", "minus", "period", "slash", "zero", "one", "two",
			"three", "four", "five", "six", "seven", "eight", "nine", "colon",
			"semicolon", "less", "equal", "greater", "question", "at", "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"bracketleft", "backslash", "bracketright", "asciicircum",
			"underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", "braceleft", "bar", "braceright",
			"asciitilde", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", ".notdef", ".notdef", ".notdef", ".notdef", ".notdef",
			".notdef", "dotlessi", "grave", "acute", "circumflex", "tilde",
			"macron", "breve", "dotaccent", "dieresis", ".notdef", "ring",
			"cedilla", ".notdef", "hungarumlaut", "ogonek", "caron", "space",
			"exclamdown", "cent", "sterling", "currency", "yen", "brokenbar",
			"section", "dieresis", "copyright", "ordfeminine", "guillemotleft",
			"logicalnot", "hyphen", "registered", "macron", "degree",
			"plusminus", "twosuperior", "threesuperior", "acute", "mu",
			"paragraph", "periodcentered", "cedilla", "onesuperior",
			"ordmasculine", "guillemotright", "onequarter", "onehalf",
			"threequarters", "questiondown", "Agrave", "Aacute", "Acircumflex",
			"Atilde", "Adieresis", "Aring", "AE", "Ccedilla", "Egrave",
			"Eacute", "Ecircumflex", "Edieresis", "Igrave", "Iacute",
			"Icircumflex", "Idieresis", "Dcroat", "Ntilde", "Ograve", "Oacute",
			"Ocircumflex", "Otilde", "Odieresis", "multiply", "Oslash",
			"Ugrave", "Uacute", "Ucircumflex", "Udieresis", "Yacute", "Thorn",
			"germandbls", "agrave", "aacute", "acircumflex", "atilde",
			"adieresis", "aring", "ae", "ccedilla", "egrave", "eacute",
			"ecircumflex", "edieresis", "igrave", "iacute", "icircumflex",
			"idieresis", "dcroat", "ntilde", "ograve", "oacute", "ocircumflex",
			"otilde", "odieresis", "divide", "oslash", "ugrave", "uacute",
			"ucircumflex", "udieresis", "yacute", "thorn", "ydieresis" };

	private PSName errordict = new PSName("errordict");
	private PSName dollarerror = new PSName("$error");
	private PSName systemdict = new PSName("systemdict");
	private PSName userdict = new PSName("userdict");
	private PSName globaldict = new PSName("globaldict");
	private PSName statusdict = new PSName("statusdict");
	private PSName standardEncoding = new PSName("StandardEncoding");
	private PSName isoLatin1Encoding = new PSName("ISOLatin1Encoding");
	private PSName fontDirectory = new PSName("FontDirectory");
	private PSName globalFontDirectory = new PSName("GlobalFontDirectory");

	public DictionaryStack() {
		super();

		// fill error dictionary
		PSDictionary error = new PSDictionary();
		error.setName("errordict");
		Class<?>[] errorClass = ErrorOperator.operators;
		for (int i = 0; i < errorClass.length; i++) {
			addOperator(error, errorClass[i]);
		}

		// fill $error dictionary
		PSDictionary dollarErrorDict = new PSDictionary();
		dollarErrorDict.setName("$error");
		dollarErrorDict.put("newerror", new PSBoolean(false));
		dollarErrorDict.put("errorname", new PSNull());
		dollarErrorDict.put("command", new PSNull());
		dollarErrorDict.put("errorinfo", new PSNull());
		dollarErrorDict.put("ostack", new PSNull());
		dollarErrorDict.put("estack", new PSNull());
		dollarErrorDict.put("dstack", new PSNull());
		dollarErrorDict.put("recordstacks", new PSBoolean(true));
		dollarErrorDict.put("binary", new PSBoolean(false));

		PSDictionary user = new PSDictionary();
		user.setName("userdict");

		PSDictionary global = new PSDictionary();
		global.setName("globaldict");

		PSDictionary status = new PSDictionary();
		status.setName("statusdict");

		// fill system dictionary
		PSDictionary system = new PSDictionary();
		system.setName("systemdict");
		system.put(systemdict, system);
		system.put(errordict, error);
		system.put(dollarerror, dollarErrorDict);
		system.put(userdict, user);
		system.put(statusdict, status);
		system.put(globaldict, global);
		for (int i = 0; i < operators.length; i++) {
			try {
				Field f = operators[i].getField("operators");
				Class<?>[] opClass = (Class[]) f.get(null);
				for (int j = 0; j < opClass.length; j++) {
					addOperator(system, opClass[j]);
				}
			} catch (NoSuchFieldException e) {
				System.err.println("Error: " + operators[i]
						+ " does not have 'operators' field.");
			} catch (IllegalAccessException e) {
				System.err.println("Error: " + operators[i]
						+ ", no access to 'operators' field.");
			}
		}

		// add encodings
		system.put(standardEncoding, standardEncodingArray);
		system.put(isoLatin1Encoding, isoLatin1EncodingArray);

		// add font directories
		system.put(fontDirectory, new PSDictionary());
		// FIXME: should be a different directory
		system.put(globalFontDirectory, system.get(fontDirectory));

		// others
		system.put(new PSName("null"), new PSNull());
		system.put(new PSName("languagelevel"), new PSInteger(2));

		// create dictionary stack
		push(system);
		push(global);
		push(user);
	}

	private void addOperator(PSDictionary dict, Class<?> clazz) {
		try {
			PSOperator op = (PSOperator) clazz.newInstance();
			PSName key = new PSName(op.getName());
			if (dict.get(key) == null) {
				dict.put(key, op);
				op.setName(key.cvs());
			} else {
				System.out.println("Duplicate operator '" + key + "'");
				System.exit(1);
			}

		} catch (ClassCastException e) {
			System.err.println("Error: " + clazz
					+ " does not inherit from PSOperator.\n" + e);
		} catch (IllegalAccessException e) {
			System.err.println("Error: " + clazz + " cannot be instantiated.\n"
					+ e);
		} catch (InstantiationException e) {
			System.err.println("Error: " + clazz + " cannot be instantiated.\n"
					+ e);
		}
	}

	public Object push(Object o) {
		throw new IllegalArgumentException(
				"Only PSDictionary allowed on stack.");
	}

	public PSDictionary push(PSDictionary d) {
		return (PSDictionary) super.push(d);
	}

	public Object pop() {
		if (elementCount <= 3) {
			return null;
		}
		return super.pop();
	}

	public PSDictionary popDictionary() {
		return (PSDictionary) pop();
	}

	public void clear() {
		for (int i = 3; i < elementCount; i++) {
			elementData[i] = null;
		}
		elementCount = 3;
	}

	public PSDictionary systemDictionary() {
		return (PSDictionary) elementData[0];
	}

	public PSDictionary globalDictionary() {
		return (PSDictionary) elementData[1];
	}

	public PSDictionary userDictionary() {
		return (PSDictionary) elementData[2];
	}

	public PSDictionary errorDictionary() {
		return (PSDictionary) systemDictionary().get(errordict);
	}

	public PSDictionary dollarError() {
		return (PSDictionary) systemDictionary().get(dollarerror);
	}

	public PSDictionary currentDictionary() {
		return (PSDictionary) elementData[elementCount - 1];
	}

	public PSDictionary fontDirectory() {
		return (PSDictionary) systemDictionary().get(fontDirectory);
	}

	public PSDictionary globalFontDirectory() {
		return (PSDictionary) systemDictionary().get(globalFontDirectory);
	}

	public PSDictionary get(PSObject key) {
		for (int i = elementCount - 1; i >= 0; i--) {
			PSDictionary d = (PSDictionary) elementData[i];
			if (d.get(key) != null) {
				return d;
			}
		}
		return null;
	}

	public PSObject lookup(PSObject key) {
		PSDictionary d = get(key);
		return (d != null) ? d.get(key) : null;
	}

	public void printStack() {
		System.out.println();
		System.out.println("== Top Dictionary Stack ==");
		super.printStack();
		System.out.println("== Bottom Dictionary Stack ==");
	}

	public String toString() {
		return "DictionaryStack";
	}
}