package org.bn.compiler.parser.model;

//
//DefinitionofBoolean
// 
public class AsnBoolean {
	public final String BUILTINTYPE = "BOOLEAN";
	public String name;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnBoolean() {
		name = "";
	}

	// toString definition

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts += name + "\t::=\t" + BUILTINTYPE;

		return ts;
	}
}
