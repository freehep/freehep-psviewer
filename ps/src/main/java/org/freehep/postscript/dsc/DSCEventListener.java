// Copyright 2004, FreeHEP.
package org.freehep.postscript.dsc;

/**
 * Class for signaling DSC comments
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/DSCEventListener.java
 *          829a8d93169a 2006/12/08 09:03:07 duns $
 */
public interface DSCEventListener {

	void dscCommentFound(DSCEvent event);
}
