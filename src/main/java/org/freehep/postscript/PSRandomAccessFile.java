// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSRandomAccessFile.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSRandomAccessFile extends PSFile {
	protected RandomAccessFile raf = null;
	protected boolean write;
	protected boolean append;

	protected PSRandomAccessFile(String n, boolean f, RandomAccessFile r) {
		super(n, f);
		raf = r;
	}

	public PSRandomAccessFile(String filename, boolean write, boolean append,
			boolean secure) throws FileNotFoundException, IOException {
		super(filename, false);

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

	public void close() throws IOException {
		if (raf != null) {
			raf.close();
			raf = null;
		}
	}

	public int read() throws IOException {
		return (raf != null) ? raf.read() : -1;
	}

	public String readLine() throws IOException {
		return (raf != null) ? raf.readLine() : null;
	}

	public void write(int b, boolean secure) throws IOException {
		if (!secure) {
			throw new IOException();
		}
		if (raf != null) {
			raf.write(b);
		} else {
			throw new IOException();
		}
	}

	public void seek(long pos) throws IOException {
		if (raf != null) {
			raf.seek(pos);
		} else {
			throw new IOException();
		}
	}

	public long getFilePointer() throws IOException {
		return (raf != null) ? raf.getFilePointer() : -1;
	}

	public int available() throws IOException {
		return (raf != null) ? (int) (raf.length() - raf.getFilePointer()) : -1;
	}

	public void flush() throws IOException {
		// ignored
	}

	public void reset() throws IOException {
		if (raf != null) {
			raf.seek(0);
		} else {
			throw new IOException();
		}
	}

	public boolean isValid() {
		return (raf != null);
	}

	public int hashCode() {
		return raf.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof PSRandomAccessFile) {
			return (raf == ((PSRandomAccessFile) o).raf);
		}
		return false;
	}

	public Object clone() {
		return new PSRandomAccessFile(filename, filter, raf);
	}

	public PSObject copy() {
		if (filter) {
			throw new RuntimeException("Filters cannot be copied");
		}

		try {
			return new PSRandomAccessFile(filename, write, append, true);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Cannot find file while copying: "
					+ filename);
		} catch (IOException e) {
			throw new RuntimeException("IOException for file while copying: "
					+ filename);
		}
	}
}
