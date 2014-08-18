package org.oasisopen.xliff.om.v1;

/**
 * Represents an extension object data.
 */
public interface IExtObjectData extends IExtObjectItem {

	/**
	 * Gets the text content of this object item.
	 * @return the text content of this object item (can be null).
	 */
	public String getContent ();
	
	/**
	 * Sets the text content of this object item.
	 * @param content the text content of this object item (can be null).
	 */
	public void setContent (String content);

	/**
	 * Indicates if, when possible, this text content should be serialized with a mechanism that allows
	 * its representation without escaping (e.g. in a CDATA section in XML).
	 * @return true if a raw output is requested.
	 */
	public boolean getRaw ();
	
	/**
	 * Sets the flag indicating if a raw output is requested.
	 * @param raw true to request a raw output, false to request a normal output.
	 */
	public void setRaw (boolean raw);
	
}
