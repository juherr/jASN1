package org.bn.compiler.parser.model;

public class AsnReal {
    public final String BUILTINTYPE = "REAL";
    public String       name;

    //~--- constructors -------------------------------------------------------

    // Default Constructor
    public AsnReal() {
        name = "";
    }

    //~--- methods ------------------------------------------------------------

    // toString Definition
    public String toString() {
        String ts = "";

        ts += name + "\t::=\t" + BUILTINTYPE;

        return ts;
    }
}
