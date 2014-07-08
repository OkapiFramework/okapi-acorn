package net.sf.okapi.acorn.xom;

import java.util.LinkedHashMap;
import java.util.Map;

import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.INotes;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

public class Group extends BaseData2 implements IGroup {

	private String id;
	private IGroup parent;
	private Map<String, IGroupOrUnit> map = new LinkedHashMap<>(2);
	
	/**
	 * Creates a new {@link IGroup} object with a given id and parent. 
	 * @param parent the parent of this new group node (use null for top-level groups).
	 * @param id the id for this group.
	 */
	public Group (IGroup parent,
		String id)
	{
		setId(id);
		this.parent = parent;
	}

	@Override
	public String getId () {
		return id;
	}

	@Override
	public void setId (String id) {
		if (( id == null ) || id.isEmpty() ) {
			throw new InvalidParameterException("Id cannot be null or empty.");
		}
		this.id = id;
	}
	
	@Override
	public boolean isUnit () {
		return false;
	}

	@Override
	public IGroup getParent () {
		return parent;
	}

	@Override
	public IGroup getGroup (String id) {
		// Try at this level
		IGroup item = (IGroup)map.get("g"+id);
		if ( item != null ) return item;
		// Else: try recursively
		for ( IGroupOrUnit gou : map.values() ) {
			if ( !gou.isUnit() ) {
				item = ((IGroup)gou).getGroup(id);
				if ( item != null ) return item;
			}
		}
		// Not found
		return null;
	}

	@Override
	public IGroup add (IGroup group) {
		map.put("g"+group.getId(), group);
		return group;
	}

	@Override
	public IUnit getUnit (String id) {
		// Try at this level
		IUnit item = (IUnit)map.get("u"+id);
		if ( item != null ) return item;
		// Else: try recursively
		for ( IGroupOrUnit gou : map.values() ) {
			if ( !gou.isUnit() ) {
				item = ((IGroup)gou).getUnit(id);
				if ( item != null ) return item;
			}
		}
		// Not found
		return null;
	}

	@Override
	public IUnit add (IUnit unit) {
		map.put("u"+unit.getId(), unit);
		return unit;
	}

	@Override
	public boolean hasNote () {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public INotes getNotes () {
		// TODO Auto-generated method stub
		return null;
	}

}
