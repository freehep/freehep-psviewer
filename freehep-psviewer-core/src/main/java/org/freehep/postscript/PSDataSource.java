// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.io.IOException;
import java.io.InputStream;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public interface PSDataSource {

	InputStream getInputStream();

	int read() throws IOException;

	void reset() throws IOException;

	DSC getDSC();
}
