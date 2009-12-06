// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public class PSRandomAccessFile extends PSFile {
	private RandomAccessFile raf = null;
	private boolean write;
	private boolean append;

	protected PSRandomAccessFile(String n, boolean f, RandomAccessFile r) {
		super(n, f, UNLIMITED);
		raf = r;
	}

	public PSRandomAccessFile(String filename, boolean write, boolean append,
			boolean secure) throws IOException {
		super(filename, false, UNLIMITED);

		if (!secure) {
			throw new IOException();
		}
		this.write = write;
		this.append = append;
		File file = new File(filename);
		if (!write && !file.exists()) {
			throw new IOException();
		}
		raf = new RandomAccessFile(file, "rw");
		if (append) {
			raf.seek(raf.length());
		}
		if (write) {
			// FIXME: not really truncated!
			raf.seek(0);
		}
	}

	@Override
	public final void close() throws IOException {
		if (raf != null) {
			raf.close();
			raf = null;
		}
	}

	@Override
	public final int read() throws IOException {
		return (raf != null) ? raf.read() : -1;
	}

	@Override
	public final String readLine() throws IOException {
		return (raf != null) ? raf.readLine() : null;
	}

	@Override
	public final void write(int b, boolean secure) throws IOException {
		if (!secure) {
			throw new IOException();
		}
		if (raf != null) {
			raf.write(b);
		} else {
			throw new IOException();
		}
	}

	@Override
	public final void seek(long pos) throws IOException {
		if (raf != null) {
			raf.seek(pos);
		} else {
			throw new IOException();
		}
	}

	@Override
	public final long getFilePointer() throws IOException {
		return (raf != null) ? raf.getFilePointer() : -1;
	}

	@Override
	public final int available() throws IOException {
		return (raf != null) ? (int) (raf.length() - raf.getFilePointer()) : -1;
	}

	@Override
	public void flush() throws IOException {
		// ignored
	}

	@Override
	public final void reset() throws IOException {
		if (raf != null) {
			raf.seek(0);
		} else {
			throw new IOException();
		}
	}

	@Override
	public final boolean isValid() {
		return (raf != null);
	}

	@Override
	public final int hashCode() {
		return raf.hashCode();
	}

	@Override
	public final boolean equals(Object o) {
		if (o instanceof PSRandomAccessFile) {
			return (raf == ((PSRandomAccessFile) o).raf);
		}
		return false;
	}

	@Override
	public final Object clone() throws CloneNotSupportedException {
		return new PSRandomAccessFile(filename, filter, raf);
	}

	@Override
	public final PSObject copy() {
		if (filter) {
			throw new IllegalArgumentException("Filters cannot be copied");
		}

		try {
			return new PSRandomAccessFile(filename, write, append, true);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(
					"Cannot find file while copying: " + filename, e);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException for file while copying: " + filename, e);
		}
	}
}
