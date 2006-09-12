// Copyright 2001, FreeHEP.
package org.freehep.postscript;


/**
 * Name Not Found Exception for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/NameNotFoundException.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class NameNotFoundException extends Exception {

    public NameNotFoundException(String msg) {
        super(msg);
    }
}
