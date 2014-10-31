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

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.sf.okapi.acorn.common.Util;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

public class DocumentTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private String[] colNames = {"ID", "Source", "Target"};
	private IDocument doc;
	private List<ISegment> segs = new ArrayList<>();
	
	public void setDocument (IDocument doc) {
		this.doc = doc;
		updateData();
	}
	
	public IDocument getDocument () {
		return doc;
	}
	
	public void refreshDisplay (boolean updateData) {
		if ( updateData ) {
			updateData();
		}
		else {
			fireTableDataChanged();
		}
	}
	
	private void updateData () {
		segs.clear();
		if ( doc == null ) return;
		for ( IFile file : doc ) {
			for ( IGroupOrUnit gou : file ) {
				if ( !gou.isUnit() ) continue;
				IUnit unit = (IUnit)gou;
				for ( ISegment seg : unit.getSegments() ) {
					if ( seg.getSource().isEmpty() ) continue;
					segs.add(seg);
				}
			}
		}
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount () {
		return colNames.length;
	}
	
	@Override
	public String getColumnName (int col) {
		return colNames[col];
	}
	
	@Override
	public int getRowCount () {
		return segs.size();
	}
	
	@Override
	public Object getValueAt (int row,
		int col)
	{
		ISegment seg = segs.get(row);
		switch ( col ) {
		case 0:
			return seg.getStore().getParent().getId() + "-"
				+ ((seg.getId() == null) ? "row"+(row+1) : seg.getId());
		case 1:
			return Util.fmt(seg.getSource());
		case 2:
			if ( seg.hasTarget() ) return Util.fmt(seg.getTarget());
			else return "";
		}
		return null;
	}

}
