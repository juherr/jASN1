package org.bn.compiler.parser.model;

//
//DefinitionofNULL
//
public class AsnNull {
	final String BUILTINTYPE = "NULL";
	public String name;
	public boolean isNull = true;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnNull() {
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
