// Copyright 2004, FreeHEP.
package org.freehep.postscript;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/*
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/VirtualDevice.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class VirtualDevice extends PSDevice {

    private Graphics2D imageGraphics = null;
    private Graphics2D graphics;
    private Dimension dimension;
    private AffineTransform device = new AffineTransform();

    public VirtualDevice(Graphics2D graphics, Dimension dimension) {
        this.graphics = graphics;
        this.dimension = dimension;
        fireComponentResizedEvent(new ComponentEvent(null, ComponentEvent.COMPONENT_RESIZED));
    }

    public double getWidth() {
        return dimension.width;
    }
    
    public double getHeight() {
        return dimension.height;
    }

    public BufferedImage convertToImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        imageGraphics = (Graphics2D)image.getGraphics();
        return image;
    }

    public Graphics2D getGraphics() {
        if (imageGraphics != null) return imageGraphics;
        return super.getGraphics();
    }

    public AffineTransform getDeviceTransform() {
        return device;
    }

    public Graphics getDeviceGraphics() {
        return graphics;
    }

    public void refresh() {
        // ignored
    }
}
