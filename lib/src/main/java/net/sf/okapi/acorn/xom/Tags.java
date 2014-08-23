package net.sf.okapi.acorn.xom;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.InvalidParameterException;
import org.oasisopen.xliff.om.v1.TagType;

public class Tags implements ITags {

	final private IStore store;
	
	private LinkedHashMap<Integer, ITag> tags;
	private LinkedHashMap<Character, Integer> lastValues;

	public Tags (IStore store) {
		if ( store == null ) {
			throw new InvalidParameterException("The store parameter cannot be null.");
		}
		this.store = store;
		// Last values for auto-keys (for the marker's indexing)
		lastValues = new LinkedHashMap<>();
		resetLastValues();
	}

	private void resetLastValues () {
		lastValues.put(Const.CODE_OPENING, -1);
		lastValues.put(Const.CODE_CLOSING, -1);
		lastValues.put(Const.CODE_STANDALONE, -1);
		lastValues.put(Const.MARKER_OPENING, -1);
		lastValues.put(Const.MARKER_CLOSING, -1);
	}

	@Override
	public IStore getStore () {
		return store;
	}

	@Override
	public int size () {
		if ( tags == null ) return 0;
		return tags.size();
	}
	
	@Override
	public ITag get (int key) {
		ITag tag = (( tags != null ) ? tags.get(key) : null);
		if ( tag == null ) {
			throw new InvalidParameterException("No tag found for the key "+key);
		}
		return tag;
	}

	@Override
	public ITag get (CharSequence ctext,
		int pos)
	{
		return get(Util.toKey(ctext.charAt(pos), ctext.charAt(pos+1)));
	}

	@Override
	public int getKey (ITag inline) {
		if ( tags != null ) {
			for ( Map.Entry<Integer, ITag> entry : tags.entrySet() ) {
				if ( entry.getValue() == inline ) return entry.getKey();
			}
		}
		return -1;
	}

	@Override
	public void remove (int key) {
		if ( tags != null ) {
			tags.remove(key);
		}
	}

	@Override
	public ITag getOpeningTag (String id) {
		if ( tags == null ) return null;
		for ( ITag marker : tags.values() ) {
			if ( marker.getId().equals(id) ) {
				if ( marker.getTagType() == TagType.OPENING ) return marker;
			}
		}
		return null;
	}

	@Override
	public ICTag getOpeningICTag (String id) {
		if ( tags == null ) return null;
		for ( ITag tag : tags.values() ) {
			if ( tag.getId().equals(id) ) {
				if ( tag.getTagType() == TagType.OPENING ) {
					if ( !(tag instanceof ICTag) ) {
						throw new InvalidParameterException(String.format(
							"The tag id='%s' exists but is not an ICTag.", id));
					}
					return (ICTag)tag;
				}
			}
		}
		return null;
	}
	
	@Override
	public ITag getClosingTag (String id) {
		if ( tags == null ) return null;
		for ( ITag marker : tags.values() ) {
			if ( marker.getId().equals(id) ) {
				if ( marker.getTagType() == TagType.CLOSING ) return marker;
			}
		}
		return null;
	}

	@Override
	public int add (ITag tag) {
		boolean isCode = (tag instanceof ICTag);
		switch ( tag.getTagType() ) {
		case OPENING:
			return add(isCode ? Const.CODE_OPENING : Const.MARKER_OPENING, tag); 
		case CLOSING:
			return add(isCode ? Const.CODE_CLOSING : Const.MARKER_CLOSING, tag);
		case STANDALONE:
			return add(Const.CODE_STANDALONE, tag);
		default:
			throw new InvalidParameterException("Invalid tag type.");
		}
	}

	private int add (char mtype,
		ITag tag)
	{
		if ( tags == null ) tags = new LinkedHashMap<>(3);
		int value = lastValues.get(mtype);
		lastValues.put(mtype, ++value);
		int key = Util.toKey(mtype, Const.TAGREF_BASE+value);
		if ( tags.containsKey(key) ) {
			throw new InvalidParameterException("The key auto-selected to add this marker exists already.");
		}
		tags.put(key, tag);
		return key;
	}

	@Override
	public Iterator<ITag> iterator () {
		if ( tags == null ) tags = new LinkedHashMap<>(3);
		return tags.values().iterator();
	}

}
