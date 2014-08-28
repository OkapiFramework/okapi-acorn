package net.sf.okapi.acorn.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.okapi.acorn.xom.Factory;

import org.oasisopen.xliff.om.v1.ISegment;

import com.mycorp.tmlib.Entry;
import com.mycorp.tmlib.SimpleTM;

public class TMPanel extends JPanel {

	private SimpleTM tm;
	private JLabel stEntries;
	private JTextField edSearch;
	private JTextArea edResult;
	
	public TMPanel (SimpleTM tm) {
		this.tm = tm;
		GridBagLayout layout =  new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();

		stEntries = new JLabel();
		c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		add(stEntries, c);
		
		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		//c.gridwidth = GridBagConstraints.REMAINDER;
		add((edSearch = new JTextField()), c);

		JButton btSearch = new JButton("Search");
		btSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				doSearch();
			}
		});
		c = new GridBagConstraints();
		c.gridx = 1; c.gridy = 1;
		add(btSearch, c);

		edResult = new JTextArea();
		Font font = new Font("Gadugi", 0, 20); // Gadugi, Euphemia //new Font("Courier New", 0, 20);
		edResult.setFont(font);
		JScrollPane scrollPane = new JScrollPane(edResult); 
		c = new GridBagConstraints(); c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 0; c.gridy = 2; c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		add(scrollPane, c);
		
		updateEntries();
	}

	public void updateEntries () {
		stEntries.setText("Number of entries in the TM = "+tm.getCount());
	}

	private void doSearch () {
		String text = edSearch.getText().trim();
		edResult.setText("");

		clearLog();
		if ( text.isEmpty() ) {
			for ( Entry entry : tm ) {
				log("src: " + entry.getSource().getPlainText());
				log("trg: " + entry.getTarget().getPlainText()+"\n");
			}
		}
		else {
			ISegment seg = Factory.XOM.createLoneSegment();
			seg.getSource().setCodedText(text);
			Entry res = tm.search(seg.getSource());
			if ( res == null ) log("No match found");
			else {
				log("src: " + res.getSource().getPlainText());
				log("trg: " + res.getTarget().getPlainText());
			}
		}
	}

	private void log (String text) {
		edResult.setText(edResult.getText()+text+"\n");
	}

	private void clearLog () {
		edResult.setText("");
	}

}
