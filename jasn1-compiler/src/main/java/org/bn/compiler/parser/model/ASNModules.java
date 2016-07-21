package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

//To hold all the parsed Modules

/**
 * This class defines the class holding for ASN.1 modules and basic Types
 */
public class ASNModules {
	ArrayList module_list;

	// ~--- constructors -------------------------------------------------------

	// Default Constructor
	ASNModules() {
		module_list = new ArrayList();
	}

	// ~--- methods ------------------------------------------------------------

	public void add(ASNModule module) {
		module_list.add(module);
	}

	// toString Method
	@Override
	public String toString() {
		String ts = "";
		Iterator i = module_list.iterator();

		while (i.hasNext()) {
			ts += i.next();
		}

		return ts;
	}
}
