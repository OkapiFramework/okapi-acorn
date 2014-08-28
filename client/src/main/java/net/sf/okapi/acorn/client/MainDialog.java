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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;

import net.sf.okapi.acorn.calais.OpenCalais;
import net.sf.okapi.lib.xliff2.processor.XLIFFProcessor;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

import com.mycorp.tmlib.SimpleTM;

public class MainDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField edPath;
	private JButton btAddFile;
	private JButton btReqTrans;
	private JTextArea edLog;
	private JTextPane edInput;
	private Vector inputFiles;
	private SimpleTM tm;
	private TMPanel tmPanel;

	public MainDialog () {
		tm = new SimpleTM();
		initComponents();
		showInfo();
		edLog.requestFocusInWindow();
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

		final String tabStart = "<html><body><table width='150'>";
		final String tabEnd = "</table></body></html>";
		
		//=== Menu
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Process an XLIFF 2 Document with Open-Calais...", KeyEvent.VK_P);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				applyOpenCalais();
			}
		});
		
		menuItem = new JMenuItem("Import an XLIFF 2 Document...", KeyEvent.VK_I);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				importXLIFF();
			}
		});
		
		menuItem = new JMenuItem("Import a DOCX Document...", KeyEvent.VK_D);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				importDocument();
			}
		});
		
		//=== Panels
		
		JTabbedPane tabPane = new JTabbedPane();

		// Add the Log tab
		edLog = new JTextArea();
		JScrollPane jsp = new JScrollPane(edLog);
		edLog.setEditable(true);
		//edLog.setContentType("text/html");
		//edLog.setBorder(BorderFactory.createLineBorder(Color.gray));
		Font font = new Font("Gadugi", 0, 20); // Gadugi, Euphemia //new Font("Courier New", 0, 20);
		edLog.setFont(font);
		//edLog.setMargin(new Insets(15, 15, 15, 15));
		tabPane.addTab(tabStart+"Log Console"+tabEnd, jsp);
		
		// Add the TM panel
		tmPanel = new TMPanel(tm);
		tabPane.addTab(tabStart+"TM Console"+tabEnd, tmPanel);
		
		// Add the TAUS API test panel
		APITestPanel atPanel = new APITestPanel();
		tabPane.addTab(tabStart+"TAUS API Test"+tabEnd, atPanel);
		
//		// Add the Input Files tab
//		JPanel panel = new JPanel(new BorderLayout());
//		// List of files
//		DefaultListModel<String> model = new DefaultListModel<String>();
//	    JList listbox = new JList(model);
//	    JScrollPane pane = new JScrollPane(listbox);
//		// Add the tab
//		tabPane.addTab("Input Files", panel);
		
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
		 setPreferredSize(new Dimension(950, 700));
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

	private void applyOpenCalais () {
		// Get an XLIFF 2.0 file
		File inputFile;
		File outputFile;
    	try {
    		JFileChooser fc = new JFileChooser();
    		fc.setDialogTitle("Select an XLIFF 2 Document");
    		int option = fc.showOpenDialog(this);
    		if ( option == JFileChooser.APPROVE_OPTION ) {
   				inputFile = fc.getSelectedFile();
   				// Set the output
   				StringBuilder path = new StringBuilder(inputFile.getAbsolutePath());
   				int p = path.lastIndexOf(".");
   				if ( p == -1 ) path.append(".out");
   				else path.insert(p, ".out");
   				outputFile = new File(path.toString());
    		}
    		else {
    			return;
    		}
    	}
    	catch ( Throwable e ) {
    		e.printStackTrace();
    		return;
    	}
		
		// Process it
		XLIFFProcessor proc = new XLIFFProcessor();
		proc.add(new OpenCalais());
		proc.run(inputFile, outputFile);
		
	}

	private void importDocument () {
		// Get a document
		File inputFile;
    	try {
    		JFileChooser fc = new JFileChooser();
    		fc.setDialogTitle("Select a DOCX Document");
    		int option = fc.showOpenDialog(this);
    		if ( option == JFileChooser.APPROVE_OPTION ) {
   				inputFile = fc.getSelectedFile();
    		}
    		else {
    			return;
    		}
    	}
    	catch ( Throwable e ) {
    		e.printStackTrace();
    		return;
    	}
		
		// Process it
    	clearLog();
    	log("===== Importing DOCX Document");
    	IntoXLIFF intoXlf = new IntoXLIFF();
    	IDocument doc = intoXlf.importDocument(inputFile, "okf_openxml");
		log("Done\n");
    	
		addDocumentToTM(doc);
	}
	
	private void importXLIFF () {
		// Get an XLIFF 2.0 file
		File inputFile;
    	try {
    		JFileChooser fc = new JFileChooser();
    		fc.setDialogTitle("Select an XLIFF 2 Document");
    		int option = fc.showOpenDialog(this);
    		if ( option == JFileChooser.APPROVE_OPTION ) {
   				inputFile = fc.getSelectedFile();
    		}
    		else {
    			return;
    		}
    	}
    	catch ( Throwable e ) {
    		e.printStackTrace();
    		return;
    	}
		
		// Process it
    	clearLog();
    	log("===== Importing XLIFF Document");
		XLIFFImport imp = new XLIFFImport();
		IDocument doc = imp.importDocument(inputFile);
		log("Done\n");
		
		addDocumentToTM(doc);
	}
	
	private void addDocumentToTM (IDocument doc) {
    	int count = 0;
    	log("===== Importing document entries into the TM");
		for ( IFile file : doc ) {
			log("== File id="+file.getId()+":");
			for ( IGroupOrUnit gou : file ) {
				if ( !gou.isUnit() ) continue;
				IUnit unit = (IUnit)gou;
				log("-- Unit id="+unit.getId()+":");
				for ( ISegment segment : unit.getSegments() ) {
					log("src: "+segment.getSource().getCodedText());
					if ( segment.hasTarget() ) {
						log("trg: "+segment.getTarget().getCodedText());
					}
				}
				count += tm.addSegments(unit);
			}
		}
		log("Entries added: "+count);
		tmPanel.updateEntries();
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
		edLog.setText(edLog.getText()+text+"\n");
	}

	private void clearLog () {
		edLog.setText("");
	}
	
	private void showInfo () {
		clearLog();
		log("---------------------------------------------------------------");
		log("Okapi Acorn - Experimental client for various purposes");
		log("---------------------------------------------------------------");
		int mb = 1024*1024;
        Runtime runtime = Runtime.getRuntime();
        String fmt = "%,1d MB";
        log("Total Memory = " + String.format(fmt, runtime.totalMemory()/mb));
        log("Maximum Memory = " + String.format(fmt, runtime.maxMemory()/mb));		
        log("Used Memory = " + String.format(fmt, (runtime.totalMemory()-runtime.freeMemory())/mb));
        log("Free Memory = " + String.format(fmt, runtime.freeMemory()/mb));
		log("---------------------------------------------------------------");
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
