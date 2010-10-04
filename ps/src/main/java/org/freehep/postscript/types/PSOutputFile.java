// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.util.io.FinishableOutputStream;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public class PSOutputFile extends PSFile implements PSDataTarget {
	private OutputStream out = null;
	private boolean append;

	protected PSOutputFile(String n, boolean f, OutputStream o) {
		super(n, f, UNLIMITED);
		out = o;
	}

	public PSOutputFile(OutputStream output) throws IOException {
		super("pipe", true, UNLIMITED);
		init(output);
	}

	public PSOutputFile(String filename, boolean append, boolean secure)
			throws IOException {
		super(filename, false, UNLIMITED);
		if (!secure) {
			throw new IOException();
		}
		this.append = append;
		init(new FileOutputStream(filename, append));
	}

	private void init(OutputStream output) {
		out = new BufferedOutputStream(output);
	}

	public final OutputStream getOutputStream() {
		return out;
	}

	@Override
	public final void close() throws IOException {
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

	@Override
	public final void write(int b, boolean secure) throws IOException {
		if (!secure) {
			throw new IOException();
		}

		if (out != null) {
			out.write(b);
		} else {
			throw new IOException();
		}
	}

	@Override
	public final void flush() throws IOException {
		if (out != null) {
			out.flush();
		}
	}

	@Override
	public final boolean isValid() {
		return (out != null);
	}

	@Override
	public final int hashCode() {
		return out.hashCode();
	}

	@Override
	public final boolean equals(Object o) {
		if (o instanceof PSOutputFile) {
			return (out == ((PSOutputFile) o).out);
		}
		return false;
	}

	@Override
	public final Object clone() throws CloneNotSupportedException {
		return new PSOutputFile(filename, filter, out);
	}

	@Override
	public final PSObject copy() {
		if (filter) {
			throw new IllegalArgumentException("Filters cannot be copied");
		}

		try {
			return new PSOutputFile(filename, append, true);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException for file while copying: " + filename, e);
		}
	}
}
