// Copyright 2001-2006, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.freehep.util.io.EEXECDecryption;

public class PSCharStringDecoder {

	protected int currentX, currentY;

	private InputStream decryption;

	private List charProc;

	protected int sbX, sbY, widthX, widthY;

	private PSPackedArray array;

	private PSDictionary dict;

	public PSCharStringDecoder(PSDictionary dict) {
		this.dict = dict;
	}

	public PSGlyph decode(PSString charString) throws IOException {

		decryption = new EEXECDecryption(charString.getInputStream(),
				EEXECDecryption.CHARSTRING_R, EEXECDecryption.N);
		currentX = currentY = 0;
		charProc = new LinkedList();

		boolean end = false;
		do {
			int byte1 = decryption.read();
			if (byte1 < 0) {
				end = true;
			} else if (byte1 >= 0 && byte1 <= 31) {
				// command
				if (byte1 != 12) {
					switch (byte1) {
					case 14:
						addCommand("eofill");
						break;
					case 13:
						hsbw();
						break;
					case 9:
						addCommand("closepath");
						break;
					case 6:
						addCommand("hlineto");
						break;
					case 22:
						addCommand("hmoveto");
						break;
					case 31:
						addCommand("hcurveto");
						break;
					case 5:
						addCommand("rlineto");
						break;
					case 21:
						addCommand("rmoveto");
						break;
					case 8:
						addCommand("rrcurveto");
						break;
					case 30:
						addCommand("vhcurveto");
						break;
					case 7:
						addCommand("vlineto");
						break;
					case 4:
						addCommand("vmoveto");
						break;
					case 1:
						ignore(2);
						break; // hstem
					case 3:
						ignore(2);
						break; // vstem
					default:
						System.err.println("Command " + byte1
								+ " not implemented");
						break;
					}
				} else {
					int byte2 = decryption.read();
					switch (byte2) {
					case 7:
						sbw();
						break;
					case 0:
						ignore(2);
						break; // dotsection
					case 2:
						ignore(6);
						break; // hstem3
					case 1:
						ignore(6);
						break; // vstem3
					case 33:
						addCommand("setcurrentpoint");
						break; // vstem3
					default:
						System.err.println("Command 12 " + byte2
								+ " not implemented");
						break;
					}
				}
			} else {
				int value = readNumber(byte1);
				addNumber(value);
			}
		} while (!end);

		PSObject[] obj = new PSObject[charProc.size()];
		Iterator i = charProc.iterator();
		int j = 0;
		while (i.hasNext()) {
			obj[j++] = (PSObject) i.next();
		}

		array = new PSPackedArray(obj);
		array.setExecutable();

		return new PSType1Glyph(array, widthX, sbX);
	}

	private int readNumber(int v) throws IOException {
		int value = 0;
		if (v >= 32 && v <= 246) {
			value = v - 139;
		} else if (v >= 247 && v <= 250) {
			int w = decryption.read();
			value = ((v - 247) * 256) + w + 108;
		} else if (v >= 251 && v <= 254) {
			int w = decryption.read();
			value = -((v - 251) * 256) - w - 108;
		} else { // v == 255
			value = decryption.read() << 3 * 8 + decryption.read() << 2 * 8 + decryption
					.read() << 1 * 8 + decryption.read() << 0 * 8;
		}
		return value;
	}

	private void addCommand(String name) {
		charProc.add(dict.get(name));
	}

	private void addNumber(int i) {
		charProc.add(new PSInteger(i));
	}

	private void ignore(int n) {
		for (int i = 0; i < n; i++) {
			charProc.remove(charProc.size() - 1);
		}
	}

	private int pop() {
		return ((PSInteger) charProc.remove(charProc.size() - 1)).getValue();
	}

	private void hsbw() {
		widthY = 0;
		widthX = pop();
		sbY = 0;
		sbX = pop();
		addInitialPoint();
	}

	private void sbw() {
		widthY = pop();
		widthX = pop();
		sbY = pop();
		sbX = pop();
		addInitialPoint();
	}

	private void addInitialPoint() {
		if (sbX == 0)
			return;
		charProc.add(0, dict.get("moveto"));
		charProc.add(0, new PSInteger(0));
		charProc.add(0, new PSInteger(sbX));
	}

	// public int getSBX() { return sbX; }
	// public int getSBY() { return sbY; }
	// public int getWidthX() { return widthX; }
	// public int getWidthY() { return widthY; }
	// public PSPackedArray getPackedArray() { return array; }

	// public PSGlyph getGlyph() {
	// return new PSGlyph(array, widthX, sbX);
	// return null;
	// }
}
