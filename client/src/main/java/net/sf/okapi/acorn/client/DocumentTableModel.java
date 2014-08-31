package net.sf.okapi.acorn.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.TagType;

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
	
	public void refreshDisplay () {
		fireTableDataChanged();
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
			return fmt(seg.getSource());
		case 2:
			if ( seg.hasTarget() ) return fmt(seg.getTarget());
			else return "";
		}
		return null;
	}

	private String fmt (IContent content) {
		if ( content == null ) return "null";
		StringBuilder tmp = new StringBuilder();
		for ( Object obj : content ) {
			if ( obj instanceof String ) {
				tmp.append((String)obj);
			}
			else if ( obj instanceof ICTag ) {
				ICTag ctag = (ICTag)obj;
				tmp.append("<");
				if ( ctag.getTagType() == TagType.CLOSING ) tmp.append("/");
				tmp.append("CODE-id:"+ctag.getId());
				if ( ctag.getTagType() == TagType.STANDALONE ) tmp.append("/");
				tmp.append(">");
			}
			else {
				tmp.append("[ERR!]");
			}
		}
		return tmp.toString();
	}
}
