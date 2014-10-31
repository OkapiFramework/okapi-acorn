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

package net.sf.okapi.acorn.xom;

public class XUtil {

	public static int toKey (int ch1,
		int ch2)
	{
		return ((ch1 << 16) | ch2);
	}

	public static String toRef (int key) {
		return ""+(char)(key >> 16)+(char)key;
	}

	/**
	 * Helper method that checks if a given character is the first special character
	 * of a tag reference.
	 * @param value the character to check.
	 * @return true if the given character is the first character of a tag reference.
	 */
	public static boolean isChar1 (char value) {
		switch ( value ) {
		case Const.CODE_STANDALONE:
		case Const.CODE_OPENING:
		case Const.CODE_CLOSING:
		case Const.MARKER_OPENING:
		case Const.MARKER_CLOSING:
			return true;
		}
		return false;
	}

	/**
	 * Gets the coded text position in a given coded text string for a given plain text position.
	 * @param codedText the coded text character sequence to use as the base for the conversion.
	 * @param plainTextPosition the plain text position to convert.
	 * @param leftOfTag true to return the left side position of the tag reference
	 * when the plain text position is on a tag reference (e.g. for end of range stopping
	 * before the reference). Use false to get the right side. 
	 * @return the coded text position.
	 */
	public static int getCodedTextPosition (CharSequence codedText,
		int plainTextPosition,
		boolean leftOfTag)
	{
		int ct = 0;
		int pt = 0;
		for ( ; ct<codedText.length(); ct++ ) {
			if ( isChar1(codedText.charAt(ct) ) ) {
				if (( pt == plainTextPosition ) && leftOfTag ) {
					return ct;
				}
				ct++;
				continue;
			}
			if ( pt == plainTextPosition ) return ct;
			pt++;
		}
		return ct;
	}

}
