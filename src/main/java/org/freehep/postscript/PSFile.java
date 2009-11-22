// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSFile.java 17245790f2a9
 *          2006/09/12 21:44:14 duns $
 */
public abstract class PSFile extends PSComposite {
	protected String filename;
	protected boolean filter;

	protected PSFile(String name, boolean isFilter) {
		super("file", true);
		filter = isFilter;
		filename = name;
	}

	public boolean execute(OperandStack os) {
		error(os, new IOError());
		return true;
	}

	public String getType() {
		return "filetype";
	}

	public abstract void close() throws IOException;

	public int read() throws IOException {
		throw new IOException();
	}

	public String readLine() throws IOException {
		throw new IOException();
	}

	public void write(int b, boolean secure) throws IOException {
		throw new IOException();
	}

	public void seek(long pos) throws IOException {
		throw new IOException();
	}

	public long getFilePointer() throws IOException {
		throw new IOException();
	}

	public int available() throws IOException {
		throw new IOException();
	}

	public void flush() throws IOException {
		throw new IOException();
	}

	public void reset() throws IOException {
		throw new IOException();
	}

	public boolean markSupported() {
		return false;
	}

	public void mark(int readLimit) {
		// ignored
	}

	public abstract boolean isValid();

	public String cvs() {
		return name;
	}

	public String toString() {
		return "--" + ((isExecutable()) ? "*" : "") + name + " (" + filename
				+ ") --";
	}
}
