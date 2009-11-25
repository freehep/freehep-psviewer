/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.freehep.postscript;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * Prints out changes to the Transform
 * 
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class DebugTransformGraphics2D extends DelegateGraphics2D {	

	public DebugTransformGraphics2D(Graphics2D graphics) {
		super(graphics);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#create()
	 */
	@Override
	public Graphics create() {
		return new DebugTransformGraphics2D((Graphics2D)hostGraphics.create());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#create(int, int, int, int)
	 */
	@Override
	public Graphics create(int x, int y, int width, int height) {
		return new DebugTransformGraphics2D((Graphics2D)hostGraphics.create(x, y, width, height));
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#setTransform(java.awt.geom.AffineTransform)
	 */
	@Override
	public void setTransform(AffineTransform t) {
		super.setTransform(t);
		System.err.println("Setting Transform to: "+t);
	}
	
    /* (non-Javadoc)
     * @see org.freehep.postscript.DelegateGraphics2D#transform(java.awt.geom.AffineTransform)
     */
    @Override
    public void transform(AffineTransform t) {
    	super.transform(t);
		System.err.println("Transform to: "+t);
    }
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#scale(double, double)
	 */
	@Override
	public void scale(double sx, double sy) {
		super.scale(sx, sy);
		System.err.println("Scale to: "+getTransform());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#shear(double, double)
	 */
	@Override
	public void shear(double shx, double shy) {
		super.shear(shx, shy);
		System.err.println("Shear to: "+getTransform());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#translate(int, int)
	 */
	@Override
	public void translate(int x, int y) {
		super.translate(x, y);
		System.err.println("Translate to: "+getTransform());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#translate(double, double)
	 */
	@Override
	public void translate(double tx, double ty) {
		super.translate(tx, ty);
		System.err.println("Translate to: "+getTransform());		
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#setClip(int, int, int, int)
	 */
	@Override
	public void setClip(int x, int y, int width, int height) {
		super.setClip(x, y, width, height);
		System.err.println("Clipped to : "+getClip());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#setClip(java.awt.Shape)
	 */
	@Override
	public void setClip(Shape clip) {
		super.setClip(clip);
		System.err.println("Clipped to : "+getClip());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#clip(java.awt.Shape)
	 */
	@Override
	public void clip(Shape clip) {
		super.clip(clip);
		System.err.println("Clipped to : "+getClip());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#clipRect(int, int, int, int)
	 */
	@Override
	public void clipRect(int x, int y, int width, int height) {
		super.clipRect(x, y, width, height);
		System.err.println("Clipped to : "+getClip());		
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#rotate(double)
	 */
	@Override
	public void rotate(double theta) {
		super.rotate(theta);
		System.err.println("Rotate to: "+getTransform());
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.DelegateGraphics2D#rotate(double, double, double)
	 */
	@Override
	public void rotate(double theta, double x, double y) {
		super.rotate(theta, x, y);
		System.err.println("Rotate to: "+getTransform());
	}
}
