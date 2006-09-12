// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * String Operators for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/StringOperator.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class StringOperator extends PSOperator {
    
    public static Class[] operators = {
        StringString.class, AnchorSearch.class, Search.class
    };

    public boolean execute(OperandStack os) {
        throw new RuntimeException("Cannot execute class: "+getClass());
    }
}

class StringString extends StringOperator {
    { operandTypes = new Class[] {PSInteger.class}; }

    public String getName() {
        return ("string");
    }

    public boolean execute(OperandStack os) {
        PSInteger n = os.popInteger();
        if (n.getValue() < 0) {
            error(os, new RangeCheck());
        } else {
            os.push(new PSString(n.getValue()));
        }
        return true;
    }
}

class AnchorSearch extends StringOperator {
    { operandTypes = new Class[] {PSString.class, PSString.class}; }

    public boolean execute(OperandStack os) {
        PSString seek = os.popString();
        PSString string = os.popString();
        if (string.indexOf(seek) != 0) {
            os.push(string);
            os.push(false);
        } else {
            os.push(string.subString(seek.size()));
            os.push(string.subString(0, seek.size()));
            os.push(true);
        }
        return true;
    }
}

class Search extends StringOperator {
    { operandTypes = new Class[] {PSString.class, PSString.class}; }

    public boolean execute(OperandStack os) {
        PSString seek = os.popString();
        PSString string = os.popString();
        int pos = string.indexOf(seek);
        if (pos < 0) {
            os.push(string);
            os.push(false);
        } else {
            os.push(string.subString(pos + seek.size()));
            os.push(string.subString(pos, seek.size()));
            os.push(string.subString(0, pos));
            os.push(true);
        }
        return true;
    }
}

