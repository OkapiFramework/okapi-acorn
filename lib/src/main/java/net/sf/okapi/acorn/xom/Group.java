package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

public class Group extends BaseData4 implements IGroup {

	private String name;
	private IGroup parent;
	
	/**
	 * Creates a new {@link IGroup} object with a given id and parent. 
	 * @param parent the parent of this new group node (use null for top-level groups).
	 * @param id the id for this group.
	 * @throws InvalidParameterException if the id is invalid.
	 */
	public Group (IGroup parent,
		String id)
	{
		setId(id);
		this.parent = parent;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setName (String name) {
		this.name = name;
	}

	@Override
	public boolean isUnit () {
		return false;
	}

	@Override
	public IGroup getParent () {
		return parent;
	}

}
