// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSObject.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public abstract class PSObject implements Cloneable {
    private boolean literal;
    protected String name;
    
    public PSObject(String name, boolean literal) {
        this.name = name;
        this.literal = literal;
    }

    public void setName(String s) {
        name = s;
    }
    
    public abstract int hashCode();
    public abstract boolean equals(Object obj);
    public abstract Object clone();
    public abstract PSObject copy();
    public abstract String getType();
    
    // returns true if the execStack needs to be popped after execute returns
    public abstract boolean execute(OperandStack os);
    public boolean checkAndExecute(OperandStack os) {
        return execute(os);
    }
        
    public static void error(OperandStack os, ErrorOperator error) {
        // set the stack back to where the operands started
        os.reset();
        
        // FIXME, we could report errors by string
        DictionaryStack ds = os.dictStack();
        ErrorOperator e = (ErrorOperator)ds.errorDictionary().get(error.getName());
        os.execStack().push(e);

        throw new PostScriptError();
    }

    public boolean isLiteral() {
        return literal;
    }
    
    public void setLiteral() {
        literal = true; 
    }
    
    public void setExecutable() {
        literal = false;
    }
    
    public boolean isExecutable() {
        return !literal;
    }
  
    public abstract String cvs();
    
    public String toString() {
        return "--nostringval--";
    }
    
    public String toPrint() {
        return toString();
    }
}

