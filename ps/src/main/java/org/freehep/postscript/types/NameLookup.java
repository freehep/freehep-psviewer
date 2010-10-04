// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;


/**
 * Lookup Interface for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public interface NameLookup {

	PSObject lookup(PSObject key);
}