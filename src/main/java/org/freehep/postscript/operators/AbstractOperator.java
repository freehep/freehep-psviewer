// Copyright FreeHEP 2009
package org.freehep.postscript.operators;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSName;
import org.freehep.postscript.types.PSOperator;

/**
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public abstract class AbstractOperator extends PSOperator {
	private static Logger slog = Logger.getLogger("org.freehep.postscript");

	public static void register(PSDictionary dict, Class<?>[] clazz) {
		for (int j = 0; j < clazz.length; j++) {
			try {
				PSOperator op = (PSOperator) clazz[j].newInstance();
				PSName key = new PSName(op.getName());
				if (dict.get(key) == null) {
					dict.put(key, op);
					op.setName(key.cvs());
				} else {
					slog.severe("Duplicate operator '" + key + "'");
					System.exit(1);
				}

			} catch (ClassCastException e) {
				slog.log(Level.WARNING, "Error: " + clazz
						+ " does not inherit from PSOperator.\n", e);
			} catch (IllegalAccessException e) {
				slog.log(Level.WARNING, "Error: " + clazz
						+ " cannot be instantiated.\n", e);
			} catch (InstantiationException e) {
				slog.log(Level.WARNING, "Error: " + clazz
						+ " cannot be instantiated.\n", e);
			}
		}
	}
}
