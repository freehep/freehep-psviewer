// Copyright 2004, FreeHEP.
package org.freehep.postscript;

/**
 * Class for signalling DSC comments
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/DSCEventListener.java
 *          829a8d93169a 2006/12/08 09:03:07 duns $
 */
public interface DSCEventListener {

	public void dscCommentFound(DSCEvent event);
}
