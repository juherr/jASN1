package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

public class AsnNamedNumberList {
	public ArrayList namedNumbers;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnNamedNumberList() {
		namedNumbers = new ArrayList();
	}

	// ~--- methods ------------------------------------------------------------

	// Return the total number of elements in the list
	public int count() {
		return namedNumbers.size();
	}
}
