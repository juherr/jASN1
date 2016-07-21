package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

public class AsnSequenceValue {
	public boolean isValPresent;
	public ArrayList namedValueList;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnSequenceValue() {
		namedValueList = new ArrayList();
	}

	// ~--- methods ------------------------------------------------------------

	// toString Method
	@Override
	public String toString() {
		String ts = "";
		Iterator i = namedValueList.iterator();

		while (i.hasNext()) {
			ts += i.next();
		}

		return ts;
	}
}
