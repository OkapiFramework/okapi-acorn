package net.sf.okapi.acorn.xom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.oasisopen.xliff.om.v1.ExtObjectItemType;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjectData;
import org.oasisopen.xliff.om.v1.IExtObjectItem;

/**
 * Implements the {@link IExtObject} interface.
 */
public class ExtObject extends BaseData1 implements IExtObject {

	private QName qname;
	private List<IExtObjectItem> items;

	/**
	 * Creates a new {@link IExtObject} object.
	 * @param nsUri the namespace URI of this object.
	 * @param name the name of this object.
	 */
	public ExtObject (String nsUri,
		String name)
	{
		this.qname = new QName(nsUri, name);
	}
	
	@Override
	public String getNSUri () {
		return qname.getNamespaceURI();
	}

	@Override
	public String getName () {
		return qname.getLocalPart();
	}

	@Override
	public boolean isModule () {
		return qname.getNamespaceURI().startsWith(Const.NS_XLIFF_MODSTART);
	}

	@Override
	public ExtObjectItemType getType () {
		return ExtObjectItemType.OBJECT;
	}

	@Override
	public boolean isEmpty () {
		if ( items == null ) return false;
		return items.isEmpty();
	}

	@Override
	public List<IExtObjectItem> getItems () {
		if ( items == null ) items = new ArrayList<IExtObjectItem>(2);
		return items;
	}

	@Override
	public IExtObjectData add (String content,
		boolean raw)
	{
		IExtObjectData item = new ExtObjectData(content);
		item.setRaw(raw);
		items.add(item);
		return item;
	}

}
