// Copyright 2004, FreeHEP.

import java.awt.Dimension;
import java.io.File;

import org.freehep.graphicsio.swf.SWFGraphics2D;
import org.freehep.postscript.Processor;
import org.freehep.postscript.PSInputFile;

public class PStoSWF {

    public static void main(String[] args) throws Exception{
        if (args.length != 2) {
            System.out.println("Usage: PStoSWF file.ps file.swf");
            System.exit(1);
        }
        
        // Open output file and associate to graphics context
        File out = new File(args[1]);
        Dimension dimension = new Dimension(800, 600);
        SWFGraphics2D graphics = new SWFGraphics2D(out, dimension);
        
        // Open input file
        PSInputFile in = new PSInputFile(args[0]);
        
        // Create processor and associate to input and output file
        Processor processor = new Processor(graphics, dimension);
        processor.setData(in);
        
        // Process
        graphics.startExport();
        processor.process();
        graphics.endExport();
    }
}
