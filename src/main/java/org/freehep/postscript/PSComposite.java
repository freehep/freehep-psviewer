// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public abstract class PSComposite extends PSObject {
	public final static int UNLIMITED = 3;
	public final static int READ_ONLY = 2;
	public final static int EXECUTE_ONLY = 1;
	public final static int NONE = 0;

	private int access;

	public PSComposite(String name, boolean literal, int access) {
		super(name, literal);
		this.access = access;
	}
	
	public PSComposite(String name, boolean literal) {
		this(name, literal, UNLIMITED);
	}

	public boolean accessWrite() {
		return access >= UNLIMITED;
	}

	public boolean accessRead() {
		return access >= READ_ONLY;
	}

	public boolean accessExecute() {
		return access >= EXECUTE_ONLY;
	}

	public boolean changeAccess(int newAccess) {
		if (newAccess > access) {
			return false;
		}
		access = newAccess;
		return true;
	}

}
