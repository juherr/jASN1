package org.bn.compiler.parser.model;

//
//DefinitionofAnyType
//
public class AsnAnyNoDecode {
	final String BUILTINTYPE = "ANY_NODECODE";
	public String definedByType;
	public boolean isDefinedBy;
	public String name;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnAnyNoDecode() {
		name = "";
		isDefinedBy = false;
		definedByType = "";
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		return name;
	}
}

// ~ Formatted by Jindent --- http://www.jindent.com
