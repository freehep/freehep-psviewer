// Copyright 2001-2009, FreeHEP.
package org.freehep.postscript.types;

import java.io.IOException;

import org.freehep.postscript.errors.IOError;
import org.freehep.postscript.errors.NameNotFoundException;
import org.freehep.postscript.errors.SyntaxError;
import org.freehep.postscript.errors.SyntaxException;
import org.freehep.postscript.errors.Undefined;
import org.freehep.postscript.stacks.OperandStack;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 * 
 * @author Mark Donszelmann
 */
public final class Dispatcher {

	private Dispatcher() {
	}

	public static boolean dispatch(OperandStack os, PSTokenizable in) {
		try {
			PSObject obj = in.token(os.packingMode(), os.dictStack());
			if (obj == null) {
				return true;
			}

			// handle token, especially for procedures built by scanner
			if (obj instanceof PSPackedArray) {
				os.push(obj);
			} else {
				os.execStack().push(obj);
			}
			return false;
		} catch (IOException e) {
			PSObject.error(os, new IOError(e));
			return true;
		} catch (SyntaxException e) {
			PSArray errorInfo = new PSArray(2);
			errorInfo.set(0, new PSInteger(e.getLineNo()));
			errorInfo.set(1, new PSString(e.getMessage()));
			os.dictStack().dollarError().put("errorinfo", errorInfo);
			PSObject.error(os, new SyntaxError());
			return true;
		} catch (NameNotFoundException e) {
			PSObject.error(os, new Undefined());
			return true;
		}
	}

}
