package org.bn.compiler.parser.model;

//
//DefinitionofCharacterString
//
public class AsnCharacterString {
	public final String BUILTINTYPE = "CHARACTER STRING";
	public AsnConstraint constraint;
	public boolean isUCSType; // Is Unrestricted Character String Type
	public String name;
	public String stringtype;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnCharacterString() {
		name = "";
		stringtype = "";
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts += name + "\t" + stringtype;

		if (constraint != null) {
			ts += constraint;
		}

		return ts;
	}
}
