// Copyright FreeHEP 2009.
package org.freehep.postscript.errors;

import java.io.IOException;

import org.freehep.postscript.operators.ErrorOperator;
import org.freehep.postscript.stacks.OperandStack;

/**
 *
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class IOError extends ErrorOperator {
	private IOException e;

	public IOError() {
		this.e = null;
	}

	public IOError(IOException e) {
		this.e = e;
	}

	@Override
	public boolean execute(OperandStack os) {
		log.warning(e.getMessage());
		return super.execute(os);
	}
}
