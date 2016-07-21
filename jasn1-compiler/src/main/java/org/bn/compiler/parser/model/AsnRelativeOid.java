package org.bn.compiler.parser.model;

public class AsnRelativeOid {
    final String  BUILTINTYPE = "RELATIVE-OID";
    public String name;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnRelativeOid() {}

    //~--- methods ------------------------------------------------------------

    public String toString() {
        String ts = "";

        ts += BUILTINTYPE;

        return ts;
    }
}

