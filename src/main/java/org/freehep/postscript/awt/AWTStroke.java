package org.freehep.postscript.awt;

import java.awt.Stroke;

import org.freehep.postscript.Shape;

public class AWTStroke implements org.freehep.postscript.Stroke {

	private Stroke s;
	
	public AWTStroke(Stroke stroke) {
		s = stroke;
	}

	public java.awt.Stroke getStroke() {
		return s;
	}

	@Override
	public Shape createStrokedShape(Shape path) {
		// TODO Auto-generated method stub
		return null;
	}
}
