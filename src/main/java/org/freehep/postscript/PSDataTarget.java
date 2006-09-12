// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSDataTarget.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public interface PSDataTarget {
    
    public OutputStream getOutputStream();
    public void write(int b, boolean secure) throws IOException;
}
