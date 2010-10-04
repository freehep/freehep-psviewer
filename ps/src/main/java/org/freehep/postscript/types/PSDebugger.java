// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.types;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.freehep.postscript.errors.BreakException;
import org.freehep.postscript.processor.DebuggerListener;
import org.freehep.postscript.stacks.DictionaryStack;
import org.freehep.postscript.stacks.ExecutableStack;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.stacks.PostScriptStack;

/**
 * Debugger for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public class PSDebugger extends JPanel {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger("org.freehep.postscript");
	private static final int NCOLS = 3;
	private static final int NROWS = 10;

	private List<DebuggerListener> listeners = new ArrayList<DebuggerListener>();
	private JButton b[][] = new JButton[NCOLS][NROWS];
	private JPanel stackPanel;
	private JPanel buttonPanel;

	public PSDebugger() {
		setLayout(new BorderLayout(10, 10));

		stackPanel = new JPanel();
		stackPanel.setLayout(new GridLayout(NROWS, NCOLS));

		for (int row = 0; row < NROWS; row++) {
			for (int col = 0; col < NCOLS; col++) {
				b[col][row] = new JButton(col + ":" + row);
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
		buttonPanel.setLayout(new GridLayout(1, 2));

		JButton step = new JButton("Step");
		buttonPanel.add(step);
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (Iterator<DebuggerListener> i = listeners.iterator(); i
						.hasNext();) {
					try {
						i.next().step();
					} catch (BreakException be) {
						// ignored, we are stepping
					} catch (IOException ioe) {
						log.log(Level.SEVERE, ioe.getMessage(), ioe.getStackTrace());
					}
				}
			}
		});

		JButton go = new JButton("Go");
		buttonPanel.add(go);
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (Iterator<DebuggerListener> i = listeners.iterator(); i
						.hasNext();) {
					try {
						i.next().go();
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
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public void update(DictionaryStack dictStack, ExecutableStack execStack,
			OperandStack operandStack) {
		update(dictStack, 0);
		update(execStack, 1);
		update(operandStack, 2);
	}

	public void update(PostScriptStack ps, int col) {
		b[col][0].setText(ps.toString() + "(" + ps.size() + ")" + "     ");
		for (int i = 1; i < NROWS; i++) {
			if (i - 1 < ps.size()) {
				b[col][i].setText(ps.elementAt(ps.size() - i).toString());
			} else {
				b[col][i].setText("--");
			}
		}
	}
}
