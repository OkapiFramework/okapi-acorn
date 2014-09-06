package net.sf.okapi.acorn.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.sf.okapi.acorn.taus.TransAPIClient;
import net.sf.okapi.acorn.xom.Factory;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

import com.mycorp.tmlib.Entry;
import com.mycorp.tmlib.SimpleTM;

public class DocumentsPanel extends JPanel {

	private final static long serialVersionUID = 1L;
	private final static IXLIFFFactory xf = Factory.XOM;
	
	private final SimpleTM tm;
	private final TransAPIClient ttapi;
	private final JTable table;
	private final DocumentTableModel tableModel;

	public DocumentsPanel (SimpleTM tm,
		TransAPIClient ttapi)
	{
		this.tm = tm;
		this.ttapi = ttapi;
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		final JButton btApplyTM = new JButton("Apply TM");
		btApplyTM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				doTMLeveraging();
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.25;
		c.gridx = 0; c.gridy = 0;
		add(btApplyTM, c);
		
		final JButton btApplyOC = new JButton("Apply Open-Calais");
		btApplyOC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				applyOpenCalais();
			}
		});
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.25;
		c.gridx = 1; c.gridy = 0;
		add(btApplyOC, c);
		
		final JButton btTausGetReq = new JButton("TAUS: Request Translations");
		btTausGetReq.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				doTAUSPostRequests();
			}
		});
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.25;
		c.gridx = 2; c.gridy = 0;
		add(btTausGetReq, c);
		
		final JButton btTausPostReq = new JButton("TAUS: Retrieve Translations");
		btTausPostReq.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				doTAUSRetrieveAndClean();
			}
		});
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.25;
		c.gridx = 3; c.gridy = 0;
		add(btTausPostReq, c);
		
		c = new GridBagConstraints();
		tableModel = new DocumentTableModel();
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table); 
		c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.BOTH;
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

	public void setDocument (IDocument doc) {
		tableModel.setDocument(doc);
	}

	private void doTMLeveraging () {
		IDocument doc = tableModel.getDocument();
		if ( doc == null ) return;
		for ( IFile file : doc ) {
			for ( IGroupOrUnit gou : file ) {
				if ( !gou.isUnit() ) continue;
				IUnit unit = (IUnit)gou;
				for ( ISegment seg : unit.getSegments() ) {
					if ( seg.hasTarget() ) continue;
					if ( seg.getSource().isEmpty() ) continue;
					Entry res = tm.search(seg.getSource());
					if ( res == null ) continue;
					seg.setTarget(xf.copyContent(seg.getStore(), true, res.getTarget()));
				}
			}
		}
		tableModel.refreshDisplay();
	}
	
	private void applyOpenCalais () {
		//TODO
		tableModel.refreshDisplay();
	}
	
//	private void applyTAAS () {
//		//TODO
//		tableModel.refreshDisplay();
//	}

	private void log (String text) {
		JOptionPane.showMessageDialog(null, text);
	}
	
	private void doTAUSPostRequests () {
		IDocument doc = tableModel.getDocument();
		if ( doc == null ) return;
		
		// First: post the translation requests for each segment
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
						log("Error "+ttapi.getResponseString());
						return;
					}
				}
			}
		}
	}
	
	private void doTAUSRetrieveAndClean () {
		IDocument doc = tableModel.getDocument();
		if ( doc == null ) return;
	
		// Then get back the translation
		for ( IFile file : doc ) {
			for ( IGroupOrUnit gou : file ) {
				if ( !gou.isUnit() ) continue;
				IUnit unit = (IUnit)gou;
				for ( ISegment seg : unit.getSegments() ) {
					if ( seg.hasTarget() ) continue;
					if ( seg.getSource().isEmpty() ) continue;
					if ( ttapi.translation_id_get(seg.getId()) >= 400 ) {
						log("Error "+ttapi.getResponseString());
						return;
					}
					// Else: parse the result back to the target content
					IContent target = ttapi.getTargetContent(ttapi.getResponseString());
					if ( target != null ) {
						seg.setTarget(Factory.XOM.copyContent(seg.getStore(), true, target));
					}
					// Remove the translation request from the server
					ttapi.translation_id_delete(seg.getId());
				}
			}
		}
		
		tableModel.refreshDisplay();
	}
	
}
