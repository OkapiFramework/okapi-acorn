package com.mycorp.tmlib;

import java.util.ArrayList;

import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.acorn.xom.Store;
import net.sf.okapi.acorn.xom.Unit;
import net.sf.okapi.acorn.xom.json.JSONReader;
import net.sf.okapi.acorn.xom.json.JSONWriter;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithStore;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class SimpleTM implements IWithStore {

	final private IXLIFFFactory xf = Factory.XOM;
	final private IStore megastore;
	final private ArrayList<Entry> entries;
	final private JSONReader jr;
	final private JSONWriter jw;
	
	public SimpleTM () {
		entries = new ArrayList<>();
		megastore = new Store(null);
		jr = new JSONReader();
		jw = new JSONWriter();
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
				xf.copyContent(megastore, false, seg.getSource()),
				xf.copyContent(megastore, true, seg.getTarget()));
			if ( entries.add(entry) ) count++;
		}
		return count;
	}

	public void addSegment (String srcPlainText,
		String trgPlainText)
	{
		IContent src = xf.createContent(megastore, false);
		src.setCodedText(srcPlainText);
		IContent trg = xf.createContent(megastore, true);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStore getStore () {
		return megastore;
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

}
