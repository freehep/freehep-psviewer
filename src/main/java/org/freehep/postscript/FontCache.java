// Copyright 2006, FreeHEP.
package org.freehep.postscript;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class keeps a reference to all physical fonts. Fonts can be looked up by
 * physical or logical name. Any font not found will issue a message and replace
 * that font by another existing font. As of that moment the replacement will be
 * returned.
 * 
 * @author duns
 * @version $Id: src/main/java/org/freehep/postscript/FontCache.java
 *          5f3e85e0001c 2006/11/20 08:39:41 duns $
 */
public class FontCache {

	private SortedMap/* <String, FontEntry> */<String, FontEntry> fonts;

	public FontCache() {
		this.fonts = new TreeMap<String, FontEntry>();

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] font = graphicsEnvironment.getAllFonts();
		for (int i = 0; i < font.length; i++) {		
			replace(font[i].getPSName(), new FontEntry(font[i]));
		}

		// add the standard fonts
		put("Monospaced.plain", new Font("Monospaced", Font.PLAIN, 12));
		put("Monospaced.bold", new Font("Monospaced", Font.BOLD, 12));
		put("Monospaced.italic", new Font("Monospaced", Font.ITALIC, 12));
		put("Monospaced.bolditalic", new Font("Monospaced", Font.BOLD
				+ Font.ITALIC, 12));
		put("SansSerif.plain", new Font("SansSerif", Font.PLAIN, 12));
		put("SansSerif.bold", new Font("SansSerif", Font.BOLD, 12));
		put("SansSerif.italic", new Font("SansSerif", Font.ITALIC, 12));
		put("SansSerif.bolditalic", new Font("SansSerif", Font.BOLD
				+ Font.ITALIC, 12));
		put("Serif.plain", new Font("Serif", Font.PLAIN, 12));
		put("Serif.bold", new Font("Serif", Font.BOLD, 12));
		put("Serif.italic", new Font("Serif", Font.ITALIC, 12));
		put("Serif.bolditalic", new Font("Serif", Font.BOLD + Font.ITALIC, 12));
	}

	public Font get(String name) {
		FontEntry entry = fonts.get(name);
		if (entry == null) {
			// FIXME, we could look in lists for replacements
			replace(name, entry);
			entry = new FontEntry(name, fonts.get("SansSerif.plain"));
		}
		return entry.getFont();
	}

	public void put(Font font) {
		put(font.getPSName(), font);
	}

	public void put(String name, Font font) {
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
