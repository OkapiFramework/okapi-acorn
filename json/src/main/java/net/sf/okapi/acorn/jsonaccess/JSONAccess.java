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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * Rules are applied in the order they are declared and the last one overrides any previous one
 * when they resolve on the same node.
 * <p>Each rule must be in the format:
 * <pre>
 * {
 *  "selector":"&lt;json-path>",
 *  "translate":true|false (optional, default is true)
 * }
 * </pre>
 */
public class JSONAccess {

	final static String LOCRULE_PATH = "$.locRules";
	
	final private JsonProvider jp;
	final private Configuration confP;

	private DocumentContext dcV;
	private DocumentContext dcP;
	private List<Rule> rules;
	private LinkedHashMap<String, Node> nodes;
	private Iterator<Entry<String, Node>> nodesIter;
	private Entry<String, Node> currentEntry;

	private class Node {
		boolean trans;
		String text;
	}
	
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
	public List<Rule> getRules () {
		return rules;
	}
	
	/**
	 * Sets new rules.
	 * @param rules the list of new rules (can be null).
	 * @return the JSONAccess object itself (to allowed dot-operations).
	 */
	public JSONAccess setRules (List<Rule> rules) {
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
			List<Object> list = JsonPath.read(jp.parse(rulesString), LOCRULE_PATH);
			compileRules(list);
		}
		return this;
	}

	/**
	 * Compiles a set of new rules.
	 * @param list the list of rules as JSON objects.
	 * (if null or empty: it results on undefined rules) 
	 */
	private void compileRules (List<Object> list) {
		if (( list == null ) || list.isEmpty() ) {
			rules = null;
			return;
		}
		// Else: process each rule
		rules = new ArrayList<>();
		try {
			for ( Object obj : list ) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>)obj;
				String selector = (String)map.get("selector");
				boolean translate = true; // default
				if ( map.containsKey("translate") ) {
					translate = (boolean)map.get("translate");
				}
				rules.add(new Rule(selector, translate));
			}
		}
		catch ( Throwable e ) {
			throw new RuntimeException("Syntax error in rule: "+e.getMessage());
		}
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
			List<Object> tmp = dcV.read(LOCRULE_PATH);
			compileRules(tmp);
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

		// Step 1: Create paths for all rules with flag for properties
		nodes = new LinkedHashMap<>();
		for ( Rule rule : rules) {
			List<String> resP = dcP.read(rule.getSelector());
			if (( resP == null ) || resP.isEmpty() ) continue;
			// Else: we have at least one match
			for ( String path : resP ) {
				// Try to get existing node (created from previous rule)
				Node node = nodes.get(path);
				// If it does not exists: create it and add it
				if ( node == null ) {
					node = new Node();
					nodes.put(path, node);
				}
				// then set the latest properties
				node.trans = rule.getTranslate();
			}
		}

		// Step 2: Remove all nodes that are not translatable
		// or get the text for the nodes that are translatable
		Iterator<Entry<String, Node>> iter = nodes.entrySet().iterator();
		while ( iter.hasNext() ) {
			Entry<String, Node> entry = iter.next();
			if ( entry.getValue().trans ) {
				// Get the text
				entry.getValue().text = dcV.read(entry.getKey());
			}
			else { // Not translatable: remove it
				iter.remove();
			}
		}
		
		if ( !nodes.isEmpty() ) {
			nodesIter = nodes.entrySet().iterator();
		}
		else {
			nodes = null;
		}
	}

	/**
	 * Indicates if the last JSON entry read has another translatable string.
	 * @return true if there is another translatable string, false otherwise.
	 */
	public boolean hasNext () {
		if ( nodesIter == null ) return false;
		return nodesIter.hasNext();
	}
	
	/**
	 * Gets the next translatable string of the last JSON string read.
	 * You must call {@link #hasNext()} before.
	 * @return the next translatable string.
	 */
	public String next () {
		currentEntry = nodesIter.next();
		return currentEntry.getValue().text;
	}
	
	/**
	 * Sets a new text value (the translation) for the current translatable string.
	 * @param newValue the new text value to set.
	 */
	public void setNewValue (String newValue) {
		dcV.set(currentEntry.getKey(), newValue);
	}

	/**
	 * Gets the JSON string after modifications done using {@link #setNewValue(String)}.
	 * @return the JSON string of the input with any modifications, or null
	 * if there is no input specified.
	 */
	public String getOutput () {
		if ( dcV == null ) return null;
		return dcV.configuration().jsonProvider().toJson(dcV.json());
	}

	/**
	 * Resets the variables used for iterating through the translatable strings.
	 */
	private void resetCursor () {
		nodes = null;
		nodesIter = null;
	}

}
