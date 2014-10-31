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

package net.sf.okapi.acorn.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IExtField;
import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjectData;
import org.oasisopen.xliff.om.v1.IExtObjectItem;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.INote;
import org.oasisopen.xliff.om.v1.INotes;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithExtFields;
import org.oasisopen.xliff.om.v1.TargetState;

public class XLIFFWriter implements AutoCloseable {

	private PrintWriter pw = null;
	private String lb = "\n";
	private NSStack nsStack;
	
	public static void saveAs (IDocument doc,
		File outputFile)
		throws Exception
	{
		try ( XLIFFWriter writer = new XLIFFWriter() ) {
			writer.process(doc, outputFile);
		}
	}
	
	private void process (IDocument doc,
		File outputFile)
		throws FileNotFoundException
	{
		Writer wrt = new OutputStreamWriter(
			new BufferedOutputStream(new FileOutputStream(outputFile)), StandardCharsets.UTF_8);
		pw = new PrintWriter(wrt);

		nsStack = new NSStack();
		nsStack.add(null, Util.NS_XLIFF_CORE20);
		
		pw.print("<?xml version=\"1.0\"?>"+lb);
		pw.print("<xliff xmlns=\""+Util.NS_XLIFF_CORE20+"\" version=\""+doc.getVersion()+"\"");
		// Source language
		pw.print(" srcLang=\""+doc.getSourceLanguage()+"\"");
		// Target language
		pw.print(" trgLang=\""+doc.getTargetLanguage()+"\"");
		pw.print(">"+lb);
		// Files
		for ( IFile file : doc ) {
			process(file);
		}
		
		pw.write("</xliff>"+lb);
	}
	
	private void process (IFile file) {
		nsStack.pushLevel();
		pw.print("<file id=\""+Util.toXML(file.getId(), true)+"\"");
		// Extension attributes
		pw.print(outputExtFields(file));
		//TODO
		pw.print(">"+lb);
		// Skeleton
		//TODO
		// Extensions
		process(file.getExtObjects());
		//TODO
		// Notes
		process(file.getNotes());
		// Groups or units
		for ( IGroupOrUnit gou : file ) {
			if ( gou.isUnit() ) process((IUnit)gou);
			else process((IGroup)gou);
		}
		pw.print("</file>"+lb);
		nsStack.popLevel();
	}
	
	private void process (IUnit unit) {
		nsStack.pushLevel();
		pw.print("<unit id=\""+Util.toXML(unit.getId(), true)+"\"");
		//TODO: other attributes
		pw.print(">"+lb);
		// Extensions
		process(unit.getExtObjects());
		// Notes
		process(unit.getNotes());
		// Original data
		//TODO
		// Segments or ignorables
		for ( IPart part : unit ) {
			process(part);
		}
		pw.print("</unit>"+lb);
		nsStack.popLevel();
	}
	
	private void process (IGroup group) {
		nsStack.pushLevel();
		pw.print("<group id=\""+Util.toXML(group.getId(), true)+"\"");
		if ( group.getName() != null )
			pw.print(" name=\""+Util.toXML(group.getName(), true)+"\"");
		pw.print(">"+lb);
		// Extensions
		process(group.getExtObjects());
		// Notes
		process(group.getNotes());
		// Groups or units
		for ( IGroupOrUnit gou : group ) {
			if ( gou.isUnit() ) process((IUnit)gou);
			else process((IGroup)gou);
		}
		pw.print("</group>"+lb);
		nsStack.popLevel();
	}

	private void process (INotes notes) {
		if (( notes == null ) || notes.isEmpty() ) return;
		nsStack.pushLevel();
		pw.print("<notes>"+lb);
		for ( INote note : notes ) {
			process(note);
		}
		pw.print("</notes>"+lb);
		nsStack.popLevel();
	}
	
	private void process (INote note) {
		nsStack.pushLevel();
		pw.print("<note");
		if ( note.getId() != null )
			pw.print(" id=\""+Util.toXML(note.getId(), true)+"\"");
		if ( note.getCategory() != null )
			pw.print(" id=\""+Util.toXML(note.getCategory(), true)+"\"");
		if ( note.getPriority() != 1 )
			pw.print(" priority=\""+note.getPriority()+"\"");
		pw.print(">"+lb);
		pw.print(Util.toXML(note.getText(), false));
		pw.print("</note>"+lb);
		nsStack.popLevel();
	}

	private void process (IPart part) {
		String name = (part.isSegment() ? "segment" : "ignorable");
		ISegment seg = (part.isSegment() ? (ISegment)part : null);
		pw.print("<"+name);
		if ( part.getId() != null ) pw.print(" id=\""+Util.toXML(part.getId(), true)+"\"");
		if ( seg != null ) {
			pw.print(" canResegment=\""+(seg.getCanResegment() ? "yes" : "no")+"\"");
			if (( seg.getState() != TargetState.INITIAL ) || ( seg.getSubState() != null ))
				pw.print(" state=\""+seg.getState()+"\"");
			if ( seg.getSubState() != null )
				pw.print(" subState=\""+Util.toXML(seg.getSubState(), true)+"\"");
		}
		// Source
		pw.print(">"+lb+"<source");
		//TODO: xml:space
		pw.print(">");
		process(part.getSource());
		pw.print("</source>"+lb);
		// Target
		if ( part.hasTarget() ) {
			pw.print("<target");
			//TODO: xml:space
			pw.print(">");
			process(part.getTarget());
			pw.print("</target>"+lb);
		}
		pw.print("</"+name+">"+lb);
	}
	
//	private boolean hasWellFormedClosing(List<ITag> tags,
//		ITag opening)
//	{
//		// Get the position of the opening tag
//		int i = 0;
//		while ( tags.get(i) != opening ) {
//			if ( ++i == tags.size() ) return false; // Opening tag not found
//		}
//		// Else: i is the position of the opening
//		Stack<String> stack = new Stack<>();
//		for ( i=i+1; i<tags.size(); i++ ) {
//			String id = tags.get(i).getId();
//			switch ( tags.get(i).getTagType() ) {
//			case OPENING:
//				stack.push(id);
//				break;
//			case CLOSING:
//				
//				if ( stack.isEmpty() || stack.peek().equals(id) ) {
//					// Not in a stack order: not well-formed
//					return false;
//				}
//				String last = stack.pop(); // Else: pop it
//				if ( stack.isEmpty() ) {
//					if ( last.equals(opening.getId()) ) return true;
//					else return false;
//				}
//				break;
//			case STANDALONE:
//				// Nothing to do
//				break;
//			}
//		}
//		return false; // Not found
//	}
	
	private void process (IContent content) {
		pw.print(toXLIFF(content));
	}
	
	public String toXLIFF (IContent content) {
		Map<ITag, Integer> statusMap = content.getOwnTagsStatus();
		StringBuilder sb = new StringBuilder();
		// Process the content
		for ( Object obj : content ) {
			if ( obj instanceof String ) {
				sb.append(Util.toSafeXML((String)obj));
			}
			else if ( obj instanceof ICTag ) {
				ICTag ctag = (ICTag)obj;
				Integer status = statusMap.get(ctag);
				switch ( ctag.getTagType() ) {
				case OPENING:
					if ( status == 2 ) {
						sb.append("<pc id=\""+ctag.getId()+"\"");
						if ( ctag.getCanOverlap() ) sb.append(" canOverlap=\"yes\"");
					}
					else {
						sb.append("<sc id=\""+ctag.getId()+"\"");
						if ( !ctag.getCanOverlap() ) sb.append(" canOverlap=\"no\"");
						if ( status == 0 ) sb.append(" isolated=\"yes\"");
					}
					//TODO: common attributes
					if ( status == 2 ) sb.append(">");
					else sb.append("/>");
					break;
				case CLOSING:
					if ( status == 2 ) {
						sb.append("</pc>");
					}
					else {
						sb.append("<ec "+(status==0 ? "id" : "startRef")+"=\""+ctag.getId()+"\"");
						if ( !ctag.getCanOverlap() ) sb.append(" canOverlap=\"no\"");
						if ( status == 0 ) sb.append(" isolated=\"yes\"");
						//TODO: common attributes
						sb.append("/>");
					}
					break;
				case STANDALONE:
					sb.append("<ph id=\""+ctag.getId()+"\"");
					//TODO: common attributes
					sb.append("/>");
					break;
				}
			}
			else if ( obj instanceof IMTag ) {
				IMTag mtag = (IMTag)obj;
				int status = statusMap.get(mtag);
				switch ( mtag.getTagType() ) {
				case OPENING:
					if ( status == 2 ) {
						nsStack.pushLevel();
						sb.append("<mrk id=\""+mtag.getId()+"\"");
					}
					else {
						nsStack.pushLevel();
						sb.append("<sm id=\""+mtag.getId()+"\"");
					}
					if ( !mtag.getType().equals("generic") )
						sb.append(" type=\""+mtag.getType()+"\"");
					if ( mtag.getValue() != null )
						sb.append(" value=\""+Util.toXML(mtag.getValue(), true)+"\"");
					if ( mtag.getRef() != null ) 
						sb.append(" ref=\""+Util.toXML(mtag.getRef(), true)+"\"");
					// Extension attributes
					sb.append(outputExtFields(mtag));
					//TODO
					if ( status == 2 ) sb.append(">");
					else {
						sb.append("/>");
						nsStack.popLevel();
					}
					break;
				case CLOSING:
					if ( status == 2 ) {
						sb.append("</mrk>");
						nsStack.popLevel();
					}
					else {
						sb.append("<em startRef=\""+mtag.getId()+"\"/>");
					}
					break;
				case STANDALONE:
					// Nothing to do for markers
					break;
				}
			}
			else {
				sb.append("[ERR:"+obj.getClass().getName()+"]");
			}
		}
		return sb.toString();
	}
	
	private void process (IExtObjects extObjs) {
		if (( extObjs == null ) || extObjs.isEmpty() ) return;
		for ( IExtObject extObj : extObjs ) {
			nsStack.pushLevel();
			outputStartTag(extObj);
			process(extObj.getItems());
			pw.print("</"+nsStack.getPrefix(extObj.getNSUri())+":"+extObj.getName()+">");
			nsStack.popLevel();
		}
	}
	
	private void process (List<IExtObjectItem> items) {
		for ( IExtObjectItem item : items ) {
			switch ( item.getType() ) {
			case INSTRUCTION:
				pw.print("<?"+((IExtObjectData)item).getContent()+"?>");
				break;
			case OBJECT:
				IExtObject eo = (IExtObject)item;
				nsStack.pushLevel();
				outputStartTag(eo);
				process(((IExtObject)item).getItems());
				pw.print("</"+nsStack.getPrefix(eo.getNSUri())+":"+eo.getName()+">");
				nsStack.popLevel();
				break;
			case TEXT:
				IExtObjectData eod = ((IExtObjectData)item);
				if ( eod.getRaw() ) {
					pw.print("<![CDATA["+eod.getContent()+"]]>");
				}
				else {
					pw.print(Util.toXML(eod.getContent(), false));
				}
				break;
			}
		}
	}
	
	private void outputStartTag (IExtObject extObj) {
		String eoUri = extObj.getNSUri();
		String ep = nsStack.getPrefix(eoUri);
		String decl = null;
		if ( ep == null ) {
			ep = getPreferredPrefix(eoUri);
			ep = nsStack.add(ep, eoUri);
			decl = " xmlns:"+ep+"=\""+eoUri+"\"";
		}
		pw.print("<"+ep+":"+extObj.getName());
		if ( decl != null ) pw.print(decl);
		pw.print(outputExtFields(extObj));
		pw.print(">");
	}
	
	private String outputExtFields (IWithExtFields parent) {
		if ( !parent.hasExtField() ) return "";
		StringBuilder tmp = new StringBuilder();
		IExtFields efs = parent.getExtFields();
		// Namespaces
		for ( String uri : efs.getNSUris() ) {
			if ( !nsStack.exists(uri) ) {
				String prefix = efs.getNSShorthand(uri);
				prefix = nsStack.add(prefix, uri);
				tmp.append(" xmlns:"+prefix+"=\""+uri+"\"");
			}
		}
		// Fields
		for ( IExtField ef : efs ) {
			String uri = ef.getNSUri();
			String ap = "";
			if ( !uri.equals(parent.getNSUri()) ) {
				ap = nsStack.getPrefix(uri);
				if ( !Util.isNoE(ap) ) ap = ap+":";
			}
			tmp.append(" "+ap+ef.getName()+"=\""+Util.toXML(ef.getValue(), true)+"\"");
		}
		return tmp.toString();
	}

	private String getPreferredPrefix (String uri) {
		switch ( uri ) {
		case Util.NS_XLIFF_CORE20:
			return "x";
		case Util.NS_XLIFF20_GLOSSARY:
			return "gls";
		default:
			return "x1";
		}
	}
	
	@Override
	public void close () 
		throws Exception
	{
		if ( pw != null ) {
			pw.close();
			pw = null;
		}
	}

}
