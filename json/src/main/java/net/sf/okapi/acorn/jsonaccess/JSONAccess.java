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

package net.sf.okapi.acorn.jsonaccess;

import java.util.ArrayList;
import java.util.List;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JsonProvider;

/**
 * Provides access to translatable strings for a JSON string.
 * The rules to specify which strings are translatable are provided through a special JSON string
 * which can be included in the data to read.
 */
public class JSONAccess {

	final static String LOCRULE_PATH = "$.locRules";
	
	final private JsonProvider jp;
	final private Configuration confP;

	private DocumentContext dcV;
	private DocumentContext dcP;
	private List<String> rules;
	private int current;
	private List<Object> strings;
	private List<String> paths;

	/**
	 * Creates a new JSONAccess object.
	 */
	public JSONAccess () {
		jp = Configuration.defaultConfiguration().jsonProvider();
		confP = Configuration.builder().options(Option.AS_PATH_LIST).build();
	}

	/**
	 * Gets the current rules.
	 * @return the current rules (can be null or empty).
	 */
	public List<String> getRules () {
		return rules;
	}
	
	/**
	 * Sets new rules.
	 * @param rules the list of new rules (can be null).
	 * @return the JSONAccess object itself (to allowed dot-operations).
	 */
	public JSONAccess setRules (List<String> rules) {
		resetCursor();
		this.rules = rules;
		return this;
	}
	
	/**
	 * Sets new rules.
	 * @param rulesString the rules string (can be null).
	 * @return the JSONAccess object itself (to allowed dot-operations).
	 */
	public JSONAccess setRules (String rulesString) {
		resetCursor();
		if ( rulesString == null ) {
			rules = null;
		}
		else {
			rules = JsonPath.read(jp.parse(rulesString), LOCRULE_PATH);
		}
		return this;
	}

	/**
	 * Sets the input to process. The input may include the rules to use.
	 * If the input includes the rules, they become the new rules. 
	 * @param jsonString the input to set.
	 * @return the JSONAccess object itself (to allowed dot-operations).
	 */
	public JSONAccess setInput (String jsonString) {
		resetCursor();
		dcV = JsonPath.parse(jsonString);
		dcP = JsonPath.using(confP).parse(jsonString);
		// Check if the rules are present in the data
		try {
			List<String> tmp = dcV.read(LOCRULE_PATH);
			rules = tmp;
		}
		catch ( PathNotFoundException e ) {
			// Do nothing: this is not an error.
		}
		return this;
	}
	
	/**
	 * Reads a given input string which may include new rules, and apply the latest rules.
	 * If the input string include the rules, they become the current rules and are applied.
	 * Otherwise some rules are already set they remain the current rules and are applied.
	 * If no rules are set, no rules are applied yet.
	 * @param inputString the data string to read.
	 */
	public void read (String inputString) {
		setInput(inputString);
		applyRules();
	}

	/**
	 * Reads a given input string with the rules provided as a separate string, and applies the rules.
	 * If the input string has rules they are not used, instead the rules specified
	 * separately are used.
	 * @param inputString the string to read.
	 * @param rulesString the string with the rules.
	 */
	public void read (String inputString,
		String rulesString)
	{
		setInput(inputString);
		applyRules(rulesString);
	}
	
	/**
	 * Applies new rules to the last data set.
	 * @param rulesString the new rules.
	 */
	public void applyRules (String rulesString) {
		setRules(rulesString);
		applyRules();
	}

	/**
	 * Applies the current rules to the current input.
	 * Both rules are input must not be null.
	 */
	public void applyRules () {
		resetCursor();
		if ( rules == null ) {
			throw new RuntimeException("No rules are set.");
		}
		if ( dcV == null ) {
			throw new RuntimeException("No input data are set.");
		}

		strings = new ArrayList<>();
		paths = new ArrayList<>();
		
		String r1 = rules.get(0);

		List<String> res = dcV.read(r1);
		if (( res != null ) && !res.isEmpty() ) {
			// Add the strings
			strings.addAll(res);
			// Get and add the corresponding paths
			List<String> resP = dcP.read(r1);
			if (( resP == null ) || ( res.size() != resP.size() )) {
				throw new RuntimeException("Mismatch between strings and paths results.");
			}
			paths.addAll(resP);
		}
	}

	/**
	 * Indicates if the last JSON entry read has another translatable string.
	 * @return true if there is another translatable string, false otherwise.
	 */
	public boolean hasNext () {
		if (( strings == null ) || strings.isEmpty() ) return false;
		if ( current+1 >= strings.size() ) return false;
		return true;
	}
	
	/**
	 * Gets the next translatable string of the last JSON string read.
	 * You must call {@link #hasNext()} before.
	 * @return the next translatable string.
	 */
	public String next () {
		return (String)strings.get(++current);
	}
	
	/**
	 * Sets a new text value (the translation) for the current translatable string.
	 * @param newValue the new text value to set.
	 */
	public void setNewValue (String newValue) {
		String s = paths.get(current);
		dcP.set(s, newValue);
	}

	/**
	 * Gets the JSON string after modifications done using {@link #setNewValue(String)}.
	 * @return the JSON string of the input with any modifications, or null
	 * if there is no input specified.
	 */
	public String getOutput () {
		if ( dcP == null ) return null;
		return dcP.configuration().jsonProvider().toJson(dcP.json());
	}

	/**
	 * Resets the variables used for iterating through the translatable strings.
	 */
	private void resetCursor () {
		current = -1;
		strings = null;
	}

}
