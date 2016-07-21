package org.bn.compiler.parser.model;

public class ErrorMacro {
	public boolean isDefinedType; // Type is Defined or builtin type
	public boolean isParameter;
	public String name;
	public String parameterName;
	public String typeName; // Name of the Defined Type
	public Object typeReference;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public ErrorMacro() {
	}

	// ~--- methods ------------------------------------------------------------

	// toString Method Definition
	@Override
	public String toString() {
		String ts = "";

		ts += (name + "\t::=\tERROR");

		if (isParameter) {
			if (parameterName != "") {
				ts += ("PARAMETER\t" + parameterName);
			}
			else {
				ts += ("PARAMETER\t");

				if (isDefinedType) {
					ts += (typeName);
				}
				else {
					ts += (typeReference.getClass().getName());
				}
			}
		}

		return ts;
	}
}
