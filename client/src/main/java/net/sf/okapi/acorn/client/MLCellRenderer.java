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