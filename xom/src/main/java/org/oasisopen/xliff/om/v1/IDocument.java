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

package org.oasisopen.xliff.om.v1;

/**
 * Represents a document object (one or more {@link IFile} objects).
 * <p>See <a href='http://opentag.com/data/xliffomapi'>http://opentag.com/data/xliffomapi</a>
 */
public interface IDocument extends Iterable<IFile> {

	/**
	 * Gets the XLIFF version of this document.
	 * @return the XLIFF version of this document.
	 */
	public String getVersion ();
	
	/**
	 * Gets the source language of this document.
	 * @return the source language of this document.
	 */
	public String getSourceLanguage ();
	
	/**
	 * Sets the source language of this document.
	 * @param srcLang the source language of this document.
	 */
	public void setSourceLanguage (String srcLang);
	
	/**
	 * Gets the target language of this document.
	 * @return the target language of this document (can be null).
	 */
	public String getTargetLanguage ();
	
	/**
	 * Sets the target language of this document.
	 * @param trgLang the target language of this document (can be null).
	 */
	public void setTargetLanguage (String trgLang);
	
	/**
	 * Gets the file for the given id.
	 * @param id the id of the file to retrieve.
	 * @return the {@link IFile} object for the given id or null if not found.
	 */
	public IFile getFile (String id);
	
	/**
	 * Gets the number of files in this document.
	 * @return the number of files in this document.
	 */
	public int getFileCount ();

	/**
	 * Adds a file to this document.
	 * @param file the file to add.
	 * @return the file that has been added.
	 * @throws InvalidParameterException if the file cannot be added.
	 */
	public IFile add (IFile file);

	/**
	 * Removes a file from this document.
	 * @param id the id of the file to remove.
	 */
	public void remove (String id);
	
}
