// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.util.io.FinishableOutputStream;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSOutputFile.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSOutputFile extends PSFile implements PSDataTarget {
	protected OutputStream out = null;
	protected boolean append;

	protected PSOutputFile(String n, boolean f, OutputStream o) {
		super(n, f);
		out = o;
	}

	public PSOutputFile(OutputStream output) throws IOException {
		super("pipe", true);
		init(output);
	}

	public PSOutputFile(String filename, boolean append, boolean secure)
			throws IOException {
		super(filename, false);
		if (!secure) {
			throw new IOException();
		}
		this.append = append;
		init(new FileOutputStream(filename, append));
	}

	private void init(OutputStream output) {
		out = new BufferedOutputStream(output);
	}

	public OutputStream getOutputStream() {
		return out;
	}

	public void close() throws IOException {
		if (out != null) {
			if (!filter) {
				out.close();
			} else {
				if (out instanceof FinishableOutputStream) {
					((FinishableOutputStream) out).finish();
				}
			}
			out = null;
		}
	}

	public void write(int b, boolean secure) throws IOException {
		if (!secure) {
			throw new IOException();
		}

		if (out != null) {
			out.write(b);
		} else {
			throw new IOException();
		}
	}

	public void flush() throws IOException {
		if (out != null) {
			out.flush();
		}
	}

	public boolean isValid() {
		return (out != null);
	}

	public int hashCode() {
		return out.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof PSOutputFile) {
			return (out == ((PSOutputFile) o).out);
		}
		return false;
	}

	public Object clone() {
		return new PSOutputFile(filename, filter, out);
	}

	public PSObject copy() {
		if (filter) {
			throw new RuntimeException("Filters cannot be copied");
		}

		try {
			return new PSOutputFile(filename, append, true);
		} catch (IOException e) {
			throw new RuntimeException("IOException for file while copying: "
					+ filename);
		}
	}
}
