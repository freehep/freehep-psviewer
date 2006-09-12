// Copyright 2001, FreeHEP.
package org.freehep.postscript;


/**
 * Lookup Interface for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/NameLookup.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public interface NameLookup {

    public PSObject lookup(PSObject key);
}