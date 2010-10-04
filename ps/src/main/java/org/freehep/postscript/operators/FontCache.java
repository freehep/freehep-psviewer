// Copyright 2006-2010, FreeHEP.
package org.freehep.postscript.operators;

import java.util.SortedMap;
import java.util.TreeMap;

import org.freehep.postscript.types.PSDevice;
import org.freehep.vectorgraphics.Font;
import org.freehep.vectorgraphics.GraphicsEnvironment;

/**
 * This class keeps a reference to all physical fonts. Fonts can be looked up by
 * physical or logical name. Any font not found will issue a message and replace
 * that font by another existing font. As of that moment the replacement will be
 * returned.
 * 
 * @author duns
 */
public class FontCache {

	private SortedMap<String, FontEntry> fonts;

	public FontCache(PSDevice device) {
		this.fonts = new TreeMap<String, FontEntry>();

		GraphicsEnvironment graphicsEnvironment = device
				.getLocalGraphicsEnvironment();
		Font[] font = graphicsEnvironment.getAllFonts();
		for (int i = 0; i < font.length; i++) {
			replace(font[i].getPSName(), new FontEntry(font[i]));
		}

		// add the standard fonts
		put("Monospaced.plain", device.createFont("Monospaced", Font.PLAIN, 12));
		put("Monospaced.bold", device.createFont("Monospaced", Font.BOLD, 12));
		put("Monospaced.italic",
				device.createFont("Monospaced", Font.ITALIC, 12));
		put("Monospaced.bolditalic",
				device.createFont("Monospaced", Font.BOLD + Font.ITALIC, 12));
		put("SansSerif.plain", device.createFont("SansSerif", Font.PLAIN, 12));
		put("SansSerif.bold", device.createFont("SansSerif", Font.BOLD, 12));
		put("SansSerif.italic", device.createFont("SansSerif", Font.ITALIC, 12));
		put("SansSerif.bolditalic",
				device.createFont("SansSerif", Font.BOLD + Font.ITALIC, 12));
		put("Serif.plain", device.createFont("Serif", Font.PLAIN, 12));
		put("Serif.bold", device.createFont("Serif", Font.BOLD, 12));
		put("Serif.italic", device.createFont("Serif", Font.ITALIC, 12));
		put("Serif.bolditalic",
				device.createFont("Serif", Font.BOLD + Font.ITALIC, 12));
	}

	public final Font get(String name) {
		FontEntry entry = fonts.get(name);
		if (entry == null) {
			// FIXME, we could look in lists for replacements
			replace(name, entry);
			entry = new FontEntry(name, fonts.get("SansSerif.plain"));
		}
		return entry.getFont();
	}

	public final void put(Font font) {
		put(font.getPSName(), font);
	}

	public final void put(String name, Font font) {
		if (fonts.get(name) == null) {
			replace(name, new FontEntry(font));
		}
	}

	private void replace(String name, FontEntry entry) {
		fonts.put(name, entry);
	}

	class FontEntry {
		private String name;
		private Font font;
		private boolean used;
		private boolean replacement;

		public FontEntry(Font font) {
			this.name = font.getPSName();
			this.font = font;
		}

		public FontEntry(String name, FontEntry replacementFont) {
			this.name = name;
			this.font = replacementFont.getFont();
			this.replacement = true;
		}

		public String getName() {
			return name;
		}

		public boolean isUsed() {
			return used;
		}

		public boolean isReplacement() {
			return replacement;
		}

		public Font getFont() {
			used = true;
			return font;
		}
	}
}
