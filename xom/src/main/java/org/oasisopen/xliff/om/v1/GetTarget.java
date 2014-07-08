package org.oasisopen.xliff.om.v1;

/**
 * Represents the possible fall-back options when trying to get the target of a part and
 * the target does not exists yet.
 */
public enum GetTarget {

	DONT_CREATE,
	
	CREATE_EMPTY,
	
	CLONE_SOURCE

}
