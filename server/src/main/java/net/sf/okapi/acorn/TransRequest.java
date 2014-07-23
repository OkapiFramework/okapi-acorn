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

package net.sf.okapi.acorn;

import java.util.Date;

public class TransRequest {

	private final String id;
	private String status;
	private int updateCounter;
	private String callbackUrl;
	private String srcLang;
	private String trgLang;
	private String source;
	private String target;
	private boolean mt;
	private boolean crowd;
	private boolean prof;
	private boolean postEdit;
	private String comment;
	private String owner;
	private String translator;
	private String creDate;
	private String modDate;

	public TransRequest (String id) {
		this.id = id;
		this.creDate = DataStore.formatDate(new Date());
	}
	
	public String getId () {
		return id;
	}
	
	public String getSource () {
		return source;
	}
	
	public void setSource (String source) {
		this.source = source;
	}
	
	public int getUpdateCounter () {
		return updateCounter;
	}
	
	public void setUpdateCounter (int updateCounter) {
		this.updateCounter = updateCounter;
	}
	
	public String getSourceLang () {
		return srcLang;
	}

	public void setSourceLang (String srcLang) {
		this.srcLang = srcLang;
	}

	public String getTargetLang () {
		return trgLang;
	}

	public void setTargetLang (String trgLang) {
		this.trgLang = trgLang;
	}

	public String getTarget () {
		return target;
	}

	public void setTarget (String target) {
		this.target = target;
	}

	public String getStatus () {
		return status;
	}

	public void setStatus (String status) {
		this.status = status;
	}
	
	public String getCallbackUrl () {
		return callbackUrl;
	}

	public void setCallbackUrl (String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public boolean getMt () {
		return mt;
	}

	public void setMt (boolean mt) {
		this.mt = mt;
	}

	public boolean getCrowd () {
		return crowd;
	}

	public void setCrowd (boolean crowd) {
		this.crowd = crowd;
	}

	public boolean getProfessional () {
		return prof;
	}

	public void setProfessional (boolean professional) {
		this.prof = professional;
	}

	public boolean getPostEdit () {
		return postEdit;
	}

	public void setPostEdit (boolean postEdit) {
		this.postEdit = postEdit;
	}

	public String getComment () {
		return comment;
	}

	public void setComment (String comment) {
		this.comment = comment;
	}

	public String getOwner () {
		return owner;
	}

	public void setOwner (String owner) {
		this.owner = owner;
	}

	public String getTranslator () {
		return translator;
	}

	public void setTranslator (String translator) {
		this.translator = translator;
	}

	public String getCreationDate () {
		return creDate;
	}

	public void setCreationDate (String creationDate) {
		this.creDate = creationDate;
	}

	public String getModificationDate () {
		return modDate;
	}

	public void setModificationDate (String modificationDate) {
		this.modDate = modificationDate;
	}

	/**
	 * Updates the update counter and modification date data that should be updated
	 * when a change is made to this request.
	 */
	public void stamp () {
		updateCounter++;
		modDate = DataStore.formatDate(new Date());
	}

	public String toJSON () {
		StringBuilder tmp = new StringBuilder();
		tmp.append("{\"translationRequest\":{");
		tmp.append("\"id\":\""+id+"\",");
		if ( callbackUrl != null ) tmp.append("\"callbackURL\":\""+DataStore.esc(callbackUrl)+"\"");
		tmp.append("\"sourceLanguage\":\""+srcLang+"\",");
		tmp.append("\"targetLanguage\":\""+trgLang+"\",");
		tmp.append("\"mt\":"+(mt?"true":"false")+",");
		tmp.append("\"crowd\":"+(crowd?"true":"false")+",");
		tmp.append("\"professional\":"+(prof?"true":"false")+",");
		tmp.append("\"postedit\":"+(postEdit?"true":"false")+",");
		if ( comment != null ) tmp.append("\"owner\":\""+DataStore.esc(owner)+"\",");
		if ( owner != null ) tmp.append("\"translator\":\""+DataStore.esc(translator)+"\",");
		if ( translator != null ) tmp.append("\"comment\":\""+DataStore.esc(comment)+"\",");
		tmp.append("\"creationDatetime\":\""+creDate+"\",");
		if ( modDate != null ) tmp.append("\"modificationDatetime\":\""+modDate+"\",");
		tmp.append("\"updateCounter\":"+updateCounter+",");
		tmp.append("\"status\":\""+status+"\"");
		tmp.append("}}");
		return tmp.toString();
	}
}
