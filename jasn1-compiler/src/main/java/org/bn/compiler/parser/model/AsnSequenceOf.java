package org.bn.compiler.parser.model;

public class AsnSequenceOf {
	final String BUILTINTYPE = "SEQUENCE OF";
	final String BUILTINTYPE1 = "SET OF";
	public AsnConstraint constraint;
	public boolean isDefinedType;
	public boolean isSequenceOf; // Differntiates between SEQUENCE OF and SET OF types
	public boolean isSizeConstraint;
	public String name; // Refers to assignment name
	public String typeName; // Name of the defined type
	public Object typeReference; // Refers to typeReference after OF KW

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	public AsnSequenceOf() {
		name = "";
		constraint = null;
		typeReference = null;
		isSequenceOf = false;
		isDefinedType = false;
		isSizeConstraint = false;
		typeName = "";
	}

	// ~--- methods ------------------------------------------------------------

	// toString definition
	@Override
	public String toString() {
		String ts = "";

		ts += name + "\t::=\t";

		if (isSequenceOf) {
			ts += ("SEQUENCE\t");

			if (constraint != null) {
				ts += (constraint);
			}

			ts += ("\tOF\t");
		}
		else {
			ts += ("SET\t");

			if (constraint != null) {
				ts += (constraint);
			}

			ts += ("\tOF\t");
		}

		if (isDefinedType) {
			ts += (typeName);
		}
		else {
			ts += (typeReference.getClass().getName()); // Print builtinType Class Name
		}

		return ts;
	}
}
