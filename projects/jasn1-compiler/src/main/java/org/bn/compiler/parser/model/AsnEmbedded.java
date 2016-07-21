package org.bn.compiler.parser.model;

//
//DefinitionofEMBEDDEDPDVTYPE
//
public class AsnEmbedded {
	final String BUILTINTYPE = "EMBEDDED PDV";
	public String name;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnEmbedded() {
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts += BUILTINTYPE;

		return ts;
	}
}
