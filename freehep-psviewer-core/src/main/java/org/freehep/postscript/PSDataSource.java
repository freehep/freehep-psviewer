// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;
import java.io.InputStream;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSDataSource.java
 *          829a8d93169a 2006/12/08 09:03:07 duns $
 */
public interface PSDataSource {

	InputStream getInputStream();

	int read() throws IOException;

	void reset() throws IOException;

	DSC getDSC();
}
