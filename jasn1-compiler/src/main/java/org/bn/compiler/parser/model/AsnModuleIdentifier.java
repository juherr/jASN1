package org.bn.compiler.parser.model;

//
//DefinitionofASNModuleIdentifier
//
public class AsnModuleIdentifier {
	public AsnOidComponentList componentList;

	// Default Constructor

	public String name;

	// ~--- constructors -------------------------------------------------------

	public AsnModuleIdentifier() {
		componentList = new AsnOidComponentList();
	}

	// toString Implementation

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts = name + "  ";

		if (componentList != null) {
			ts += componentList;
		}

		return ts;
	}
}
