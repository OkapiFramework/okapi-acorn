/*===========================================================================
  Copyright (C) 2014 by the Okapi Framework contributors
-----------------------------------------------------------------------------
  This library is free software; you can redistribute it and/or modify it 
  under the terms of the GNU Lesser General Public License as published by 
  the Free Software Foundation; either version 2.1 of the License, or (at 
  your option) any later version.

  This library is distributed in the hope that it will be useful, but 
  WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
  General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License 
  along with this library; if not, write to the Free Software Foundation, 
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  See also the full LGPL text here: http://www.gnu.org/copyleft/lesser.html
===========================================================================*/

package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.TagType;

abstract class Tag implements ICTag {

	protected CTagCommon cc = null;

	private final TagType tagType;
	private String data;
	private String disp;
	private String equiv = "";
	private String subFlows;
	private Direction dataDir = Direction.AUTO;

	Tag (CTagCommon cc,
		TagType tagType,
		String id,
		String data)
	{
		if ( cc == null ) this.cc = new CTagCommon(id);
		else this.cc = cc;
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
		return cc.getId();
	}

	@Override
	public void setId (String id) {
		cc.setId(id);
	}

	@Override
	public String getType () {
		return cc.getType();
	}

	@Override
	public void setType (String type) {
		cc.setType(type);
	}

	@Override
	public String getSubType () {
		return cc.getSubType();
	}

	@Override
	public void setSubType (String subType) {
		cc.setSubType(subType);
	}

	@Override
	public boolean getCanCopy () {
		return cc.getCanCopy();
	}

	@Override
	public void setCanCopy (boolean canCopy) {
		cc.setCanCopy(canCopy);
	}

	@Override
	public boolean getCanOverlap () {
		return cc.getCanOverlap();
	}

	@Override
	public void setCanOverlap (boolean canOverlap) {
		cc.setCanOverlap(canOverlap);
	}

	@Override
	public boolean getCanDelete () {
		return cc.getCanDelete();
	}

	@Override
	public void setCanDelete (boolean canDelete) {
		cc.setCanDelete(canDelete);
	}

	@Override
	public CanReorder getCanReorder () {
		return cc.getCanReorder();
	}

	@Override
	public void setCanReorder (CanReorder canReorder) {
		cc.setCanReorder(canReorder);
	}

	@Override
	public String getCopyOf () {
		return cc.getCopyOf();
	}

	@Override
	public void setCopyOf (String id) {
		cc.setCopyOf(id);
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
		return cc.getDir();
	}

	@Override
	public void setDir (Direction dir) {
		cc.setDir(dir);
	}

}
