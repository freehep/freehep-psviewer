// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.lang.reflect.Field;

/**
 * OperandStack for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/GStateStack.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class GStateStack extends PostScriptStack {
            
    public GStateStack() {
        super();
    }
    
    public Object push(Object o){
        throw new IllegalArgumentException("Only PSGState allowed on stack.");
    }
    
    public PSGState push(PSGState gs) {
        return (PSGState)super.push(gs);
    }
        
    public PSGState popGState() {
        return (PSGState)pop();
    }
 
    public String toString() {
        return "GStateStack";
    }
}