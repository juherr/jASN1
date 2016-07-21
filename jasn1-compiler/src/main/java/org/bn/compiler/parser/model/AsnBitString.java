package org.bn.compiler.parser.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

//
//DefinitionofBITSTRINGType
//
public class AsnBitString {
    //public final String              BUILTINTYPE = "BIT     STRING";
    public final String  BUILTINTYPE = "BIT STRING";
    public AsnConstraint      constraint;
    public String             name;
    public AsnNamedNumberList namedNumberList;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnBitString() {
        name = "";
    }

    //~--- methods ------------------------------------------------------------

    // toString definition
    public String toString() {
        String ts = "";

        ts += (name + "\t::=\t" + BUILTINTYPE + "\n {");

        if (namedNumberList != null) {
            Iterator nl = namedNumberList.namedNumbers.iterator();

            while (nl.hasNext()) {
                ts += nl.next();
            }
        }

        ts += "}";

        if (constraint != null) {
            ts += constraint;
        }

        return ts;
    }
}

