// Copyright 2004-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import org.freehep.util.argv.ArgumentFormatException;
import org.freehep.util.argv.ArgumentParser;
import org.freehep.util.argv.BooleanOption;
import org.freehep.util.argv.DoubleOption;
import org.freehep.util.argv.IntOption;
import org.freehep.util.argv.MissingArgumentException;
import org.freehep.util.argv.StringParameter;

/**
 * PS Viewer
 * 
 * @author Mark Donszelmann
 */
public final class PSViewer {

	public PSViewer(Processor processor, String name, int pageNo, double sx, double sy, double tx, double ty, boolean debug) throws IOException {
		processor.setData(new PSInputFile(name));
		processor.setPageNo(pageNo);
		processor.setScale(sx, sy);
		processor.setTranslation(tx, ty);
		if (debug) {
			PSDebugger debugger = new PSDebugger();
			processor.attach(debugger);
			debugger.showInFrame();
			processor.reset();
		} else {
			processor.process();
		}
	}

	public static void main(String args[]) throws Exception {
		BooleanOption help = new BooleanOption("-help", "-h",
				"Show this help page", true);
		BooleanOption version = new BooleanOption("-version", "-v",
				"Show product version", true);
		BooleanOption debug = new BooleanOption("-debug", "-d",
				"Run with debugger");
		DoubleOption scale = new DoubleOption("-scale", "-s", "factor", 1.0,
				"Scale output by factor");
		DoubleOption tx = new DoubleOption("-transX", "-tx", "dx", 0.0,
				"Displace output in x by dx");
		DoubleOption ty = new DoubleOption("-transY", "-ty", "dy", 0.0,
				"Displace output in y by dy");
		IntOption page = new IntOption("-page", "-p", "#", 1,
				"Page to be displayed");
		StringParameter file = new StringParameter("file", "PostScript File");

		ArgumentParser parser = new ArgumentParser(
				"org.freehep.postscript.PSViewer");
		parser.add(help);
		parser.add(version);
		parser.add(debug);
		parser.add(scale);
		parser.add(tx);
		parser.add(ty);
		parser.add(page);
		parser.add(file);

		try {
			List<?> extra = parser.parse(args);

			if (!extra.isEmpty() || help.getValue()) {
				parser.printUsage(System.out);
				return;
			}

			if (version.getValue()) {
				System.err
						.println("$Id: src/main/java/org/freehep/postscript/PSViewer.java 829a8d93169a 2006/12/08 09:03:07 duns $");
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
			new PSViewer(processor, name, page.getValue().intValue(), scale
					.getValue().doubleValue(), scale.getValue().doubleValue(), tx.getValue().doubleValue(), ty.getValue()
					.doubleValue(), debug.getValue());
			panel.repaint();
		} catch (FileNotFoundException fnfe) {
			System.err.println("File: '" + name + "' cannot be found.");
			System.exit(1);
		}
	}
}
