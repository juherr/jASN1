package org.bn.compiler.parser.model;

public class AsnObjectIdentifier {
	public final String BUILTINTYPE = "OBJECT IDENTIFIER";
	public String name;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnObjectIdentifier() {
		name = "";
	}

	// ~--- methods ------------------------------------------------------------

	// toString() definition
	@Override
	public String toString() {
		String ts = "";

		ts += name + "\t::=\t" + BUILTINTYPE;

		return ts;
	}
}
