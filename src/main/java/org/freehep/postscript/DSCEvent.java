// Copyright 2004, FreeHEP.
package org.freehep.postscript;

import java.util.*;

/**
 * Class for signalling DSC comments
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/DSCEvent.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class DSCEvent extends EventObject {

    public static int PARSED    = 0;    // comment found and arguments (if any) parsed ok
    public static int UNPARSED  = 1;    // comment not found
    public static int ERROR     = 2;    // comment found, but arguments did not parse ok

    private String comment;
    private Object args;
    private int state;

    public DSCEvent(Object src, String comment, Object args, int state) {
        super(src);
        this.comment = comment;
        this.args = args;
        this.state = state;
    }
    
    public String getComment() {
        return comment;
    }
    
    public Object getArgs() {
        return args;
    }
    
    public int getState() {
        return state;
    }
    
    public String toString() {
        return "DSCEvent ["+comment+", "+args+", "+state+"]";
    }        
}
