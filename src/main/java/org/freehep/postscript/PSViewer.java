// Copyright 2004-2009, FreeHEP.
package org.freehep.postscript;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

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

	public PSViewer(Processor processor, String name, int pageNo, double sx,
			double sy, double tx, double ty, int buffer, boolean debug) throws IOException {
		this(processor, new PSInputFile(name, buffer), pageNo, sx, sy, tx, ty, debug);
	}

	public PSViewer(Processor processor, URL url, int pageNo, double sx,
			double sy, double tx, double ty, int buffer, boolean debug) throws IOException {
		this(processor, new PSInputFile(url.toExternalForm(), buffer), pageNo, sx, sy,
				tx, ty, debug);
	}

	public PSViewer(Processor processor, PSInputFile data, int pageNo,
			double sx, double sy, double tx, double ty, boolean debug)
			throws IOException {
		processor.setData(data);
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
		IntOption buffer = new IntOption("-buffer", "-b", 0x8000,
				"Buffer size to use for document");
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
		parser.add(buffer);
		parser.add(file);

		try {
			List<?> extra = parser.parse(args);

			if (!extra.isEmpty() || help.getValue()) {
				parser.printUsage(System.out);
				return;
			}

			if (version.getValue()) {
				System.err.println("FreeHEP PSViewer "+getVersion());
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
					.getValue().doubleValue(), scale.getValue().doubleValue(),
					tx.getValue().doubleValue(), ty.getValue().doubleValue(), buffer.getValue().intValue(),
					debug.getValue());
			panel.repaint();
		} catch (FileNotFoundException fnfe) {
			System.err.println("File: '" + name + "' cannot be found.");
			System.exit(1);
		}
	}

	/**
	 * @return
	 * @throws IOException 
	 */
	public static String getVersion() throws IOException {
		String version = "";
		InputStream is = PSViewer.class.getResourceAsStream("/META-INF/maven/org.freehep/freehep-psviewer/pom.properties");
		if (is != null) {
			Properties pom = new Properties();
			pom.load(is);
			is.close();
			version = pom.getProperty("version", "");
		}
		return version;
	}
}
