// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;

/**
 * PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/Processor.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class Processor implements DebuggerListener {
        
    private PSDevice device;
    private boolean secure;
    
    private PSObject data;
    private DSC dsc = null;
    private long pageNo = 1;
    private double sx = 1.0;
    private double sy = 1.0;
    private double tx = 0.0;
    private double ty = 0.0;
    private boolean debug = false;
    private PSDebugger debugger;
    
    private DictionaryStack dictStack;
    private ExecutableStack execStack;
    private OperandStack operandStack;
    private GStateStack gstateStack;
    private ActionListener listener;
    private long currentPageNo;
    
    public Processor(PSPanel panel) {
        this(new PanelDevice(panel), false);
    }

    public Processor(PSDevice device, boolean secure) {
        this.device = device;
        device.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                try {
                    if (debugger == null) process();
                } catch (IOException e) {
                    e.printStackTrace();
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
        PSGState gstate = new PSGState(device);
        dictStack = new DictionaryStack();
        execStack = new ExecutableStack();
        operandStack = new OperandStack(this, gstate, secure);
        gstateStack = new GStateStack();

        if (data instanceof PSFile) {
            ((PSFile)data).reset();
        }
        execStack.push(data);
        currentPageNo = 0;

        if (debugger != null) debugger.update(dictStack, execStack, operandStack);
    }
    
    public void setData(PSObject data) {
        setData(data, Integer.MAX_VALUE);
    }
    
    public void setData(PSObject data, int bufferLimit) {
        this.data = data;        
        if (data instanceof PSFile) ((PSFile)data).mark(bufferLimit);
        if (data instanceof PSDataSource) {
            if (dsc != null) dsc.removeDSCEventListener(device);
            dsc = ((PSDataSource)data).getDSC();
            if (dsc != null) dsc.addDSCEventListener(device);
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
        if (execStack.isEmpty()) {
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
        
        if (update && (debugger != null)) debugger.update(dictStack, execStack, operandStack);

        return true;
    }

    public void go() {
        try {
            boolean go;
            do {
                try {
                    go = step(false);
                } catch (BreakException e) {
                    if (debugger != null) {
                        System.out.println("BreakPoint Found...");
                        go = false;
                    } else {
                        System.out.println("BreakPoint found but ignored, run with -debug to break.");
                        go = true;
                    }
                }
            } while (go);
        } catch (ClassCastException cce) {
            System.out.println("PS processing stopped due to ClassCastException");
            cce.printStackTrace();
        }

        if (debugger != null) debugger.update(dictStack, execStack, operandStack);
        device.refresh();
    }
        
    public void process() throws IOException {
        reset();
        go();
    }        

    public void attach(PSDebugger debugger) {
        if (this.debugger != null) {
            this.debugger.removeDebuggerListener(this);
        }    
        this.debugger = debugger;
        debugger.addDebuggerListener(this);
    }
}
