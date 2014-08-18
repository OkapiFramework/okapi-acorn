package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.ExtObjectItemType;
import org.oasisopen.xliff.om.v1.IExtObjectData;

/**
 * Implements the {@link IExtObjectData} interface.
 */
public class ExtObjectData implements IExtObjectData {

	private String content;
	private boolean raw;

	/**
	 * Creates a new {@link IExtObjectData} object with a given content.
	 * @param content the content of the object item.
	 */
	public ExtObjectData (String content) {
		this.content = content;
	}
	
	@Override
	public ExtObjectItemType getType () {
		return ExtObjectItemType.TEXT;
	}

	@Override
	public String getContent () {
		return content;
	}

	@Override
	public void setContent (String content) {
		this.content = content;
	}

	@Override
	public boolean getRaw () {
		return raw;
	}

	@Override
	public void setRaw (boolean raw) {
		this.raw = raw;
	}

}
