// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Utilities for PostScript
 * 
 * @author Mark Donszelmann
 */
public final class PSUtils {
	private PSUtils() {
	}

	public static PSNumber parseNumber(String name)
			throws NumberFormatException {

		try {
			int hash = name.indexOf('#');
			if (hash > 0) {
				int radix = Integer.parseInt(name.substring(0, hash));
				return new PSInteger(Integer.parseInt(name.substring(hash + 1),
						radix));
			} else {
				return new PSInteger(Integer.parseInt(name));
			}
		} catch (NumberFormatException nfe) {
		}

		double d = Double.parseDouble(name);
		return new PSReal(d);
	}

}