// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.Map;

import org.freehep.graphics2d.font.CharTable;
import org.freehep.graphics2d.font.Lookup;

/**
 * Quasi Type 1 Font mapping Java Fonts to PostScript ones. This object is never
 * casted to.
 * 
 * @author Mark Donszelmann
 */
public class PSFontDictionary extends PSDictionary {

	private static final char box = '\u25a1';
	
	protected PSFontDictionary(Map<Object, PSObject> table) {
		super(table);
	}

	public PSFontDictionary(Font font, String encodingTable) {
		super();
		font = init(font);
		CharTable table = Lookup.getInstance().getTable(encodingTable);
		put("javaEncoding", encodingTable);
		PSArray encoding = new PSArray(256);
		put("Encoding", encoding);
		
		// FIXME: should go somewhere settable?
		boolean antiAliasing = true;
		FontRenderContext fontRenderContext = new FontRenderContext(null,
				antiAliasing, true);

		PSDictionary charStrings = new PSDictionary();
		put("CharStrings", charStrings);
		PSDictionary metrics = new PSDictionary();
		put("Metrics", metrics);

		// iterate over all cc/names and fill Encoding, CharStrings/Metrics
		// table
		char[] uc = new char[1];
		for (int cc = 0; cc < 256; cc++) {
			String name = table.toName(cc);
			if (name != null) {
				uc[0] = table.toUnicode(name);
			} else {
				name = ".notdef";
				uc[0] = box;
			}
			// System.out.println(cc+" "+name+" "+uc[0]);
			GlyphVector gv = font.createGlyphVector(fontRenderContext, uc);
			GlyphMetrics m = gv.getGlyphMetrics(0);
			PSGlyph glyph = new PSJavaGlyph(gv);
			encoding.set(cc, name);
			charStrings.put(name, glyph);
			metrics.put(name, m.getAdvance());
		}
	}
	
	public PSFontDictionary(Font font, PSArray encoding) {
		super();
		font = init(font);
		put("Encoding", encoding);	
		
		// FIXME: should go somewhere settable?
		boolean antiAliasing = true;
		FontRenderContext fontRenderContext = new FontRenderContext(null,
				antiAliasing, true);

		PSDictionary charStrings = new PSDictionary();
		put("CharStrings", charStrings);
		PSDictionary metrics = new PSDictionary();
		put("Metrics", metrics);

		// iterate over all cc/names and fill Encoding, CharStrings/Metrics
		// table
		char[] uc = new char[1];
		for (int cc = 0; cc < 256; cc++) {
			String name = encoding.getName(cc).getValue();
			uc[0] = name.equals(".notdef") ? box : getUniCode(name);
			
			// System.out.println(cc+" "+name+" "+uc[0]);
			GlyphVector gv = font.createGlyphVector(fontRenderContext, uc);
			GlyphMetrics m = gv.getGlyphMetrics(0);
			PSGlyph glyph = new PSJavaGlyph(gv);
			encoding.set(cc, name);
			charStrings.put(name, glyph);
			metrics.put(name, m.getAdvance());
		}
	}	
		
	private Font init(Font font) {
		setName("fontdictionary");
		// System.out.println("Creating Type1 font from: "+font+" using table: "+encodingTable+" and transform "+font.getTransform());

		// FIXME: should be in the private dictionary
		put("javafont", new PSJavaFont(font));

		// Generic Font Entries
		put("FontType", 1);
		put("FontMatrix", new PSArray(
				new float[] { 1.0f, 0f, 0f, 1.0f, 0f, 0f }));
		put("FontName", new PSName(font.getPSName()));
		// FIXME: fake
		put("FontBBox", new PSArray(new float[] { 0f, 0f, 0f, 0f }));

		// Type 1 entries
		put("PaintType", 0);
		put("Private", new PSDictionary());

		// turn font upside down
		AffineTransform at = font.getTransform();
		AffineTransform upsideDown = new AffineTransform(1.0, 0, 0, -1.0, 0, 0);
		at.concatenate(upsideDown);
		font = font.deriveFont(at);
		
		return font;
	}
	
	// NOTE: we should just generate a full table from name to unicode by our encoding files in graphics2d.
	private char getUniCode(String name) {
		String[] tables = { "STDLatin", "ISOLatin", "Symbol", "Zapfdingbats"};
		for (int i = 0; i< tables.length; i++) {
			CharTable table = Lookup.getInstance().getTable(tables[i]);
			try {
				return table.toUnicode(name);
			} catch (NullPointerException npe) {
				
			}
		}
		log.warning("Unicode of character with name '"+name+"' not found, replaced by 'box'. Looked in ");
		for (int i = 0; i< tables.length; i++) {
			log.warning("   "+tables[i]);
		}
		return box;
	}

	@Override
	public String getType() {
		return "javafontdictionary";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSFontDictionary(table);
	}

	@Override
	public String toString() {
		return "--" + ((isExecutable()) ? "*" : "") + name + " (" + size()
				+ ")--";
	}
}
