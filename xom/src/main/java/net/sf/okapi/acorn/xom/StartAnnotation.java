package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IAnnotation;
import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.TagType;

public class StartAnnotation implements IAnnotation {

	private String id;
	private String type;
	private String value;
	private String ref;
	private boolean translate;
	private IExtFields xFields;
	
	public StartAnnotation (String id,
		String type)
	{
		this.id = id;
		this.type = type;
	}

	@Override
	public TagType getTagType () {
		return TagType.OPENING;
	}

	@Override
	public String getId () {
		return id;
	}

	@Override
	public void setId (String id) {
		this.id = id;
	}

	@Override
	public String getType () {
		return type;
	}

	@Override
	public void setType (String type) {
		this.type = type;
	}

	@Override
	public boolean isCode () {
		return false;
	}

	@Override
	public boolean getTranslate () {
		return translate;
	}

	@Override
	public void setTranslate (boolean translate) {
		this.translate = translate;
	}

	@Override
	public String getValue () {
		return value;
	}

	@Override
	public void setValue (String value) {
		this.value = value;
	}

	@Override
	public String getRef () {
		return ref;
	}

	@Override
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
