package org.freehep.vectorgraphics.awt;

import java.awt.Stroke;
import java.awt.BasicStroke;

import org.freehep.vectorgraphics.Shape;

public class AWTStroke implements org.freehep.vectorgraphics.Stroke {

	private Stroke s;
	
	public AWTStroke(Stroke stroke) {
		s = stroke;
	}

	public AWTStroke(float lineWidth, int cap, int join, float miterLimit,
			float[] dash, float dashPhase) {
		s = new BasicStroke(lineWidth, cap, join, miterLimit, dash, dashPhase);
	}

	public Stroke getStroke() {
		return s;
	}

	@Override
	public Shape createStrokedShape(Shape p) {
		return new AWTShape(s.createStrokedShape(((AWTShape)p).getShape()));
	}
}
