// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.io.InputStream;
import java.io.IOException;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSDataSource.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public interface PSDataSource {

    public InputStream getInputStream();
    public int read() throws IOException;
    public void reset() throws IOException;
    public DSC getDSC();
}
