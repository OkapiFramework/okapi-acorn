package net.sf.okapi.acorn.xom;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
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
	public ICTag startCodeSpan (String id,
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
	public IMTag startMarkerSpan (String id,
		String type)
	{
		StartAnnotation anno = new StartAnnotation(id, type);
		ctext.append(Util.toRef(tags.add(anno)));
		return anno;
	}

	@Override
	public IMTag closeMarkerSpan (String id) {
		ITag il = tags.getOpeningTag(id);
		if ( il == null ) {
			throw new InvalidParameterException(
				String.format("Cannot add closing annotation tag because id '%s' does not exist.", id));
		}
		if ( !(il instanceof StartAnnotation) ) {
			throw new InvalidParameterException(
				String.format("The id '%s' does not correspond to an annotation.", id));
		}
		StartAnnotation sa = (StartAnnotation)il;
		EndAnnotation ea = new EndAnnotation(sa);
		ctext.append(Util.toRef(tags.add(ea)));
		return ea;
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

}
