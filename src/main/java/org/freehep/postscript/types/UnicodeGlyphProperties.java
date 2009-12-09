// Copyright FreeHEP 2009
package org.freehep.postscript.types;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class UnicodeGlyphProperties {
	private static Logger slog = Logger.getLogger("org.freehep.postscript");
	private static OnDiskProperties properties;

	static {
		String name = "UnicodeGlyphList.properties";
		try {
			properties = new OnDiskProperties(name, 50, false);
		} catch (FileNotFoundException e) {
			slog.log(Level.WARNING, "Cannot find " + name, e);
		}
	}

	private UnicodeGlyphProperties() {
	}

	public static char get(String name) {
		try {
			String unicode = properties.getProperty(name);
			if (unicode == null)
				throw new IllegalArgumentException("Character '" + name
						+ "' not defined in UnicodeGlyphProperties");
			return (char) Integer.parseInt(unicode.substring(2), 16);
		} catch (IOException ioe) {
			throw new IllegalArgumentException(
					"UnicodeGlyphProperties could not read character '" + name
							+ "'", ioe);
		}
	}
}
