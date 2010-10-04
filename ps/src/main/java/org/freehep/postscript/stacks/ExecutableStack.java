// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.stacks;

import org.freehep.postscript.types.PSFile;

/**
 * Excutable Stack for PostScript Processor
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/ExecutableStack.java
 *          17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class ExecutableStack extends PostScriptStack {
	private static final long serialVersionUID = 1L;

	public PSFile getCurrentFile() {
		for (int i = elementCount - 1; i >= 0; i--) {
			if (elementData[i] instanceof PSFile) {
				return (PSFile) elementData[i];
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "ExecutionStack";
	}
}