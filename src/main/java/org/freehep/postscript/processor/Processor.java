// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.processor;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JApplet;
import javax.swing.JPanel;

import org.freehep.postscript.device.PanelDevice;
import org.freehep.postscript.device.VirtualDevice;
import org.freehep.postscript.dsc.DSC;
import org.freehep.postscript.errors.BreakException;
import org.freehep.postscript.errors.PostScriptError;
import org.freehep.postscript.operators.ShowPage;
import org.freehep.postscript.stacks.DictionaryStack;
import org.freehep.postscript.stacks.ExecutableStack;
import org.freehep.postscript.stacks.GStateStack;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSDataSource;
import org.freehep.postscript.types.PSDebugger;
import org.freehep.postscript.types.PSDevice;
import org.freehep.postscript.types.PSFile;
import org.freehep.postscript.types.PSGState;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.viewer.RefreshListener;

/**
 * PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class Processor implements DebuggerListener {
	private Logger log = Logger.getLogger("org.freehep.postscript");
	
	private PSDevice device;
	private boolean secure;

	private PSObject data;
	private DSC dsc = null;
	private long pageNo = 1;
	private double sx = 1.0;
	private double sy = 1.0;
	private double tx = 0.0;
	private double ty = 0.0;
	private PSDebugger debugger;

	private DictionaryStack dictStack;
	private ExecutableStack execStack;
	private OperandStack operandStack;
	private GStateStack gstateStack;
	private long currentPageNo;

	public Processor(JPanel panel) {
		this(new PanelDevice(panel), false);
	}
	
	public Processor(Applet applet) {
		this(new PanelDevice(applet), false);
	}

	public Processor(JApplet applet) {
		this(new PanelDevice(applet), false);
	}

	public Processor(PSDevice device, boolean secure) {
		this.device = device;
		device.addComponentRefreshListener(new RefreshListener() {
			public void componentRefreshed() {
				try {
					if (debugger == null) {
						process();
					}
				} catch (IOException e) {
					log.log(Level.SEVERE, e.getMessage(), e.getStackTrace());
				}
			}
		});
		this.secure = secure;
		this.currentPageNo = 0;
	}

	public Processor(Graphics2D graphics, Dimension dimension, boolean secure) {
		this.device = new VirtualDevice(graphics, dimension);
		this.secure = secure;
		this.currentPageNo = 0;
	}

	public Processor(Graphics2D graphics, Dimension dimension) {
		this(graphics, dimension, false);
	}

	public void reset() throws IOException {
		if (device.getGraphics() == null) {
			return;
		}
		if (data == null) {
			return;
		}
		
		dictStack = new DictionaryStack();
		PSGState gstate = new PSGState(device, dictStack);
		execStack = new ExecutableStack();
		operandStack = new OperandStack(this, gstate, secure);
		gstateStack = new GStateStack();

		if (data instanceof PSFile) {
			((PSFile) data).reset();

		}
		execStack.push(data);
		currentPageNo = 0;

		if (debugger != null) {
			debugger.update(dictStack, execStack, operandStack);
		}
	}

	public void setData(PSObject data) throws IOException {
		this.data = data;
		if (data instanceof PSFile) {
			((PSFile) data).reset();
		}
		if (data instanceof PSDataSource) {
			if (dsc != null) {
				dsc.removeDSCEventListener(device);
			}
			dsc = ((PSDataSource) data).getDSC();
			if (dsc != null) {
				dsc.addDSCEventListener(device);
			}
		}
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	public void setScale(double sx, double sy) {
		this.sx = sx;
		this.sy = sy;
		device.setTransform(new AffineTransform(sx, 0, 0, sy, tx, ty));
	}

	public void setTranslation(double tx, double ty) {
		this.tx = tx;
		this.ty = ty;
		device.setTransform(new AffineTransform(sx, 0, 0, sy, tx, ty));
	}

	public DictionaryStack dictStack() {
		return dictStack;
	}

	public ExecutableStack execStack() {
		return execStack;
	}

	public OperandStack operandStack() {
		return operandStack;
	}

	public GStateStack gstateStack() {
		return gstateStack;
	}

	public DSC getDSC() {
		return dsc;
	}

	public boolean step() {
		return step(true);
	}

	public boolean step(boolean update) {
		if (execStack == null || execStack.isEmpty()) {
			return false;
		}

		PSObject obj = execStack.peekObject();
		try {
			boolean pop = obj.checkAndExecute(operandStack);
			if (pop) {
				if (execStack.pop() instanceof ShowPage) {
					currentPageNo++;
					if (currentPageNo == pageNo) {
						return false;
					}
				}
			}
		} catch (PostScriptError e) {
			// error is on the stack and will be executed
		}

		if (update && (debugger != null)) {
			debugger.update(dictStack, execStack, operandStack);
		}

		return true;
	}

	public int go() {
		int i = 0;
		try {
			boolean go;
			do {
				try {
					go = step(false);
					i++;
				} catch (BreakException e) {
					if (debugger != null) {
						log.info("BreakPoint Found...");
						go = false;
					} else {
						log.info("BreakPoint found but ignored, run with -debug to break.");
						go = true;
					}
				}
			} while (go);
		} catch (ClassCastException cce) {
			log.severe("PS processing stopped due to ClassCastException after "+i+" steps");
			log.log(Level.SEVERE, cce.getMessage(), cce.getStackTrace());
		}

		if (debugger != null) {
			debugger.update(dictStack, execStack, operandStack);
			// device.refresh();
		}
		return i;
	}

	public void process() throws IOException {
		reset();
		log.fine("Processing...");
		int steps = go();
		log.fine("Processing finished "+steps+" steps.");
	}

	public void attach(PSDebugger debugger) {
		if (this.debugger != null) {
			this.debugger.removeDebuggerListener(this);
		}
		this.debugger = debugger;
		debugger.addDebuggerListener(this);
	}
}
