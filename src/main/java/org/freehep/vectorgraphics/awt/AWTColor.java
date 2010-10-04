package org.freehep.vectorgraphics.awt;

import java.awt.Color;

public class AWTColor implements org.freehep.vectorgraphics.Color {

	private static final long serialVersionUID = 1L;
	private Color c;
	
	public AWTColor(float r, float g, float b) {
		c = new Color(r,g,b);
	}

	public AWTColor(Color c) {
		this.c = c;
	}

	@Override
	public float[] getColorComponents(float[] array) {
		return c.getColorComponents(array);
	}

	public java.awt.Color getColor() {
		return c;
	}

	@Override
	public int getRGB() {
		return c.getRGB();
	}
}
