package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.ICode;
import org.oasisopen.xliff.om.v1.TagType;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class BaseCode implements ICode {

	protected CodeCommon cm = null;

	private final TagType tagType;
	private String data;
	private String disp;
	private String equiv = "";
	private String subFlows;
	private Direction dataDir = Direction.AUTO;

	protected BaseCode (CodeCommon cm,
		TagType tagType,
		String id,
		String data)
	{
		if ( cm == null ) this.cm = new CodeCommon(id);
		else this.cm = cm;
		this.tagType = tagType;
		this.data = data;
	}
	
	@Override
	public boolean isCode () {
		return true;
	}
	
	@Override
	public TagType getTagType () {
		return tagType;
	}

	@Override
	public String getId () {
		return cm.getId();
	}

	@Override
	public void setId (String id) {
		cm.setId(id);
	}

	@Override
	public String getType () {
		return cm.getType();
	}

	@Override
	public void setType (String type) {
		cm.setType(type);
	}

	@Override
	@JsonProperty("subt")
	public String getSubType () {
		return cm.getSubType();
	}

	@Override
	public void setSubType (String subType) {
		cm.setSubType(subType);
	}

	@Override
	@JsonProperty("canc")
	public boolean getCanCopy () {
		return cm.getCanCopy();
	}

	@Override
	public void setCanCopy (boolean canCopy) {
		cm.setCanCopy(canCopy);
	}

	@Override
	public boolean getCanOverlap () {
		return cm.getCanOverlap();
	}

	@Override
	public void setCanOverlap (boolean canOverlap) {
		cm.setCanOverlap(canOverlap);
	}

	@Override
	public boolean getCanDelete () {
		return cm.getCanDelete();
	}

	@Override
	public void setCanDelete (boolean canDelete) {
		cm.setCanDelete(canDelete);
	}

	@Override
	public CanReorder getCanReorder () {
		return cm.getCanReorder();
	}

	@Override
	public void setCanReorder (CanReorder canReorder) {
		cm.setCanReorder(canReorder);
	}

	@Override
	public String getCopyOf () {
		return cm.getCopyOf();
	}

	@Override
	public void setCopyOf (String id) {
		cm.setCopyOf(id);
	}

	@Override
	public String getData () {
		return data;
	}

	@Override
	public void setData (String data) {
		this.data = data;
	}

	@Override
	public Direction getDataDir () {
		return dataDir;
	}

	@Override
	public void setDataDir (Direction dir) {
		this.dataDir = dir;
	}

	@Override
	public String getDisp () {
		return disp;
	}

	@Override
	public void setDisp (String disp) {
		this.disp = disp;
	}

	@Override
	public String getEquiv () {
		return equiv;
	}

	@Override
	public void setEquiv (String equiv) {
		if ( equiv == null ) this.equiv = "";
		this.equiv = equiv;
	}

	@Override
	public String getSubFlows () {
		return subFlows;
	}

	@Override
	public void setSubFlows (String subFlows) {
		this.subFlows = subFlows;
	}

	@Override
	public Direction getDir () {
		return cm.getDir();
	}

	@Override
	public void setDir (Direction dir) {
		cm.setDir(dir);
	}

}
