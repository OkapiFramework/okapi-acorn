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

package net.sf.okapi.acorn.jsonaccess;

import java.io.File;
import java.io.FileInputStream;

import net.sf.okapi.acorn.common.IDocumentReader;
import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.common.LocaleId;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class JsonReader implements IDocumentReader {

	private static final IXLIFFFactory xf = Factory.XOM;

	private LocaleId srcLoc = LocaleId.ENGLISH;
	private LocaleId trgLoc = LocaleId.FRENCH;

	@Override
	public IDocument load (File inputFile) {
		IDocument doc = null; 
		JSONAccess ja = new JSONAccess();
		try ( FileInputStream fis = new FileInputStream(inputFile) ) {
			ja.read(fis);
			doc = xf.createDocument();
			doc.setSourceLanguage(srcLoc.toString());
			doc.setTargetLanguage(trgLoc.toString());
			IFile file = doc.add(xf.createFile("f1"));
			while ( ja.hasNext() ) {
				String text = ja.next();
				IUnit unit = xf.createUnit(ja.getEntryPath());
				unit.appendSegment().setSource(text);
				file.add(unit);
			}
		}
		catch (Throwable  e) {
			throw new RuntimeException("Error reading document.", e);
		}
		return doc;
	}

}
