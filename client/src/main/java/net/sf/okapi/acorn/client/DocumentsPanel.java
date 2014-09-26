package net.sf.okapi.acorn.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.sf.okapi.acorn.calais.OpenCalais;
import net.sf.okapi.acorn.dbpedia.DBpediaSpotlight;
import net.sf.okapi.acorn.taas.TAAS;
import net.sf.okapi.acorn.taus.TransAPIClient;
import net.sf.okapi.acorn.yahoo.YahooAnalyzer;

import org.oasisopen.xliff.om.v1.IContent;
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

	public void applyTM () {
		main.clearLog();
		main.log("=== Applying TM");
		try {
			IDocument doc = tableModel.getDocument();
			if ( doc == null ) return;
			// Try to find TM matches
			for ( IFile file : doc ) {
				for ( IGroupOrUnit gou : file ) {
					if ( !gou.isUnit() ) continue;
					IUnit unit = (IUnit)gou;
					for ( ISegment seg : unit.getSegments() ) {
						if ( seg.hasTarget() ) continue;
						if ( seg.getSource().isEmpty() ) continue;
						Entry res = tm.search(seg.getSource());
						if ( res != null ) {
							seg.copyTarget(res.getTarget());
						}
					}
				}
			}
			tableModel.refreshDisplay();
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
			new OpenCalais().process(tableModel.getDocument());
			tableModel.refreshDisplay();
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
			new YahooAnalyzer().process(tableModel.getDocument());
			tableModel.refreshDisplay();
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
			new DBpediaSpotlight("iu").process(tableModel.getDocument());
			tableModel.refreshDisplay();
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
			new TAAS().process(tableModel.getDocument());
			tableModel.refreshDisplay();
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
			IDocument doc = tableModel.getDocument();
			if ( doc == null ) return;
			// Post the translation requests for each segment
			for ( IFile file : doc ) {
				for ( IGroupOrUnit gou : file ) {
					if ( !gou.isUnit() ) continue;
					IUnit unit = (IUnit)gou;
					for ( ISegment seg : unit.getSegments() ) {
						if ( seg.hasTarget() ) continue;
						if ( seg.getSource().isEmpty() ) continue;
						if ( ttapi.translation_post(seg.getId(),
							doc.getSourceLanguage(), doc.getTargetLanguage(),
							seg.getSource()) >= 400 )
						{
							main.log("Error "+ttapi.getResponseString());
							return;
						}
					}
				}
			}
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
			IDocument doc = tableModel.getDocument();
			if ( doc == null ) return;
			// Get back the translations
			for ( IFile file : doc ) {
				for ( IGroupOrUnit gou : file ) {
					if ( !gou.isUnit() ) continue;
					IUnit unit = (IUnit)gou;
					for ( ISegment seg : unit.getSegments() ) {
						if ( seg.hasTarget() ) continue;
						if ( seg.getSource().isEmpty() ) continue;
						if ( ttapi.translation_id_get(seg.getId()) >= 400 ) {
							main.log("Error "+ttapi.getResponseString());
							return;
						}
						// Else: parse the result back to the target content
						IContent target = ttapi.getTargetContent(ttapi.getResponseString());
						if ( target != null ) {
							seg.copyTarget(target);
						}
						// Remove the translation request from the server
						ttapi.translation_id_delete(seg.getId());
					}
				}
			}
			tableModel.refreshDisplay();
			main.log("Done");
			main.setTab(MainDialog.TAB_XOM);
		}
		catch ( Throwable e ) {
			main.log(e);
			main.setTab(MainDialog.TAB_LOG);
		}
	}
	
}
