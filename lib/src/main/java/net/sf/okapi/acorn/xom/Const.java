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
