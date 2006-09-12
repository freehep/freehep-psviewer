// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Extra Operators for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/ExtraOperator.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class ExtraOperator extends PSOperator {
    
    public static Class[] operators = {
        Break.class
    };

    public boolean execute(OperandStack os) {
        throw new RuntimeException("Cannot execute class: "+getClass());
    }
}

class Break extends ExtraOperator {

    public boolean execute(OperandStack os) {
        os.execStack().pop();
        throw new BreakException();
    }
}