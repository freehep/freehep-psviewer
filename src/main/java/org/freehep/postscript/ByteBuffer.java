// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Byte Buffer for PostScript Scanner
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/ByteBuffer.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class ByteBuffer {

	private char[] buffer;
	private int len;

	public ByteBuffer() {
		this(256);
	}

	public ByteBuffer(int len) {
		buffer = new char[len];
		reset();
	}

	public void reset() {
		len = 0;
	}

	public void append(int i) {
		this.append((char) i);
	}

	public void append(char c) {
		if (len >= buffer.length) {
			char[] newBuffer = new char[buffer.length * 2];
			System.arraycopy(buffer, 0, newBuffer, 0, len);
			buffer = newBuffer;
		}
		buffer[len] = c;
		len++;
	}

	public String getString() {
		return new String(buffer, 0, len);
	}

	public char[] getChars() {
		char[] r = new char[len];
		System.arraycopy(buffer, 0, r, 0, len);
		return r;
	}

}