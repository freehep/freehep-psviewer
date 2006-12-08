// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Debugger for PostScript Processor
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSDebugger.java 829a8d93169a 2006/12/08 09:03:07 duns $
 */
public class PSDebugger extends JPanel {
    private static final int NCOLS=3;
    private static final int NROWS=10;
    
    private List listeners = new ArrayList();
    private JButton b[][] = new JButton[NCOLS][NROWS];
    private JPanel stackPanel;
    private JPanel buttonPanel;
    
    public PSDebugger() {
        setLayout(new BorderLayout(10,10));
        
        stackPanel = new JPanel();
        stackPanel.setLayout(new GridLayout(NROWS,NCOLS));
        
        for (int row = 0; row < NROWS; row++) {
            for (int col = 0; col < NCOLS; col++) {
                b[col][row] = new JButton(col+":"+row);
                stackPanel.add(b[col][row]);
            }
        }
        
        b[0][0].setText("********************************");
        b[0][0].setEnabled(false);
        b[1][0].setText("********************************");
        b[1][0].setEnabled(false);
        b[2][0].setText("********************************");
        b[2][0].setEnabled(false);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        
        JButton step = new JButton("Step");
        buttonPanel.add(step);
        step.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                for (Iterator i=listeners.iterator(); i.hasNext(); ) {
                    try {
                        ((DebuggerListener)i.next()).step();                        
                    } catch (BreakException be) {
                        // ignored, we are stepping
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }  
                }
            }
        });
       
        JButton go = new JButton("Go");
        buttonPanel.add(go);
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                for (Iterator i=listeners.iterator(); i.hasNext(); ) {
                    try {
                        ((DebuggerListener)i.next()).go(); 
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }                        
                }
            }
        });

        add(stackPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
   
    public void addDebuggerListener(DebuggerListener listener) {
        listeners.add(listener);
    }
    
    public void removeDebuggerListener(DebuggerListener listener) {
        listeners.remove(listener);
    }
    
    public void showInFrame() {
        JFrame frame = new JFrame("FreeHEP PostScript Debugger");
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(DictionaryStack dictStack, ExecutableStack execStack, OperandStack operandStack) {
        update(dictStack, 0);    
        update(execStack, 1);    
        update(operandStack, 2);    
    }
    
    public void update(PostScriptStack ps, int col) {
        b[col][0].setText(ps.toString()+"("+ps.size()+")"+"     ");
        for (int i=1; i<NROWS; i++) {
            if (i-1 < ps.size()) {
                b[col][i].setText(ps.elementAt(ps.size()-i).toString());
            } else {
                b[col][i].setText("--");
            }
        }
    }
}
