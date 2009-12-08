// Copyright FreeHEP, 2009
package org.freehep.psviewer.test;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.freehep.postscript.stacks.DictionaryStack;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.UnicodeGlyphList;

/**
 * 
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class GenerateShortGlyphList {

	private static Map<String, Character> list = new TreeMap<String, Character>();

	private static void fill(PSArray encoding) {
		for (int cc = 0; cc < 256; cc++) {
			String name = encoding.getName(cc).getValue();
			if (name.equals(".notdef"))
				continue;

			char uc = UnicodeGlyphList.get(name);
			list.put(name, uc);
		}		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DictionaryStack stack = new DictionaryStack();
		PSDictionary system = stack.systemDictionary();
		
		fill(system.getArray(DictionaryStack.standardEncoding.getValue()));
		fill(system.getArray(DictionaryStack.isoLatin1Encoding.getValue()));
		fill(system.getArray(DictionaryStack.symbolEncoding.getValue()));
		fill(system.getArray(DictionaryStack.zapfDingbatsEncoding.getValue()));
		
		System.out.println("list = new HashMap<String, Character>(600);");
		for (Iterator<String> i=list.keySet().iterator(); i.hasNext(); ) {
			String name = i.next();
			char c = list.get(name);
			System.out.println("list.put(\""+name+"\", new Character((char) 0x"+Integer.toHexString((int)c)+"));");
		}
	}
}
