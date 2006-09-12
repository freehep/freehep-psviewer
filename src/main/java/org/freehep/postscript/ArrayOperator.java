// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

/**
 * Array Operators for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/ArrayOperator.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class ArrayOperator extends PSOperator {
        
    public static Class[] operators = {
        Array.class, ArrayBegin.class, ArrayEnd.class,
        AStore.class
    };

    public boolean execute(OperandStack os) {
        throw new RuntimeException("Cannot execute class: "+getClass());
    }
}

class Array extends ArrayOperator {
    { operandTypes = new Class[] {PSInteger.class}; }

    public boolean execute(OperandStack os) {
        PSInteger n = os.popInteger();
        if (n.getValue() < 0) {
            error(os, new RangeCheck());
        } else {
            os.push(new PSArray(n.getValue()));
        }
        return true;
    }
}

class ArrayBegin extends ArrayOperator {

    public String getName() {
        return("[");
    }
    
    public boolean execute(OperandStack os) {
        os.push(new PSMark());
        return true;
    }
}

class ArrayEnd extends ArrayOperator {

    public String getName() {
        return("]");
    }
    
    // FREEHEP-139: nothing done about InvalidAccess
    public boolean execute(OperandStack os) {
        int n = os.countToMark();
        if (n < 0) {
            error(os, new UnmatchedMark());
        } else {            
            PSObject[] a = new PSObject[n];
            for (int i=n-1; i>=0; i--) {
                PSObject o = os.popObject();
                a[i] = o;
            }
            PSMark mark = os.popMark();
            
            os.push(new PSArray(a));
        }
        return true;
    }
}

class AStore extends ArrayOperator {
    { operandTypes = new Class[] {PSArray.class}; }

    public boolean execute(OperandStack os) {
        PSArray a = os.popArray();
        int n = a.size();
        if (n > os.size()) {
            error(os, new StackUnderflow());
        } else {
            for (int i=n-1; i>=0; i--) {
                PSObject o = os.popObject();
                a.set(i, o);
            }
            os.push(a);            
        }
        return true;
    }
}

