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

package org.freehep.postscript.device;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.freehep.postscript.types.PSDevice;

/**
 *
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class ImageDevice extends PSDevice {
	private BufferedImage image;
	private Container container;
	private AffineTransform deviceTranform = new AffineTransform();
	
	public ImageDevice(int width, int height) {
		image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
	}
	
	public ImageDevice(Container container, int width, int height) {
		this.container = container;
		image = (BufferedImage) container.createImage(width, height);
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#getDeviceGraphics()
	 */
	@Override
	public Graphics getDeviceGraphics() {
		return image.getGraphics();
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#getDeviceTransform()
	 */
	@Override
	public AffineTransform getDeviceTransform() {
		return deviceTranform;
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#getHeight()
	 */
	@Override
	public double getHeight() {
		return image.getHeight();
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#getWidth()
	 */
	@Override
	public double getWidth() {
		return image.getWidth();
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#refresh()
	 */
	@Override
	public void refresh() {
		// ignored
	}

	@Override
	public Graphics2D getGraphics() {
		return (Graphics2D)image.getGraphics();
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#createImageDevice(int, int)
	 */
	@Override
	public ImageDevice createImageDevice(int width, int height) {
		return container == null ? new ImageDevice(width, height) : new ImageDevice(container, width, height);
	}

	/**
	 * @return
	 */
	public BufferedImage getImage() {
		return image;
	}
}
