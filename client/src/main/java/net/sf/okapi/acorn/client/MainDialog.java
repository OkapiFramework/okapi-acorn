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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.okapi.acorn.common.FilterBasedReader;
import net.sf.okapi.acorn.common.IDocumentReader;
import net.sf.okapi.acorn.common.Segmenter;
import net.sf.okapi.acorn.common.XLIFF2Reader;
import net.sf.okapi.acorn.common.XLIFFWriter;
import net.sf.okapi.acorn.taus.TransAPIClient;

import org.oasisopen.xliff.om.v1.IDocument;

import com.mycorp.tmlib.SimpleTM;

public class MainDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final int TAB_LOG = 0;
	public static final int TAB_XOM = 1;
	public static final int TAB_TM = 2;
	public static final int TAB_TAUS = 3;
	
	private JTextArea edLog;
	private SimpleTM tm;
	private TransAPIClient ttapi;
	private JTabbedPane tabPane;
	private TMPanel tmPanel;
	private DocumentsPanel docPanel;
	private JMenuItem miSaveAsXLF;
	private JMenuItem miApplyTM;
	private JMenuItem miApplyDBpedia;
	private JMenuItem miApplyYahoo;
	private JMenuItem miApplyOpenCalais;
	private JMenuItem miApplyTAAS;
	private JMenuItem miPostTAUSRequests;
	private JMenuItem miRetrieveTAUSRequests;

	public MainDialog () {
		tm = new SimpleTM();
		ttapi = new TransAPIClient(null);
		initComponents();
		showInfo();
		edLog.requestFocusInWindow();
	}

	private void initComponents () {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Okapi Acorn - Client");

		final String tabStart = "<html><body><table width='160'>";
		final String tabEnd = "</table></body></html>";
		
		//=== Menu
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("Object Model");
		menu.setMnemonic(KeyEvent.VK_O);
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Import File into XLIFF Object Model...", KeyEvent.VK_I);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				importFile(false);
			}
		});
		
		miSaveAsXLF = new JMenuItem("Save As XLIFF File...", KeyEvent.VK_S);
		miSaveAsXLF.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menu.add(miSaveAsXLF);
		miSaveAsXLF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				saveAsXLIFF();
			}
		});
		
		menu.addSeparator();
		
		miApplyTM = new JMenuItem("Apply Translation Memory", KeyEvent.VK_T);
		menu.add(miApplyTM);
		miApplyTM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				docPanel.applyTM();
			}
		});
		
		menu.addSeparator();
		
		miApplyOpenCalais = new JMenuItem("Apply Open-Calais Web Service", KeyEvent.VK_O);
		menu.add(miApplyOpenCalais);
		miApplyOpenCalais.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				docPanel.applyOpenCalais();
			}
		});
		
		miApplyYahoo = new JMenuItem("Apply Yahoo Analyzer Web Service", KeyEvent.VK_Y);
		menu.add(miApplyYahoo);
		miApplyYahoo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				docPanel.applyYahooAnalyzer();
			}
		});
		
		miApplyDBpedia = new JMenuItem("Apply DBpedia Spotlight Web Service", KeyEvent.VK_D);
		menu.add(miApplyDBpedia);
		miApplyDBpedia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				docPanel.applyDBpediaSpotlight();
			}
		});
		
		miApplyTAAS = new JMenuItem("Apply TAAS Web Service", KeyEvent.VK_T);
		menu.add(miApplyTAAS);
		miApplyTAAS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				docPanel.applyTAAS();
			}
		});
		
		menu.addSeparator();
		
		miPostTAUSRequests = new JMenuItem("Send Translation Requests to TAUS Server", KeyEvent.VK_P);
		menu.add(miPostTAUSRequests);
		miPostTAUSRequests.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				docPanel.doTAUSPostRequests();
			}
		});
		
		miRetrieveTAUSRequests = new JMenuItem("Retrieve Translation Requests from TAUS Server", KeyEvent.VK_P);
		menu.add(miRetrieveTAUSRequests);
		miRetrieveTAUSRequests.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				docPanel.doTAUSRetrieveAndClean();
			}
		});
		
		menu = new JMenu("Translation Memory");
		menu.setMnemonic(KeyEvent.VK_T);
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Import from a File...", KeyEvent.VK_I);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				importFile(true);
			}
		});
		
		//=== Panels
		
		tabPane = new JTabbedPane();

		// Add the Log tab
		edLog = new JTextArea();
		JScrollPane jsp = new JScrollPane(edLog);
		edLog.setLineWrap(true);
		edLog.setWrapStyleWord(true);
		edLog.setFont(new Font("Gadugi", 0, 20));
		//edLog.setMargin(new Insets(15, 15, 15, 15));
		tabPane.addTab(tabStart+"Log"+tabEnd, jsp);
		
		// Add the Documents panel
		docPanel = new DocumentsPanel(tm, ttapi, this);
		tabPane.addTab(tabStart+"XLIFF Object Model"+tabEnd, docPanel);
		
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
		updateMenu();

		// Center the dialog
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getSize().width) / 2,
			(dim.height - getSize().height) / 2);
	}

	private void saveAsXLIFF () {
		IDocument doc = docPanel.getDocument();
		if ( doc == null ) return;
    	try {
    		File outputFile;
    		JFileChooser fc = new JFileChooser();
    		FileNameExtensionFilter ff;
   			fc.setDialogTitle("Save As XLIFF");
        	fc.addChoosableFileFilter(new FileNameExtensionFilter("XLIFF 2.0 Files", "xlf"));
        	ff = new FileNameExtensionFilter("All Supported Files", "xlf");
    		
    		// In both case:
    		fc.addChoosableFileFilter(ff);
    		fc.setFileFilter(ff);
    		int option = fc.showSaveDialog(this);
    		if ( option == JFileChooser.APPROVE_OPTION ) {
   				outputFile = fc.getSelectedFile();
    		}
    		else {
    			return;
    		}

        	clearLog();
    		XLIFFWriter.saveAs(doc, outputFile);
    		log("Done");
    	}
    	catch ( Throwable e ) {
    		log(e);
    	}
	}
	
	private void importFile (boolean toFeedIntoTheTM) {
		// Get an input file
		File inputFile;
    	try {
    		JFileChooser fc = new JFileChooser();
    		FileNameExtensionFilter ff;
    		if ( toFeedIntoTheTM ) {
    			fc.setDialogTitle("Import into the TM");
        		ff = new FileNameExtensionFilter("All Supported Files", "xlf", "tmx");
    		}
    		else {
    			fc.setDialogTitle("Import into the XLIFF Object Model");
        		fc.addChoosableFileFilter(new FileNameExtensionFilter("DOCX Files", "docx"));
        		fc.addChoosableFileFilter(new FileNameExtensionFilter("HTML Files", "html"));
        		ff = new FileNameExtensionFilter("All Supported Files", "xlf", "docx", "html", "tmx");
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

    		clearLog();
    		// Import into TM if it is the option selected
    		if ( toFeedIntoTheTM ) {
    			log("===== Import file into the TM");
    			int count = tm.importSegments(inputFile);
    			log("Entries added: "+count);
    			tmPanel.updateEntries();
        		log("Done");
    			setTab(TAB_TM);
    			return;
    		}
    	
    		// Or: load the document
    		log("===== Import file into XLIFF Object Model");
    	
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
	    	case "html":
	    		reader = new FilterBasedReader("okf_html");
	    		break;
	    	default:
				log("Unsupported or unknown file format.");
				setTab(TAB_LOG);
	    		return; // Not supported
	    	}
    	
    		IDocument doc = reader.load(inputFile);
    		new Segmenter().process(doc);
    		docPanel.setDocument(doc, path);
    		log("Done");
			setTab(TAB_XOM);
    	}
    	catch ( Throwable e ) {
    		log(e);
			setTab(TAB_LOG);
   	}
    	finally {
    		updateMenu();
    	}
	}
	
	public void log (String text) {
		edLog.setText(edLog.getText()+text+"\n");
	}
	
	public void log (Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw); 
		e.printStackTrace(pw);
		log(sw.toString());
	}

	public void clearLog () {
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
	
	private void updateMenu () {
		boolean enabled = (docPanel.getDocument() != null);
		miSaveAsXLF.setEnabled(enabled);
		miApplyDBpedia.setEnabled(enabled);
		miApplyTM.setEnabled(enabled);
		miApplyYahoo.setEnabled(enabled);
		miApplyOpenCalais.setEnabled(enabled);
		miPostTAUSRequests.setEnabled(enabled);
		miRetrieveTAUSRequests.setEnabled(enabled);
		miApplyTAAS.setEnabled(enabled);
	}

	public void setTab (int tabIndex) {
		tabPane.setSelectedIndex(tabIndex);
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
