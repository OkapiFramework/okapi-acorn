package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IWithExtFields;

class MTagCommon implements IWithExtFields {

	private String id;
	private String type;
	private String value;
	private String ref;
	private boolean translate;
	private IExtFields xFields;
	
	MTagCommon (String id,
		String type)
	{
		this.id = id;
		this.type = type;
	}

	MTagCommon (IMTag original) {
		this.id = original.getId();
		this.type = original.getType();
		this.value = original.getValue();
		this.ref = original.getRef();
		this.translate = original.getTranslate();
		if ( original.hasExtField() ) {
			xFields = new ExtFields(this, original.getExtFields());
		}
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

	public boolean getTranslate () {
		return translate;
	}

	public void setTranslate (boolean translate) {
		this.translate = translate;
	}

	public String getValue () {
		return value;
	}

	public void setValue (String value) {
		this.value = value;
	}

	public String getRef () {
		return ref;
	}

	public void setRef (String ref) {
		this.ref = ref;
	}

	@Override
	public boolean hasExtField () {
		if ( xFields == null ) return false;
		return !xFields.isEmpty();
	}

	@Override
	public IExtFields getExtFields () {
		if ( xFields == null ) xFields = new ExtFields(this);
		return xFields;
	}

	@Override
	public String getNSUri () {
		return Const.NS_XLIFF_CORE20;
	}

}
