package net.sf.okapi.acorn.xom;

import java.util.LinkedHashMap;
import java.util.Map;

import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

/**
 * Implements the {@link IFile} interface.
 */
public class File extends BaseData3 implements IFile {

	private String id;
	private Map<String, IGroupOrUnit> map = new LinkedHashMap<>(2);

	/**
	 * Creates a new {@link Unit} object.
	 * @param id the id of the unit.
	 */
	public File (String id) {
		setId(id);
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

}
