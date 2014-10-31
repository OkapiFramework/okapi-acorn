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

import java.util.regex.Pattern;

public class Const {

	public static final char CODE_OPENING = '\uE101';
	public static final char CODE_CLOSING = '\uE102';
	public static final char CODE_STANDALONE = '\uE103';
	public static final char MARKER_OPENING = '\uE104';
	public static final char MARKER_CLOSING = '\uE105';
	public static final char PCONT_STANDALONE = '\uE106';

	public static final int TAGREF_BASE = 0xE110;
	public static final int TAGREF_MAX = (0xF8FF-TAGREF_BASE);

	/**
	 * Compiled regular expression for all tag references (the two characters) in a coded text
	 */
	public static final Pattern TAGREF_REGEX = Pattern.compile("[\uE101\uE102\uE103\uE104\uE105\uE106].");

	/**
	 * Starting part of all the XLIFF 2 modules namespaces.
	 */
	public static final String NS_XLIFF_MODSTART = "urn:oasis:names:tc:xliff:";

	/**
	 * URI for the XLIFF 2.0 namespace.
	 */
	public static final String NS_XLIFF_CORE20 = "urn:oasis:names:tc:xliff:document:2.0";

	/**
	 * URI for the XLIFF Format Style module 2.0
	 */
	public static final String NS_XLIFF_FS20 = "urn:oasis:names:tc:xliff:fs:2.0";

}
