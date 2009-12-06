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

	protected PSFontDictionary(Map<Object, PSObject> table) {
		super(table);
	}

	public PSFontDictionary(Font font, String encodingTable) {
		super();
		setName("fontdictionary");
		// System.out.println("Creating Type1 font from: "+font+" using table: "+encodingTable+" and transform "+font.getTransform());

		// FIXME: should go somewhere settable?
		boolean antiAliasing = true;
		FontRenderContext fontRenderContext = new FontRenderContext(null,
				antiAliasing, true);

		CharTable table = Lookup.getInstance().getTable(encodingTable);

		// FIXME: should be in the private dictionary
		put("javafont", new PSJavaFont(font));
		put("javaEncoding", encodingTable);

		// Generic Font Entries
		put("FontType", 1);
		put("FontMatrix", new PSArray(
				new float[] { 1.0f, 0f, 0f, 1.0f, 0f, 0f }));
		put("FontName", new PSName(font.getPSName()));
		PSArray encoding = new PSArray(256);
		put("Encoding", encoding);
		// FIXME: fake
		put("FontBBox", new PSArray(new float[] { 0f, 0f, 0f, 0f }));

		// Type 1 entries
		put("PaintType", 0);
		PSDictionary charStrings = new PSDictionary();
		put("CharStrings", charStrings);
		PSDictionary metrics = new PSDictionary();
		put("Metrics", metrics);
		put("Private", new PSDictionary());

		// turn font upside down
		AffineTransform at = font.getTransform();
		AffineTransform upsideDown = new AffineTransform(1.0, 0, 0, -1.0, 0, 0);
		at.concatenate(upsideDown);
		font = font.deriveFont(at);

		// iterate over all cc/names and fill Encoding, CharStrings/Metrics
		// table
		char[] uc = new char[1];
		for (int cc = 0; cc < 256; cc++) {
			String name = table.toName(cc);
			if (name != null) {
				uc[0] = table.toUnicode(name);
			} else {
				name = ".notdef";
				uc[0] = '\u25a1'; // box
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
