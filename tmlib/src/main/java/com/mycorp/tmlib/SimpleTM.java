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

package com.mycorp.tmlib;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import net.sf.okapi.acorn.common.FilterBasedReader;
import net.sf.okapi.acorn.common.IDocumentReader;
import net.sf.okapi.acorn.common.XLIFF2Reader;
import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.acorn.xom.Store;
import net.sf.okapi.acorn.xom.Unit;
import net.sf.okapi.acorn.xom.json.JSONReader;
import net.sf.okapi.acorn.xom.json.JSONWriter;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithStore;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class SimpleTM implements IWithStore, Iterable<Entry> {

	private static final IXLIFFFactory OKAPI_XLIFFIMPL = Factory.XOM;
	
	private final String tmId;
	private final IStore store;
	private final ArrayList<Entry> entries;
	private final JSONReader jr;
	private final JSONWriter jw;
	
	public SimpleTM () {
		entries = new ArrayList<>();
		store = new Store(null);
		jr = new JSONReader();
		jw = new JSONWriter();
		tmId = UUID.randomUUID().toString();
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
			if ( !seg.hasTarget() ) continue; // Skip segments without target
			IContent srcContent = seg.getSource();
			IContent trgContent = seg.getTarget();
			if ( srcContent.isEmpty() ) continue; // Skip segments with empty source
			if ( trgContent.isEmpty() ) continue; // Skip segments with empty target
			// Else: add the segment
			Entry entry = new Entry(
				OKAPI_XLIFFIMPL.copyContent(store, false, srcContent),
				OKAPI_XLIFFIMPL.copyContent(store, true, trgContent));
			if ( entries.add(entry) ) count++;
		}
		return count;
	}

	public void addSegment (String srcPlainText,
		String trgPlainText)
	{
		IContent src = OKAPI_XLIFFIMPL.createContent(store, false);
		src.setCodedText(srcPlainText);
		IContent trg = OKAPI_XLIFFIMPL.createContent(store, true);
		trg.setCodedText(trgPlainText);
		entries.add(new Entry(src, trg));
	}

	/**
	 * Search for the best match for a given source content.
	 * @param source the source content.
	 * @return the best entry found or null.
	 */
	public Entry search (IContent source) {
		String src = Entry.makeSearchKey(source);
		for ( Entry entry : entries ) {
			if ( entry.getSearchKey().equals(src) ) {
				return entry;
			}
		}
		return null;
	}
	
	public String search (String sourceInJson) {
		Unit unit = new Unit("tmp");
		IContent src = jr.readContent(unit.getStore(), false, sourceInJson);
		Entry res = search(src);
		if ( res == null ) return null;
		// Else: serialize to JSON and return
		return jw.fromContent(res.getTarget()).toJSONString();
	}
	
	@Override
	public String getId () {
		return tmId;
	}

	@Override
	public IStore getStore () {
		return store;
	}

	public void loadDefaultEntries () {
		addSegment("It's a very good idea and it's something that we can take into consideration.",
			"ᐱᐅᔪᒻᒪᕆᐊᓗᒃ ᐊᒻᒪᓗ ᑖᓐᓇ ᐃᓱᒪᔅᓴᖅᓯᐅᕈᑎᒋᔪᓐᓇᕋᑦᑎᒍ.");
			// piujummarialuk ammalu taanna isumassaqsiurutigijunnarattigu.
		
		addSegment("We can pave the way for the future.",
			"ᓯᕗᓂᑦᓴᑎᓐᓄᑦ ᐊᖁᑎᒃᓴᓕᐅᕆᐊᖃᕋᑦᑕ.");
			// sivunitsatinnut aqutiksaliuriaqaratta.
		
//		addSegment("I just wanted to make sure that it really happens.",
//			"ᑕᐃᒪᓐᓇᐃᓕᖃᑦᑕᕋᓗᐊᕐᒪᖔᖅ ᖃᐅᔨᔪᒪᑐᐃᓐᓇᖃᑖᖅᑕᕋ.");
		
		addSegment("There is no limit to what a community can accomplish when they work together for a common goal.",
			"ᑭᒡᓕᖃᖏᑦᑎᐊᕐᒪᑦ ᓄᓇᓕᐅᔪᓄᑦ ᖃᓄᐃᓕᐅᕐᓂᐅᔪᓐᓇᕐᑐᖅ ᐱᓕᕆᖃᑎᒌᑦᑎᐊᖅᑎᓪᓗᒋᑦ ᐊᑕᐅᓯᕐᒥᒃ ᑐᕌᒐᖃᖅᑎᓪᓗᒋᑦ.");
			// kigliqangittiarmat nunaliujunut qanuiliurniujunnartuq piliriqatigiittiaqtillugit atausirmik turaagaqaqtillugit.
		
		addSegment("We have no choice but to follow the rules.",
			"ᖃᓄᐃᓕᐅᕕᖃᙱᓇᑦᑕ ᑭᓯᐊᓂᓕ ᒪᓕᑦᑕᐅᔭᕆᐊᓖᑦ ᒪᓕᓪᓗᒋᑦ.");
			// qanuiliuviqannginatta kisianili malittaujarialiit malillugit.
	}

	public int getCount () {
		return entries.size();
	}

	@Override
	public Iterator<Entry> iterator () {
		return entries.iterator();
	}

	public int importSegments (File inputFile) {
    	int count = 0;
    	// Get the extension
    	String path = inputFile.getPath();
    	int p = path.lastIndexOf('.');
    	String ext = "";
    	if ( p > -1 ) ext = path.substring(p+1).toLowerCase();

    	// Instantiate the proper reader based on the type of document
    	IDocumentReader reader;
    	switch ( ext ) {
    	case "xlf":
    		reader = new XLIFF2Reader();
    		break;
    	case "tmx":
    		reader = new FilterBasedReader("okf_tmx");
    		break;
    	default:
			throw new RuntimeException("Unsupported or unknown file format: ."+ext);
    	}
    	
    	IDocument doc = reader.load(inputFile);
		for ( IFile file : doc ) {
			for ( IGroupOrUnit gou : file ) {
				if ( !gou.isUnit() ) continue;
				IUnit unit = (IUnit)gou;
				for ( ISegment segment : unit.getSegments() ) {
					if ( segment.hasTarget() ) {
					}
				}
				count += addSegments(unit);
			}
		}
		
		return count;
	}

	@Override
	public boolean isIdUsed (String id) {
		return tmId.equals(id);
	}

}
