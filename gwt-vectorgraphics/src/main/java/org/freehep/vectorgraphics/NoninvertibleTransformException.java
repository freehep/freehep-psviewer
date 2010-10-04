package org.freehep.vectorgraphics;

public class NoninvertibleTransformException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoninvertibleTransformException(String message, Throwable cause) {
		super(message, cause);
	}
}
