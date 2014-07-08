package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IExtField;

/**
 * Implements the {@link IExtField} interface.
 */
public class ExtField implements IExtField {

	private String nsUri;
	private String name;
	private String value;

	public ExtField (String namespaceUri,
		String name,
		String value)
	{
		this.nsUri = namespaceUri;
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getNSUri () {
		return nsUri;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public String getValue () {
		return value;
	}

	@Override
	public void setValue (String value) {
		this.value = value;
	}

	@Override
	public boolean isModule () {
		return nsUri.startsWith(Const.NS_XLIFF_MODSTART);
	}

}
