// Copyright 2001, FreeHEP.
package org.freehep.postscript;



/**
 * OperandStack for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/OperandStack.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class OperandStack extends PostScriptStack {
    private Processor processor;
    private PSGState gstate;
    private final boolean secure;
    private boolean packingMode = true;

    public OperandStack(Processor p, PSGState gs, boolean secure) {
        super();
        processor = p;
        gstate = gs;
        this.secure = secure;
    }

    public PSGState gstate() {
        return gstate;
    }
    
    public void gsave() {
        gstateStack().push((PSGState)gstate.copy());
    }
    
    public void grestore() {
        gstate = gstateStack().popGState();
    }
    
    public boolean isSecure() {
        return secure;
    }
    
    public void setPackingMode(boolean state) {
        packingMode = state;
    }
    
    public boolean packingMode() {
        return packingMode;
    }
    
    public DictionaryStack dictStack() {
        return processor.dictStack();
    }
    
    public ExecutableStack execStack() {
        return processor.execStack();
    }
    
    public GStateStack gstateStack() {
        return processor.gstateStack();
    }
    
    public DSC getDSC() {
        return processor.getDSC();
    }
    
    public void push(PSObject o) {
        super.push(o);
    }
    
    public void push(int i) {
        super.push(new PSInteger(i));
    }
    
    public void push(double d) {
        super.push(new PSReal(d));
    }
    
    public void push(boolean b) {
        super.push(new PSBoolean(b));
    }
    
    public PSComposite popComposite() {
        return (PSComposite)super.pop();
    }
    
    public PSNumber popNumber() {
        return (PSNumber)super.pop();
    }
    
    public PSInteger popInteger() {
        return (PSInteger)super.pop();
    }
    
    public PSReal popReal() {
        return (PSReal)super.pop();
    }
    
    public PSBoolean popBoolean() {
        return (PSBoolean)super.pop();
    }
    
    public PSName popName() {
        return (PSName)super.pop();
    }
    
    public PSFontID popFontID() {
        return (PSFontID)super.pop();
    }
    
    public PSGState popGState() {
        return (PSGState)super.pop();
    }
    
    public PSArray popArray() {
        return (PSArray)super.pop();
    }
    
    public PSPackedArray popPackedArray() {
        return (PSPackedArray)super.pop();
    }
    
    public PSString popString() {
        return (PSString)super.pop();
    }
    
    public PSDictionary popDictionary() {
        return (PSDictionary)super.pop();
    }
    
    public PSFile popFile() {
        return (PSFile)super.pop();
    }
    
    public PSSave popSave() {
        return (PSSave)super.pop();
    }
    
    public PSDataSource popDataSource() {
        return (PSDataSource)super.pop();
    }
    
    public PSDataTarget popDataTarget() {
        return (PSDataTarget)super.pop();
    }
    
    public void exch() {
        Object t = elementData[elementCount-1];
        elementData[elementCount-1] = elementData[elementCount-2];
        elementData[elementCount-2] = t;
    }
    
    public void dup() {
        super.push(((PSObject)elementData[elementCount-1]).clone());
    }
    
    public void copy(int n) {
        int m = elementCount-n;
        int j = elementCount;
        for (int i=m; i<j; i++) {
            super.push(((PSObject)elementData[i]).clone());
        }
    }
    
    public void index(int n) {
        if (n > 0) {
            super.push(((PSObject)elementData[elementCount-n-1]).clone());
        }
    }
    
    public void roll(int n, int j) {
        // make j positive   
        if (j < 0) {
            j = n + j;
        }
        
        // skip if unnecessary
        if ((j == n) || (j == 0)) {
            return;
        }
        
        // move to tmp
        Object[] tmp = new Object[j];
        for (int i=0; i<j; i++) {
            tmp[i] = elementData[elementCount - j + i];
        }
        
        // move pre
        for (int i=n-j-1; i>=0; i--) {
            elementData[elementCount - n + j + i] = elementData[elementCount - n + i];
        }
        
        // move post
        for (int i=0; i<j; i++) {
            elementData[elementCount - n + i] = tmp[i];
        }
    }
            
    public boolean clearToMark() {
        int i = elementCount - 1;
        if (i < 0) return false;
        
        while (i >= 0 && !(elementData[i] instanceof PSMark)) {
            i--;
        }
        
        if (i < 0) {
            return false;
        }
        elementCount = i;
        return true;
    }
    
    public boolean checkType(Class type) {
        if (elementCount < 1) return false;
        
        if (type.isInstance(elementData[elementCount-1])) {
           return true;
        }
        return false;
    }

    public boolean checkType(Class type1, Class type2) {
        if (elementCount < 2) return false;
        
        if (type1.isInstance(elementData[elementCount-2]) &&
            type2.isInstance(elementData[elementCount-1])) {
           return true;
        }
        return false;
    }

    public boolean checkType(Class type1, Class type2, Class type3) {
        if (elementCount < 3) return false;
        
        if (type1.isInstance(elementData[elementCount-3]) &&
            type2.isInstance(elementData[elementCount-2]) &&
            type3.isInstance(elementData[elementCount-1])) {
           return true;
        }
        return false;
    }

    public boolean checkType(Class type1, Class type2, Class type3, Class type4) {
        if (elementCount < 4) return false;
        
        if (type1.isInstance(elementData[elementCount-4]) &&
            type2.isInstance(elementData[elementCount-3]) &&
            type3.isInstance(elementData[elementCount-2]) &&
            type4.isInstance(elementData[elementCount-1])) {
           return true;
        }
        return false;
    }

    public boolean checkType(Class type1, Class type2, Class type3, Class type4, Class type5) {
        if (elementCount < 5) return false;
        
        if (type1.isInstance(elementData[elementCount-5]) &&
            type2.isInstance(elementData[elementCount-4]) &&
            type3.isInstance(elementData[elementCount-3]) &&
            type4.isInstance(elementData[elementCount-2]) &&
            type5.isInstance(elementData[elementCount-1])) {
           return true;
        }
        return false;
    }

    public boolean checkType(Class[] types) {
        if (elementCount < types.length) return false;
        
        for (int i=0; i<types.length; i++) {
            if (!types[i].isInstance(elementData[elementCount-types.length+i])) {
                return false;
            }
        }
        return true;
    }
    
    public void printStack() {
        System.out.println();
        System.out.println("== Top Operand Stack ==");
        super.printStack();
        System.out.println("== Bottom Operand Stack ==");
    }

    public String toString() {
        return "OperandStack";
    }
}
