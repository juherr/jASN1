package org.bn.compiler.parser.model;

//
//DefinitionofOID_Component
//
public class AsnOidComponent {
	public AsnDefinedValue defval;
	public boolean isDefinedValue;
	public String name;
	public boolean nameAndNumberForm;
	public boolean nameForm;
	public Integer num;
	public boolean numberForm;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnOidComponent() {
		name = "";
		num = null;
	}

	// ~--- methods ------------------------------------------------------------

	// toString Implementation
	@Override
	public String toString() {
		String ts = "";

		if (numberForm) {
			ts += num + "\t";
		}
		else if (nameForm) {
			ts += name;

			if (nameAndNumberForm) {
				ts += "(" + num + ")\t";
			}
		}
		else if (isDefinedValue) {
			ts += defval;
		}

		return ts;
	}
}
