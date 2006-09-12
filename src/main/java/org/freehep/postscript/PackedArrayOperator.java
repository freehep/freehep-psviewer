// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * PackedArray Operators for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PackedArrayOperator.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PackedArrayOperator extends PSOperator {
    
    public static Class[] operators = {
        PackedArray.class, SetPacking.class, CurrentPacking.class
    };

    public boolean execute(OperandStack os) {
        throw new RuntimeException("Cannot execute class: "+getClass());
    }
}

class PackedArray extends PackedArrayOperator {
    { operandTypes = new Class[] {PSInteger.class}; }
    
    // FREEHEP-155: nothing done about InvalidAccess
    public boolean execute(OperandStack os) {
        PSInteger n = os.popInteger();
        if (n.getValue() < 0) {
            error(os, new RangeCheck());
        } else {            
            PSObject[] a = new PSObject[n.getValue()];
            for (int i=n.getValue()-1; i>=0; i--) {
                PSObject o = os.popObject();
                a[i] = o;
            }
            
            os.push(new PSPackedArray(a));
        }
        return true;
    }
}

class SetPacking extends PackedArrayOperator {
    { operandTypes = new Class[] {PSBoolean.class}; }

    public boolean execute(OperandStack os) {
        PSBoolean b = os.popBoolean();
        os.setPackingMode(b.getValue());
        return true;
    }
}

class CurrentPacking extends PackedArrayOperator {

    public boolean execute(OperandStack os) {
        os.push(os.packingMode());
        return true;
    }
}

