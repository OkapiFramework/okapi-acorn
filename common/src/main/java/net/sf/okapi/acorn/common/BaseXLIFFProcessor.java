package net.sf.okapi.acorn.common;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.IUnit;

public abstract class BaseXLIFFProcessor implements IXLIFFProcessor {

	@Override
	public void process (IDocument document) {
		if ( document == null ) return;
		for ( IFile file : document ) {
			for ( IGroupOrUnit gou : file ) {
				if ( gou.isUnit() ) process((IUnit)gou);
				else process((IGroup)gou);
			}
		}
	}
	
	protected void process (IGroup group) {
		for ( IGroupOrUnit gou : group ) {
			if ( gou.isUnit() ) process((IUnit)gou);
			else process((IGroup)gou);
		}
	}

	abstract public void process (IUnit unit);

}
