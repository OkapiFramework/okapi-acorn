package org.oasisopen.xliff.om.v1;

/**
 * Represents a document object (one or more {@link IFile} objects).
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
