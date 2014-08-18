package net.sf.okapi.acorn.xom;

public class Util {

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

}
