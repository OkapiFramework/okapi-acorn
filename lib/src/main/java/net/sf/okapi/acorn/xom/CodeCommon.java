package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;

class CodeCommon {

	private static final int CANCOPY = 0x01;
	private static final int CANDELETE = 0x02;

	private String id;
	private String type;
	private int hints = (CANCOPY | CANDELETE);
	private boolean canOverlap;
	private String subType;
	private CanReorder canReorder = CanReorder.YES;
	private String copyOf;
	private Direction dir = Direction.INHERITED;
	
	CodeCommon (String id) {
		this.id = id;
	}
	
	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}

	public String getType () {
		return type;
	}

	public void setType (String type) {
		this.type = type;
	}

	public String getSubType () {
		return subType;
	}

	public void setSubType (String subType) {
		this.subType = subType;
	}

	public boolean getCanCopy () {
		return (( hints & CANCOPY ) == CANCOPY);
	}

	public void setCanCopy (boolean canCopy) {
		if ( canCopy ) hints |= CANCOPY;
		else hints &= ~CANCOPY;
	}

	public boolean getCanDelete () {
		return (( hints & CANDELETE ) == CANDELETE);
	}

	public void setCanDelete (boolean canDelete) {
		if ( canDelete ) hints |= CANDELETE;
		else hints &= ~CANDELETE;
	}

	public boolean getCanOverlap () {
		return canOverlap;
	}

	public void setCanOverlap (boolean canOverlap) {
		this.canOverlap = canOverlap;
	}

	public CanReorder getCanReorder () {
		return canReorder;
	}

	public void setCanReorder (CanReorder canReorder) {
		this.canReorder = canReorder;
		if ( canReorder != CanReorder.YES ) {
			setCanDelete(false);
			setCanCopy(false);
		}
	}

	public String getCopyOf () {
		return copyOf;
	}

	public void setCopyOf (String copyOf) {
		this.copyOf = copyOf;
	}

	public Direction getDir () {
		return dir;
	}

	public void setDir (Direction dir) {
		this.dir = dir;
	}

}
