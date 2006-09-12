// Copyright 2004, FreeHEP.
package org.freehep.postscript;

import java.awt.*;
import java.io.*;
import java.math.*;
import java.util.List;
import javax.swing.*;

import org.freehep.util.argv.*;
import org.freehep.graphics2d.BufferedPanel;

/**
 * PS Viewer
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSViewer.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSViewer {
        
    public static void main(String args[]) throws Exception {
        BooleanOption   help        = new BooleanOption(  "-help",      "-h",   "Show this help page", true);
        BooleanOption   version     = new BooleanOption(  "-version",   "-v",   "Show product version", true);
        BooleanOption   debug       = new BooleanOption(  "-debug",     "-d",   "Run with debugger");
        DoubleOption    scale       = new DoubleOption(   "-scale",     "-s",   "factor", 1.0,    "Scale output by factor");
        DoubleOption    tx          = new DoubleOption(   "-transX",    "-tx",  "dx",     0.0,    "Displace output in x by dx");
        DoubleOption    ty          = new DoubleOption(   "-transY",    "-ty",  "dy",     0.0,    "Displace output in y by dy");
        IntOption       page        = new IntOption(      "-page",      "-p",   "#",      1,      "Page to be displayed");
        StringParameter file        = new StringParameter("file", "PostScript File");
                    
        ArgumentParser parser = new ArgumentParser("org.freehep.postscript.PSViewer");
        parser.add(help);
        parser.add(version);
        parser.add(debug);
        parser.add(scale);
        parser.add(tx);
        parser.add(ty);
        parser.add(page);
        parser.add(file);
        
        try {
            List extra = parser.parse(args);
        
            if (!extra.isEmpty() || help.getValue()) {
                parser.printUsage(System.out);
                return;
            }
            
            if (version.getValue()) {
                System.err.println("$Id: src/main/java/org/freehep/postscript/PSViewer.java 17245790f2a9 2006/09/12 21:44:14 duns $");
                return;
            }
        } catch (MissingArgumentException mae) {
            System.out.println(mae.getMessage());
            return;
        } catch (ArgumentFormatException afe) {
            System.out.println(afe.getMessage());
            return;
        }
        
        String name = file.getValue();
                
        try {
            PSPanel panel = new PSPanel();
            panel.setBackground(Color.WHITE);    
            JFrame frame = new JFrame("PostScript Output");
            frame.getContentPane().add(panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 700);
            frame.setVisible(true);
            Processor processor = new Processor(panel);
            processor.setData(new PSInputFile(name));
            processor.setPageNo(page.getValue().intValue());
            processor.setScale(scale.getValue().doubleValue(), scale.getValue().doubleValue());
            processor.setTranslation(tx.getValue().doubleValue(), ty.getValue().doubleValue());
            if (debug.getValue()) {
                PSDebugger debugger = new PSDebugger();
                processor.attach(debugger);
                debugger.showInFrame();
                processor.reset();
            } else {
                processor.process();
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("File: '"+name+"' cannot be found.");
            System.exit(1);
        }
    }
}
