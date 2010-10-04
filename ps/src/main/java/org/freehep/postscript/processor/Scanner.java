// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.processor;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.logging.Logger;

import org.freehep.postscript.dsc.DSC;
import org.freehep.postscript.errors.NameNotFoundException;
import org.freehep.postscript.errors.SyntaxException;
import org.freehep.postscript.stacks.PostScriptStack;
import org.freehep.postscript.types.NameLookup;
import org.freehep.postscript.types.PSArray;
import org.freehep.postscript.types.PSComment;
import org.freehep.postscript.types.PSDSC;
import org.freehep.postscript.types.PSMark;
import org.freehep.postscript.types.PSName;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSPackedArray;
import org.freehep.postscript.types.PSString;
import org.freehep.postscript.types.PSUtils;
import org.freehep.util.io.ASCII85InputStream;
import org.freehep.util.io.ASCIIHexInputStream;
import org.freehep.util.io.EncodingException;

/**
 * Scanner for PostScript input
 * 
 * @author Mark Donszelmann
 */
public class Scanner {
	private static Logger log = Logger.getLogger("org.freehep.postscript");

	private PushbackInputStream in;
	private ByteBuffer buffer;
	private int lineNo;
	private int openCurly, openSquare;
	private PostScriptStack procStack;
	private boolean nextIsComment;
	private boolean nextIsDSC;
	private DSC dsc;

	public Scanner(InputStream in, DSC dsc) throws IOException {
		this.in = new PushbackInputStream(in);
		buffer = new ByteBuffer(80);
		lineNo = 1;
		openCurly = 0;
		openSquare = 0;
		procStack = new PostScriptStack();
		nextIsComment = false;
		nextIsDSC = false;
		this.dsc = dsc;
	}

	public void close() throws IOException {
		in.close();
	}

	public PSObject nextToken(boolean packingMode, NameLookup lookup)
			throws IOException, SyntaxException, NameNotFoundException {
		PSObject obj = nextObject(packingMode, lookup);
		// deferred execution ?
		while ((openCurly > 0) && (!(obj instanceof PSComment))
				&& (!(obj instanceof PSDSC))) {
			procStack.push(obj);
			obj = nextObject(packingMode, lookup);
		}
		return obj;
	}

	private PSObject nextObject(boolean packingMode, NameLookup lookup)
			throws IOException, SyntaxException, NameNotFoundException {

		buffer.reset();

		// needed in case DSC reads too far searching for continuation DSC
		// comments.
		if (nextIsComment) {
			return readComment();
		}
		if (nextIsDSC) {
			return readDSC();
		}

		int next = readNext();
		switch (next) {
		case -1:
			return null;

		case '(':
			return readString();

		case ')':
			throw new SyntaxException(lineNo, "Unbalanced Parenthesis");

		case '~':
			next = in.read();
			if (next == '>') {
				throw new SyntaxException(lineNo, "Non matching '~>'");
			}
			if (next > 0) {
				in.unread(next);
			}
			return readName('~', false);

		case '>':
			next = in.read();
			if (next == '>') {
				return new PSName(">>");
			}
			if (next > 0) {
				in.unread(next);
			}
			throw new SyntaxException(lineNo, "Non matching '>'");

		case '<':
			return readEncoding();

		case '[':
			openSquare++;
			return new PSName("[");

		case ']':
			if (openSquare > 0) {
				openSquare--;
				return new PSName("]");
			}
			throw new SyntaxException(lineNo, "Non matching ']'");

		case '{':
			openCurly++;
			return new PSMark();

		case '}':
			if (openCurly > 0) {
				openCurly--;
			}

			int n = procStack.countToMark();
			if (n < 0) {
				throw new SyntaxException(lineNo, "Unmatched '{'");
			}

			PSObject[] array = new PSObject[n];
			for (int i = n - 1; i >= 0; i--) {
				array[i] = procStack.popObject();
			}
			procStack.popMark();

			PSPackedArray proc;
			if (packingMode) {
				proc = new PSPackedArray(array);
			} else {
				proc = new PSArray(array);
			}
			proc.setExecutable();

			return proc;

		case '/':
			return readLiteral(lookup);

		case '%': // normal comment or DSC
			next = in.read();
			if (((next == '%') || (next == '!')) && (dsc != null)) {
				return readDSC();
			}
			if (next > 0) {
				in.unread(next);
			}
			return readComment();

		case '@': // special name we do not handle at this time, normally
			// associated to PCL commands
			// FIXME, look up the PCL details somewhere.
			return readComment();

		case '+':
		case '-':
		case '.':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return readNumber(next);

		default:
			return readName(next, false);
		}
	}

	public int getLineNo() {
		return lineNo;
	}

	private PSString readString() throws IOException {
		int balance = 1;
		int next = in.read();
		;
		while ((balance > 0) && (next != -1)) {
			switch (next) {
			case '\\':
				escapeChar();
				break;

			case '(':
				balance++;
				buffer.append(next);
				break;

			case ')':
				balance--;
				if (balance > 0) {
					buffer.append(next);
				}
				break;

			case '\r': // replaced by LF in string
				next = in.read();
				if ((next != '\n') && (next > 0)) {
					in.unread(next);
				}
				buffer.append('\n');
				lineNo++;
				break;

			case '\n':
				buffer.append(next);
				lineNo++;
				break;

			default:
				buffer.append(next);
				break;
			}
			next = in.read();
		}

		if ((balance == 0) && (next > 0)) {
			in.unread(next);
		}
		return new PSString(buffer.getChars());
	}

	private void escapeChar() throws IOException {
		int next = in.read();
		if (next == -1) {
			throw new EOFException("Unexpected End Of File");
		}

		switch (next) {
		case 'n':
			buffer.append('\n');
			break;
		case 'r':
			buffer.append('\r');
			break;
		case 't':
			buffer.append('\t');
			break;
		case 'b':
			buffer.append('\b');
			break;
		case 'f':
			buffer.append('\f');
			break;
		case '\\':
			buffer.append('\\');
			break;
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
			StringBuffer octal = new StringBuffer(3);
			octal.append(next - '0');
			next = in.read();
			if (next == -1) {
				throw new EOFException("Unexpected End Of File");
			}
			if (('0' <= next) && (next <= '7')) {
				octal.append(next - '0');
				next = in.read();
				if (next == -1) {
					throw new EOFException("Unexpected End Of File");
				}
				if (('0' <= next) && (next <= '7')) {
					octal.append(next - '0');
					// next = in.read();
				} else if (next > 0) {
					in.unread(next);
				}
			} else if (next > 0) {
				in.unread(next);
			}
			buffer.append(Integer.parseInt(octal.toString(), 8));
			return;

		case '\n':
			next = in.read();
			lineNo++;
			return;

		case '\r':
			next = in.read();
			if (next == '\n') {
				next = in.read();
			}
			lineNo++;
			return;

		case '(':
		case ')':
		default:
			buffer.append(next);
			break;
		}
	}

	private PSComment readComment() throws IOException {
		nextIsComment = false;

		int next = in.read();
		while ((next != -1) && (next != '\r') && (next != '\n')) {
			buffer.append(next);
			next = in.read();
		}

		return new PSComment(buffer.getString());
	}

	private PSDSC readDSC() throws IOException {
		nextIsDSC = false;

		while (true) {
			int next = in.read();
			while ((next != -1) && (next != '\r') && (next != '\n')) {
				buffer.append(next);
				next = in.read();
			}

			// read next ignoring '\r'
			next = in.read();
			if (next == '\r') {
				next = in.read();
			}

			if (next != '%') {
				if (next > 0) {
					in.unread(next);
				}
				break;
			}

			next = in.read();
			if (next != '%') {
				if (next > 0) {
					in.unread(next);
				}
				nextIsComment = true;
				break;
			}

			next = in.read();
			if (next != '+') {
				if (next > 0) {
					in.unread(next);
				}
				nextIsDSC = true;
				break;
			}
		}

		return new PSDSC(buffer.getString(), dsc);
	}

	private PSObject readLiteral(NameLookup lookup) throws IOException,
			SyntaxException, NameNotFoundException {
		int next = in.read();
		if (next == -1) {
			throw new IOException("Unexpected EOF reached");
		}

		if (next == '/') {
			// immediate
			next = in.read();
			PSName name = readName(next, true);
			PSObject obj = lookup.lookup(name);
			if (obj == null) {
				throw new NameNotFoundException("Immediate name /'" + name
						+ " not found.");
			}
			return obj;
		}
		return readName(next, true);
	}

	private PSName readName(int next, boolean literal) throws IOException,
			SyntaxException {
		boolean found = false;
		while (!found) {
			switch (next) {
			case -1:
			case '\000':
			case '\b':
			case '\t':
			case '\n':
			case '\f':
			case '\r':
			case ' ':
				found = true;
				break;

			case ')':
				throw new SyntaxException(lineNo, "Unbalanced Parenthesis");

			case '%':
			case '(':
			case '/':
			case '<':
			case '>':
			case '[':
			case ']':
			case '{':
			case '}':
				found = true;
				in.unread(next);
				break;

			default:
				buffer.append(next);
				next = in.read();
				break;
			}
		}

		PSName name = new PSName(buffer.getString(), literal);
		return name;
	}

	private PSObject readNumber(int next) throws IOException, SyntaxException {
		String name = readName(next, false).getValue();
		try {
			return PSUtils.parseNumber(name);
		} catch (NumberFormatException e) {
		}

		return new PSName(name);
	}

	private PSObject readEncoding() throws IOException, SyntaxException {
		int next = in.read();
		switch (next) {
		case '~':
			ASCII85InputStream in85 = new ASCII85InputStream(in);
			try {
				next = in85.read();
				while (next != -1) {
					buffer.append(next);
					next = in85.read();
				}
			} catch (EncodingException e) {
				SyntaxException se = new SyntaxException(lineNo, e.getMessage());
				se.initCause(e);
				throw se;
			}
			lineNo += in85.getLineNo() - 1;
			return new PSString(buffer.getChars());

		case '<':
			return new PSName("<<");

		default:
			if (next > 0) {
				in.unread(next);
			}
			ASCIIHexInputStream inHex = new ASCIIHexInputStream(in);
			try {
				next = inHex.read();
				while (next != -1) {
					buffer.append(next);
					next = inHex.read();
				}
			} catch (EncodingException e) {
				SyntaxException se = new SyntaxException(lineNo, e.getMessage());
				se.initCause(e);
				throw se;
			}
			lineNo += inHex.getLineNo() - 1;
			return new PSString(buffer.getChars());
		}
	}

	// skips whitespace until next token
	private int readNext() throws IOException {
		boolean ws;
		int next;
		do {
			next = in.read();
			switch (next) {
			case '\r':
				next = in.read();
				if ((next != '\n') && (next > 0)) {
					in.unread(next);
				}
				// fallthrough
			case '\n':
				lineNo++;
				ws = true;
				break;
			case '\000':
			case '\b':
			case '\t':
			case '\f':
			case ' ':
				ws = true;
				break;
			default:
				ws = false;
			}
		} while ((next != -1) && ws);

		return (ws) ? -1 : next;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			log.info("Usage: Scanner filename.ps");
			System.exit(1);
		}

		DSC dsc = new DSC();
		Scanner scanner = new Scanner(new PushbackInputStream(
				new FileInputStream(args[0])), dsc);

		PSObject token;
		while ((token = scanner.nextToken(true, null)) != null) {
			log.info(scanner.getLineNo() + ": "
					+ token.getClass().getName() + ": " + token);
		}

		scanner.close();
	}
}
