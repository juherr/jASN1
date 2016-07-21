package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

public class ObjectType {
	public final String BUILTINTYPE = "OBJECT-TYPE";
	public String accessPart;
	public ArrayList elements;
	public String statusPart;
	public Object type;
	public AsnValue value;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public ObjectType() {
		elements = new ArrayList();
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts += ("AccessPart is " + accessPart);

		return ts;
	}
}
