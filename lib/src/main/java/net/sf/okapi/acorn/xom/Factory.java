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

package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.GetTarget;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;
import org.oasisopen.xliff.om.v1.InvalidParameterException;
import org.oasisopen.xliff.om.v1.TagType;

/**
 * Implements a singleton instance of the {@link IXLIFFFactory} interface.
 */
public enum Factory implements IXLIFFFactory {

	/**
	 * The unique instance of the factory.
	 */
	XOM;
	
	@Override
	public boolean supports (String moduleUri) {
		// No modules are supported directly for now.
		return false;
	}

	@Override
	public IDocument createDocument () {
		return new Document();
	}

	@Override
	public IFile createFile (String id) {
		return new File(id);
	}
	
	@Override
	public IGroup createGroup (IGroup parent,
		String id)
	{
		return new Group(parent, id);
	}
	
	@Override
	public IUnit createUnit (String id) {
		return new Unit(id);
	}

	@Override
	public IContent createContent (IStore store,
		boolean isTarget)
	{
		return new Content(store, isTarget);
	}
	
	public IUnit copyUnit (IUnit original) {
		IUnit dest = new Unit(original.getId());
		for ( IPart oriPart : original ) {
			((Unit)dest).appendPart(copyPart(dest.getStore(), oriPart, true, false));
		}
		dest.setCanResegment(original.getCanResegment());
		dest.setId(original.getId());
		dest.setName(original.getName());
		dest.setPreserveWS(original.getPreserveWS());
		dest.setSourceDir(original.getSourceDir());
		dest.setTargetDir(original.getTargetDir());
		dest.setTranslate(original.getTranslate());
		dest.setType(original.getType());
		return dest;
	}

	@Override
	public IContent copyContent (IStore store,
		boolean isTarget,
		IContent original)
	{
		IContent dest = new Content(store, isTarget);
		ITags oriTags = original.getTags();
		ITags destTags = dest.getTags();
		StringBuilder tmp = new StringBuilder(original.getCodedText());
		int key;
		for ( int i=0; i<tmp.length(); i++ ) {
			char ch = tmp.charAt(i);
			if ( XUtil.isChar1(ch) ) {
				ITag tag = oriTags.get(tmp, i);
				key = destTags.add(copyTag(tag, destTags));
				tmp.replace(i, i+2, XUtil.toRef(key)); i++;
			}
		}
		dest.setCodedText(tmp.toString());
		return dest;
	}
	
	public IPart copyPart (IStore store,
		IPart original,
		boolean copyContent,
		boolean newId)
	{
		if ( original.isSegment() ) {
			return copySegment(store, (ISegment)original, copyContent, newId);
		}
		
		IPart dest = createPart(store);
		if ( copyContent ) {
			dest.setSource(copyContent(store, false, original.getSource()));
		}

		GetTarget gtOpt = (copyContent ? GetTarget.CLONE_SOURCE : GetTarget.CREATE_EMPTY);
		if ( original.hasTarget() ) {
			dest.getTarget(gtOpt);
			if ( copyContent ) {
				dest.setTarget(copyContent(store, true, original.getTarget()));
			}
		}
		// Copy xml:space info (source/target level in XLIFF but stored in part in library)
		dest.setPreserveWS(original.getPreserveWS());
		// Update ID value if requested
		if ( newId ) dest.setId(dest.getStore().suggestId(true));
		else {
			if ( !store.getParent().isIdUsed(original.getId()) ) {
				dest.setId(original.getId());
			}
		}
		return dest;
	}
	
	public ISegment copySegment (IStore store,
		ISegment original,
		boolean copyContent,
		boolean newId)
	{
		ISegment dest = createSegment(store);
		if ( copyContent ) {
			dest.setSource(copyContent(store, false, original.getSource()));
		}

		GetTarget gtOpt = (copyContent ? GetTarget.CLONE_SOURCE : GetTarget.CREATE_EMPTY);
		// Copy the metadata for the source
//TODO		seg.getSource().setDir(original.getSource().getDir());
		// Make sure we have a target if the original segment has one
		if ( original.hasTarget() ) {
			// Create the target
			// and at the same time copy the metadata for the target
			dest.getTarget(gtOpt);
			if ( copyContent ) {
				dest.setTarget(copyContent(store, true, original.getTarget()));
			}
			 //TODO .setDir(original.getTarget().getDir());
		}
		// Copy xml:space info (source/target level in XLIFF but stored in part in library)
		dest.setPreserveWS(original.getPreserveWS());
		// Copy the metadata for the segment
		dest.setCanResegment(original.getCanResegment());
		dest.setState(original.getState());
		dest.setSubState(original.getSubState());
		// Update ID value if requested
		if ( newId ) dest.setId(dest.getStore().suggestId(true));
		else {
			if ( !store.getParent().isIdUsed(original.getId()) ) {
				dest.setId(original.getId());
			}
		}
		return dest;
	}

	@Override
	public ISegment copyEmptySegment (IStore store,
		ISegment original)
	{
		return copySegment(store, original, false, true);
	}

	@Override
	public IPart createPart (IStore store) {
		return new Part(store);
	}

	@Override
	public IPart createPart (IStore store,
		String sourcePlainText)
	{
		IPart part = createPart(store);
		part.getSource().append(sourcePlainText);
		return part;
	}

	@Override
	public ISegment createSegment (IStore store) {
		return new Segment(store); 
	}

	@Override
	public ISegment createLoneSegment () {
		return new Segment(new Store(null)); 
	}

	@Override
	public ITag copyTag (ITag original,
		ITags destinationTags)
	{
		if ( original instanceof ICTag ) {
			ICTag oct = null;
			if ( original.getTagType() == TagType.CLOSING ) {
				oct = destinationTags.getOpeningCTag(original.getId());
			}
			return new CTag((ICTag)original, (CTag)oct);
		}
		if ( original instanceof IMTag ) {
			IMTag omt = null;
			if ( original.getTagType() == TagType.CLOSING ) {
				omt = destinationTags.getOpeningMTag(original.getId());
			}
			return new MTag((IMTag)original, (MTag)omt);
		}
		// Else: error
		throw new InvalidParameterException("The type of the original object is invalid.");
	}

	@Override
	public IExtObject createExtObject (String nsUri,
		String name)
	{
		return new ExtObject(nsUri, name);
	}

	@Override
	public IMTag createOpeningMTag (String id,
		String type)
	{
		return new MTag(true, id, type);
	}
	
}
