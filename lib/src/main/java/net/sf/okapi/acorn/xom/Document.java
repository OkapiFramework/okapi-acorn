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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

/**
 * Implements the {@link IDocument} interface.
 */
public class Document implements IDocument {

	private String version = "2.0";
	private String srcLang = "en";
	private String trgLang;
	private Map<String, IFile> files = new HashMap<>(1);
	
	/**
	 * Creates a new empty document.
	 */
	Document () {
		// Nothing to do: hide this constructor from outside
	}
	
	@Override
	public IFile getFile (String id) {
		return files.get(id);
	}

	@Override
	public int getFileCount () {
		return files.size();
	}

	@Override
	public String getVersion () {
		return version;
	}

	@Override
	public String getSourceLanguage () {
		return srcLang;
	}

	@Override
	public void setSourceLanguage (String srcLang) {
		this.srcLang = srcLang;
	}

	@Override
	public String getTargetLanguage () {
		return trgLang;
	}

	@Override
	public void setTargetLanguage (String trgLang) {
		this.trgLang = trgLang;
	}

	@Override
	public IFile add (IFile file) {
		if ( files.containsKey(file.getId()) ) {
			throw new InvalidParameterException(String.format(
				"There is aleardy a file with id='%s' in the document", file.getId()));
		}
		files.put(file.getId(), file);
		return file;
	}

	@Override
	public void remove (String id) {
		files.remove(id);
	}

	@Override
	public Iterator<IFile> iterator () {
		return files.values().iterator();
	}
	
}
