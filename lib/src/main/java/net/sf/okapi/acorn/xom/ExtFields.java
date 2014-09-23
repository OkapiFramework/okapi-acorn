package net.sf.okapi.acorn.xom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.oasisopen.xliff.om.v1.IExtField;
import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.IWithExtFields;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

/**
 * Implements the {@link IExtFields} interface.
 */
public class ExtFields implements IExtFields {

	private final IWithExtFields parent;

	private ArrayList<IExtField> fields;
	private Map<String, String> namespaces;
	private int autoPrefixCount = 1;

	/**
	 * Creates a new {@link IExtFields} object for a given parent.
	 * @param parent the parent of this new object.
	 */
	public ExtFields (IWithExtFields parent) {
		this.parent = parent;
	}
	
	/**
	 * Copy constructor.
	 * @param parent the parent of this new object.
	 * @param original the original object to duplicate.
	 */
	ExtFields (IWithExtFields parent,
		IExtFields original)
	{
		this.parent = parent;
		for ( IExtField field : original ) {
			set(field.getNSUri(), field.getName(), field.getValue());
		}
		autoPrefixCount = 1;
		for ( String uri : original.getNSUris() ) {
			String sh = original.getNSShorthand(uri);
			setNS(uri, sh);
			// try to update the auto-prefix counter
			if ( sh.startsWith("x") && (( sh.length() > 1 ) && ( Character.isDigit(sh.charAt(1)) )) ) {
				try {
					int n = Integer.parseInt(sh.substring(1));
					if ( n > autoPrefixCount ) autoPrefixCount = n+1;
				}
				catch ( NumberFormatException e ) {
					// Nothing to do: not really an error
				}
			}
		}
	}

	@Override
	public Iterator<IExtField> iterator () {
		if ( fields == null ) fields = new ArrayList<>(2);
		return fields.iterator();
	}

	@Override
	public IWithExtFields getParent () {
		return parent;
	}

	@Override
	public IExtField get (String nsUri,
		String name)
	{
		if ( fields == null ) return null;
		for ( IExtField field : fields ) {
			if ( field.getName().equals(name)
				&& field.getNSUri().equals(nsUri) ) {
				return field;
			}
		}
		return null;
	}

	@Override
	public String getValue (String nsUri,
		String name)
	{
		if ( fields == null ) return null;
		for ( IExtField field : fields ) {
			if ( field.getName().equals(name)
				&& field.getNSUri().equals(nsUri) ) {
				return field.getValue();
			}
		}
		return null;
	}

	@Override
	public IExtField set (String nsUri,
		String name,
		String value)
	{
		if ( fields == null ) fields = new ArrayList<>(2);
		if ( nsUri.isEmpty() ) nsUri = parent.getNSUri();
		ensureNamespaceAndShorthand(nsUri, null);
		IExtField field = get(nsUri, name);
		if ( field == null ) {
			field = new ExtField(nsUri, name, value);
			fields.add(field);
		}
		else {
			field.setValue(value);
		}
		return field;
	}

	@Override
	public IExtField set (String name,
		String value)
	{
		return set("", name, value);
	}

	@Override
	public IExtField set (IExtField field) {
		if ( fields == null ) fields = new ArrayList<>(2);
		ensureNamespaceAndShorthand(field.getNSUri(), null);
		int pos = 0;
		for ( IExtField f : fields ) {
			if ( f.getName().equals(field.getName())
				&& f.getNSUri().equals(field.getNSUri()) ) {
				fields.set(pos, field);
				return field;
			}
			pos++;
		}
		// No such field found: create it
		fields.add(field);
		return field;
	}

	@Override
	public void delete (String nsUri,
		String name)
	{
		if ( fields != null ) {
			IExtField field = get(nsUri, name);
			if ( field != null ) fields.remove(field);
		}
	}

	@Override
	public boolean isEmpty () {
		if (( fields != null ) && ( fields.size() > 0 )) return false;
		if (( namespaces != null ) && ( namespaces.size() > 0 )) return false;
		return true;
	}

	@Override
	public void clear () {
		fields = null;
		namespaces = null;
		autoPrefixCount = 1;
	}

	@Override
	public void setNS (String nsUri,
		String nsShorthand)
	{
		if ( namespaces == null ) namespaces = new LinkedHashMap<String, String>(2);
		namespaces.put(nsUri, nsShorthand);
	}

	@Override
	public String getNSShorthand (String nsUri) {
		if ( namespaces == null ) return null;
		return namespaces.get(nsUri);
	}

	@Override
	public String getNSUri (String nsShorthand) {
		if ( namespaces == null ) return null;
		for ( Entry<String, String> entry : namespaces.entrySet() ) {
			if ( entry.getValue().equals(nsShorthand) ) return entry.getKey(); 
		}
		return null;
	}

	@Override
	public Iterable<String> getNSUris () {
		if ( namespaces == null ) namespaces = new LinkedHashMap<String, String>(2);
		return namespaces.keySet();
	}

	/**
	 * Makes sure there is a shorthand associated with each namespace.
	 * If the given URI has no associated shorthand, one is create automatically for it.
	 * @param nsUri the namespace URI to check.
	 * @param nsShorthand the shorthand of the namespace (or null).
	 * @throws InvalidParameterException if the namespace URI is the Core namespace URI.
	 */
	private void ensureNamespaceAndShorthand (String nsUri,
		String nsShorthand)
	{
		// Cannot be the core namespace
		if ( nsUri.equals(Const.NS_XLIFF_CORE20) ) {
			throw new InvalidParameterException("You cannot use the core namespace for extension fields.");
		}

		if ( getNSShorthand(nsUri) == null ) {
			if ( namespaces == null ) {
				namespaces = new LinkedHashMap<String, String>();
			}
			String newShorthand;
			// First: try to re-use the provided shorthand
			if (( nsShorthand != null ) && !namespaces.containsValue(nsShorthand) ) {
				newShorthand = nsShorthand;
			}
			else { // Else: try auto-shorthand until get get one
				while ( true ) {
					newShorthand = "x"+autoPrefixCount;
					if ( namespaces.containsValue(newShorthand) ) autoPrefixCount++;
					else break;
				}
			}
			setNS(nsUri, newShorthand);
		}
	}

}
