// Copyright FreeHEP 2009
package org.freehep.postscript;

import java.awt.Graphics;

/**
 *
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public interface PSContainer {

	/**
	 * @param refreshListener
	 */
	void addRefreshListener(RefreshListener refreshListener);

	Graphics getOffscreenGraphics();
}
