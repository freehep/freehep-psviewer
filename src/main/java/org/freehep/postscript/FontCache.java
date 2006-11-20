// Copyright 2006, FreeHEP.
package org.freehep.postscript;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class keeps a reference to all physical fonts.
 * Fonts can be looked up by physical or logical name.
 * Any font not found will issue a message and 
 * replace that font by another existing font. As of that
 * moment the replacement will be returned.
 * 
 * @author duns
 * @version $Id: src/main/java/org/freehep/postscript/FontCache.java 68d526b93849 2006/11/20 07:20:33 duns $
 */
public class FontCache {

	private SortedMap/*<String, FontEntry>*/ fonts;
	private FontEntry defaultFont;
	
	public FontCache() {
		this.fonts = new TreeMap();

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] font = graphicsEnvironment.getAllFonts();
        for (int i=0; i<font.length; i++) {
        	replace(font[i]);
        	if ((defaultFont == null) && font[i].getPSName().equals("SansSerif.plain")) {
        		defaultFont = new FontEntry(font[i]);
        	}
        }
	}
	
	public Font get(String name) {
		FontEntry entry = (FontEntry)fonts.get(name);
		if (entry == null) {
			entry = new FontEntry(name, defaultFont);
		}
		return entry.getFont();
	}

	public void put(Font font) {
		put(font.getPSName(), font);
	}
	
	public void put(String name, Font font) {
		if (fonts.get(name) != null) {
			fonts.put(name, new FontEntry(font));
		}		
	}

	public void replace(Font font) {
		fonts.put(font.getPSName(), new FontEntry(font));
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
