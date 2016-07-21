package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

//
//DefinitionofBasicType
//
public class AsnTypes {
	public ArrayList anys;
	public ArrayList bitStrings;
	public ArrayList booleans;
	public ArrayList characterStrings;
	public ArrayList choices;
	public ArrayList defineds;
	public ArrayList embeddeds;
	public ArrayList enums;
	public ArrayList externals;
	public ArrayList integers;
	public ArrayList macroErrors;
	public ArrayList macroObjectTypes;
	public ArrayList macroOperations;
	public ArrayList nulls;
	public ArrayList objectIdentifiers;
	public ArrayList octetStrings;
	public ArrayList reals;
	public ArrayList relativeOids;
	public ArrayList selections;
	public ArrayList sequenceSets;
	public ArrayList sequenceSetsOf;
	public ArrayList taggeds;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	AsnTypes() {
		anys = new ArrayList();
		bitStrings = new ArrayList();
		booleans = new ArrayList();
		characterStrings = new ArrayList();
		choices = new ArrayList();
		enums = new ArrayList();
		integers = new ArrayList();
		nulls = new ArrayList();
		objectIdentifiers = new ArrayList();
		octetStrings = new ArrayList();
		reals = new ArrayList();
		sequenceSets = new ArrayList();
		sequenceSetsOf = new ArrayList();
		externals = new ArrayList();
		relativeOids = new ArrayList();
		selections = new ArrayList();
		taggeds = new ArrayList();
		defineds = new ArrayList();
		macroOperations = new ArrayList();
		macroErrors = new ArrayList();
		macroObjectTypes = new ArrayList();
	}

	// ~--- methods ------------------------------------------------------------

	// toString Method
	@Override
	public String toString() {
		String ts = "";

		// Define the method
		return ts;
	}
}
