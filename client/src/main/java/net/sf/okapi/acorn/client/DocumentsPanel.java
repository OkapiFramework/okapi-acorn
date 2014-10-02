package net.sf.okapi.acorn.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.sf.okapi.acorn.calais.OpenCalais;
import net.sf.okapi.acorn.common.MarkerCleaner;
import net.sf.okapi.acorn.common.Segmenter;
import net.sf.okapi.acorn.common.Util;
import net.sf.okapi.acorn.dbpedia.DBpediaSpotlight;
import net.sf.okapi.acorn.taas.TAAS;
import net.sf.okapi.acorn.taus.TAUSTransRequester;
import net.sf.okapi.acorn.taus.TAUSTransRetriever;
import net.sf.okapi.acorn.taus.TransAPIClient;
import net.sf.okapi.acorn.yahoo.YahooAnalyzer;

import org.oasisopen.xliff.om.v1.GetTarget;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

import com.mycorp.tmlib.Entry;
import com.mycorp.tmlib.SimpleTM;

public class DocumentsPanel extends JPanel {

	private final static long serialVersionUID = 1L;
	
	private final SimpleTM tm;
	private final TransAPIClient ttapi;
	private final JTextField edPath;
	private final JTable table;
	private final DocumentTableModel tableModel;
	private final MainDialog main;

	public DocumentsPanel (SimpleTM tm,
		TransAPIClient ttapi,
		MainDialog main)
	{
		this.tm = tm;
		this.ttapi = ttapi;
		this.main = main;
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		GridBagConstraints c = new GridBagConstraints();
		edPath = new JTextField();
		edPath.setEditable(false);
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
		add(edPath, c);
		
		c = new GridBagConstraints();
		tableModel = new DocumentTableModel();
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 0; c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0; c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		add(scrollPane, c);

		table.getColumn("ID").setPreferredWidth(170);
		table.getColumn("ID").setMaxWidth(300);
		
		final MLCellRenderer mlCellRenderer = new MLCellRenderer();
		table.getColumn("Source").setCellRenderer(mlCellRenderer);
		table.getColumn("Target").setCellRenderer(mlCellRenderer);
		tableModel.setDocument(null);
	}

	public void setDocument (IDocument doc,
		String path)
	{
		tableModel.setDocument(doc);
		edPath.setText(path);
	}

	public IDocument getDocument () {
		return tableModel.getDocument();
	}
	
	public String getPath () {
		return edPath.getText().trim();
	}

	public void leveragefromTM () {
		main.clearLog();
		main.log("=== Leverage from TM");
		try {
			SimpleTMLeveraging task = new SimpleTMLeveraging(tm);
			task.setDocument(tableModel.getDocument());
			main.runTask("Simple TM Leveraging", task);
			if ( task.getError() != null ) throw task.getError();
			tableModel.refreshDisplay(false);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void applySegmentation () {
		main.clearLog();
		main.log("=== Applying segmentation");
		try {
			Segmenter segmenter = new Segmenter();
			segmenter.process(tableModel.getDocument());
			tableModel.refreshDisplay(true);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void removeMarkers () {
		main.clearLog();
		main.log("=== Clearing markers");
		try {
			new MarkerCleaner().process(tableModel.getDocument());
			tableModel.refreshDisplay(false);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void applyOpenCalais () {
		main.clearLog();
		main.log("=== Applying Open-Calais Web services");
		try {
			OpenCalais task = new OpenCalais();
			task.setDocument(tableModel.getDocument());
			main.runTask("Open-Calais Web Service", task);
			if ( task.getError() != null ) throw task.getError();
			tableModel.refreshDisplay(false);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void applyYahooAnalyzer () {
		main.clearLog();
		main.log("=== Applying Yahoo Analyzer Web service");
		try {
			YahooAnalyzer task = new YahooAnalyzer();
			task.setDocument(tableModel.getDocument());
			main.runTask("Yahoo Analyzer Web Service", task);
			if ( task.getError() != null ) throw task.getError();
			tableModel.refreshDisplay(false);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void applyDBpediaSpotlight () {
		main.clearLog();
		main.log("=== Applying DBpedia Spotlight Web service");
		try {
			DBpediaSpotlight task = new DBpediaSpotlight("iu");
			task.setDocument(tableModel.getDocument());
			main.runTask("DBpedia Spotlight Web Service", task);
			if ( task.getError() != null ) throw task.getError();
			tableModel.refreshDisplay(false);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void applyTAAS () {
		main.clearLog();
		main.log("=== Applying TAAS Web service");
		try {
			TAAS task = new TAAS();
			task.setDocument(tableModel.getDocument());
			main.runTask("TAAS Web Service", task);
			if ( task.getError() != null ) throw task.getError();
			tableModel.refreshDisplay(false);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void doTAUSPostRequests () {
		main.clearLog();
		main.log("=== Posting translation requests to TAUS server");
		try {
			TAUSTransRequester task = new TAUSTransRequester("iu", ttapi);
			task.setDocument(tableModel.getDocument());
			main.runTask("TAUS Translation API - Requester", task);
			if ( task.getError() != null ) throw task.getError();
			main.log("Done");
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
	public void doTAUSRetrieveAndClean () {
		main.clearLog();
		main.log("=== Retrieving translation requests from TAUS server");
		try {
			TAUSTransRetriever task = new TAUSTransRetriever(ttapi);
			task.setDocument(tableModel.getDocument());
			main.runTask("TAUS Translation API - Retriever", task);
			if ( task.getError() != null ) throw task.getError();
			tableModel.refreshDisplay(false);
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
}
