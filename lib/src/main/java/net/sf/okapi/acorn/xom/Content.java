package net.sf.okapi.acorn.xom;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.InvalidPositionException;
import org.oasisopen.xliff.om.v1.TagType;

public class Content implements IContent {

	private final boolean isTarget;

	private StringBuilder ctext;
	private ITags tags;
	
	/**
	 * Inner class for the generic iterable.
	 * @param <T> the class of the object to iterate.
	 */
	private class ContentIterable<T> implements Iterable<T> {

		final private Class<T> theClass;
		
		public ContentIterable (Class<T> theClass) {
			this.theClass = theClass;
		}
		
		@Override
		public Iterator<T> iterator () {
			return new ContentIterator<T>(theClass);
		}
		
	}

	/**
	 * Inner class for the generic iterator.
	 * @param <T> the class of the object to iterate.
	 */
	private class ContentIterator<T> implements Iterator<T> {

		final private int mode;
		
		private int start, pos;
		private int returnType; // -1: no more, 0=string, 1=tag, 2=protected content
		
		public ContentIterator (Class<T> typeClass) {
			String typeName = typeClass.getName();
			if ( typeName.equals(Object.class.getName()) ) mode = 0;
			else if ( typeName.equals(String.class.getName()) ) mode = 1;
			else if ( typeName.equals(ITag.class.getName()) ) mode = 2;
			else if ( typeName.equals(ICTag.class.getName()) ) mode = 3;
			else if ( typeName.equals(MTag.class.getName()) ) mode = 4;
			else {
				throw new InvalidParameterException("Unsupported iteration type.");
			}
			start = pos = 0;
			returnType = -1;
			findNext();
		}
		
		@Override
		public boolean hasNext () {
			return (returnType != -1);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next () {
			int posNow = pos;
			int startNow = start;
			switch ( returnType ) {
			case 0: // String
				findNext();
				return (T)ctext.substring(startNow, posNow);
			case 1: // ITag, ICTag and IMTag
				pos += 2;
				findNext();
				return (T)tags.get(Util.toKey(ctext.charAt(posNow), ctext.charAt(posNow+1)));
//			case 2: // PCont
//				pos += 2;
//				findNext();
//				return (T)tags.getPCont(ctext, posNow);
			default: // Nothing
				return null;
			}
		}

		@Override
		public void remove () {
			throw new UnsupportedOperationException("The method remove() not supported.");
		}
		
		private void findNext () {
			// Search for next tag
			start = pos;
			for ( ; pos < ctext.length(); pos++ ) {
				char ch = ctext.charAt(pos);
				if ( Util.isChar1(ch) ) {
					// Do we have text before?
					// and if we in 'string' and 'object' modes
					if (( start < pos ) && ( mode <= 1 )) {
						// If we do: string to send will be ctext.substring(start, pos);
						returnType = 0;
						return;
					}
					else { // No string before
						if ( mode == 1 ) {
							// 'string' mode: skip the code and look for next one or end
							pos++;
							start = pos+1; // New start is the first char after the code
							continue;
						}
					}
					// Else: look at the tag
					switch ( ch ) {
					case Const.CODE_OPENING:
					case Const.CODE_CLOSING:
					case Const.CODE_STANDALONE:
						if (( mode == 0 ) || ( mode == 2 ) || ( mode == 3 )) {
							returnType = 1;
							return;
						}
						break;
					case Const.MARKER_OPENING:
					case Const.MARKER_CLOSING:
						if (( mode == 0 ) || ( mode == 2 ) || ( mode == 4 )) {
							returnType = 1;
							return;
						}
						break;
//					case Const.PCONT_STANDALONE:
//						if (( mode == 0 ) || ( mode == 5 )) {
//							returnType = 2;
//							return;
//						}
//						break;
					}
				}
			}
			
			// No tag found: for 'string and 'object' modes we send the remaining of the content
			if ( mode <= 1 ) {
				// Next string is the remaining sub-string ctext.substring(start);
				pos = ctext.length();
				if ( start < pos ) returnType = 0;
				else returnType = -1;
			}
			else {
				returnType = -1; // Nothing left
			}
		}
	}
	
	public Content (IStore store,
		boolean isTarget)
	{
		if ( store == null ) {
			throw new InvalidParameterException("The store parameter cannot be null.");
		}
		this.isTarget = isTarget;
		ctext = new StringBuilder();
		if ( isTarget ) tags = store.getTargetTags();
		else tags = store.getSourceTags();
	}
	
	@Override
	public boolean isEmpty () {
		return (ctext.length()==0);
	}
	
	@Override
	public boolean isTarget () {
		return isTarget;
	}

	@Override
	public boolean hasTag () {
		if ( tags.size() == 0 ) return false;
		// Else check the tag references
		// (the tags object is shared across the whole unit)
		for ( int i=0; i<ctext.length(); i++ ) {
			if ( Util.isChar1(ctext.charAt(i)) ) return true;
		}
		return false;
	}

	@Override
	public String getPlainText () {
		return Const.TAGREF_REGEX.matcher(new String(ctext)).replaceAll("");
	}

	@Override
	public String getCodedText () {
		return ctext.toString();
	}

	@Override
	public void setCodedText (String codedText) {
		ctext = new StringBuilder(codedText);
	}

	@Override
	public ITags getTags () {
		return tags;
	}

	@Override
	public List<ITag> getOwnTags () {
		if ( tags.size() == 0 ) return Collections.emptyList();
		ArrayList<ITag> list = new ArrayList<>();
		for ( int i=0; i<ctext.length(); i++ ) {
			if ( Util.isChar1(ctext.charAt(i)) ) {
				list.add(tags.get(Util.toKey(ctext.charAt(i), ctext.charAt(++i))));
			}
		}
		return list;
	}

	@Override
	public void clear () {
		// Removes all the tags
		for ( int i=0; i<ctext.length(); i++ ) {
			char ch = ctext.charAt(i);
			if ( Util.isChar1(ch) ) {
				tags.remove(Util.toKey(ch, ctext.charAt(++i)));
			}
		}
		// Reset the text
		ctext = new StringBuilder();
	}
	
	@Override
	public void delete (int start,
		int end)
	{
		if ( end == -1 ) end = ctext.length();
		checkPosition(start);
		checkPosition(end);
		// Removes all the tags
		for ( int i=start; i<end; i++ ) {
			char ch = ctext.charAt(i);
			if ( Util.isChar1(ch) ) {
				tags.remove(Util.toKey(ch, ctext.charAt(++i)));
			}
		}
		ctext.delete(start, end);
	}

	@Override
	public IContent append (CharSequence plainText) {
		ctext.append(plainText);
		return this;
	}
	
	@Override
	public IContent append (char ch) {
		ctext.append(ch);
		return this;
	}

	/**
	 * Appends an {@link ICTag} object at the end of this content.
	 * @param code the code to append.
	 * @return the appended code (same code as the parameter).
	 */
	@Override
	public ICTag append (ICTag code) {
		ctext.append(Util.toRef(tags.add(code)));
		return code;
	}
	
	@Override
	public ICTag openCodeSpan (String id,
		String data)
	{
		CTag ctag = new CTag(null, TagType.OPENING, id, data);
		ctext.append(Util.toRef(tags.add(ctag)));
		return ctag;
	}

	@Override
	public ICTag closeCodeSpan (String id,
		String data)
	{
		ITag opening = tags.getOpeningTag(id);
		if ( opening == null ) {
			throw new InvalidParameterException(
				String.format("Cannot add closing code tag because id '%s' does not exist.", id));
		}
		if ( !(opening instanceof CTag) ) {
			throw new InvalidParameterException(
				String.format("The id '%s' does not correspond to a code.", id));
		}
		CTag ec = new CTag((CTag)opening, data);
		ctext.append(Util.toRef(tags.add(ec)));
		return ec;
	}

	@Override
	public ICTag appendCode (String id,
		String data)
	{
		CTag ctag = new CTag(null, TagType.STANDALONE, id, data);
		ctext.append(Util.toRef(tags.add(ctag)));
		return ctag;
	}

	@Override
	public IMTag openMarkerSpan (String id,
		String type)
	{
		MTag mtag = new MTag(true, id, type);
		ctext.append(Util.toRef(tags.add(mtag)));
		return mtag;
	}

	@Override
	public IMTag closeMarkerSpan (String id) {
		ITag opening = tags.getOpeningTag(id);
		if ( opening == null ) {
			throw new InvalidParameterException(
				String.format("Cannot add closing marker tag because id '%s' does not exist.", id));
		}
		if ( !(opening instanceof MTag) ) {
			throw new InvalidParameterException(
				String.format("The id '%s' does not correspond to a marker.", id));
		}
		MTag em = new MTag((MTag)opening);
		ctext.append(Util.toRef(tags.add(em)));
		return em;
	}

	private void checkPosition (int pos) {
		if ( pos > 0 ) {
			if ( Util.isChar1(ctext.charAt(pos-1)) ) {
				throw new InvalidPositionException (
					String.format("Position %d is inside a tag reference.", pos));
			}
		}
	}

	@Override
	public IContent insert (int pos,
		String plainText)
	{
		checkPosition(pos);
		ctext.insert(pos, plainText);
		return this;
	}

	@Override
	public ICTag insert (int pos,
		ICTag code)
	{
		checkPosition(pos);
		ctext.insert(pos, Util.toRef(tags.add(code)));
		return code;
	}

	@Override
	public Iterator<Object> iterator () {
		return (new ContentIterable<Object>(Object.class)).iterator();
	}

}
