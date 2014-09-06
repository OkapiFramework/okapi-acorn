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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.okapi.acorn.calais.OpenCalais;
import net.sf.okapi.acorn.common.FilterBasedReader;
import net.sf.okapi.acorn.common.IDocumentReader;
import net.sf.okapi.acorn.common.XLIFF2Reader;
import net.sf.okapi.acorn.taus.TransAPIClient;
import net.sf.okapi.lib.xliff2.processor.XLIFFProcessor;

import org.oasisopen.xliff.om.v1.IDocument;

import com.mycorp.tmlib.SimpleTM;

public class MainDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField edPath;
	private JTextArea edLog;
	private SimpleTM tm;
	private TransAPIClient ttapi;
	private TMPanel tmPanel;
	private DocumentsPanel docPanel;

	public MainDialog () {
		tm = new SimpleTM();
		ttapi = new TransAPIClient(null);
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

		final String tabStart = "<html><body><table width='160'>";
		final String tabEnd = "</table></body></html>";
		
		//=== Menu
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("Document");
		menu.setMnemonic(KeyEvent.VK_D);
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Process an XLIFF 2 Document with Open-Calais...", KeyEvent.VK_P);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				applyOpenCalais();
			}
		});
		
		menuItem = new JMenuItem("Load a Document...", KeyEvent.VK_L);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				loadDocument(false);
			}
		});
		
		menu = new JMenu("Translation Memory");
		menu.setMnemonic(KeyEvent.VK_T);
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Import from a Document...", KeyEvent.VK_I);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				loadDocument(true);
			}
		});
		
		//=== Panels
		
		JTabbedPane tabPane = new JTabbedPane();

		// Add the Log tab
		edLog = new JTextArea();
		JScrollPane jsp = new JScrollPane(edLog);
		edLog.setLineWrap(true);
		edLog.setWrapStyleWord(true);
		edLog.setFont(new Font("Gadugi", 0, 20));
		//edLog.setMargin(new Insets(15, 15, 15, 15));
		tabPane.addTab(tabStart+"Log"+tabEnd, jsp);
		
		// Add the Documents panel
		docPanel = new DocumentsPanel(tm, ttapi);
		tabPane.addTab(tabStart+"Document"+tabEnd, docPanel);
		
		// Add the TM panel
		tmPanel = new TMPanel(tm);
		tabPane.addTab(tabStart+"TM Console"+tabEnd, tmPanel);
		
		// Add the TAUS API test panel
		TausAPIPanel atPanel = new TausAPIPanel(ttapi);
		tabPane.addTab(tabStart+"TAUS API Console"+tabEnd, atPanel);
		
		add(tabPane);
		
		// Set minimum and preferred size for the dialog
		setMinimumSize(new Dimension(550, 250));
		setPreferredSize(new Dimension(950, 700));
		pack();

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
    		log(e);
    		return;
    	}
		
		// Process it
		XLIFFProcessor proc = new XLIFFProcessor();
		proc.add(new OpenCalais());
		proc.run(inputFile, outputFile);
	}

	private void loadDocument (boolean toFeedIntoTheTM) {
		// Get an input file
		File inputFile;
    	try {
    		JFileChooser fc = new JFileChooser();
    		FileNameExtensionFilter ff;
    		if ( toFeedIntoTheTM ) {
    			fc.setDialogTitle("Select the Document to Import into the TM");
        		ff = new FileNameExtensionFilter("All Supported Files", "xlf", "tmx");
    		}
    		else {
    			fc.setDialogTitle("Select the Document to Load");
        		fc.addChoosableFileFilter(new FileNameExtensionFilter("DOCX Files", "docx"));
        		ff = new FileNameExtensionFilter("All Supported Files", "xlf", "docx", "tmx");
    		}
    		// In both case:
    		fc.addChoosableFileFilter(new FileNameExtensionFilter("XLIFF 2.0 Files", "xlf"));
    		fc.addChoosableFileFilter(new FileNameExtensionFilter("TMX Files", "tmx"));
    		fc.addChoosableFileFilter(ff);
    		fc.setFileFilter(ff);
    		int option = fc.showOpenDialog(this);
    		if ( option == JFileChooser.APPROVE_OPTION ) {
   				inputFile = fc.getSelectedFile();
    		}
    		else {
    			return;
    		}
    	}
    	catch ( Throwable e ) {
    		log(e);
    		return;
    	}

    	clearLog();

    	try {
    		// Import into TM if it is the option selected
    		if ( toFeedIntoTheTM ) {
    			log("===== Import document entries into the TM");
    			int count = tm.importSegments(inputFile);
    			log("Entries added: "+count);
    			tmPanel.updateEntries();
        		log("Done");
    			return;
    		}
    	
    		// Or: load the document
    		log("===== Load document");
    	
    		// Get the extension
    		String path = inputFile.getPath();
    		int p = path.lastIndexOf('.');
    		String ext = "";
    		if ( p > -1 ) ext = path.substring(p+1).toLowerCase();
    	
    		// Instantiate the proper reader based on the type of document
    		IDocumentReader reader;
	    	switch ( ext ) {
	    	case "xlf":
	    		reader = new XLIFF2Reader();
	    		break;
	    	case "docx":
	    		reader = new FilterBasedReader("okf_openxml");
	    		break;
	    	case "tmx":
	    		reader = new FilterBasedReader("okf_tmx");
	    		break;
	    	default:
				JOptionPane.showMessageDialog(null, "Unsupported or unknown file format.");
	    		return; // Not supported
	    	}
    	
    		IDocument doc = reader.load(inputFile);
    		docPanel.setDocument(doc);
    		log("Done");
    	}
    	catch ( Throwable e ) {
    		log(e);
    	}
	}
	
	private void log (String text) {
		edLog.setText(edLog.getText()+text+"\n");
	}
	
	private void log (Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw); 
		e.printStackTrace(pw);
		log(sw.toString());
	}

	private void clearLog () {
		edLog.setText("");
	}
	
	private void showInfo () {
		clearLog();
		log("-------------------------------------------------------------------------------");
		log("Okapi Acorn - Experimental client for various purposes");
		log("https://code.google.com/p/okapi-acorn");
		log("-------------------------------------------------------------------------------");
		Runtime rt = Runtime.getRuntime();
		rt.runFinalization();
		rt.gc();
        log("Java version: " + System.getProperty("java.version"));
        log(String.format("Platform: %s, %s, %s",
			System.getProperty("os.name"), //$NON-NLS-1$ 
			System.getProperty("os.arch"), //$NON-NLS-1$
			System.getProperty("os.version")));
		NumberFormat nf = NumberFormat.getInstance();
        log(String.format("Java VM memory: free=%s KB, total=%s KB",
			nf.format(rt.freeMemory()/1024),
			nf.format(rt.totalMemory()/1024)));
		log("-------------------------------------------------------------------------------");
	
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
