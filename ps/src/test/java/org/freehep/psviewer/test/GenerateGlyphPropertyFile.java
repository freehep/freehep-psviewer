//Copyright FreeHEP 2009
package org.freehep.psviewer.test;

import java.io.IOException;
import java.util.Iterator;

import org.freehep.postscript.types.OnDiskProperties;

/**
 * 
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class GenerateGlyphPropertyFile {

	private static String filename = "UnicodeGlyphList.properties";
	private static int RECORD_LENGTH = 50;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		OnDiskProperties properties = new OnDiskProperties(filename, RECORD_LENGTH, true);
		for (Iterator<String> i = UnicodeGlyphList.keySet().iterator(); i
				.hasNext();) {
			String name = i.next();
			char c = UnicodeGlyphList.get(name);
			String unicode = "000" + Integer.toHexString((int) c);
			unicode = "0x" + unicode.substring(unicode.length() - 4, unicode.length());
			properties.add(name, unicode);
		}
		properties.close();

		properties = new OnDiskProperties(filename, RECORD_LENGTH, false);

		String name="Ydieresis";

		long t0 = System.nanoTime();
		UnicodeGlyphList.get(name);
		System.err.println(System.nanoTime() - t0);
		
		t0 = System.nanoTime();
		String ucs = properties.getProperty(name);
		System.err.println(System.nanoTime() - t0);
		System.err.println(ucs);
	}

}
