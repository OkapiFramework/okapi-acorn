package net.sf.okapi.acorn.xom;

import net.sf.okapi.acorn.xom.Const;
import net.sf.okapi.acorn.xom.XUtil;

/**
 * Provides a set of utility functions for testing.
 */
public class U {

	/**
	 * Converts a given index value for an opening-code marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kOC (int value) {
		return XUtil.toKey(Const.CODE_OPENING, Const.TAGREF_BASE+value);
	}
	
	/**
	 * Converts a given index value for an closing-code marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kCC (int value) {
		return XUtil.toKey(Const.CODE_CLOSING, Const.TAGREF_BASE+value);
	}
	
	/**
	 * Converts a given index value for a standalone-code marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kSC (int value) {
		return XUtil.toKey(Const.CODE_STANDALONE, Const.TAGREF_BASE+value);
	}

	/**
	 * Converts a given index value for an opening-annotation marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kOA (int value) {
		return XUtil.toKey(Const.MARKER_OPENING, Const.TAGREF_BASE+value);
	}
	
	/**
	 * Converts a given index value for an closing-annotation marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kCA (int value) {
		return XUtil.toKey(Const.MARKER_CLOSING, Const.TAGREF_BASE+value);
	}

}
