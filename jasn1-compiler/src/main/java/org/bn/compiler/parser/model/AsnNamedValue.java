package org.bn.compiler.parser.model;

public class AsnNamedValue {
	public String name;
	public AsnValue value;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnNamedValue() {
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts += name;
		ts += ("\t" + value);

		return ts;
	}

	public String toString(boolean name) {
		String ts = "";

		if (name) {
			ts += name;
		}
		else {
			ts += value;
		}

		return ts;
	}
}
