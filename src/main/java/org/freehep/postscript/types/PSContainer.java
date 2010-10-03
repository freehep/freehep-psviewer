// Copyright FreeHEP 2009-2010
package org.freehep.postscript.types;

import org.freehep.postscript.GraphicsContext;
import org.freehep.postscript.viewer.RefreshListener;

/**
 *
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public interface PSContainer {

	/**
	 * @param refreshListener
	 */
	void addRefreshListener(RefreshListener refreshListener);

	GraphicsContext getGraphicsContext();
}
