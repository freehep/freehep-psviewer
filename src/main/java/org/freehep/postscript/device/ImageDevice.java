// FreeHEP 2009-2010
package org.freehep.postscript.device;

import org.freehep.postscript.Container;
import org.freehep.postscript.GraphicsContext;
import org.freehep.postscript.Image;
import org.freehep.postscript.Transform;
import org.freehep.postscript.types.PSDevice;

/**
 *
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public abstract class ImageDevice extends PSDevice {
	private Image image;
	protected Container container;
	private Transform deviceTranform;
	
	private ImageDevice() {
		deviceTranform = createTransform();		
	}
	
	public ImageDevice(int width, int height) {
		this();
		image = createImage(width, height,
				Image.TYPE_INT_ARGB);
	}	
	
	public ImageDevice(Container container, int width, int height) {
		this();
		this.container = container;
		image = container.createImage(width, height);
	}
	
	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#getDeviceGraphics()
	 */
	@Override
	public GraphicsContext getDeviceGraphics() {
		return image.getGraphics();
	}

	/* (non-Javadoc)
	 * @see org.freehep.postscript.PSDevice#getDeviceTransform()
	 */
	@Override
	public Transform getDeviceTransform() {
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
	public GraphicsContext getGraphics() {
		return image.getGraphics();
	}

//	public ImageDevice createImageDevice(int width, int height) {
//		return container == null ? new ImageDevice(width, height) : new ImageDevice(container, width, height);
//	}

	/**
	 * @return
	 */
	public Image getImage() {
		return image;
	}
}
