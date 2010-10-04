package org.freehep.vectorgraphics.awt;

import java.awt.GraphicsConfiguration;

import org.freehep.vectorgraphics.Transform;

public class AWTGraphicsConfiguration implements org.freehep.vectorgraphics.GraphicsConfiguration {

	private GraphicsConfiguration gc;

	public AWTGraphicsConfiguration(
			GraphicsConfiguration gc) {
		this.gc = gc;
	}

	@Override
	public Transform getDefaultTransform() {
		return new AWTTransform(gc.getDefaultTransform());
	}

}
