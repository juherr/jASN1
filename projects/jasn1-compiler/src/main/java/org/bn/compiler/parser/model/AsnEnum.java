package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

//
//DefinitionofENUMERATED
// 
public class AsnEnum {
	final String BUILTINTYPE = "ENUMERATED";
	public String name;
	public AsnNamedNumberList namedNumberList;
	public String snaccName; // specifically added for snacc code generation
	public Boolean isEnum = true;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnEnum() {
		name = "";
		snaccName = "";
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts += name + "\t::=" + BUILTINTYPE + "\t{";

		if (namedNumberList != null) {
			Iterator nl = namedNumberList.namedNumbers.iterator();

			while (nl.hasNext()) {
				ts += nl.next();
			}
		}

		ts += "}";

		return ts;
	}
}
