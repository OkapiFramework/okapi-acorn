package org.oasisopen.xliff.om.v1;

/**
* Possible values for the <code>state</code> attribute of a target content:
* {@link #INITIAL}, {@link #TRANSLATED}, {@link #REVIEWED} and {@link #FINAL}.
*/
public enum TargetState {

	/**
	 * Target state: initial.
	 */
	INITIAL("initial"),
	
	/**
	 * Target state: translated.
	 */
	TRANSLATED("translated"),
	
	/**
	 * Target state: reviewed.
	 */
	REVIEWED("reviewed"),
	
	/**
	 * Target state: final.
	 */
	FINAL("final");
	
	private String name;

	/**
	 * Creates a new targetState object with a given name.
	 * @param name the name of the item to create.
	 */
	private TargetState (String name) {
		this.name = name;
	}

	@Override
	public String toString () {
		return name;
	}
	
	/**
	 * Converts a given string representing the name of a target state into a {@link TargetState} object.
	 * @param name the name of the target state.
	 * @return the object for the given name.
	 * @throws InvalidParameterException if the name is invalid.
	 */
	public static TargetState fromString (String name) {
		if ( name == null ) {
			throw new InvalidParameterException("State cannot be null.");
		}
		switch ( name ) {
		case "initial":
			return INITIAL;
		case "translated":
			return TRANSLATED;
		case "reviewed":
			return REVIEWED;
		case "final":
			return FINAL;
		default:
			throw new InvalidParameterException(String.format("Invalid state value: '%s'.", name));
		}
	}

}
