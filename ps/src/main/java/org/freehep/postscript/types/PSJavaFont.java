// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

import org.freehep.postscript.stacks.OperandStack;
import org.freehep.vectorgraphics.Font;

/**
 * Object is only for storage and lookup in Dictionaries and Arrays, not to be
 * executed.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSJavaFont.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSJavaFont extends PSSimple {
	private Font font;

	public PSJavaFont(Font f) {
		super("javafont", true);
		font = f;
	}

	@Override
	public boolean execute(OperandStack os) {
		// no-op
		return true;
	}

	@Override
	public String getType() {
		return "javafont";
	}

	public org.freehep.vectorgraphics.Font getFont() {
		return font;
	}

	@Override
	public int hashCode() {
		return font.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSJavaFont) {
			return font.equals(((PSJavaFont) o).font);
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSJavaFont(font);
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return "--" + name + "--" + font + " " + font.getTransform();
	}
}
