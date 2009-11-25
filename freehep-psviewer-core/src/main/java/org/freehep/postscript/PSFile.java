// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public abstract class PSFile extends PSComposite {
	protected String filename;
	protected boolean filter;

	protected PSFile(String name, boolean isFilter, int access) {
		super("file", true, access);
		filter = isFilter;
		filename = name;
	}

	@Override
	public boolean execute(OperandStack os) {
		error(os, new IOError(new IOException("Cannot execute PSFile: "+filename)));
		return true;
	}

	@Override
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
	
	public abstract boolean isValid();

	@Override
	public String cvs() {
		return name;
	}

	@Override
	public String toString() {
		return "--" + ((isExecutable()) ? "*" : "") + name + " (" + filename
				+ ") --";
	}
}
