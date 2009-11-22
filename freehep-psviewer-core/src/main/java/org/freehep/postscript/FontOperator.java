// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Font Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/FontOperator.java
 *          829a8d93169a 2006/12/08 09:03:07 duns $
 */
public class FontOperator extends PSOperator {

	protected static FontCache fontCache;

	static {
		fontCache = new FontCache();

		// add standard 14 entries (PDF like)
		// QUESTION: should we point to Lucida Fonts!
		fontCache.put("Courier", fontCache.get("Monospaced.plain"));
		fontCache.put("Courier-Bold", fontCache.get("Monospaced.bold"));
		fontCache.put("Courier-Oblique", fontCache.get("Monospaced.italic"));
		fontCache.put("Courier-BoldOblique", fontCache
				.get("Monospaced.bolditalic"));
		fontCache.put("Helvetica", fontCache.get("SansSerif.plain"));
		fontCache.put("Helvetica-Bold", fontCache.get("SansSerif.bold"));
		fontCache.put("Helvetica-Oblique", fontCache.get("SansSerif.italic"));
		fontCache.put("Helvetica-BoldOblique", fontCache
				.get("SansSerif.bolditalic"));
		fontCache.put("Times-Roman", fontCache.get("Serif.plain"));
		fontCache.put("Times-Bold", fontCache.get("Serif.bold"));
		fontCache.put("Times-Italic", fontCache.get("Serif.italic"));
		fontCache.put("Times-BoldItalic", fontCache.get("Serif.bolditalic"));

		// FIXME: windows specific, does not work, handled in font selection
		// fontCache.put("ZapfDingbats", fontCache.get("Wingdings"));
		// fontCache.put("Symbol", fontCache.get("Symbol"));

		/*
		 * // add extra entries for testing (FIXME: this should all be read from
		 * a file) fontCache.put("Palatino-Roman",
		 * fontCache.get("Lucida Bright-Regular"));
		 * fontCache.put("Palatino-Italic",
		 * fontCache.get("Lucida Bright-Italic"));
		 * fontCache.put("Palatino-Bold",
		 * fontCache.get("Lucida Bright-Demibold"));
		 * fontCache.put("Palatino-BoldItalic",
		 * fontCache.get("Lucida Bright-DemiboldItalic"));
		 * 
		 * fontCache.put("Optima", fontCache.get("Lucida Sans-Regular")); // No
		 * Oblique anymore (after 1.3?) fontCache.put("Optima-Oblique",
		 * fontCache.get("Lucida Sans-Regular")); fontCache.put("Optima-Bold",
		 * fontCache.get("Lucida Sans-Demibold")); // No Oblique anymore (after
		 * 1.3?) fontCache.put("Optima-BoldOblique",
		 * fontCache.get("Lucida Sans-Demibold"));
		 * 
		 * fontCache.put("ZapfChancery-MediumItalic",fontCache.get(
		 * "Lucida Bright-DemiboldItalic"));
		 */
	}

	public static Class[] operators = { DefineFont.class, ComposeFont.class,
			UndefineFont.class, FindFont.class, ScaleFont.class,
			MakeFont.class, SetFont.class, RootFont.class, CurrentFont.class,
			SelectFont.class, Show.class, AShow.class, WidthShow.class,
			AWidthShow.class, XShow.class, XYShow.class, YShow.class,
			GlyphShow.class, StringWidth.class, CShow.class, KShow.class,
			FindEncoding.class, SetCacheDevice.class, SetCacheDevice2.class,
			SetCharWidth.class };

	protected PSDictionary findFont(PSDictionary fontDirectory, PSName key) {
		PSDictionary font = (PSDictionary) fontDirectory.get(key);
		if (font == null) {
			String fontName = key.getValue();
			String encoding = "STDLatin";
			if (fontName.equals("Symbol")) {
				encoding = "Symbol";
				fontName = "SansSerif.plain"; // 31 chars missing, mainly math
			} else if (fontName.equals("ZapfDingbats")) {
				encoding = "Zapfdingbats";
				fontName = "SansSerif.plain";
			}

			Font javaFont = fontCache.get(fontName);
			font = new PSFontDictionary(javaFont, encoding);
		}
		return font;
	}

	protected void defineFont(PSDictionary fontDirectory, PSName key,
			PSDictionary font) {

		// FREEHEP-149: no check done on the font
		font.put("FID", new PSFontID());
		font.changeAccess(PSComposite.READ_ONLY);
		fontDirectory.put(key, font);
	}

	protected PSDictionary makeFont(PSDictionary font, double[] matrix) {
		PSJavaFont psfont = (PSJavaFont) font.get("javafont");
		if (psfont == null) {
			double[] cfm = font.getPackedArray("FontMatrix").toDoubles();
			AffineTransform at = new AffineTransform(cfm);
			at.concatenate(new AffineTransform(matrix));
			at.getMatrix(cfm);
			PSDictionary fontCopy = (PSDictionary) font.copy();
			fontCopy.put("FontMatrix", new PSArray(cfm));
			return fontCopy;
		} else {
			String encoding = font.getString("javaEncoding");
			Font javaFont = psfont.getFont();
			AffineTransform at = new AffineTransform(matrix);
			at.concatenate(javaFont.getTransform());
			javaFont = javaFont.deriveFont(at);

			return new PSFontDictionary(javaFont, encoding);
		}
	}

	protected void setFont(OperandStack os, PSDictionary font) {
		// FIXME: is this valid for type 1 fonts?
		font.put("_CachedGlyphs", new PSDictionary());
		os.gstate().setFont(font);
	}

	protected PSGlyph getCachedGlyph(PSDictionary font, PSName name) {
		PSDictionary cache = font.getDictionary("_CachedGlyphs");
		if (cache == null) {
			cache = new PSDictionary();
			font.put("_CachedGlyphs", cache);
		}
		PSGlyph glyph = (PSGlyph) cache.get(name);
		if (glyph == null) {
			glyph = new PSGlyph();
			cache.put(name, glyph);
		}
		font.put("_CurrentGlyph", glyph);
		return glyph;
	}

	protected void show(OperandStack os, int cc) {
		show(os, cc, null);
	}

	protected void show(OperandStack os, int cc, PSName name) {
		PSGState gs = os.gstate();
		PSDictionary font = gs.font();
		show(os, gs, font, cc, name);
	}

	// CHECK: this static? prevents 2 ps processors running at the same time.
	// maybe should move to the graphics state...
	protected PSGlyph currentGlyph;

	protected void show(OperandStack os, PSGState gs, PSDictionary font,
			int cc, PSName name) {
		int type = font.getInteger("FontType");
		PSPackedArray encoding = font.getPackedArray("Encoding");
		if (name == null) {
			if (type == 0) {
				name = new PSName(".notdef");
			} else {
				name = encoding.getName(cc);
			}
		}
		// System.out.println("Switched to Font "+font.get("FontName")+" "+type+" "+name);
		currentGlyph = getCachedGlyph(font, name);

		switch (type) {
		case 0: // Composed fonts
			// FIXME: nested Type 0 fonts not handled
			PSPackedArray fontList = font.getPackedArray("FDepVector");
			PSDictionary currentFont = font.getDictionary("_CurrentFont");
			switch (font.getInteger("FMapType")) {
			case 2: // 8/8 Mapping
				if ((currentFont == null) || (font.getBoolean("_ChangeFont"))) {
					currentFont = setCurrentFont(cc, font, encoding, fontList);
					return;
				}

				show(os, gs, currentFont, cc, null);
				font.put("_ChangeFont", true);
				break;

			case 3: // Escape Mapping
				if (currentFont == null) {
					currentFont = setCurrentFont(0, font, encoding, fontList);
				}

				if (font.getBoolean("_ChangeFont")) {
					currentFont = setCurrentFont(cc, font, encoding, fontList);
					return;
				}

				if (cc == font.getInteger("EscChar")) {
					font.put("_ChangeFont", true);
					return;
				}

				show(os, gs, currentFont, cc, null);
				break;

			case 4: // 1/7 Mapping
				currentFont = setCurrentFont((cc >> 7) & 0x01, font, encoding,
						fontList);
				show(os, gs, currentFont, cc & 0x7f, null);
				break;

			case 5: // 9/7 Mapping
				if ((currentFont == null) || (!font.getBoolean("_ChangeFont"))) {
					font.put("_FontOffset", cc << 1);
					font.put("_ChangeFont", true);
					return;
				}

				int fontNumber = font.getInteger("_FontOffset");
				fontNumber += ((cc >> 7) & 0x01);
				currentFont = setCurrentFont(fontNumber, font, encoding,
						fontList);
				show(os, gs, currentFont, cc & 0x7f, null);
				break;

			case 6: // SubsVector Mapping
				System.out
						.println("Type 0 font with SubsVector Mapping ignored.");
				break;

			case 7: // Double Escape Mapping
				if (currentFont == null) {
					currentFont = setCurrentFont(0, font, encoding, fontList);
				}

				if (font.getBoolean("_ChangeFont")) {
					if (cc == font.getInteger("EscChar")) {
						font.put("_FontOffset", 256);
					} else {
						cc += font.getInteger("_FontOffset");
						currentFont = setCurrentFont(cc, font, encoding,
								fontList);
					}
					return;
				}

				if (cc == font.getInteger("EscChar")) {
					font.put("_ChangeFont", true);
					return;
				}

				show(os, gs, currentFont, cc, null);
				break;

			case 8: // Shift Mapping
				if (currentFont == null) {
					currentFont = setCurrentFont(0, font, encoding, fontList);
					if (font.get("ShiftIn") == null) {
						font.put("ShiftIn", 15);
					}
					if (font.get("ShiftOut") == null) {
						font.put("ShiftOut", 14);
					}
				}

				if (cc == font.getInteger("ShiftIn")) {
					currentFont = setCurrentFont(0, font, encoding, fontList);
					return;
				} else if (cc == font.getInteger("ShiftOut")) {
					currentFont = setCurrentFont(1, font, encoding, fontList);
					return;
				}

				show(os, gs, currentFont, cc, null);
				break;

			case 9: // CMap Mapping
				System.out.println("Type 0 font with CMap Mapping ignored.");
				break;

			default:
				System.out.println("Type 0 font with invalid FMapType "
						+ font.getInteger("FMapType") + " ignored.");
				break;
			}

			break;

		case 1: // Quasi Type 1 Font, FIXME... for caching...
			PSDictionary charstrings = font.getDictionary("CharStrings");
			PSObject obj = charstrings.get(name);
			if (obj instanceof PSGlyph) {
				show(os, gs, (PSGlyph) obj);
			} else if (obj instanceof PSPackedArray) {
				// FIXME does not handle FontMatrix
				// FIXME does only work for glyphshow, refer page 352
				os.push(os.dictStack().systemDictionary());
				os.push(font);
				os.push(name); // FIXME should be CC for a show!
				os.execStack().push(obj);
				// FIXME: we should pop the 2 dictionaries, but how?
			} else if (obj instanceof PSString) {
				PSCharStringDecoder decoder = new PSCharStringDecoder(os
						.dictStack().systemDictionary());
				try {
					PSGlyph glyph = decoder.decode((PSString) obj);
					charstrings.put(name, glyph);
					show(os, gs, glyph);
				} catch (IOException e) {
					System.err.println("IOError while reading charstring '"
							+ name + "'.");
				}
			} else {
				System.out.println("Show Ignored " + obj);
			}
			break;

		case 3: // Type 3 Font
			PSPackedArray proc = font.getPackedArray("BuildGlyph");
			if (proc != null) {
				// BuildGlyph
				os.push(font);
				os.push(name);
			} else {
				// BuildChar
				proc = font.getPackedArray("BuildChar");
				os.push(font);
				os.push(cc);
			}
			os.execStack().push(new GRestore());
			os.execStack().push(proc);
			os.execStack().push(new GSave());
			break;

		default:
			os.execStack().pop();
			error(os, new InvalidFont());
			break;
		}

	}

	private void show(OperandStack os, PSGState gs, PSGlyph g) {
		currentGlyph.wy = g.wy;
		currentGlyph.wx = g.wx;
		currentGlyph.llx = g.llx;
		currentGlyph.urx = g.urx;
		if (g instanceof PSJavaGlyph) {
			// FIXME: no idea why the 0.5 factor is here
			// seems like the lsb given by Java is not
			// really accurate, especially with derived (small) fonts
			gs.show(((PSJavaGlyph) g).getGlyph(), (float) (currentGlyph
					.getLSB() * 0.5), 0);
		} else {
			// Embedded type1 font procedure
			os.execStack().push(new GRestore());
			os.execStack().push(((PSType1Glyph) g).getProc());
			os.execStack().push(new GSave());
		}
	}

	private PSDictionary setCurrentFont(int fontNumber, PSDictionary type0font,
			PSPackedArray encoding, PSPackedArray fontList) {
		int fontIndex = encoding.getInteger(fontNumber);
		PSDictionary currentFont = fontList.getDictionary(fontIndex);
		type0font.put("_CurrentFont", currentFont);
		type0font.put("_FontOffset", 0);
		type0font.put("_ChangeFont", false);
		return currentFont;
	}

	public static float stringWidth(OperandStack os, int cc) {
		PSGState gs = os.gstate();
		PSDictionary font = gs.font();
		// FIXME: only works for type 1 and 3
		PSName name;
		switch (font.getInteger("FontType")) {
		case 1:
		case 3:
			name = font.getPackedArray("Encoding").getName(cc);
			break;

		default:
			name = new PSName(".notdef");
			break;
		}
		return stringWidth(font, name);
	}

	protected static float stringWidth(PSDictionary font, PSName name) {
		PSDictionary metrics = font.getDictionary("Metrics");
		// FIXME: not correct
		if (metrics == null) {
			return 0.0f;
		}

		PSObject obj = metrics.get(name);
		// FIXME: obj may be an array of 2 or 4
		float width = ((PSNumber) obj).getFloat();
		// System.out.println(name+" "+width);
		return width;
	}

	public boolean execute(OperandStack os) {
		throw new RuntimeException("Cannot execute class: " + getClass());
	}
}

class DefineFont extends FontOperator {
	{
		operandTypes = new Class[] { PSName.class, PSDictionary.class };
	}

	public boolean execute(OperandStack os) {
		PSDictionary font = os.popDictionary();
		PSName name = os.popName();
		defineFont(os.dictStack().fontDirectory(), name, font);
		os.push(font);
		return true;
	}
}

class ComposeFont extends FontOperator {
	{
		operandTypes = new Class[] { PSName.class, PSObject.class,
				PSPackedArray.class };
	}

	public boolean execute(OperandStack os) {
		// Level 3
		error(os, new Unimplemented());
		return true;
	}
}

class UndefineFont extends FontOperator {
	{
		operandTypes = new Class[] { PSName.class };
	}

	public boolean execute(OperandStack os) {
		PSName key = os.popName();
		os.dictStack().fontDirectory().remove(key);
		return true;
	}
}

class FindFont extends FontOperator {
	{
		operandTypes = new Class[] { PSName.class };
	}

	public boolean execute(OperandStack os) {
		PSName name = os.popName();
		PSDictionary font = findFont(os.dictStack().fontDirectory(), name);
		if (font == null) {
			error(os, new InvalidFont());
		} else {
			defineFont(os.dictStack().fontDirectory(), name, font);
			os.push(font);
		}
		return true;
	}
}

class ScaleFont extends FontOperator {
	{
		operandTypes = new Class[] { PSDictionary.class, PSNumber.class };
	}

	public boolean execute(OperandStack os) {
		double scale = os.popNumber().getDouble();
		PSDictionary font = os.popDictionary();

		os.push(makeFont(font, new double[] { scale, 0, 0, scale, 0, 0 }));
		return true;
	}
}

class MakeFont extends FontOperator {
	{
		operandTypes = new Class[] { PSDictionary.class, PSPackedArray.class };
	}

	public boolean execute(OperandStack os) {
		PSPackedArray matrix = os.popPackedArray();
		PSDictionary font = os.popDictionary();
		os.push(makeFont(font, matrix.toDoubles()));
		return true;
	}
}

class SetFont extends FontOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	public boolean execute(OperandStack os) {
		PSDictionary font = os.popDictionary();
		setFont(os, font);
		return true;
	}
}

class RootFont extends FontOperator {

	public boolean execute(OperandStack os) {
		// FIXME: wrong for type 0 font
		os.push(os.gstate().font());
		return true;
	}
}

class CurrentFont extends FontOperator {

	public boolean execute(OperandStack os) {
		os.push(os.gstate().font());
		return true;
	}
}

class SelectFont extends FontOperator {
	{
		operandTypes = new Class[] { PSName.class, PSObject.class };
	}

	public boolean execute(OperandStack os) {
		PSName key;
		PSPackedArray matrix;
		if (os.checkType(PSName.class, PSNumber.class)) {
			double scale = os.popNumber().getDouble();
			matrix = new PSPackedArray(
					new double[] { scale, 0, 0, scale, 0, 0 });
			key = os.popName();
		} else if (os.checkType(PSName.class, PSPackedArray.class)) {
			matrix = os.popPackedArray();
			key = os.popName();
		} else {
			error(os, new TypeCheck());
			return true;
		}

		PSDictionary font = findFont(os.dictStack().fontDirectory(), key);
		if (font == null) {
			error(os, new InvalidFont());
			return true;
		}

		defineFont(os.dictStack().fontDirectory(), key, font);

		// FIXME: should be a copy
		// font = makeFont((PSDictionary)font.copy(), matrix.toDoubles());
		font = makeFont(font, matrix.toDoubles());

		setFont(os, font);

		return true;
	}
}

class Show extends FontOperator {
	private String text;
	private int index;
	private double sx, sy;

	public Show() {
	}

	public Show(String t, double sx, double sy) {
		text = t;
		index = -1;
		this.sx = sx;
		this.sy = sy;
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();
		if (text == null) {
			if (!os.checkType(PSString.class)) {
				error(os, new TypeCheck());
				return true;
			}
			Point2D point = gs.position();
			if (point == null) {
				error(os, new NoCurrentPoint());
				return true;
			}
			float x0 = (float) point.getX();
			float y0 = (float) point.getY();

			String t = os.popString().getValue();

			double[] cfm = gs.font().getPackedArray("FontMatrix").toDoubles();
			AffineTransform at = new AffineTransform(cfm[0], cfm[1], cfm[2],
					cfm[3], x0, y0);
			double sx = at.getScaleX();
			double sy = at.getScaleY();

			os.execStack().pop();
			os.execStack().push(new Show(t, sx, sy));
			os.gsave();

			gs.transform(at);

			return false;
		}

		if (index >= 0) {
			// System.out.println("Width "+currentGlyph.wx+" "+currentGlyph.getLSB()+" "+currentGlyph.getRSB()+" "+gs.font().get("FontName"));
			gs.translate(currentGlyph.wx, currentGlyph.wy);
		}

		index++;

		if (index >= text.length()) {
			Point2D t = gs.position();
			os.grestore();
			Point2D p = os.gstate().position();
			os.gstate().path().moveTo((float) (p.getX() - (t.getX() * sx)),
					(float) (p.getY() - (t.getY() * sy)));
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		return false;
	}
}

class AShow extends FontOperator {

	private String text;
	private int index;
	private double ax, ay;

	public AShow() {
	}

	public AShow(String t, double xa, double ya) {
		text = t;
		ax = xa;
		ay = ya;
		index = 0;
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();

		if (text == null) {
			if (!os.checkType(PSNumber.class, PSNumber.class, PSString.class)) {
				error(os, new TypeCheck());
				return true;
			}

			Point2D point = gs.position();
			if (point == null) {
				error(os, new NoCurrentPoint());
				return true;
			}
			float x0 = (float) point.getX();
			float y0 = (float) point.getY();

			String t = os.popString().getValue();
			double ya = os.popNumber().getDouble();
			double xa = os.popNumber().getDouble();
			os.execStack().pop();
			os.execStack().push(new AShow(t, ya, xa));
			os.gsave();

			double[] cfm = gs.font().getPackedArray("FontMatrix").toDoubles();
			AffineTransform at = new AffineTransform(cfm[0], cfm[1], cfm[2],
					cfm[3], x0, y0);
			gs.transform(at);

			return false;
		}

		if (index >= 0) {
			os.gstate().translate(currentGlyph.wx + ax, currentGlyph.wy + ay);
		}

		index++;

		if (index >= text.length()) {
			os.grestore();
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		return false;
	}
}

class WidthShow extends FontOperator {

	private String text;
	private int index;
	private int chd;
	private double cx, cy;

	public WidthShow(String t, int c, double xc, double yc) {
		text = t;
		index = -1;
		chd = c;
		cx = xc;
		cy = yc;
	}

	public WidthShow() {
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();

		if (text == null) {
			if (!os.checkType(PSNumber.class, PSNumber.class, PSInteger.class,
					PSString.class)) {
				error(os, new TypeCheck());
				return true;
			}

			Point2D point = gs.position();
			if (point == null) {
				error(os, new NoCurrentPoint());
				return true;
			}
			float x0 = (float) point.getX();
			float y0 = (float) point.getY();

			String t = os.popString().getValue();
			int c = os.popInteger().getValue();
			double yc = os.popNumber().getDouble();
			double xc = os.popNumber().getDouble();

			os.execStack().pop();
			os.execStack().push(new WidthShow(t, c, xc, yc));
			os.gsave();

			double[] cfm = gs.font().getPackedArray("FontMatrix").toDoubles();
			AffineTransform at = new AffineTransform(cfm[0], cfm[1], cfm[2],
					cfm[3], x0, y0);
			gs.transform(at);

			return false;
		}

		if (index >= 0) {
			double wx = currentGlyph.wx;
			double wy = currentGlyph.wy;
			if (text.charAt(index) == chd) {
				wx += cx;
				wy += cy;
			}
			os.gstate().translate(wx, wy);
		}

		index++;

		if (index >= text.length()) {
			os.grestore();
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		return false;
	}
}

class AWidthShow extends FontOperator {
	private String text;
	private int index;
	private int chd;
	private double ax, ay;
	private double cx, cy;

	public AWidthShow(String t, int c, double xc, double yc, double xa,
			double ya) {
		text = t;
		index = -1;
		chd = c;
		cx = xc;
		cy = yc;
		ax = xa;
		ay = ya;
	}

	public AWidthShow() {
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();

		if (text == null) {
			if (!os.checkType(new Class[] { PSNumber.class, PSNumber.class,
					PSInteger.class, PSNumber.class, PSNumber.class,
					PSString.class })) {
				error(os, new TypeCheck());
				return true;
			}

			Point2D point = gs.position();
			if (point == null) {
				error(os, new NoCurrentPoint());
				return true;
			}
			float x0 = (float) point.getX();
			float y0 = (float) point.getY();

			String t = os.popString().getValue();
			double ay = os.popNumber().getDouble();
			double ax = os.popNumber().getDouble();
			int c = os.popInteger().getValue();
			double yc = os.popNumber().getDouble();
			double xc = os.popNumber().getDouble();

			os.execStack().pop();
			os.execStack().push(new AWidthShow(t, c, xc, yc, ax, ay));
			os.gsave();

			double[] cfm = gs.font().getPackedArray("FontMatrix").toDoubles();
			AffineTransform at = new AffineTransform(cfm[0], cfm[1], cfm[2],
					cfm[3], x0, y0);
			gs.transform(at);

			return false;
		}

		if (index >= 0) {
			double wx = currentGlyph.wx + ax;
			double wy = currentGlyph.wy + ay;
			if (text.charAt(index) == chd) {
				wx += cx;
				wy += cy;
			}
			os.gstate().translate(wx, wy);
		}

		index++;

		if (index >= text.length()) {
			os.grestore();
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		return false;
	}
}

class XShow extends FontOperator {

	private String text;
	private int index;
	private float[] offset;

	public XShow() {
	}

	public XShow(String t, float[] o) {
		text = t;
		offset = o;
		index = -1;
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();

		if (text == null) {
			if (os.checkType(PSString.class, PSPackedArray.class)) {
				Point2D point = gs.position();
				if (point == null) {
					error(os, new NoCurrentPoint());
					return true;
				}
				float x0 = (float) point.getX();
				float y0 = (float) point.getY();

				float[] o = os.popPackedArray().toFloats();
				String t = os.popString().getValue();

				if (text.length() > offset.length) {
					error(os, new RangeCheck());
					return true;
				}

				os.execStack().pop();
				os.execStack().push(new XShow(t, o));
				os.gsave();

				double[] cfm = gs.font().getPackedArray("FontMatrix")
						.toDoubles();
				AffineTransform at = new AffineTransform(cfm[0], cfm[1],
						cfm[2], cfm[3], x0, y0);
				gs.transform(at);

				return false;

			} else if (os.checkType(PSString.class, PSString.class)) {
				error(os, new Unimplemented());
				return true;
			} else {
				error(os, new TypeCheck());
				return true;
			}
		}

		if (index >= 0) {
			os.gstate().translate(offset[index], 0);
		}

		index++;

		if (index >= text.length()) {
			os.grestore();
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		return false;
	}
}

class XYShow extends FontOperator {

	private String text;
	private int index;
	private float[] offset;

	public XYShow() {
	}

	public XYShow(String t, float[] o) {
		text = t;
		offset = o;
		index = -1;
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();

		if (text == null) {
			if (os.checkType(PSString.class, PSPackedArray.class)) {
				Point2D point = gs.position();
				if (point == null) {
					error(os, new NoCurrentPoint());
					return true;
				}
				float x0 = (float) point.getX();
				float y0 = (float) point.getY();

				float[] o = os.popPackedArray().toFloats();
				String t = os.popString().getValue();

				if (text.length() * 2 > offset.length) {
					error(os, new RangeCheck());
					return true;
				}

				os.execStack().pop();
				os.execStack().push(new XYShow(t, o));
				os.gsave();

				double[] cfm = gs.font().getPackedArray("FontMatrix")
						.toDoubles();
				AffineTransform at = new AffineTransform(cfm[0], cfm[1],
						cfm[2], cfm[3], x0, y0);
				gs.transform(at);

				return false;

			} else if (os.checkType(PSString.class, PSString.class)) {
				error(os, new Unimplemented());
				return true;
			} else {
				error(os, new TypeCheck());
				return true;
			}
		}

		if (index >= 0) {
			os.gstate().translate(offset[index * 2], offset[index * 2 + 1]);
		}

		index++;

		if (index >= text.length()) {
			os.grestore();
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		return false;
	}
}

class YShow extends FontOperator {
	private String text;
	private int index;
	private float[] offset;

	public YShow() {
	}

	public YShow(String t, float[] o) {
		text = t;
		offset = o;
		index = -1;
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();

		if (text == null) {
			if (os.checkType(PSString.class, PSPackedArray.class)) {
				Point2D point = gs.position();
				if (point == null) {
					error(os, new NoCurrentPoint());
					return true;
				}
				float x0 = (float) point.getX();
				float y0 = (float) point.getY();

				float[] o = os.popPackedArray().toFloats();
				String t = os.popString().getValue();

				if (text.length() > offset.length) {
					error(os, new RangeCheck());
					return true;
				}

				os.execStack().pop();
				os.execStack().push(new YShow(t, o));
				os.gsave();

				double[] cfm = gs.font().getPackedArray("FontMatrix")
						.toDoubles();
				AffineTransform at = new AffineTransform(cfm[0], cfm[1],
						cfm[2], cfm[3], x0, y0);
				gs.transform(at);

				return false;

			} else if (os.checkType(PSString.class, PSString.class)) {
				error(os, new Unimplemented());
				return true;
			} else {
				error(os, new TypeCheck());
				return true;
			}
		}

		if (index >= 0) {
			os.gstate().translate(0, offset[index]);
		}

		index++;

		if (index >= text.length()) {
			os.grestore();
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		return false;
	}
}

class GlyphShow extends FontOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	public boolean execute(OperandStack os) {
		if (os.checkType(PSName.class)) {
			PSName name = os.popName();
			Point2D point = os.gstate().position();
			if (point == null) {
				error(os, new NoCurrentPoint());
				return true;
			}
			// FIXME for positioning and matrix
			// pop myself, replace it with a char proc, or just show,
			// but do not pop again
			os.execStack().pop();
			show(os, 0, name);
			return false;
		} else if (os.checkType(PSInteger.class)) {
			error(os, new Unimplemented());
		} else {
			error(os, new TypeCheck());
		}
		return true;
	}
}

class StringWidth extends FontOperator {
	{
		operandTypes = new Class[] { PSString.class };
	}

	public boolean execute(OperandStack os) {
		// FIXME: cannot calculate stringwidth of a type 3 font...
		// since we would need some kind of inactive graphics state
		// so that the BuildGlyph procedure can be executed
		// without doing the actual drawing...
		double width = 0;
		String text = os.popString().getValue();
		for (int i = 0; i < text.length(); i++) {
			width += stringWidth(os, text.charAt(i));
		}
		os.push(width);
		os.push(0.0);
		return true;
	}
}

class CShow extends FontOperator {
	{
		operandTypes = new Class[] { PSPackedArray.class, PSString.class };
	}

	public boolean execute(OperandStack os) {
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class KShow extends FontOperator {
	private int index;
	private String text;
	private PSPackedArray proc;
	private boolean skip;

	private KShow(PSPackedArray p, String t) {
		proc = p;
		text = t;
		index = -1;
		skip = true;
	}

	public KShow() {
	}

	public boolean execute(OperandStack os) {
		PSGState gs = os.gstate();

		if (proc == null) {
			if (!os.checkType(PSPackedArray.class, PSString.class)) {
				error(os, new TypeCheck());
				return true;
			}

			Point2D point = gs.position();
			if (point == null) {
				error(os, new NoCurrentPoint());
				return true;
			}
			float x0 = (float) point.getX();
			float y0 = (float) point.getY();

			String t = os.popString().getValue();
			PSPackedArray p = os.popPackedArray();
			os.execStack().pop();
			os.execStack().push(new KShow(p, t));
			os.gsave();

			double[] cfm = gs.font().getPackedArray("FontMatrix").toDoubles();
			AffineTransform at = new AffineTransform(cfm[0], cfm[1], cfm[2],
					cfm[3], x0, y0);
			gs.transform(at);

			return false;
		}

		if (!skip) {
			double wx = currentGlyph.wx;
			double wy = currentGlyph.wy;
			os.gstate().translate(wx, wy);

			if (index < text.length() - 1) {
				int c0 = text.charAt(index);
				int c1 = text.charAt(index + 1);
				os.push(c0);
				os.push(c1);
				os.execStack().push(proc);
				skip = true;
				return false;
			}
		}

		index++;

		if (index >= text.length()) {
			os.grestore();
			return true;
		}
		int ch = text.charAt(index);
		show(os, ch);
		skip = false;
		return false;
	}
}

class FindEncoding extends FontOperator {
	{
		operandTypes = new Class[] { PSName.class };
	}

	public boolean execute(OperandStack os) {
		PSName key = os.popName();
		PSObject object = os.dictStack().lookup(key);
		if ((object == null) || !(object instanceof PSPackedArray)) {
			error(os, new UndefinedResource());
		}
		os.push(object);
		return true;
	}
}

class SetCacheDevice extends FontOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class, PSNumber.class };
	}

	public boolean execute(OperandStack os) {
		PSGlyph g = (PSGlyph) os.gstate().font().get("_CurrentGlyph");
		g.ury = os.popNumber().getDouble();
		g.urx = os.popNumber().getDouble();
		g.lly = os.popNumber().getDouble();
		g.llx = os.popNumber().getDouble();
		g.wy = os.popNumber().getDouble();
		g.wx = os.popNumber().getDouble();
		return true;
	}
}

class SetCacheDevice2 extends FontOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class, PSNumber.class,
				PSNumber.class, PSNumber.class, PSNumber.class, PSNumber.class };
	}

	public boolean execute(OperandStack os) {
		PSGlyph g = (PSGlyph) os.gstate().font().get("_CurrentGlyph");
		// FIXME
		error(os, new Unimplemented());
		return true;
	}
}

class SetCharWidth extends FontOperator {
	{
		operandTypes = new Class[] { PSNumber.class, PSNumber.class };
	}

	public boolean execute(OperandStack os) {
		PSGlyph g = (PSGlyph) os.gstate().font().get("_CurrentGlyph");
		g.wy = os.popNumber().getDouble();
		g.wx = os.popNumber().getDouble();
		return true;
	}
}
