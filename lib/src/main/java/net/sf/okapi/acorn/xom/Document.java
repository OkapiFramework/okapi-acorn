package net.sf.okapi.acorn.xom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

/**
 * Implements the {@link IDocument} interface.
 */
public class Document implements IDocument {

	private String version = "2.0";
	private String srcLang = "en";
	private String trgLang;
	private Map<String, IFile> files = new HashMap<>(1);
	
	@Override
	public IFile getFile (String id) {
		return files.get(id);
	}

	@Override
	public int getFileCount () {
		return files.size();
	}

	@Override
	public boolean supports (String moduleUri) {
		// No modules are supported directly for now.
		return false;
	}

	@Override
	public String getVersion () {
		return version;
	}

	@Override
	public String getSourceLanguage () {
		return srcLang;
	}

	@Override
	public void setSourceLanguage (String srcLang) {
		this.srcLang = srcLang;
	}

	@Override
	public String getTargetLanguage () {
		return trgLang;
	}

	@Override
	public void setTargetLanguage (String trgLang) {
		this.trgLang = trgLang;
	}

	@Override
	public IFile add (IFile file) {
		if ( files.containsKey(file.getId()) ) {
			throw new InvalidParameterException(String.format(
				"There is aleardy a file with id='%s' in the document", file.getId()));
		}
		files.put(file.getId(), file);
		return file;
	}

	@Override
	public void remove (String id) {
		files.remove(id);
	}

	@Override
	public Iterator<IFile> iterator () {
		return files.values().iterator();
	}
	
}
