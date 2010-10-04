// Copyright 2004-2010, FreeHEP.
package org.freehep.postscript.dsc;

import java.util.EventObject;

/**
 * Class for signalling DSC comments
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/DSCEvent.java 829a8d93169a
 *          2006/12/08 09:03:07 duns $
 */
public class DSCEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public enum State {
		PARSED, UNPARSED, ERROR
	};

	private String comment;
	private Object args;
	private State state;

	public DSCEvent(Object src, String comment, Object args, State state) {
		super(src);
		this.comment = comment;
		this.args = args;
		this.state = state;
	}

	public final String getComment() {
		return comment;
	}

	public final Object getArgs() {
		return args;
	}

	public final State getState() {
		return state;
	}

	@Override
	public final String toString() {
		return "DSCEvent [" + comment + ", " + args + ", " + state + "]";
	}
}
