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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.okapi.acorn.common.Util;
import net.sf.okapi.acorn.xom.Factory;

import org.oasisopen.xliff.om.v1.ISegment;

import com.mycorp.tmlib.Entry;
import com.mycorp.tmlib.SimpleTM;

public class TMPanel extends JPanel {

	private final static long serialVersionUID = 1L;

	private SimpleTM tm;
	private JLabel stEntries;
	private JTextField edSearch;
	private JTextArea edResult;
	
	public TMPanel (SimpleTM tm) {
		this.tm = tm;
		GridBagLayout layout =  new GridBagLayout();
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

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
		edSearch.setToolTipText("Leave empty to show all entries.");

		final JButton btSearch = new JButton("Search");
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
		Font font = new Font("Gadugi", 0, 20);
		edResult.setFont(font);
		edResult.setLineWrap(true);
		edResult.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(edResult); 
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 0; c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
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
				log("src: " + Util.fmt(entry.getSource()));
				log("trg: " + Util.fmt(entry.getTarget())+"\n");
			}
		}
		else {
			ISegment seg = Factory.XOM.createLoneSegment();
			seg.getSource().setCodedText(text);
			Entry res = tm.search(seg.getSource());
			if ( res == null ) log("No match found");
			else {
				log("src: " + Util.fmt(res.getSource()));
				log("trg: " + Util.fmt(res.getTarget()));
			}
		}
		if ( edResult.getText().isEmpty() ) {
			log("No match found.");
		}
	}

	private void log (String text) {
		edResult.setText(edResult.getText()+text+"\n");
	}

	private void clearLog () {
		edResult.setText("");
	}

}
