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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("reject")
public class Reject {
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
	public Response reject_id_PUT (
    	@PathParam("id") String id
		)
	{
		if (( id == null ) || id.trim().isEmpty() ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID must not be null or empty.");
		}
		TransRequest treq = DataStore.getInstance().get(id);
		if ( treq == null ) {
			return ErrorResponse.create(Response.Status.NOT_FOUND, id,
				String.format("ID '%s' does not exists.", id));
		}
		treq.setStatus(TransRequest.STATUS_REJECTED);
		treq.stamp();

		return Response.ok(treq.toJSON(), MediaType.APPLICATION_JSON).build();
	}

}
