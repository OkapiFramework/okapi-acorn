package net.sf.okapi.acorn.xom;

public class Const {

	public static final char OPENING_CODE = '\uE101';
	public static final char CLOSING_CODE = '\uE102';
	public static final char STANDALONE_CODE = '\uE103';
	public static final char OPENING_ANNOTATION = '\uE104';
	public static final char CLOSING_ANNOTATION = '\uE105';

	public static final int INDEX_BASE = 0xE110;
	public static final int INDEX_MAX = (0xF8FF-INDEX_BASE);

	public static final char JSON_OPENING = '[';
	public static final char JSON_CLOSING = ']';
	public static final char JSON_STANDALONE = '.';

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
