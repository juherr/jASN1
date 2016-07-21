package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

//
//DefinitionofINTEGER
// 
public class AsnInteger {
	public final String BUILTINTYPE = "INTEGER";
	public AsnConstraint constraint;
	public String name;
	public AsnNamedNumberList namedNumberList;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnInteger() {
		name = "";
	}

	// ~--- methods ------------------------------------------------------------

	// toString() definition
	@Override
	public String toString() {
		String ts = "";

		ts += name + "\t::=" + BUILTINTYPE + "\t";

		if (namedNumberList != null) {
			Iterator nl = namedNumberList.namedNumbers.iterator();

			while (nl.hasNext()) {
				ts += nl.next();
			}
		}

		if (constraint != null) {
			ts += constraint;
		}

		return ts;
	}
}
