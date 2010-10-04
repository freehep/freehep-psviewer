// Copyright FreeHEP, 2009
package org.freehep.postscript.operators;

import org.freehep.postscript.stacks.OperandStack;

/**
 *
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class ShowPage extends OutputOperator {

	@Override
	public boolean execute(OperandStack os) {
		Object showPage = os.execStack().pop();
		os.execStack().push(new InitGraphics());
		os.execStack().push(new ErasePage());
		os.execStack().push(showPage);
		return true;
	}
}
