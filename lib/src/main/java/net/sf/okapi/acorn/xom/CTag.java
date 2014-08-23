package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.InvalidParameterException;
import org.oasisopen.xliff.om.v1.TagType;

public class CTag implements ICTag {

	protected CodeCommon cm = null;

	private final TagType tagType;
	private String data;
	private String disp;
	private String equiv = "";
	private String subFlows;
	private Direction dataDir = Direction.AUTO;

	CTag (CodeCommon cm,
		TagType tagType,
		String id,
		String data)
	{
		if ( cm == null ) this.cm = new CodeCommon(id);
		else this.cm = cm;
		this.tagType = tagType;
		this.data = data;
	}
	
	/**
	 * Creates a new code tag (without any link to another tag).
	 * @param tagType the tag type.
	 * @param id the id (should not be null).
	 * @param data the data (can be null).
	 */
	CTag (TagType tagType,
		String id,
		String data)
	{
		this(null, tagType, id, data);
	}

	CTag (CTag opposite,
		String data)
	{
		switch ( opposite.tagType ) {
		case CLOSING:
			this.tagType = TagType.OPENING;
			break;
		case OPENING:
			this.tagType = TagType.CLOSING;
			break;
		case STANDALONE:
		default:
			throw new InvalidParameterException("Counterpart must be an opening or closing tag.");
		}
		this.cm = opposite.cm;
		this.data = data;
	}
	
	/**
	 * Copy constructor.
	 * @param original the original object to copy.
	 * @param opposite the opening/closing tag to connect with this new tag.
	 * this parameter must be created already. 
	 */
	CTag (ICTag original,
		CTag opposite)
	{
		if ( opposite == null ) this.cm = new CodeCommon(original);
		else this.cm = opposite.cm;

		this.tagType = original.getTagType();
		this.data = original.getData();
		this.dataDir = original.getDataDir();
//		this.dataRef = original.dataRef;
//		this.initialWithData = original.initialWithData;
//		this.canReorder = original.canReorder;
		this.disp = original.getDisp();
		this.equiv = original.getEquiv();
		this.subFlows = original.getSubFlows();
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
	public String getSubType () {
		return cm.getSubType();
	}

	@Override
	public void setSubType (String subType) {
		cm.setSubType(subType);
	}

	@Override
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
