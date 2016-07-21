package org.bn.compiler.parser.model;

public class AsnOctetString {
    public final String  BUILTINTYPE = "OCTET STRING";
    public AsnConstraint constraint;
    public String        name;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnOctetString() {
        name = "";
    }

    //~--- methods ------------------------------------------------------------

    // toString Definition
    public String toString() {
        String ts = "";

        ts += name + "\t::=" + BUILTINTYPE + "\t";

        if (constraint != null) {
            ts += constraint;
        }

        return ts;
    }
}
