// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PanelDevice.java 829a8d93169a 2006/12/08 09:03:07 duns $
 */
public class PanelDevice extends PSDevice {
    
    private final JPanel panel;
    private Graphics2D imageGraphics = null;

    public PanelDevice(JPanel panel) {
        this.panel = panel;
        
        // forward the resize
        panel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                fireComponentResizedEvent(event);
            }
        });
    }
    
    public double getWidth() {
        return panel.getWidth();
    }
    
    public double getHeight() {
        return panel.getHeight();
    }
      
    public AffineTransform getDeviceTransform() {
        return panel.getGraphicsConfiguration().getDefaultTransform();  
    }
        
    public Graphics getDeviceGraphics() {
        return panel.getGraphics();
    }

    public Graphics2D getGraphics() {
        if (imageGraphics != null) return imageGraphics;
        return super.getGraphics();
    }

    public void refresh() {
        panel.repaint();
    }
    
    public BufferedImage convertToImage(int width, int height) {
        BufferedImage image = (BufferedImage)panel.createImage(width, height);
        imageGraphics = (Graphics2D)image.getGraphics();
        return image;
    }
}
