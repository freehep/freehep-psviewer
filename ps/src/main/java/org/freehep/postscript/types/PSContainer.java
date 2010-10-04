// Copyright FreeHEP 2009-2010
package org.freehep.postscript.types;

import org.freehep.vectorgraphics.GraphicsContext;

/**
 *
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public interface PSContainer {

	/**
	 * @param refreshListener
	 */
// TODO
	//	void addRefreshListener(RefreshListener refreshListener);

	GraphicsContext getGraphicsContext();
}
