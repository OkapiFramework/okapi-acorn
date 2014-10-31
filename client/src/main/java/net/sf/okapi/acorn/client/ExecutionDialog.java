/*===========================================================================
  Copyright (C) 2014 by the Okapi Framework contributors
-----------------------------------------------------------------------------
  This library is free software; you can redistribute it and/or modify it 
  under the terms of the GNU Lesser General Public License as published by 
  the Free Software Foundation; either version 2.1 of the License, or (at 
  your option) any later version.

  This library is distributed in the hope that it will be useful, but 
  WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
  General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License 
  along with this library; if not, write to the Free Software Foundation, 
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  See also the full LGPL text here: http://www.gnu.org/copyleft/lesser.html
===========================================================================*/

package net.sf.okapi.acorn.client;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class ExecutionDialog extends JDialog implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	private final JEditorPane edNote;
	private final JButton btStart;
	private final JButton btStop;
	private final JButton btInfo;
	private final JProgressBar pbProgress;
	private final JTextField edText;

	private String infoLink;
	private XLIFFDocumentTask task;
	private boolean done = false;
	private boolean wasCanceled = false;

	public ExecutionDialog (JFrame owner,
		boolean modal)
	{
		super(owner, modal);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		JPanel panel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		pbProgress = new JProgressBar(0, 100);
		pbProgress.setStringPainted(true);
		c.gridx = 0; c.gridy = 0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(pbProgress, c);

		c = new GridBagConstraints();
		edText = new JTextField();
		edText.setEditable(false);
		c.gridx = 0; c.gridy = 1;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(edText, c);

		c = new GridBagConstraints();
		btStart = new JButton("Start");
		btStart.setActionCommand("start");
		btStart.addActionListener(this);
		c.gridx = 0; c.gridy = 2;
		c.weightx = 0.33;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btStart, c);

		c = new GridBagConstraints();
		btStop = new JButton("Cancel");
		btStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				stopProcess();
			}
		});
		c.gridx = 1; c.gridy = 2;
		c.weightx = 0.33;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btStop, c);

		c = new GridBagConstraints();
		btInfo = new JButton("Info...");
		btInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				showInfo();
			}
		});
		c.gridx = 2; c.gridy = 2;
		c.weightx = 0.33;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btInfo, c);

		c = new GridBagConstraints();
		edNote = new JEditorPane();
		edNote.setContentType("text/html");
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 0; c.gridy = 3;
		c.weightx = 1.0; c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		panel.add(edNote, c);

		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		add(panel, BorderLayout.CENTER);

		resetUI();

		pack();
		setMinimumSize(getPreferredSize());
		setPreferredSize(new Dimension(700, 500));
		pack();
		
		setLocationRelativeTo(owner);
	}

	private void resetUI () {
		done = false;
		wasCanceled = false;
		btStart.setText("Start");
		btStop.setEnabled(false);
		pbProgress.setValue(0);
		edText.setText("Click Start to start the process");
		btInfo.setEnabled(infoLink!=null);
	}

	public void setTask (XLIFFDocumentTask task,
		String title)
	{
		setTitle(title);
		edNote.setText(task.getInfo());
		this.task = task;
		this.infoLink = task.getInfoLink();
		resetUI();
	}
	
	@Override
	public void propertyChange (PropertyChangeEvent event) {
		switch ( event.getPropertyName() ) {
		case "progress":
			if ( wasCanceled ) return;
			int progress = (Integer)event.getNewValue();
			pbProgress.setValue(progress);
			edText.setText(String.format("Completed %d%% of task...", task.getProgress()));
			break;
		case "state":
			if ( "DONE".equals(event.getNewValue().toString()) ) {
				done();
			}
			break;
		}
	}

	@Override
	public void actionPerformed (ActionEvent event) {
		if ( done ) { // Close
			setVisible(false);
		}
		else { // Start
			btStart.setEnabled(false);
			btStop.setEnabled(true);
			task.addPropertyChangeListener(this);
			task.execute();
		}
	}

	private void done () {
		done = true;
		btStart.setText("Close");
		btStart.setEnabled(true);
		btStop.setEnabled(false);
		if ( wasCanceled ) {
			edText.setText("The tasks was was canceled.");
		}
		else {
			pbProgress.setValue(pbProgress.getMaximum());
			if ( task.getError() != null ) edText.setText("Error(s) occurred: See the log.");
			else edText.setText("Done");
		}
	}

	private void stopProcess () {
		wasCanceled = true;
		task.cancel(true);
	}
	
	private void showInfo () {
		if ( infoLink == null ) return;
		try {
			if ( Desktop.isDesktopSupported() ) {
				Desktop.getDesktop().browse(new URI(infoLink));
			}
		}
		catch ( Throwable e ) {
		}
	}
}
