// Copyright 2001, FreeHEP.
package org.freehep.postscript.test;

import org.freehep.postscript.*;

import java.io.*;

public class TestCharString {
    
    public static void main(String[] argv) throws Exception {
	InputStream in = new FileInputStream("type1.hex");
	//InputStream in = new FileInputStream("type1.eps");
	byte[] bytes = new byte[47*2+2];
	
	char[] chars = new char[bytes.length];
	in.read(bytes);
	for (int i = 0; i < chars.length; i++) {
	    chars[i] = (char)(bytes[i] & 0x00FF);
	}
	
	String str = new String(bytes);

	PSString charString = new PSString(chars);

	DictionaryStack stack = new DictionaryStack();
	
	PSCharStringDecoder decoder = new PSCharStringDecoder(stack.systemDictionary());
	decoder.decode(charString);
	/*
	System.out.println();
	System.out.println(decoder.getSBX() + ", " + decoder.getSBX() + "      "+
			   decoder.getWidthX() + ", "+decoder.getWidthY());
	System.out.println(decoder.getPackedArray());
	System.out.println(decoder.getPackedArray().toPrint());
	*/
    }
}
