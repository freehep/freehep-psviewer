// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import org.freehep.postscript.dsc.DSC;
import org.freehep.postscript.errors.IOError;
import org.freehep.postscript.errors.NameNotFoundException;
import org.freehep.postscript.errors.SyntaxException;
import org.freehep.postscript.processor.Scanner;
import org.freehep.postscript.stacks.OperandStack;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public class PSInputFile extends PSFile implements PSTokenizable, PSDataSource {
	private InputStream in = null;
	private Scanner scanner = null;
	private DSC dsc = null;
	private int buffer;
	private static final int DEFAULT_BUFFER = 0x8000; // 32 Kbyte

	protected PSInputFile(String n, boolean f, InputStream i, Scanner s, DSC d) {
		super(n, f, READ_ONLY);
		in = i;
		scanner = s;
		dsc = d;
	}

	public PSInputFile(InputStream input, DSC dsc) throws IOException {
		super("pipe", true, READ_ONLY);
		this.dsc = dsc;
		in = input;
	}

	public PSInputFile(String filename) throws IOException {
		this(filename, DEFAULT_BUFFER, new DSC());
	}

	public PSInputFile(String filename, int buffer) throws IOException {
		this(filename, buffer, new DSC());
	}

	public PSInputFile(String filename, DSC dsc) throws IOException {
		this(filename, DEFAULT_BUFFER, dsc);
	}
		
	public PSInputFile(String filename, int buffer, DSC dsc) throws IOException {
		super(filename, false, READ_ONLY);
		this.dsc = dsc;
		this.buffer = buffer;
		init();
	}

	private void init() throws IOException {
		InputStream input;
		try {
			URL url = new URL(filename);
			input = url.openStream();
			// FIXME maybe we should modify name here to something shorter...
		} catch (MalformedURLException e) {
			input = new FileInputStream(filename);
		} catch (ZipException e) {
			FileNotFoundException fnfe = new FileNotFoundException("Archive cannot be found: "
					+ filename);
			fnfe.initCause(e);
			throw fnfe;
		}

		if (filename.toLowerCase().endsWith(".gz")) {
			input = new GZIPInputStream(input);
		}

		in = new BufferedInputStream(input, buffer);
		in.mark(buffer);
		
		scanner = null;
	}

	public final InputStream getInputStream() {
		return in;
	}

	public final DSC getDSC() {
		return dsc;
	}

	private void getScanner() throws IOException {
		if (scanner == null) {
			if (in == null) {
				throw new IOException();
			}
			scanner = new Scanner(in, dsc);
		}
	}

	public final PSObject token(boolean packingMode, NameLookup lookup)
			throws IOException, SyntaxException, NameNotFoundException {
		getScanner();
		return scanner.nextToken(packingMode, lookup);
	}

	@Override
	public final boolean execute(OperandStack os) {
		if (in == null) {
			return true;
		}
		try {
			getScanner();
		} catch (IOException e) {
			error(os, new IOError(e));
			return true;
		}
		return Dispatcher.dispatch(os, this);
	}

	@Override
	public final void close() throws IOException {
		if (in != null) {
			if (!filter) {
				in.close();
			}
			in = null;
			scanner = null;
		}
	}

	@Override
	public final int read() throws IOException {
		return (in != null) ? in.read() : -1;
	}

	private BufferedReader reader;

	@Override
	public final String readLine() throws IOException {
		try {
			if (in == null) {
				return null;
			}

			if (reader == null) {
				reader = new BufferedReader(new InputStreamReader(in));
			}
			return reader.readLine();

		} catch (ClassCastException e) {
			throw new IOException("Cannot readLine ", e);
		}
	}

	@Override
	public final int available() throws IOException {
		return (in != null) ? in.available() : -1;
	}

	@Override
	public final void flush() throws IOException {
		if (in != null) {
			int b;
			do {
				b = in.read();
			} while (b != -1);

			if (filter) {
				close();
			}
		}
	}

	@Override
	public final void reset() throws IOException {
		try {
			if (in.markSupported()) {
				in.reset();
				return;
			}
		} catch (IOException e) {
			// continue to re-open file
		}

		// re-open file
		try {
			if (in != null) {
				in.close();
				in = null;
			}
		} catch (IOException e) {
			// ignore
		}

		init();
	}

	@Override
	public final boolean isValid() {
		return (in != null);
	}

	@Override
	public final int hashCode() {
		return in.hashCode();
	}

	@Override
	public final boolean equals(Object o) {
		if (o instanceof PSInputFile) {
			return (in == ((PSInputFile) o).in);
		}
		return false;
	}

	@Override
	public final Object clone() throws CloneNotSupportedException {
		return new PSInputFile(filename, filter, in, scanner, dsc);
	}

	@Override
	public final PSObject copy() {
		if (filter) {
			throw new IllegalArgumentException("Filters cannot be copied");
		}

		try {
			return new PSInputFile(filename, buffer, dsc);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(
					"Cannot find file while copying: " + filename, e);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException for file while copying: " + filename, e);
		}
	}
}
