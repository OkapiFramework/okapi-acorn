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
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class MLCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	public MLCellRenderer () {
		setWrapStyleWord(true);
		setLineWrap(true);
		setFont(new Font("Gadugi", 0, 20));
		setBackground(Color.WHITE);
	}

	@Override
	public Component getTableCellRendererComponent (JTable table,
		Object value,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int col)
	{
		setText((String)value);
		setSize(table.getColumnModel().getColumn(col).getWidth(), getPreferredSize().height);
		// Make sure resizing of the second column does not overrides the one of the first
		// if the first is higher
		int nh = getPreferredSize().height;
		if ( col == 2 ) {
			nh = Math.max(nh, table.getRowHeight(row));
			if ( nh > table.getRowHeight(row) ) {
				table.setRowHeight(row, nh);
			}
		}
		else {
			if ( table.getRowHeight(row) != nh ) {
				table.setRowHeight(row, nh);
			}
		}
		return this;
	}
}