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

package org.oasisopen.xliff.om.v1;

/**
 * The abstract representation of a native code. Most of the time inline codes are coming directly from markup 
 * in the original document that was extracted.
 * <p><b>IMPORTANT</b>: A number of fields are shared by the opening and closing tags, implementations must ensure
 * that both objects have always the same values for those fields (i.e: when you change a value in one, it is
 * automatically changed in the other as well.
 */
public interface ICTag extends ITag {

	/**
	 * Gets the sub-type for this code.
	 * @return the sub-type for this code.
	 */
	public String getSubType ();
	
	/**
	 * Sets the sub-type for this code.
	 * @param subType the new sub-type.
	 */
	public void setSubType (String subType);

	/**
	 * Indicates if this code can be copied.
	 * @return true if this code can be copied, false it it must not be copied.
	 */
	public boolean getCanCopy ();
	
	/**
	 * Sets the flag indicating if this code can be copied.
	 * @param canCopy true to allow this code to be copied, false otherwise.
	 */
	public void setCanCopy (boolean canCopy);
	
	/**
	 * Indicates if this code can overlap another.
	 * @return true if this code can overlap another.
	 */
	public boolean getCanOverlap ();
	
	/**
	 * Sets the flag indicating if this code can overlap another.
	 * @param canOverlap true to allow this code to overlap another, false otherwise.
	 */
	public void setCanOverlap (boolean canOverlap);
	
	public boolean getCanDelete ();
	
	public void setCanDelete (boolean canDelete);
	
	/**
	 * Indicates if this code can be re-ordered or not.
	 * @return one of the {@link CanReorder} values.
	 */
	public CanReorder getCanReorder ();
	
	/**
	 * Sets the flag indicating if this code can be re-ordered or not.
	 * @param canReorder the new value to set.
	 */
	public void setCanReorder (CanReorder canReorder);
	
	public String getCopyOf ();
	
	public void setCopyOf (String id);
	
	/**
	 * Gets the directionality of the content of this code.
	 * This method is not applicable to standalone codes and must return null for a standalone code.
	 * @return the directionality of the content of this code.
	 */
	public Direction getDir ();

	/**
	 * Sets the directionality of the content of this code.
	 * This method is not applicable to standalone codes and should have no effect when called on a standalone code.
	 * @param dir the new directionality.
	 */
	public void setDir (Direction dir);


	//===============================================================
	// Methods for which opening and ending codes have their own data
	//===============================================================
	
	/**
	 * Gets the original data for this code.
	 * @return the original data for this code (can be null).
	 */
	public String getData ();
	
	/**
	 * Sets the original data for this code.
	 * @param data the new data to set (can be null).
	 */
	public void setData (String data);
	
	/**
	 * Gets the directionality for the original data.
	 * @return the directionality for the original data.
	 */
	public Direction getDataDir ();
	
	/**
	 * Sets the directionality for the original data.
	 * @param dir the new directionality.
	 */
	public void setDataDir (Direction dir);
	
	public String getDisp ();
	
	public void setDisp (String disp);

	/**
	 * Gets the text equivalent string for this code.
	 * @return the text equivalent string for this code (never null but can be empty).
	 */
	public String getEquiv ();
	
	/**
	 * Sets the text equivalent string for this code.
	 * @param equiv the new text equivalent string. If the parameter is null
	 * the new text equivalent must be set to its default value which is
	 * an empty string.
	 */
	public void setEquiv (String equiv);

	public String getSubFlows ();
	
	public void setSubFlows (String subFlows);

}
