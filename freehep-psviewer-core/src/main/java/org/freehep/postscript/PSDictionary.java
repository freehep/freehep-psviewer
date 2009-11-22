// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public class PSDictionary extends PSComposite {
	protected Map<Object, PSObject> table;

	protected PSDictionary(Map<Object, PSObject> t) {
		super("dictionary", true);
		table = t;
	}

	public PSDictionary(int n) {
		this(new HashMap<Object, PSObject>(n));
	}

	public PSDictionary() {
		this(new HashMap<Object, PSObject>());
	}

	@Override
	public boolean execute(OperandStack os) {
		os.push(this);
		return true;
	}

	@Override
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
		return table.get(key);
	}

	public PSObject get(String key) {
		return table.get(new PSName(key));
	}

	public String getString(String key) {
		return ((PSString) table.get(new PSName(key))).getValue();
	}

	public PSNumber getNumber(String key) {
		return (PSNumber) table.get(new PSName(key));
	}

	public int getInteger(String key) {
		return ((PSInteger) table.get(new PSName(key))).getValue();
	}

	public double getReal(String key) {
		return ((PSReal) table.get(new PSName(key))).getValue();
	}

	public boolean getBoolean(String key) {
		return ((PSBoolean) table.get(new PSName(key))).getValue();
	}

	public PSPackedArray getPackedArray(String key) {
		return (PSPackedArray) table.get(new PSName(key));
	}

	public PSDictionary getDictionary(String key) {
		return (PSDictionary) table.get(new PSName(key));
	}

	public PSObject remove(PSObject key) {
		return table.remove(key);
	}

	public PSObject remove(String key) {
		return table.remove(new PSName(key));
	}

	public void copyInto(PSDictionary d) {
		for (Iterator<Object> i = table.keySet().iterator(); i.hasNext();) {
			PSObject key = (PSObject) i.next();
			PSObject value = table.get(key);
			d.put(key, value);
		}
	}

	public void forAll(PSArray proc, OperandStack os) {
		for (Iterator<Object> i = table.keySet().iterator(); i.hasNext();) {
			PSObject key = (PSObject) i.next();
			os.push(key);
			os.push(table.get(key));
			proc.execute(os);
		}
	}

	@Override
	public int hashCode() {
		return table.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PSDictionary) {
			return (table == ((PSDictionary) o).table);
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PSDictionary(table);
	}

	@Override
	public PSObject copy() {
		Hashtable<Object, PSObject> newTable = new Hashtable<Object, PSObject>();
		Iterator<Object> i = table.keySet().iterator();
		while (i.hasNext()) {
			Object key = i.next();
			newTable.put(key, table.get(key));
		}
		return new PSDictionary(newTable);
		// throw new RuntimeException("Dictionaries cannot be copied.");
	}

	@Override
	public String cvs() {
		return toString();
	}

	@Override
	public String toString() {
		return "--" + ((isExecutable()) ? "*" : "") + "dictionary (" + size()
				+ ")--";
	}

	public String dumpKeys() {
		StringBuffer out = new StringBuffer();
		for (Iterator<Object> i = table.keySet().iterator(); i.hasNext();) {
			out.append(i.next().toString());
			out.append(";");
		}
		return out.toString();
	}
}
