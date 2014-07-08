package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IAnnotation;
import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.TagType;

/**
 * Implements a closing {@link IAnnotation}. 
 * This is just a delegate of {@link StartAnnotation} where all the data is placed.
 */
public class EndAnnotation implements IAnnotation {

	final private StartAnnotation start;
	
	public EndAnnotation (StartAnnotation start) {
		this.start = start;
	}

	@Override
	public TagType getTagType () {
		return TagType.CLOSING;
	}

	@Override
	public String getId () {
		return start.getId();
	}

	@Override
	public void setId (String id) {
		start.setId(id);
	}

	@Override
	public String getType () {
		return start.getType();
	}

	@Override
	public void setType (String type) {
		start.setType(type);
	}

	@Override
	public boolean isCode () {
		return false;
	}

	@Override
	public boolean getTranslate () {
		return start.getTranslate();
	}

	@Override
	public void setTranslate (boolean translate) {
		start.setTranslate(translate);
	}

	@Override
	public String getValue () {
		return start.getValue();
	}

	@Override
	public void setValue (String value) {
		start.setValue(value);
	}

	@Override
	public String getRef () {
		return start.getRef();
	}

	@Override
	public void setRef (String ref) {
		start.setRef(ref);
	}

	@Override
	public boolean hasExtField () {
		return start.hasExtField();
	}

	@Override
	public IExtFields getExtFields () {
		return start.getExtFields();
	}

	@Override
	public String getNSUri () {
		return start.getNSUri();
	}

}
