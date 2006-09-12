// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

/**
 * Object is only for storage and lookup in Dictionaries and Arrays, 
 * not to be executed.
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSJavaGlyph.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSJavaGlyph extends PSGlyph {
    private GlyphVector gv;
    
    public PSJavaGlyph(GlyphVector gv) {
        this.gv = gv;
        GlyphMetrics gm = gv.getGlyphMetrics(0);
        wx = gm.getAdvance();
        Rectangle2D r = gm.getBounds2D();
        llx = r.getMinX();
        lly = r.getMinY();
        urx = r.getMaxX();
        ury = r.getMaxY();
    }
        
    public GlyphVector getGlyph() {
        return gv;
    }
        
    // FIXME    
    public int hashCode() {
        return 0;
    }

    // FIXME
    public boolean equals(Object o) {
        return false;
    }

    // FIXME
    public Object clone() {
        return null;
    }
}

