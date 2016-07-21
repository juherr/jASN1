package org.bn.compiler.parser.model;

public class NamedConstraint {
	public AsnConstraint constraint;
	public boolean isAbsentKw;
	public boolean isConstraint;
	public boolean isOptionalKw;
	public boolean isPresentKw;
	public String name;

	// ~--- constructors -------------------------------------------------------

	public NamedConstraint() {
	}

	// ~--- methods ------------------------------------------------------------

	@Override
	public String toString() {
		String ts = "";

		ts += name;

		if (isConstraint) {
			ts += constraint;
		}

		if (isPresentKw) {
			ts += "\t" + "PRESENT";
		}

		if (isAbsentKw) {
			ts += "\t" + "ABSENT";
		}

		if (isOptionalKw) {
			ts += "\t" + "OPTIONAL";
		}

		return ts;
	}
}
