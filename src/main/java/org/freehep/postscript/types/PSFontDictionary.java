// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

import java.util.Map;

import org.freehep.vectorgraphics.FontRenderContext;
import org.freehep.vectorgraphics.GlyphMetrics;
import org.freehep.vectorgraphics.GlyphVector;
import org.freehep.vectorgraphics.Transform;

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
	
	public PSFontDictionary(PSDevice device, org.freehep.vectorgraphics.Font javaFont, PSArray encoding) {
		super();
		setName("fontdictionary");
		// System.out.println("Creating Type1 font from: "+font+" using table: "+encodingTable+" and transform "+font.getTransform());

		// FIXME: should be in the private dictionary
		put("javafont", new PSJavaFont(javaFont));

		// Generic Font Entries
		put("FontType", 1);
		put("FontMatrix", new PSArray(
				new float[] { 1.0f, 0f, 0f, 1.0f, 0f, 0f }));
		put("FontName", new PSName(javaFont.getPSName()));
		// FIXME: fake
		put("FontBBox", new PSArray(new float[] { 0f, 0f, 0f, 0f }));

		// Type 1 entries
		put("PaintType", 0);
		put("Private", new PSDictionary());

		// turn font upside down
		Transform at = javaFont.getTransform();
		Transform upsideDown = device.createTransform(1.0, 0, 0, -1.0, 0, 0);
		at.concatenate(upsideDown);
		javaFont = javaFont.deriveFont(at);

		put("Encoding", encoding);	
		
		// FIXME: should go somewhere settable?
		boolean antiAliasing = true;
		FontRenderContext fontRenderContext = device.createFontRenderContext(null,
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
			uc[0] = name.equals(".notdef") ? box : UnicodeShortGlyphList.get(name);
			
			// System.out.println(cc+" "+name+" "+uc[0]);
			GlyphVector gv = javaFont.createGlyphVector(fontRenderContext, uc);
			GlyphMetrics m = gv.getGlyphMetrics(0);
			PSGlyph glyph = new PSJavaGlyph(device, gv);
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
