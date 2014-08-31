package net.sf.okapi.acorn.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.sf.okapi.acorn.xom.Factory;

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
	private final JTable table;
	private final DocumentTableModel tableModel;

	public DocumentsPanel (SimpleTM tm) {
		this.tm = tm;
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
		c.weightx = 0.5;
		c.gridx = 0; c.gridy = 0;
		add(btApplyTM, c);
		
		final JButton btApplyTaus = new JButton("Apply TAUS Translate");
		btApplyTaus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				doTAUSTranslate();
			}
		});
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1; c.gridy = 0;
		add(btApplyTaus, c);
		
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
					Entry res = tm.search(seg.getSource());
					if ( res == null ) continue;
					seg.setTarget(xf.copyContent(seg.getStore(), true, res.getTarget()));
				}
			}
		}
		tableModel.refreshDisplay();
	}
	
	private void doTAUSTranslate () {
		IDocument doc = tableModel.getDocument();
		if ( doc == null ) return;
		for ( IFile file : doc ) {
			for ( IGroupOrUnit gou : file ) {
				if ( !gou.isUnit() ) continue;
				IUnit unit = (IUnit)gou;
				for ( ISegment seg : unit.getSegments() ) {
					if ( seg.hasTarget() ) continue;
					Entry res = tm.search(seg.getSource());
					if ( res == null ) continue;
					seg.setTarget(xf.copyContent(seg.getStore(), true, res.getTarget()));
				}
			}
		}
	}
	
	
}
