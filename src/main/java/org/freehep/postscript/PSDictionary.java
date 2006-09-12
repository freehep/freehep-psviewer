// Copyright 2001, FreeHEP.
package org.freehep.postscript;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSDictionary.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSDictionary extends PSComposite {
    protected Hashtable table;
    
    protected PSDictionary(Hashtable t) {
        super("dictionary", true);
        table = t;
    }
    
    public PSDictionary(int n) {
        this(new Hashtable(n));
    }
    
    public PSDictionary() {
        this(new Hashtable());
    }
    
    public boolean execute(OperandStack os) {
        os.push(this);
        return true;
    }
    
    public String getType() {
        return "dicttype";
    }

    public int capacity() {
        return table.size();
    }
    
    public int size() {
        return table.size();
    }
    
    public void put(PSObject key, PSObject value) {
        table.put(key, value);
    }

    public void put(String key, PSObject value) {
        table.put(new PSName(key), value);
    }
    
    public void put(String key, String value) {
        table.put(new PSName(key), new PSString(value));
    }
    
    public void put(String key, int value) {
        table.put(new PSName(key), new PSInteger(value));
    }
    
    public void put(String key, double value) {
        table.put(new PSName(key), new PSReal(value));
    }
        
    public void put(String key, boolean value) {
        table.put(new PSName(key), new PSBoolean(value));
    }
        
    public void put(PSName key, String[] value) {
        PSArray array = new PSArray(value);
        array.setLiteral();
        put(key, array);
    }
        
    public Object[] keys() {
        return table.keySet().toArray();
    }
    
    public PSObject get(PSObject key) {
        return (PSObject)table.get(key);
    }

    public PSObject get(String key) {
        return (PSObject)table.get(new PSName(key));
    }
    
    public String getString(String key) {
        return ((PSString)table.get(new PSName(key))).getValue();
    }
        
    public PSNumber getNumber(String key) {
        return (PSNumber)table.get(new PSName(key));
    }
    
    public int getInteger(String key) {
        return ((PSInteger)table.get(new PSName(key))).getValue();
    }
    
    public double getReal(String key) {
        return ((PSReal)table.get(new PSName(key))).getValue();
    }
    
    public boolean getBoolean(String key) {
        return ((PSBoolean)table.get(new PSName(key))).getValue();
    }
    
    public PSPackedArray getPackedArray(String key) {
        return (PSPackedArray)table.get(new PSName(key));
    }
    
    public PSDictionary getDictionary(String key) {
        return (PSDictionary)table.get(new PSName(key));
    }
    
    public PSObject remove(PSObject key) {
        return (PSObject)table.remove(key);
    }
    
    public PSObject remove(String key) {
        return (PSObject)table.remove(new PSName(key));
    }
    
    public void copyInto(PSDictionary d) {
        for (Enumeration e = table.keys(); e.hasMoreElements(); ) {
            PSObject key = (PSObject)e.nextElement();
            PSObject value = (PSObject)table.get(key);
            d.put(key, value);
        }
    }
    
    public void forAll(PSArray proc, OperandStack os) {
        for (Enumeration e = table.keys(); e.hasMoreElements(); ) {
            PSObject key = (PSObject)e.nextElement();
            os.push(key);
            os.push((PSObject)table.get(key));
            proc.execute(os);
        }
    }

    public int hashCode() {
	    return table.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof PSDictionary) {
            return (table == ((PSDictionary)o).table);
        }
        return false;
    }

    public Object clone() {
        return new PSDictionary(table);
    }
    
    public PSObject copy() {
	Hashtable newTable = new Hashtable();
	Iterator i = table.keySet().iterator();
	while (i.hasNext()) {
	    Object key = i.next();
	    newTable.put(key, table.get(key));
	}
	return new PSDictionary(newTable);
	//throw new RuntimeException("Dictionaries cannot be copied.");
    }
    
    public String cvs() {
        return toString();
    }
    
    public String toString() {
        return "--"+((isExecutable()) ? "*" : "")+
               "dictionary ("+size()+")--";
    }
    
    public String dumpKeys() {
        StringBuffer out = new StringBuffer();
        for (Enumeration e=table.keys(); e.hasMoreElements(); ) {
            out.append(e.nextElement().toString());
            out.append(";");
        }
        return out.toString();        
    }
}
