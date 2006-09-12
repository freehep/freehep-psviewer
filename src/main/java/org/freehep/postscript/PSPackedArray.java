// Copyright 2001, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSPackedArray.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSPackedArray extends PSComposite {    
    protected PSObject[] array;
    protected int start = 0;
    protected int length = 0;
    
    protected PSPackedArray(String name, PSObject[] a, int index, int count) {
        super(name, true);
        array = a;
        start = index;
        length = count;
        access = READ_ONLY;
    }
    
    protected PSPackedArray(int n) {
        super("packedarray", true);
        array = new PSObject[n];        
        for (int i=0; i<array.length; i++) {
            array[i] = new PSNull();
        }
        start = 0;
        length = n;
        access = READ_ONLY;
    }    
    
    public PSPackedArray(PSObject[] a) {
        super("packedarray", true);
        array = a;
        start = 0;
        length = a.length;
        access = READ_ONLY;
    }
    
    public PSPackedArray(float[] f) {
        this(f.length);
        for (int i=0; i<f.length; i++) {
            array[i] = new PSReal(f[i]);
        }
    }
    
    public PSPackedArray(double[] d) {
        this(d.length);
        for (int i=0; i<d.length; i++) {
            array[i] = new PSReal(d[i]);
        }
    }
    
    public PSPackedArray(String[] s) {
        this(s.length);
        for (int i=0; i<s.length; i++) {
            array[i] = new PSName(s[i], true);
        }
    }
    
    private int execIndex = -1;
    public boolean execute(OperandStack os) {
        if (isLiteral()) {
            os.push(this);
            return true;
        }
        
        if (execIndex == -1) {
            // replace yourself with a copy, since the execIndex is a state variable
            PSPackedArray copy = (PSPackedArray)clone();
            copy.setExecutable();
            copy.execIndex = 0;
            os.execStack().pop();
            os.execStack().push(copy);
            return false;
        }

        if (execIndex < size()) {
            PSObject obj = get(execIndex);
            if ((obj instanceof PSPackedArray) && 
                ((PSPackedArray)obj).isExecutable()) {
                // subarrays (exec) are just pushed on the stack.
                os.push(obj);
            } else {
                os.execStack().push(obj);
            }
            execIndex++;
            return false;
        } else {
            execIndex = -1;
            return true;
        }
    }
    
    public String getType() {
        return "packedarraytype";
    }

    public PSObject get(int i) {
        return array[start+i];
    }

    public int getInteger(int i) {
        return ((PSInteger)array[start+i]).getValue();
    }

    public PSName getName(int i) {
        return (PSName)array[start+i];
    }

    public PSDictionary getDictionary(int i) {
        return (PSDictionary)array[start+i];
    }

    public void bind(int i, PSOperator op) {
        array[start+i] = op;
    }

    public int size() {
        return length;
    }    
    
    public PSPackedArray subPackedArray(int index, int count) {
        if ((index < 0) || (index+count) > length) {
            throw new IllegalArgumentException("Trying to create subPackedArray("+index+", "+count+") with length="+length+".");
        }        
        return new PSPackedArray(name, array, index, count);
    }       

    public int hashCode() {
        return array.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof PSPackedArray) {
            return (array == ((PSPackedArray)o).array);
        }
        return false;
    }

    public Object clone() {
        PSPackedArray p = new PSPackedArray(name, array, start, length);
        if (isExecutable()) p.setExecutable();
        return p;
    }
    
    public PSObject copy() {
        PSPackedArray p = new PSPackedArray(toObjects());
        if (isExecutable()) p.setExecutable();
        return p;
    }
    
    public float[] toFloats() {
        float[] f = new float[length];
        for (int i=0; i<length; i++) {
            f[i] = ((PSNumber)get(i)).getFloat();
        }
        return f;
    }        
    
    public double[] toDoubles() {
        double[] d = new double[length];
        for (int i=0; i<length; i++) {
            d[i] = ((PSNumber)get(i)).getDouble();
        }
        return d;
    }        
    
    public PSObject[] toObjects() {
        PSObject[] o = new PSObject[length];
        for (int i=0; i<length; i++) {
            o[i] = get(i);
        }
        return o;
    }        
    
    public String cvs() {
        return toString();
    }
    
    public String toString() {
        return "--"+((isExecutable()) ? "*" : "")+name+
               " ("+start+".."+(start+length)+", "+execIndex+") --";
    }
    
    public String toPrint() {
        StringBuffer s = new StringBuffer();
        s.append((isExecutable()) ? "{ " : "[ "); 
        for (int i=0; i<size(); i++) {
            s.append(get(i).toString());
            s.append(" ");
        }
        s.append((isExecutable()) ? "}" : "]"); 
        return s.toString();
    }

}
