package net.sf.okapi.acorn.client;

import javax.swing.SwingWorker;

import net.sf.okapi.acorn.common.IXLIFFProcessor;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

public class XLIFFDocumentTask extends SwingWorker<Void, Void> implements IXLIFFProcessor {

	private IDocument document;
	private int currentSeg;
	private int segCount;
	private Throwable error;
	
	@Override
	protected Void doInBackground ()
		throws Exception
	{
		error = null;
		try {
			process(document);
		}
		catch ( Throwable e ) {
			error = e;
		}
		return null;
	}
	
	public void setDocument (IDocument document) {
		this.document = document;
	}

	public String getInfo () {
		return "<html></html>";
	}
	
	public String getInfoLink () {
		return null;
	};
	
	public Throwable getError () {
		return error;
	}
	
	@Override
	public void process (IDocument document) {
		if ( document == null ) return;
		// Get the number of segments
		segCount = 0;
		for ( IFile file : document ) {
			for ( IGroupOrUnit gou : file ) {
				countSegments(gou);
			}
		}
		
		currentSeg = 0;
		for ( IFile file : document ) {
			for ( IGroupOrUnit gou : file ) {
				process(gou);
			}
		}
	}
	
	private void countSegments (IGroupOrUnit gou) {
		if ( gou.isUnit() ) {
			segCount += ((IUnit)gou).getSegmentCount();
		}
		else {
			for ( IGroupOrUnit nested : (IGroup)gou ) {
				countSegments(nested);
			}
		}
	}
	
	private void process (IGroupOrUnit gou) {
		if ( gou.isUnit() ) {
			process((IUnit)gou);
		}
		else {
			for ( IGroupOrUnit nested : (IGroup)gou ) {
				process(nested);
			}
		}
	}
	
	public void process (IUnit unit) {
		for ( ISegment seg : unit.getSegments() ) {
			process(seg);
		}
	}
	
	protected void process (ISegment segment) {
		int percentage = (int)(((float)currentSeg / (float)segCount) * 100.0);
		setProgress(percentage);
		currentSeg++;
	}

}
