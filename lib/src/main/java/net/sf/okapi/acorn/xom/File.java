package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IFile;

/**
 * Implements the {@link IFile} interface.
 */
public class File extends BaseData4 implements IFile {

	/**
	 * Creates a new {@link Unit} object.
	 * @param id the id of the unit.
	 */
	File (String id) {
		setId(id);
	}

}
