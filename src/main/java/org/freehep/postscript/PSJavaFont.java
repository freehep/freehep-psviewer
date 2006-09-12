// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.awt.Font;

/**
 * Object is only for storage and lookup in Dictionaries and Arrays, 
 * not to be executed.
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSJavaFont.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSJavaFont extends PSSimple {
    private Font font;
    
    public PSJavaFont(Font f) {
        super("javafont", true);
        font = f;
    }
    
    public boolean execute(OperandStack os) {
        // no-op
        return true;
    }
        
    public String getType() {
        return "javafont";
    }
    
    public Font getFont() {
        return font;
    }
    
    public int hashCode() {
        return font.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof PSJavaFont) {
            return font.equals(((PSJavaFont)o).font);
        }
        return false;
    }

    public Object clone() {
        return new PSJavaFont(font);
    }
    
    public String cvs() {
        return toString();
    }
    
    public String toString() {
        return "--"+name+"--"+font+" "+font.getTransform();
    }
}

