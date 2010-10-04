// Copyright 2004-2009, FreeHEP.
package org.freehep.psviewer.test;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.freehep.postscript.processor.Processor;
import org.freehep.postscript.types.PSInputFile;
import org.freehep.postscript.viewer.PSPanel;
import org.freehep.vectorgraphics.awt.AWTPanelDevice;

/**
 * @author Mark Donszelmann (Mark.Donszelmann@gmail.com)
 */
public class EmbeddedPSViewer {

    public static void main(String[] args) throws Exception{
        if (args.length != 2) {
            System.out.println("Usage: EmbeddedPSViewer file1.ps file2.ps");
            System.exit(1);
        }
                
        // Create Panels
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        PSPanel ps1 = new PSPanel();
        panel.add(ps1);
        PSPanel ps2 = new PSPanel();
        panel.add(ps2);
        
        // Show Panel
        JFrame frame = new JFrame("Embedded PSViewer");
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 800);
        frame.setVisible(true);
        
        // Create processors and associate to panels and input files
        Processor processor1 = new Processor(new AWTPanelDevice(ps1), false);
        processor1.setData(new PSInputFile(args[0]));
        Processor processor2 = new Processor(new AWTPanelDevice(ps2), false);
        processor2.setData(new PSInputFile(args[1]));
        
        // Process
        processor1.process();
        processor2.process();
    }
}
