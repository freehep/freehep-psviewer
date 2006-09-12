// Copyright 2004, FreeHEP.
package org.freehep.postscript;

import java.util.*;

/**
 * Class for signalling DSC comments
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/DSCEventListener.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public interface DSCEventListener {

    public void dscCommentFound(DSCEvent event);
}
