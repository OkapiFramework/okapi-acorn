package net.sf.okapi.acorn.xom;

import net.sf.okapi.acorn.xom.Const;
import net.sf.okapi.acorn.xom.Util;

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
		return Util.toKey(Const.OPENING_CODE, Const.INDEX_BASE+value);
	}
	
	/**
	 * Converts a given index value for an closing-code marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kCC (int value) {
		return Util.toKey(Const.CLOSING_CODE, Const.INDEX_BASE+value);
	}
	
	/**
	 * Converts a given index value for a standalone-code marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kSC (int value) {
		return Util.toKey(Const.STANDALONE_CODE, Const.INDEX_BASE+value);
	}

	/**
	 * Converts a given index value for an opening-annotation marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kOA (int value) {
		return Util.toKey(Const.OPENING_ANNOTATION, Const.INDEX_BASE+value);
	}
	
	/**
	 * Converts a given index value for an closing-annotation marker to its key.
	 * @param value the index value.
	 * @return the key.
	 */
	static public int kCA (int value) {
		return Util.toKey(Const.CLOSING_ANNOTATION, Const.INDEX_BASE+value);
	}

}
