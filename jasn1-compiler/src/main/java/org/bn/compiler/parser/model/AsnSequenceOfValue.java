package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class AsnSequenceOfValue {
	boolean isValPresent;
	public ArrayList value;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnSequenceOfValue() {
		value = new ArrayList();
	}

	// ~--- methods ------------------------------------------------------------

	// toString Method
	@Override
	public String toString() {
		String ts = "";
		Iterator i = value.iterator();

		while (i.hasNext()) {
			ts += i.next();
		}

		return ts;
	}
}
