package org.freehep.vectorgraphics.awt;

import java.awt.PaintContext;

import org.freehep.vectorgraphics.ColorModel;
import org.freehep.vectorgraphics.Rectangle;
import org.freehep.vectorgraphics.RenderingHints;
import org.freehep.vectorgraphics.Transform;

public class AWTPaintContext implements org.freehep.vectorgraphics.PaintContext {

	PaintContext pc;
	
	public AWTPaintContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle userBounds, Transform at, RenderingHints hints) {
	}

}
