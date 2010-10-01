// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.stacks;

import org.freehep.postscript.operators.ArithmeticOperator;
import org.freehep.postscript.operators.ArrayOperator;
import org.freehep.postscript.operators.ControlOperator;
import org.freehep.postscript.operators.ConversionOperator;
import org.freehep.postscript.operators.DeviceOperator;
import org.freehep.postscript.operators.DictionaryOperator;
import org.freehep.postscript.operators.ErrorOperator;
import org.freehep.postscript.operators.ExtraOperator;
import org.freehep.postscript.operators.FileOperator;
import org.freehep.postscript.operators.FontOperator;
import org.freehep.postscript.operators.FormOperator;
import org.freehep.postscript.operators.GeneralOperator;
import org.freehep.postscript.operators.GraphicsStateOperator;
import org.freehep.postscript.operators.MatrixOperator;
import org.freehep.postscript.operators.MemoryOperator;
import org.freehep.postscript.operators.MiscellaneousOperator;
import org.freehep.postscript.operators.OutputOperator;
import org.freehep.postscript.operators.PackedArrayOperator;
import org.freehep.postscript.operators.PaintingOperator;
import org.freehep.postscript.operators.PathOperator;
import org.freehep.postscript.operators.RelationalOperator;
import org.freehep.postscript.operators.StackOperator;
import org.freehep.postscript.operators.StringOperator;
import org.freehep.postscript.types.NameLookup;
import org.freehep.postscript.types.PSBoolean;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSInteger;
import org.freehep.postscript.types.PSName;
import org.freehep.postscript.types.PSNull;
import org.freehep.postscript.types.PSObject;

/**
 * OperandStack for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class DictionaryStack extends PostScriptStack implements NameLookup {
	private static final long serialVersionUID = 1L;
	private static final String notdef = ".notdef";

	private static String[] standardEncodingArray = { notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, "space", "exclam",
			"quotedbl", "numbersign", "dollar", "percent", "ampersand",
			"quoteright", "parenleft", "parenright", "asterisk", "plus",
			"comma", "hyphen", "period", "slash", "zero", "one", "two", "three",
			"four", "five", "six", "seven", "eight", "nine", "colon",
			"semicolon", "less", "equal", "greater", "question", "at", "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"bracketleft", "backslash", "bracketright", "asciicircum",
			"underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", "braceleft", "bar", "braceright",
			"asciitilde", notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, "exclamdown", "cent", "sterling",
			"fraction", "yen", "florin", "section", "currency", "quotesingle",
			"quotedblleft", "guillemotleft", "guilsinglleft", "guilsinglright",
			"fi", "fl", notdef, "endash", "dagger", "daggerdbl",
			"periodcentered", notdef, "paragraph", "bullet", "quotesinglbase",
			"quotedblbase", "quotedblright", "guillemotright", "ellipsis",
			"perthousand", notdef, "questiondown", notdef, "grave", "acute",
			"circumflex", "tilde", "macron", "breve", "dotaccent", "dieresis",
			notdef, "ring", "cedilla", notdef, "hungarumlaut", "ogonek",
			"caron", "emdash", notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, "AE", notdef, "ordfeminine", notdef, notdef,
			notdef, notdef, "Lslash", "Oslash", "OE", "ordmasculine", notdef,
			notdef, notdef, notdef, notdef, "ae", notdef, notdef, notdef,
			"dotlessi", notdef, notdef, "lslash", "oslash", "oe", "germandbls",
			notdef, notdef, notdef, notdef };

	private static String[] isoLatin1EncodingArray = { notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, "space", "exclam",
			"quotedbl", "numbersign", "dollar", "percent", "ampersand",
			"quoteright", "parenleft", "parenright", "asterisk", "plus",
			"comma", "minus", "period", "slash", "zero", "one", "two", "three",
			"four", "five", "six", "seven", "eight", "nine", "colon",
			"semicolon", "less", "equal", "greater", "question", "at", "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"bracketleft", "backslash", "bracketright", "asciicircum",
			"underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", "braceleft", "bar", "braceright",
			"asciitilde", notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, "dotlessi", "grave", "acute", "circumflex",
			"tilde", "macron", "breve", "dotaccent", "dieresis", notdef,
			"ring", "cedilla", notdef, "hungarumlaut", "ogonek", "caron",
			"space", "exclamdown", "cent", "sterling", "currency", "yen",
			"brokenbar", "section", "dieresis", "copyright", "ordfeminine",
			"guillemotleft", "logicalnot", "hyphen", "registered", "macron",
			"degree", "plusminus", "twosuperior", "threesuperior", "acute",
			"mu", "paragraph", "periodcentered", "cedilla", "onesuperior",
			"ordmasculine", "guillemotright", "onequarter", "onehalf",
			"threequarters", "questiondown", "Agrave", "Aacute", "Acircumflex",
			"Atilde", "Adieresis", "Aring", "AE", "Ccedilla", "Egrave",
			"Eacute", "Ecircumflex", "Edieresis", "Igrave", "Iacute",
			"Icircumflex", "Idieresis", "Eth", "Ntilde", "Ograve", "Oacute",
			"Ocircumflex", "Otilde", "Odieresis", "multiply", "Oslash",
			"Ugrave", "Uacute", "Ucircumflex", "Udieresis", "Yacute", "Thorn",
			"germandbls", "agrave", "aacute", "acircumflex", "atilde",
			"adieresis", "aring", "ae", "ccedilla", "egrave", "eacute",
			"ecircumflex", "edieresis", "igrave", "iacute", "icircumflex",
			"idieresis", "eth", "ntilde", "ograve", "oacute", "ocircumflex",
			"otilde", "odieresis", "divide", "oslash", "ugrave", "uacute",
			"ucircumflex", "udieresis", "yacute", "thorn", "ydieresis" };

	private static String[] symbolEncodingArray = { notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, "space", "exclam",
			"universal", "numbersign", "existential", "percent", "ampersand",
			"suchthat", "parenleft", "parenright", "asteriskmath", "plus",
			"comma", "minus", "period", "slash", "zero", "one", "two", "three",
			"four", "five", "six", "seven", "eight", "nine", "colon",
			"semicolon", "less", "equal", "greater", "question", "congruent",
			"Alpha", "Beta", "Chi", "Delta", "Epsilon", "Phi", "Gamma", "Eta",
			"Iota", "theta1", "Kappa", "Lambda", "Mu", "Nu", "Omicron", "Pi",
			"Theta", "Rho", "Sigma", "Tau", "Upsilon", "sigma1", "Omega", "Xi",
			"Psi", "Zeta", "bracketleft", "therefore", "bracketright",
			"perpendicular", "underscore", "radicalex", "alpha", "beta", "chi",
			"delta", "epsilon", "phi", "gamma", "eta", "iota", "phi1", "kappa",
			"lambda", "mu", "nu", "omicron", "pi", "theta", "rho", "sigma",
			"tau", "upsilon", "omega1", "omega", "xi", "psi", "zeta",
			"braceleft", "bar", "braceright", "similar", notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, "Euro",
			"Upsilon1", "minute", "lessequal", "fraction", "infinity",
			"florin", "club", "diamond", "heart", "spade", "arrowboth",
			"arrowleft", "arrowup", "arrowright", "arrowdown", "degree",
			"plusminus", "second", "greaterequal", "multiply", "proportional",
			"partialdiff", "bullet", "divide", "notequal", "equivalence",
			"approxequal", "ellipsis", "arrowvertex", "arrowhorizex",
			"carriagereturn", "aleph", "Ifraktur", "Rfraktur", "weierstrass",
			"circlemultiply", "circleplus", "emptyset", "intersection",
			"union", "propersuperset", "reflexsuperset", "notsubset",
			"propersubset", "reflexsubset", "element", "notelement", "angle",
			"gradient", "registerserif", "copyrightserif", "trademarkserif",
			"product", "radical", "dotmath", "logicalnot", "logicaland",
			"logicalor", "arrowdblboth", "arrowdblleft", "arrowdblup",
			"arrowdblright", "arrowdbldown", "lozenge", "angleleft",
			"registersans", "copyrightsans", "trademarksans", "summation",
			"parenlefttp", "parenleftex", "parenleftbt", "bracketlefttp",
			"bracketleftex", "bracketleftbt", "bracelefttp", "braceleftmid",
			"braceleftbt", "braceex", notdef, "angleright", "integral",
			"integraltp", "integralex", "integralbt", "parenrighttp",
			"parenrightex", "parenrightbt", "bracketrighttp", "bracketrightex",
			"bracketrightbt", "bracerighttp", "bracerightmid", "bracerightbt",
			notdef };

	private static String[] zapfDingbatsEncodingArray = { notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, "space", "a1",
			"a2", "a202", "a3", "a4", "a5", "a119", "a118", "a117", "a11",
			"a12", "a13", "a14", "a15", "a16", "a105", "a17", "a18", "a19",
			"a20", "a21", "a22", "a23", "a24", "a25", "a26", "a27", "a28",
			"a6", "a7", "a8", "a9", "a10", "a29", "a30", "a31", "a32", "a33",
			"a34", "a35", "a36", "a37", "a38", "a39", "a40", "a41", "a42",
			"a43", "a44", "a45", "a46", "a47", "a48", "a49", "a50", "a51",
			"a52", "a53", "a54", "a55", "a56", "a57", "a58", "a59", "a60",
			"a61", "a62", "a63", "a64", "a65", "a66", "a67", "a68", "a69",
			"a70", "a71", "a72", "a73", "a74", "a203", "a75", "a204", "a76",
			"a77", "a78", "a79", "a81", "a82", "a83", "a84", "a97", "a98",
			"a99", "a100", notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, notdef, notdef, notdef, notdef,
			notdef, notdef, notdef, notdef, "a101", "a102", "a103", "a104",
			"a106", "a107", "a108", "a112", "a111", "a110", "a109", "a120",
			"a121", "a122", "a123", "a124", "a125", "a126", "a127", "a128",
			"a129", "a130", "a131", "a132", "a133", "a134", "a135", "a136",
			"a137", "a138", "a139", "a140", "a141", "a142", "a143", "a144",
			"a145", "a146", "a147", "a148", "a149", "a150", "a151", "a152",
			"a153", "a154", "a155", "a156", "a157", "a158", "a159", "a160",
			"a161", "a163", "a164", "a196", "a165", "a192", "a166", "a167",
			"a168", "a169", "a170", "a171", "a172", "a173", "a162", "a174",
			"a175", "a176", "a177", "a178", "a179", "a193", "a180", "a199",
			"a181", "a200", "a182", notdef, "a201", "a183", "a184", "a197",
			"a185", "a194", "a198", "a186", "a195", "a187", "a188", "a189",
			"a190", "a191", notdef };

	private PSName errordict = new PSName("errordict");
	private PSName dollarerror = new PSName("$error");
	private PSName systemdict = new PSName("systemdict");
	private PSName userdict = new PSName("userdict");
	private PSName globaldict = new PSName("globaldict");
	private PSName statusdict = new PSName("statusdict");
	public static final PSName standardEncoding = new PSName("StandardEncoding");
	public static final PSName isoLatin1Encoding = new PSName(
			"ISOLatin1Encoding");
	public static final PSName symbolEncoding = new PSName("SymbolEncoding");
	public static final PSName zapfDingbatsEncoding = new PSName(
			"ZapfDingbatsEncoding");
	private PSName fontDirectory = new PSName("FontDirectory");
	private PSName globalFontDirectory = new PSName("GlobalFontDirectory");

	public DictionaryStack() {
		super();

		// fill error dictionary
		PSDictionary error = new PSDictionary();
		error.setName("errordict");
		ErrorOperator.register(error);

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

		// register all operator groups
		GeneralOperator.register(system);
		StackOperator.register(system);
		ArithmeticOperator.register(system);
		ArrayOperator.register(system);
		PackedArrayOperator.register(system);
		DictionaryOperator.register(system);
		StringOperator.register(system);
		RelationalOperator.register(system);
		ControlOperator.register(system);
		ConversionOperator.register(system);
		FileOperator.register(system);
		GraphicsStateOperator.register(system);
		DeviceOperator.register(system);
		MatrixOperator.register(system);
		MemoryOperator.register(system);
		MiscellaneousOperator.register(system);
		PathOperator.register(system);
		PaintingOperator.register(system);
		FormOperator.register(system);
		OutputOperator.register(system);
		FontOperator.register(system);
		ExtraOperator.register(system);

		// add encodings
		system.put(standardEncoding, standardEncodingArray);
		system.put(isoLatin1Encoding, isoLatin1EncodingArray);
		system.put(symbolEncoding, symbolEncodingArray);
		system.put(zapfDingbatsEncoding, zapfDingbatsEncodingArray);

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
		log.info("");
		log.info("== Top Dictionary Stack ==");
		super.printStack();
		log.info("== Bottom Dictionary Stack ==");
	}

	public String toString() {
		return "DictionaryStack";
	}
}