package org.oasisopen.xliff.om.v1;

/**
 * Represents the type of extension object.
 */
public enum ExtObjectItemType {

	/**
	 * Indicates a text content.
	 */
	TEXT,
	
	/**
	 * Indicates a set of instructions (e.g. a PI section in XML)
	 */
	INSTRUCTION,

	/**
	 * Indicates a content made of other extension objects. 
	 */
	OBJECT
}
