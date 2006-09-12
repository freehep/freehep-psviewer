// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Utilities for PostScript
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSUtils.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSUtils {
    private PSUtils() {
    }

    public static PSNumber parseNumber(String name) throws NumberFormatException {
        
        try {
            int hash = name.indexOf('#');
            if (hash > 0) {
                int radix = Integer.parseInt(name.substring(0,hash));
                return new PSInteger(Integer.parseInt(name.substring(hash+1), radix));
            } else {
                return new PSInteger(Integer.parseInt(name));
            }
        } catch (NumberFormatException nfe) {
        }

        double d = Double.parseDouble(name);
        return new PSReal(d);
    }

}