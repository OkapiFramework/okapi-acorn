package org.oasisopen.xliff.om.v1;

/**
 * Represents the common interface of an {@link IExtObject} and an {@link IExtObjectData}.
 */
public interface IExtObjectItem {

	/**
	 * Gets the type of content for this extension item.
	 * @return the type of this extension item.
	 */
	public ExtObjectItemType getType ();

}
