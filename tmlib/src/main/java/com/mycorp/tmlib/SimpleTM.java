package com.mycorp.tmlib;

import java.util.ArrayList;

import net.sf.okapi.acorn.xom.Store;
import net.sf.okapi.acorn.xom.XLIFFFactory;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithStore;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class SimpleTM implements IWithStore {

	private IStore megastore;
	private ArrayList<Entry> entries;
	private IXLIFFFactory fac;
	
	public SimpleTM () {
		entries = new ArrayList<>();
		megastore = new Store(null);
		fac = new XLIFFFactory();
	}
	
	/**
	 * Adds into the TM any of the bilingual segments marked as final that are present
	 * in the given unit. 
	 * @param unit the unit to process.
	 * @return the number of segment added.
	 */
	public int addSegments (IUnit unit) {
		int count = 0;
		for ( ISegment seg : unit.getSegments() ) {
			if ( !seg.hasTarget() ) continue;
			// Else: add the segment
			Entry entry = new Entry(
				fac.createContent(megastore, false, seg.getSource()),
				fac.createContent(megastore, true, seg.getTarget()));
			if ( entries.add(entry) ) count++;
		}
		return count;
	}

	/**
	 * Search for the best match for a given source content.
	 * @param source the source content.
	 * @return the best entry found or null.
	 */
	public Entry search (IContent source) {
		String src = Entry.makeSearchKey(source);
		for ( Entry entry : entries ) {
			if ( entry.getSource().getPlainText().toLowerCase().equals(src) ) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public String getId () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStore getStore () {
		return megastore;
	}

}
