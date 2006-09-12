// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Error Operators for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/ErrorOperator.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public abstract class ErrorOperator extends PSOperator {
    
    public static Class[] operators = {
        ConfigurationError.class, DictFull.class, DictStackOverflow.class,
        DictStackUnderflow.class, ExecStackOverflow.class, HandleError.class,
        Interrupt.class, InvalidAccess.class, InvalidExit.class,
        InvalidFileAccess.class, InvalidFont.class, InvalidRestore.class,
        IOError.class, LimitCheck.class, NoCurrentPoint.class,
        RangeCheck.class, StackOverflow.class, StackUnderflow.class,
        SyntaxError.class, Timeout.class, TypeCheck.class, 
        Undefined.class, UndefinedFileName.class, UndefinedResource.class,
        UndefinedResult.class, UnmatchedMark.class,
        Unregistered.class, VMError.class, Unimplemented.class
    };
    
    // default error handler
    public boolean execute(OperandStack os) {
        // fill error dict
        PSDictionary error = os.dictStack().dollarError();
        error.put("newerror", new PSBoolean(true));
        error.put("errorname", new PSName(getName()));
        error.put("command", os.execStack().peekObject(1));
        if (error.get("recordstacks").equals(new PSBoolean(true))) {
            error.put("ostack", stackToArray(os));            
            error.put("estack", stackToArray(os.execStack()));
            error.put("dstack", stackToArray(os.dictStack()));
        }
        
        // now execute stop
        os.execStack().pop();
        os.execStack().push("stop");
        
        return false;
    }

    private static PSArray stackToArray(PostScriptStack stack) {
        PSArray array = new PSArray(stack.size());
        stack.copyInto(array);
        return array;
    }        
}

class ConfigurationError extends ErrorOperator {
}

class DictFull extends ErrorOperator {
}

class DictStackOverflow extends ErrorOperator {
}

class DictStackUnderflow extends ErrorOperator {
}

class ExecStackOverflow extends ErrorOperator {
}

class HandleError extends ErrorOperator {

    // FIXME, should print more
    public boolean execute(OperandStack os) {
        // report on error
        PSDictionary error = os.dictStack().dollarError();
        if (error.get("newerror").equals(new PSBoolean(true))) {
            System.err.print("\n\n%%[Error: ");
            System.err.print(error.get("errorname").toPrint());
            System.err.print("; Offending Command: ");
            System.err.print(error.get("command").toPrint());
            System.err.println("]%%\n");
            
            if (!error.get("errorinfo").equals(new PSNull())) {
                System.err.print("Error Info: ");
                System.err.println(error.get("errorinfo").toPrint());
                System.err.println();
            }
            
            if (error.get("recordstacks").equals(new PSBoolean(true))) {
                System.err.println("Operand Stack (bottom..top)");
                System.err.println(error.get("ostack").toPrint());
                System.err.println();
                
                System.err.println("Execution Stack (bottom..top)");
                System.err.println(error.get("estack").toPrint());
                System.err.println();
                
                System.err.println("Dictionary Stack (bottom..top)");
                System.err.println(error.get("dstack").toPrint());
                System.err.println();                
            }
            
            // reset error dict
            error.put("newerror", new PSBoolean(false));
            error.put("errorinfo", new PSNull());
        }

        return true;
    }
}

class Interrupt extends ErrorOperator {
    public boolean execute(OperandStack os) {
        os.execStack().pop();
        os.execStack().push("stop");
        return false;
    }
}

class InvalidAccess extends ErrorOperator {
}

class InvalidExit extends ErrorOperator {
}

class InvalidFileAccess extends ErrorOperator {
}

class InvalidFont extends ErrorOperator {
}

class InvalidRestore extends ErrorOperator {
}

class IOError extends ErrorOperator {
}

class LimitCheck extends ErrorOperator {
}

class NoCurrentPoint extends ErrorOperator {
}

class RangeCheck extends ErrorOperator {
}

class StackOverflow extends ErrorOperator {
}

class StackUnderflow extends ErrorOperator {
}

class SyntaxError extends ErrorOperator {
}

class Timeout extends ErrorOperator {
    public boolean execute(OperandStack os) {
        os.execStack().pop();
        os.execStack().push("stop");
        return false;
    }
}

class TypeCheck extends ErrorOperator {
}

class Undefined extends ErrorOperator {
}

class UndefinedFileName extends ErrorOperator {
}

class UndefinedResource extends ErrorOperator {
}

class UndefinedResult extends ErrorOperator {
}

class UnmatchedMark extends ErrorOperator {
}

class Unregistered extends ErrorOperator {
}

class VMError extends ErrorOperator {
    public String getName() {
        return "VMerror";
    }
}

// Not part of standard, added while implementation is not complete
class Unimplemented extends ErrorOperator {
}
