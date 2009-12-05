// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public class PSString extends PSComposite implements PSTokenizable,
		PSDataSource, PSDataTarget, Comparable<Object> {
	private char[] value;
	private int start = 0;
	private int length = 0;
	private ByteInputStream in = null;
	private Scanner scanner = null;
	private ByteOutputStream out = null;
	private DSC dsc = null;

	protected PSString(char[] chars, int index, int count, DSC dsc) {
		super("string", true);
		value = new char[chars.length];
		System.arraycopy(chars, 0, value, 0, chars.length);
		start = index;
		length = count;
		this.dsc = dsc;
	}

	public PSString(String string) {
		this(string, new DSC());
	}

	public PSString(String string, DSC dsc) {
		this(string.length());
		string.getChars(0, string.length(), value, 0);
		this.dsc = dsc;
	}

	public PSString(int n) {
		this(new char[n]);
	}

	public PSString(char[] chars) {
		this(chars, 0, chars.length, new DSC());
	}

	public InputStream getInputStream() {
		if (in == null) {
			in = new ByteInputStream();
		}
		return in;
	}

	public DSC getDSC() {
		return dsc;
	}

	public int read() throws IOException {
		if (in == null) {
			in = new ByteInputStream();
		}
		return in.read();
	}

	public void reset() throws IOException {
		if (in == null) {
			in = new ByteInputStream();
		}
		in.reset();
	}

	public OutputStream getOutputStream() {
		if (out == null) {
			out = new ByteOutputStream();
		}
		return out;
	}

	public void write(int b, boolean secure) throws IOException {
		if (!secure) {
			throw new IOException();
		}
		if (out == null) {
			out = new ByteOutputStream();
		}
		out.write(b);
	}

	public PSObject token(boolean packingMode, NameLookup lookup)
			throws IOException, SyntaxException, NameNotFoundException {
		getScanner();

		return scanner.nextToken(packingMode, lookup);
	}

	@Override
	public boolean execute(OperandStack os) {
		if (isLiteral()) {
			try {
				os.push(this.clone());
			} catch (CloneNotSupportedException e) {
				error(os, new Unimplemented());
			}
			return true;
		}

		try {
			getScanner();
		} catch (IOException e) {
			error(os, new IOError(e));
		}

		return Dispatcher.dispatch(os, this);
	}

	@Override
	public String getType() {
		return "stringtype";
	}

	public int size() {
		return length;
	}

	public char get(int i) {
		return value[start + i];
	}

	public void set(int i, byte b) {
		set(i, (char) b);
	}

	public void set(int i, int b) {
		set(i, (char) b);
	}

	public void set(int i, char c) {
		value[start + i] = c;
	}

	public PSString set(String s) {
		byte[] b = s.getBytes();
		for (int i = 0; i < b.length; i++) {
			set(i, b[i]);
		}
		return subString(0, b.length);
	}

	public PSString subString(int index, int count) {
		if ((index < 0) || (index + count) > length) {
			return null;
		}
		return new PSString(value, index, count, dsc);
	}

	public PSString subString(int index) {
		return subString(index, length - index);
	}

	public int indexOf(PSString s) {
		String string = getValue();
		String seek = s.getValue();
		return string.indexOf(seek);
	}

	public int compareTo(Object o) {
		String s1 = getValue();
		String s2 = ((PSString) o).getValue();
		return s1.compareTo(s2);
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSString) {
			return getValue().equals(((PSString) o).getValue());
		} else if (o instanceof PSName) {
			return getValue().equals(((PSName) o).getValue());
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSString(value, start, length, dsc);
	}

	@Override
	public PSObject copy() {
		return new PSString(new String(value, start, length), dsc);
	}

	@Override
	public String cvs() {
		return getValue();
	}

	@Override
	public String toString() {
		return "(" + getValue() + ")";
	}

	public String getValue() {
		return new String(value, start, length);
	}

	private void getScanner() throws IOException {
		if (scanner == null) {
			if (in == null) {
				in = new ByteInputStream();
			}
			scanner = new Scanner(in, dsc);
		}
	}

	class ByteInputStream extends PushbackInputStream {
		private int initStart;
		private int initLength;

		public ByteInputStream() {
			super(null, 1);
			initStart = start;
			initLength = length;
		}

		// make bytes available one at a time
		@Override
		public int available() throws IOException {
			return (length > 0) ? 1 : 0;
		}

		@Override
		public void close() throws IOException {
		}

		@Override
		public synchronized void mark(int readLimit) {
		}

		@Override
		public boolean markSupported() {
			return false;
		}

		@Override
		public int read() throws IOException {
			if (length > 0) {
				int b = value[start] & 0xFF;
				start++;
				length--;
				return b;
			}
			return -1;
		}

		@Override
		public int read(byte[] b) throws IOException {
			return read(b, 0, b.length);
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if (b == null) {
				throw new IllegalArgumentException();
			} else if ((off < 0) || (off > b.length) || (len < 0)
					|| ((off + len) > b.length) || ((off + len) < 0)) {
				throw new IndexOutOfBoundsException();
			} else if (len == 0) {
				return 0;
			}

			int c = read();
			if (c == -1) {
				return -1;
			}
			b[off] = (byte) c;

			return 1;
		}

		@Override
		public synchronized void reset() throws IOException {
			start = initStart;
			length = initLength;
		}

		@Override
		public long skip(long n) throws IOException {
			if (n <= length) {
				start += n;
				length -= n;
				return n;
			}
			return -1;
		}

		@Override
		public void unread(int b) throws IOException {
			if (start > 0) {
				start--;
				length++;
			}
		}

		@Override
		public void unread(byte[] b) throws IOException {
			unread(b, 0, b.length);
		}

		@Override
		public void unread(byte[] b, int off, int len) throws IOException {
			if (len > start) {
				throw new IOException();
			}
			start -= len;
			length += len;
		}

	}

	class ByteOutputStream extends OutputStream {

		public ByteOutputStream() {
			super();
		}

		@Override
		public void write(int b) throws IOException {
			if (length >= value.length) {
				throw new IOException();
			}

			value[length] = (char) (b & 0x00FF);
			length++;
		}
	}
}
