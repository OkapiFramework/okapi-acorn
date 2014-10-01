package net.sf.okapi.acorn.xom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IWithExtObjects;

/**
 * Implements the {@link IExtObjects} interface.
 */
public class ExtObjects extends BaseData1 implements IExtObjects {

	private IWithExtObjects parent;
	private ArrayList<IExtObject> list; 
	
	public ExtObjects (IWithExtObjects parent) {
		this.parent = parent;
	}
	
	@Override
	public Iterator<IExtObject> iterator () {
		if ( list == null ) list = new ArrayList<>(2);
		return list.iterator();
	}

	@Override
	public IWithExtObjects getParent () {
		return parent;
	}

	@Override
	public IExtObject add (String nsUri,
		String name)
	{
		if ( list == null ) list = new ArrayList<>();
		IExtObject xo = new ExtObject(nsUri, name);
		list.add(xo);
		return xo;
	}

	@Override
	public List<IExtObject> find (String nsUri,
		String name)
	{
		if ( list == null ) return Collections.emptyList();
		ArrayList<IExtObject> res = new ArrayList<>();
		for ( IExtObject obj : list ) {
			if ( obj.getNSUri().equals(nsUri)
				&& obj.getName().equals(name) ) {
				res.add(obj);
			}
		}
		return res;
	}

	@Override
	public IExtObject getOrCreate (String nsUri,
		String name)
	{
		if ( list == null ) list = new ArrayList<>();
		for ( IExtObject obj : list ) {
			if ( obj.getNSUri().equals(nsUri)
				&& obj.getName().equals(name) ) {
				return obj; // Found
			}
		}
		// None found, create one
		return add(nsUri, name);
	}

	@Override
	public boolean isEmpty () {
		if ( list == null ) return true;
		return list.isEmpty();
	}

	@Override
	public void clear () {
		list = null;
	}

	@Override
	public void delete (IExtObject object) {
		if ( list != null ) {
			list.remove(object);
		}
	}

	@Override
	public void setNS (String nsUri,
		String nsShorthand)
	{
		getExtFields().setNS(nsUri, nsShorthand);
	}

}
