package org.bn.compiler.parser.model;

public class AsnDefinedValue {
	public boolean isDotPresent;
	public String moduleIdentifier;
	public String name;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnDefinedValue() {
	}

	// ~--- methods ------------------------------------------------------------

	// toString() Method Definition
	@Override
	public String toString() {
		String ts = "";

		if (isDotPresent) {
			ts += (moduleIdentifier + "." + name);
		}
		else {
			ts += name;
		}

		return ts;
	}
}
