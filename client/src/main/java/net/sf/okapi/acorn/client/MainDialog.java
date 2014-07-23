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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;

public class MainDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField edPath;
	private JButton btAddFile;
	private JButton btReqTrans;
	private JTextPane edLog;
	private JTextPane edInput;
	private Vector inputFiles;

	public MainDialog () {
		initComponents();
	}

	class DocumentTransferhandler extends TransferHandler {

		private static final long serialVersionUID = 1L;

		@Override
		public boolean canImport (TransferHandler.TransferSupport support) {
			if ( !support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ) {
				return false;
			}
			boolean copySupported = (COPY & support.getSourceDropActions()) == COPY;
			if ( !copySupported ) {
				return false;
			}
			support.setDropAction(COPY);
			return true;
		}

		@Override
		public boolean importData (TransferHandler.TransferSupport support) {
			if ( !canImport(support) ) {
				return false;
			}
			Transferable t = support.getTransferable();
			try {
				@SuppressWarnings("unchecked")
				java.util.List<File> l = (java.util.List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
				edPath.setText(l.get(0).getAbsolutePath());
			}
			catch (Throwable e) {
				return false;
			}
			return true;
		}
	}

	private void initComponents () {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Okapi Acorn - Client");

		JTabbedPane tabPane = new JTabbedPane();

		// Add the API test panel
		APITestPanel atPanel = new APITestPanel();
		// Add the tab
		tabPane.addTab("TAUS API Test", atPanel);
		
		// Add the Input Files tab
		JPanel panel = new JPanel(new BorderLayout());
		// List of files
		DefaultListModel<String> model = new DefaultListModel<String>();
	    JList listbox = new JList(model);
	    JScrollPane pane = new JScrollPane(listbox);
		// Add the tab
//		tabPane.addTab("Input Files", panel);
		
		// Add the Log tab
		edLog = new JTextPane();
		edLog.setEditable(false);
		edLog.setBorder(BorderFactory.createLineBorder(Color.gray));
//		tabPane.addTab("Log", edLog);
		
		add(tabPane);
		
		// setLayout(new GridBagLayout());
		//
		// JLabel lbPath = new
		// JLabel("Enter, select or drag & drop the XLIFF document to process");
		// GridBagConstraints c = new GridBagConstraints();
		// c.anchor = GridBagConstraints.FIRST_LINE_START;
		// c.gridx = 0; c.gridwidth = 2;
		// c.gridy = 0;
		// c.insets = new Insets(5, 5, 5, 5);
		// add(lbPath, c);
		//
		// edPath = new JTextField();
		// edPath.requestFocusInWindow();
		// c = new GridBagConstraints();
		// c.anchor = GridBagConstraints.LINE_START;
		// c.gridx = 0;
		// c.gridy = 1;
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.weightx = 1.0;
		// c.insets = new Insets(0, 5, 5, 5);
		// add(edPath, c);
		// pack();
		//
		// btGetPath = new JButton();
		// btGetPath.setText("...");
		// c = new GridBagConstraints();
		// c.anchor = GridBagConstraints.LINE_END;
		// c.gridx = 1;
		// c.gridy = 1;
		// c.insets = new Insets(0, 0, 5, 5);
		// //btGetPath.setPreferredSize(new Dimension(35, edPath.getHeight()));
		// add(btGetPath, c);
		//
		// btValidate = new JButton();
		// btValidate.setText("Validate");
		// c = new GridBagConstraints();
		// c.anchor = GridBagConstraints.LINE_START;
		// c.gridx = 0;
		// c.gridy = 2;
		// c.insets = new Insets(0, 5, 5, 5);
		// add(btValidate, c);
		//
		// // Set the actions
		// btGetPath.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent event) {
		// selectDocument();
		// }
		// });
		// btValidate.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent event) {
		// //validateDocument();
		// }
		// });
		//
		//
		//
		// lbPath = new
		// JLabel("For additional functions use Lynx in command-line mode (-h option to see all commands)");
		// c = new GridBagConstraints();
		// c.anchor = GridBagConstraints.LAST_LINE_START;
		// c.gridx = 0; c.gridwidth = 2;
		// c.gridy = 4;
		// c.insets = new Insets(0, 5, 5, 5);
		// add(lbPath, c);
		//
		 // Set minimum and preferred size for the dialog
		 setMinimumSize(new Dimension(550, 250));
		 setPreferredSize(new Dimension(850, 550));
		 pack();
		//
		// // Set the drag and drop handlers
		// final DocumentTransferhandler dth = new DocumentTransferhandler();
		// setTransferHandler(dth);
		// edLog.setTransferHandler(dth);

		// Center the dialog
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getSize().width) / 2,
			(dim.height - getSize().height) / 2);
	}

	private void selectDocument () {
		try {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Select a Document to Load");
			int option = fc.showOpenDialog(this);
			if ( option == JFileChooser.APPROVE_OPTION ) {
				edPath.setText(fc.getSelectedFile().getAbsolutePath());
			}
		}
		catch (Throwable e) {
			log(e.getLocalizedMessage());
		}
	}

	private void log (String text) {
		edLog.setText(edLog.getText() + text + "\n");
	}

	public static void start () {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run () {
				new MainDialog().setVisible(true);
			}
		});
	}
}
